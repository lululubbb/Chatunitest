package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.sql.Time;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

class SqlTimeTypeAdapter_32_5Test {

  private SqlTimeTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();

    // Set the private 'format' field's locale and timezone to US/UTC to avoid locale/timezone issues
    Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    DateFormat format = (DateFormat) formatField.get(adapter);
    // Recreate format with Locale.US to ensure consistent parsing of "AM"/"PM"
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.US);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    formatField.set(adapter, sdf);
  }

  @Test
    @Timeout(8000)
  void testRead_withValidTimeString() throws Exception {
    String timeString = "01:23:45 PM";
    JsonReader reader = new JsonReader(new StringReader("\"" + timeString + "\""));

    Time time = adapter.read(reader);

    DateFormat expectedFormat = new SimpleDateFormat("hh:mm:ss a", Locale.US);
    expectedFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
    Date expectedDate = expectedFormat.parse(timeString);
    assertEquals(new Time(expectedDate.getTime()), time);
  }

  @Test
    @Timeout(8000)
  void testRead_withNull() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    Time result = adapter.read(reader);

    assertNull(result);
    verify(reader).nextNull();
  }

  @Test
    @Timeout(8000)
  void testRead_withMalformedString_throwsJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("bad time format");

    assertThrows(com.google.gson.JsonSyntaxException.class, () -> adapter.read(reader));
  }

  @Test
    @Timeout(8000)
  void testWrite_withValidTime() throws Exception {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);

    DateFormat format = getPrivateFormat(adapter);
    // Recreate format with Locale.US and UTC timezone to match adapter's format
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.US);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
    Date date = sdf.parse("02:34:56 PM");
    Time time = new Time(date.getTime());

    adapter.write(writer, time);
    writer.flush();

    String expected = "\"" + sdf.format(time) + "\"";
    assertEquals(expected, stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void testWrite_withNull() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);

    adapter.write(writer, null);
    writer.flush();

    assertEquals("null", stringWriter.toString());
  }

  private DateFormat getPrivateFormat(SqlTimeTypeAdapter adapter) throws Exception {
    Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    return (DateFormat) formatField.get(adapter);
  }
}