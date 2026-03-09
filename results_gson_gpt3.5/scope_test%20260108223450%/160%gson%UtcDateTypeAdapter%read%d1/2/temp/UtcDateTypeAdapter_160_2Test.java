package com.google.gson.typeadapters;
import org.junit.jupiter.api.Timeout;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonParseException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParsePosition;
import java.util.Date;

class UtcDateTypeAdapter_160_2Test {

  private UtcDateTypeAdapter adapter;

  @BeforeEach
  void setUp() {
    adapter = new UtcDateTypeAdapter();
  }

  @Test
    @Timeout(8000)
  void read_returnsNull_whenJsonTokenIsNull() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);

    Date result = adapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_returnsParsedDate_whenJsonTokenIsString() throws Exception {
    JsonReader in = mock(JsonReader.class);
    String dateString = "2024-06-01T12:34:56Z";

    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn(dateString);

    // Use reflection to get the private parse method
    Method parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);
    Date expectedDate = (Date) parseMethod.invoke(null, dateString, new ParsePosition(0)); // static method, so null for instance

    Date actualDate = adapter.read(in);

    assertEquals(expectedDate, actualDate);
  }

  @Test
    @Timeout(8000)
  void read_throwsJsonParseException_whenParseThrowsParseException() throws IOException {
    JsonReader in = mock(JsonReader.class);
    String invalidDateString = "invalid-date";

    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn(invalidDateString);

    // We cannot subclass final UtcDateTypeAdapter, so instead we mock JsonReader to cause parse failure
    // The adapter calls parse(dateString, new ParsePosition(0)) which throws ParseException on invalid string
    // So just call read with invalid input and check exception

    JsonParseException ex = assertThrows(JsonParseException.class, () -> adapter.read(in));
    assertTrue(ex.getCause() instanceof java.text.ParseException);
  }
}