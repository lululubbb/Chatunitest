package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlTimeTypeAdapter_32_2Test {

  private SqlTimeTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();

    // Set the format field's timezone to UTC to avoid locale/timezone issues during parsing
    Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    DateFormat format = (DateFormat) formatField.get(adapter);
    format.setLenient(false);
    format.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
  }

  @Test
    @Timeout(8000)
  void testRead_null() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    Time result = adapter.read(reader);

    verify(reader).peek();
    verify(reader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_validTime() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("12:34:56 PM");

    // Set adapter's format timezone and leniency to match test expectations
    Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    SimpleDateFormat adapterFormat = (SimpleDateFormat) formatField.get(adapter);
    adapterFormat.setLenient(false);
    adapterFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

    Time result = adapter.read(reader);

    assertNotNull(result);
    // The time should parse as 12:34:56 PM in UTC
    DateFormat df = new SimpleDateFormat("hh:mm:ss a");
    df.setLenient(false);
    df.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
    Date expectedDate = df.parse("12:34:56 PM");
    assertEquals(new Time(expectedDate.getTime()), result);
  }

  @Test
    @Timeout(8000)
  void testRead_invalidTime_throwsJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("invalid-time");

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
    assertTrue(thrown.getCause() instanceof java.text.ParseException);
  }

  @Test
    @Timeout(8000)
  void testWrite_null() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(writer).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_validTime() throws IOException, Exception {
    JsonWriter writer = mock(JsonWriter.class);

    DateFormat df = new SimpleDateFormat("hh:mm:ss a");
    df.setLenient(false);
    df.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
    Date date = df.parse("01:23:45 PM");
    Time time = new Time(date.getTime());

    // Set the adapter's format timezone to UTC to match parsing and formatting
    Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    SimpleDateFormat adapterFormat = (SimpleDateFormat) formatField.get(adapter);
    adapterFormat.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
    adapterFormat.setLenient(false);

    adapter.write(writer, time);

    verify(writer).value("01:23:45 PM");
  }

  @Test
    @Timeout(8000)
  void testFormatFieldIsSimpleDateFormat() throws Exception {
    Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    Object formatValue = formatField.get(adapter);

    assertTrue(formatValue instanceof SimpleDateFormat);
    SimpleDateFormat sdf = (SimpleDateFormat) formatValue;
    assertEquals("hh:mm:ss a", sdf.toPattern());
  }
}