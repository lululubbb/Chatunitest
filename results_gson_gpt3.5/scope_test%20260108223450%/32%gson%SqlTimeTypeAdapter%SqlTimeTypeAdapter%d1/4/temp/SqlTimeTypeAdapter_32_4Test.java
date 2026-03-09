package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import java.text.DateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlTimeTypeAdapter_32_4Test {

  private SqlTimeTypeAdapter adapter;

  @BeforeEach
  void setUp() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();

    // Set the format field's locale to US to fix locale-dependent parsing/formatting issues
    try {
      Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
      formatField.setAccessible(true);
      SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a", Locale.US);
      formatField.set(adapter, sdf);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void testWrite_NullValue_WritesNull() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    adapter.write(writer, null);
    verify(writer).nullValue();
    verifyNoMoreInteractions(writer);
  }

  @Test
    @Timeout(8000)
  void testWrite_ValidTime_WritesFormattedString() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);
    Time time = Time.valueOf("13:45:30"); // 1:45:30 PM
    adapter.write(writer, time);
    // The format is "hh:mm:ss a", so 13:45:30 -> 01:45:30 PM
    verify(writer).value("01:45:30 PM");
    verifyNoMoreInteractions(writer);
  }

  @Test
    @Timeout(8000)
  void testRead_NullToken_ReturnsNull() throws IOException {
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
  void testRead_ValidString_ReturnsTime() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("01:45:30 PM");

    Time result = adapter.read(reader);

    verify(reader).peek();
    verify(reader).nextString();
    assertNotNull(result);
    // The time should correspond to 13:45:30 in 24h format
    assertEquals(13, result.toLocalTime().getHour());
    assertEquals(45, result.toLocalTime().getMinute());
    assertEquals(30, result.toLocalTime().getSecond());
  }

  @Test
    @Timeout(8000)
  void testRead_InvalidString_ThrowsJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("invalid time");

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> adapter.read(reader));
    assertTrue(ex.getCause() instanceof ParseException);
  }

  @Test
    @Timeout(8000)
  void testPrivateFormatField_IsSimpleDateFormat() throws Exception {
    Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
    formatField.setAccessible(true);
    Object format = formatField.get(adapter);
    assertTrue(format instanceof SimpleDateFormat);
    assertEquals("hh:mm:ss a", ((SimpleDateFormat) format).toPattern());
  }

  @Test
    @Timeout(8000)
  void testFactoryCreate_ReturnsAdapterForTime() {
    var factory = SqlTimeTypeAdapter.FACTORY;
    var gson = mock(com.google.gson.Gson.class);
    var typeToken = TypeToken.get(Time.class);
    var adapterFromFactory = factory.create(gson, typeToken);
    assertNotNull(adapterFromFactory);
    assertEquals(SqlTimeTypeAdapter.class, adapterFromFactory.getClass());
  }

  @Test
    @Timeout(8000)
  void testFactoryCreate_ReturnsNullForOtherType() {
    var factory = SqlTimeTypeAdapter.FACTORY;
    var gson = mock(com.google.gson.Gson.class);
    var typeToken = TypeToken.get(String.class);
    var adapterFromFactory = factory.create(gson, typeToken);
    assertNull(adapterFromFactory);
  }
}