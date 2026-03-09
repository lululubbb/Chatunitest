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
import java.text.ParseException;
import java.util.Date;

public class UtcDateTypeAdapter_160_3Test {

  private UtcDateTypeAdapter adapter;
  private JsonReader jsonReaderMock;

  @BeforeEach
  public void setUp() {
    adapter = new UtcDateTypeAdapter();
    jsonReaderMock = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  public void testRead_nullToken_returnsNull() throws IOException {
    when(jsonReaderMock.peek()).thenReturn(JsonToken.NULL);

    Date result = adapter.read(jsonReaderMock);

    verify(jsonReaderMock).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testRead_validDateString_returnsParsedDate() throws Exception {
    String dateString = "2023-06-01T12:34:56Z";
    when(jsonReaderMock.peek()).thenReturn(JsonToken.STRING);
    when(jsonReaderMock.nextString()).thenReturn(dateString);

    // Use reflection to get the private static parse method
    Method parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);

    // Invoke the private static parse method directly to get expectedDate
    Date expectedDate = (Date) parseMethod.invoke(null, dateString, new ParsePosition(0));

    Date actualDate = adapter.read(jsonReaderMock);

    assertEquals(expectedDate, actualDate);
  }

  @Test
    @Timeout(8000)
  public void testRead_parseThrowsParseException_throwsJsonParseException() throws Exception {
    String dateString = "invalid-date";
    when(jsonReaderMock.peek()).thenReturn(JsonToken.STRING);
    when(jsonReaderMock.nextString()).thenReturn(dateString);

    // Use reflection to get the private static parse method
    Method parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);

    // Create a spy of adapter but do not mock parse (since it's static and private)
    UtcDateTypeAdapter spyAdapter = Mockito.spy(adapter);

    // Instead of mocking parse (impossible because it's private static),
    // we simulate the exception by passing invalid input that causes parse to throw ParseException
    JsonParseException thrown = assertThrows(JsonParseException.class, () -> {
      spyAdapter.read(jsonReaderMock);
    });

    assertTrue(thrown.getCause() instanceof ParseException);
  }
}