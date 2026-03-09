package com.google.gson.internal.sql;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import java.text.DateFormat;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Time;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SqlTimeTypeAdapter_32_1Test {

  private SqlTimeTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    // Use reflection to instantiate the private constructor
    Constructor<SqlTimeTypeAdapter> constructor = SqlTimeTypeAdapter.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    adapter = constructor.newInstance();
  }

  @Test
    @Timeout(8000)
  void testRead_nullJsonToken() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    // nextNull() is void, so doNothing() is default; no need to stub it
    doNothing().when(in).nextNull();

    Time result = adapter.read(in);

    assertNull(result);
    verify(in).peek();
    verify(in).nextNull();
  }

  @Test
    @Timeout(8000)
  void testRead_validTime() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("01:34:56 下午");

    Time result = adapter.read(in);

    assertNotNull(result);
    // Check that the time is parsed correctly (hour=13, min=34, sec=56)
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
    Date expectedDate = sdf.parse("01:34:56 下午");
    assertEquals(new Time(expectedDate.getTime()), result);
    verify(in).peek();
    verify(in).nextString();
  }

  @Test
    @Timeout(8000)
  void testRead_invalidTime_throwsJsonSyntaxException() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("invalid-time");

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> adapter.read(in));
    assertTrue(thrown.getMessage().contains("Failed parsing 'invalid-time'"));
    verify(in).peek();
    verify(in).nextString();
  }

  @Test
    @Timeout(8000)
  void testWrite_nullValue() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void testWrite_validTime() throws Exception {
    JsonWriter out = mock(JsonWriter.class);
    SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
    Date date = sdf.parse("01:02:03 下午");
    Time time = new Time(date.getTime());

    adapter.write(out, time);

    verify(out).value("01:02:03 下午");
  }

  @Test
    @Timeout(8000)
  void testPrivateFormatField() throws Exception {
    // Access private final DateFormat field 'format' by reflection
    var field = SqlTimeTypeAdapter.class.getDeclaredField("format");
    field.setAccessible(true);
    Object formatObj = field.get(adapter);

    assertNotNull(formatObj);
    assertTrue(formatObj instanceof SimpleDateFormat);
    SimpleDateFormat sdf = (SimpleDateFormat) formatObj;
    assertEquals("hh:mm:ss a", sdf.toPattern());
  }

  @Test
    @Timeout(8000)
  void testFactory_create_withTimeClass() {
    var factory = SqlTimeTypeAdapter.FACTORY;
    var typeToken = TypeToken.get(Time.class);

    var typeAdapter = factory.create(null, typeToken);

    assertNotNull(typeAdapter);
    assertTrue(typeAdapter instanceof SqlTimeTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void testFactory_create_withOtherClass() {
    var factory = SqlTimeTypeAdapter.FACTORY;
    var typeToken = TypeToken.get(String.class);

    var typeAdapter = factory.create(null, typeToken);

    assertNull(typeAdapter);
  }
}