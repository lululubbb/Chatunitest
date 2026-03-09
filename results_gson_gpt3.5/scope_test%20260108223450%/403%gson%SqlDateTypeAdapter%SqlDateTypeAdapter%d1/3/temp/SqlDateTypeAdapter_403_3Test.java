package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlDateTypeAdapter_403_3Test {

  private SqlDateTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    // Use reflection to instantiate private constructor
    var constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();

    // Set the 'format' field to use Locale.US to avoid locale issues in tests
    Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    formatField.set(adapter, new java.text.SimpleDateFormat("MMM d, yyyy", Locale.US));
  }

  @Test
    @Timeout(8000)
  void testRead_null() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);

    assertNull(adapter.read(in));
    verify(in).nextNull();
  }

  @Test
    @Timeout(8000)
  void testRead_validDate() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("Jan 1, 2020");

    java.sql.Date date = adapter.read(in);
    assertNotNull(date);

    // Verify date is parsed correctly by format
    Field formatField = SqlDateTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    DateFormat format = (DateFormat) formatField.get(adapter);
    assertEquals("Jan 1, 2020", format.format(date));
  }

  @Test
    @Timeout(8000)
  void testRead_invalidDate_throwsJsonSyntaxException() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("invalid-date");

    assertThrows(com.google.gson.JsonSyntaxException.class, () -> adapter.read(in));
  }

  @Test
    @Timeout(8000)
  void testWrite_null() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);
    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_validDate() throws IOException, Exception {
    JsonWriter out = mock(JsonWriter.class);
    // Create a date for Jan 1, 2020
    java.sql.Date date = java.sql.Date.valueOf("2020-01-01");

    adapter.write(out, date);

    // Capture the string written
    verify(out).value("Jan 1, 2020");
  }
}