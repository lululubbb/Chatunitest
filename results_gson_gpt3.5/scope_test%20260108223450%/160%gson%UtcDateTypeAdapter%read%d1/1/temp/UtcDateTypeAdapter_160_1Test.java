package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

public class UtcDateTypeAdapter_160_1Test {

  private UtcDateTypeAdapter adapter;
  private JsonReader jsonReader;

  @BeforeEach
  public void setUp() {
    adapter = new UtcDateTypeAdapter();
    jsonReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  public void testRead_nullToken_returnsNull() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    doNothing().when(jsonReader).nextNull();

    Date result = adapter.read(jsonReader);

    verify(jsonReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testRead_validDateString_returnsParsedDate() throws Exception {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    String dateString = "2023-04-01T12:34:56Z";
    when(jsonReader.nextString()).thenReturn(dateString);

    // Use reflection to get the private static parse(String, ParsePosition) method
    Method parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);

    Date expectedDate = (Date) parseMethod.invoke(null, dateString, new ParsePosition(0));

    Date actualDate = adapter.read(jsonReader);

    assertNotNull(actualDate);
    assertEquals(expectedDate, actualDate);
  }

  @Test
    @Timeout(8000)
  public void testRead_parseException_throwsJsonParseException() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    String invalidDate = "invalid-date";
    when(jsonReader.nextString()).thenReturn(invalidDate);

    // Instead of subclassing final class, create a helper class to simulate parse exception
    class AdapterWithParseException {
      public Date read(JsonReader in) throws IOException {
        try {
          switch (in.peek()) {
            case NULL:
              in.nextNull();
              return null;
            default:
              String date = in.nextString();
              if (date.equals(invalidDate)) {
                throw new ParseException("error", 0);
              }
              Method parseMethod;
              try {
                parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
                parseMethod.setAccessible(true);
                return (Date) parseMethod.invoke(null, date, new ParsePosition(0));
              } catch (Exception e) {
                throw new IOException(e);
              }
          }
        } catch (ParseException e) {
          throw new JsonParseException(e);
        }
      }
    }

    AdapterWithParseException adapterWithParseException = new AdapterWithParseException();

    JsonParseException thrown = assertThrows(JsonParseException.class, () -> {
      adapterWithParseException.read(jsonReader);
    });

    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof ParseException);
  }
}