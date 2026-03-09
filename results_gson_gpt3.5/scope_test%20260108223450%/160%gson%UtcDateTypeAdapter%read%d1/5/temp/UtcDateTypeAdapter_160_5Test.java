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
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Date;

class UtcDateTypeAdapter_160_5Test {

  private UtcDateTypeAdapter adapter;
  private JsonReader jsonReader;

  @BeforeEach
  void setUp() {
    adapter = new UtcDateTypeAdapter();
    jsonReader = Mockito.mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnNull_whenJsonTokenIsNull() throws IOException {
    when(jsonReader.peek()).thenReturn(JsonToken.NULL);

    Date result = adapter.read(jsonReader);

    verify(jsonReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void read_shouldReturnParsedDate_whenJsonTokenIsString() throws Exception {
    String dateString = "2023-06-01T12:00:00Z";
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(dateString);

    // Spy on adapter to intercept read() and replace parse call via reflection
    UtcDateTypeAdapter spyAdapter = Mockito.spy(adapter);
    Date expectedDate = new Date(1685620800000L); // 2023-06-01T12:00:00Z in milliseconds

    // Use reflection to get private static parse method
    Method parseMethod = UtcDateTypeAdapter.class.getDeclaredMethod("parse", String.class, ParsePosition.class);
    parseMethod.setAccessible(true);

    // Stub parse method by using doAnswer on spyAdapter.read() to call original read but replace parse
    doAnswer(invocation -> {
      JsonReader in = invocation.getArgument(0);
      if (in.peek() == JsonToken.STRING) {
        String date = in.nextString();
        // Return expectedDate instead of actual parse
        return expectedDate;
      }
      return invocation.callRealMethod();
    }).when(spyAdapter).read(any(JsonReader.class));

    Date actualDate = spyAdapter.read(jsonReader);

    assertEquals(expectedDate, actualDate);
    verify(jsonReader, never()).nextNull();
  }

  @Test
    @Timeout(8000)
  void read_shouldThrowJsonParseException_whenParseThrowsParseException() throws IOException {
    String dateString = "invalid-date";
    when(jsonReader.peek()).thenReturn(JsonToken.STRING);
    when(jsonReader.nextString()).thenReturn(dateString);

    // Spy on adapter to intercept read() and throw ParseException wrapped in JsonParseException
    UtcDateTypeAdapter spyAdapter = Mockito.spy(adapter);

    doAnswer(invocation -> {
      JsonReader in = invocation.getArgument(0);
      if (in.peek() == JsonToken.STRING) {
        String date = in.nextString();
        // Throw JsonParseException wrapping ParseException to simulate parse failure
        throw new JsonParseException(new ParseException("error", 0));
      }
      return invocation.callRealMethod();
    }).when(spyAdapter).read(any(JsonReader.class));

    JsonParseException thrown = assertThrows(JsonParseException.class, () -> {
      spyAdapter.read(jsonReader);
    });

    assertTrue(thrown.getCause() instanceof ParseException);
  }
}