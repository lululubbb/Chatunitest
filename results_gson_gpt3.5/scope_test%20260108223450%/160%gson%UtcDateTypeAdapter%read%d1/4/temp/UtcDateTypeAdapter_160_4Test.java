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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

class UtcDateTypeAdapter_160_4Test {

  private UtcDateTypeAdapter adapter;
  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    adapter = new UtcDateTypeAdapter();
    jsonReader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  void read_nullToken_returnsNull() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    Date result = adapter.read(jsonReader);

    verify(jsonReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_validDateString_returnsParsedDate() throws Exception {
    String dateString = "2024-06-15T12:34:56Z";
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(dateString);

    Method parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);
    Date expectedDate = (Date) parseMethod.invoke(null, dateString, new ParsePosition(0));

    Date actualDate = adapter.read(jsonReader);

    assertEquals(expectedDate, actualDate);
  }

  @Test
    @Timeout(8000)
  void read_parseThrowsParseException_throwsJsonParseException() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn("invalid-date");

    // Use reflection to invoke private static parse method and simulate ParseException
    Method parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);

    // Create a proxy adapter that calls the original read method but mocks parse to throw ParseException
    // Since class is final, we cannot subclass, so we simulate the exception by calling parse directly

    JsonParseException thrown = assertThrows(JsonParseException.class, () -> {
      try {
        // Call adapter.read, but intercept parse method call by directly calling parse with invalid input
        // Actually call adapter.read(jsonReader), which calls parse internally and should throw JsonParseException
        adapter.read(jsonReader);
      } catch (JsonParseException e) {
        // rethrow to satisfy assertThrows
        throw e;
      }
    });

    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof ParseException);
  }
}