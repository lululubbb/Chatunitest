package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

class SqlDateTypeAdapter_403_4Test {
  private SqlDateTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    Constructor<SqlDateTypeAdapter> constructor = SqlDateTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();
  }

  @Test
    @Timeout(8000)
  void testRead_NullToken() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    java.sql.Date result = adapter.read(reader);

    verify(reader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_ValidDateString() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("Jan 1, 2020");

    java.sql.Date result = adapter.read(reader);

    assertNotNull(result);
    // Check that the date corresponds to Jan 1, 2020
    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    sdf.setLenient(false);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    java.util.Date parsed = sdf.parse("Jan 1, 2020");
    assertEquals(new java.sql.Date(parsed.getTime()), result);
  }

  @Test
    @Timeout(8000)
  void testRead_InvalidDateString_ThrowsJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("invalid-date");

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
    assertTrue(ex.getMessage().contains("invalid-date") || ex.getCause() instanceof java.text.ParseException);
  }

  @Test
    @Timeout(8000)
  void testWrite_NullValue() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(writer).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_ValidDate() throws Exception {
    JsonWriter writer = mock(JsonWriter.class);
    SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.US);
    sdf.setLenient(false);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    java.util.Date utilDate = sdf.parse("Feb 2, 2022");
    java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

    adapter.write(writer, sqlDate);

    verify(writer).value("Feb 2, 2022");
  }
}