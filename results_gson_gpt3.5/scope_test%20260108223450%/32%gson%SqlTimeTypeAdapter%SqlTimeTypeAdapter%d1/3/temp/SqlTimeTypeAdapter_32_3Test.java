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
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlTimeTypeAdapter_32_3Test {

  private SqlTimeTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();
  }

  @Test
    @Timeout(8000)
  void testRead_nullValue() throws IOException {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    // Do NOT call reader.nextNull() here, let adapter.read call it
    // Instead, stub nextNull to do nothing when called
    doNothing().when(reader).nextNull();

    Time time = adapter.read(reader);

    verify(reader).nextNull();
    assertNull(time);
  }

  @Test
    @Timeout(8000)
  void testRead_validTime() throws Exception {
    String timeString = "10:15:30 AM";
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(timeString);

    Time time = adapter.read(reader);

    DateFormat df = new SimpleDateFormat("hh:mm:ss a");
    Date parsedDate = df.parse(timeString);
    Time expectedTime = new Time(parsedDate.getTime());

    assertEquals(expectedTime, time);
  }

  @Test
    @Timeout(8000)
  void testRead_invalidTime_throwsJsonSyntaxException() throws IOException {
    String invalidTime = "invalid-time";
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.STRING);
    when(reader.nextString()).thenReturn(invalidTime);

    assertThrows(com.google.gson.JsonSyntaxException.class, () -> adapter.read(reader));
  }

  @Test
    @Timeout(8000)
  void testWrite_nullValue() throws IOException {
    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(writer).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_validTime() throws IOException, Exception {
    String timeString = "10:15:30 AM";
    DateFormat df = new SimpleDateFormat("hh:mm:ss a");
    Date date = df.parse(timeString);
    Time time = new Time(date.getTime());

    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, time);

    verify(writer).value(timeString);
  }

  @Test
    @Timeout(8000)
  void testPrivateFormatField() throws Exception {
    // Access private DateFormat field "format" via reflection
    Field field = SqlTimeTypeAdapter.class.getDeclaredField("format");
    field.setAccessible(true);
    DateFormat format = (DateFormat) field.get(adapter);

    SimpleDateFormat expectedFormat = new SimpleDateFormat("hh:mm:ss a");
    assertEquals(expectedFormat.toPattern(), ((SimpleDateFormat) format).toPattern());
  }

  @Test
    @Timeout(8000)
  void testFactoryCreate_returnsAdapterForTime() {
    var factory = SqlTimeTypeAdapter.FACTORY;
    var typeToken = com.google.gson.reflect.TypeToken.get(Time.class);

    var typeAdapter = factory.create(null, typeToken);

    assertNotNull(typeAdapter);
    assertEquals(SqlTimeTypeAdapter.class, typeAdapter.getClass());
  }

  @Test
    @Timeout(8000)
  void testFactoryCreate_returnsNullForOtherTypes() {
    var factory = SqlTimeTypeAdapter.FACTORY;
    var typeToken = com.google.gson.reflect.TypeToken.get(String.class);

    var typeAdapter = factory.create(null, typeToken);

    assertNull(typeAdapter);
  }
}