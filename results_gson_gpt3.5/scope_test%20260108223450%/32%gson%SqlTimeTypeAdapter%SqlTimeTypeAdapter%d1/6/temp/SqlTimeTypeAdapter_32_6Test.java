package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlTimeTypeAdapter_32_6Test {

  private SqlTimeTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();
  }

  @Test
    @Timeout(8000)
  void testRead_NullValue() throws IOException {
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
  void testRead_ValidTime() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("02:30:15 PM");

    try {
      Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
      formatField.setAccessible(true);
      formatField.set(adapter, new SimpleDateFormat("hh:mm:ss a"));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error setting format field: " + e.getMessage());
    }

    Time result = adapter.read(reader);

    assertNotNull(result);
    DateFormat format = new SimpleDateFormat("hh:mm:ss a");
    Date expectedDate = format.parse("02:30:15 PM");
    assertEquals(new Time(expectedDate.getTime()), result);
  }

  @Test
    @Timeout(8000)
  void testRead_InvalidTime_ThrowsJsonSyntaxException() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn("invalid-time");

    assertThrows(com.google.gson.JsonSyntaxException.class, () -> adapter.read(reader));
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
  void testWrite_ValidTime() throws IOException, ParseException {
    JsonWriter writer = mock(JsonWriter.class);

    try {
      Field formatField = SqlTimeTypeAdapter.class.getDeclaredField("format");
      formatField.setAccessible(true);
      formatField.set(adapter, new SimpleDateFormat("hh:mm:ss a"));
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error setting format field: " + e.getMessage());
    }

    DateFormat format = new SimpleDateFormat("hh:mm:ss a");
    Time time = new Time(format.parse("11:45:30 AM").getTime());

    adapter.write(writer, time);

    verify(writer).value("11:45:30 AM");
  }

  @Test
    @Timeout(8000)
  void testFactoryCreate_ReturnsAdapterForTime() {
    var factory = SqlTimeTypeAdapter.FACTORY;
    var gson = mock(com.google.gson.Gson.class);
    var typeToken = com.google.gson.reflect.TypeToken.get(Time.class);

    var adapterCreated = factory.create(gson, typeToken);

    assertNotNull(adapterCreated);
    assertTrue(adapterCreated instanceof SqlTimeTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void testFactoryCreate_ReturnsNullForOtherType() {
    var factory = SqlTimeTypeAdapter.FACTORY;
    var gson = mock(com.google.gson.Gson.class);
    var typeToken = com.google.gson.reflect.TypeToken.get(String.class);

    var adapterCreated = factory.create(gson, typeToken);

    assertNull(adapterCreated);
  }

  @Test
    @Timeout(8000)
  void testPrivateFormatField() throws Exception {
    Field field = SqlTimeTypeAdapter.class.getDeclaredField("format");
    field.setAccessible(true);
    DateFormat format = (DateFormat) field.get(adapter);
    assertNotNull(format);
    assertEquals("hh:mm:ss a", ((SimpleDateFormat) format).toPattern());
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    var constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    var instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof SqlTimeTypeAdapter);
  }
}