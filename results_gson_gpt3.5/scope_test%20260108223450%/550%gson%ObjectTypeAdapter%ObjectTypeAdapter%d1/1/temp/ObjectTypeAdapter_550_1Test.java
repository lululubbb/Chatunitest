package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ObjectTypeAdapter_550_1Test {

  private Gson gsonMock;
  private ToNumberStrategy toNumberStrategyMock;
  private ObjectTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    gsonMock = mock(Gson.class);
    toNumberStrategyMock = ToNumberPolicy.DOUBLE; // Use a real policy for coverage

    // Use reflection to access private constructor
    Constructor<ObjectTypeAdapter> constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(gsonMock, toNumberStrategyMock);
  }

  @Test
    @Timeout(8000)
  void testRead_null() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Object result = adapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_booleanTrue() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);
    when(in.nextBoolean()).thenReturn(true);

    Object result = adapter.read(in);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
    @Timeout(8000)
  void testRead_booleanFalse() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);
    when(in.nextBoolean()).thenReturn(false);

    Object result = adapter.read(in);

    assertEquals(Boolean.FALSE, result);
  }

  @Test
    @Timeout(8000)
  void testRead_string() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("testString");

    Object result = adapter.read(in);

    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  void testRead_number() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(in.nextString()).thenReturn("123.45");

    // We expect toNumberStrategy to parse "123.45" to a Double (default for DOUBLE)
    Object result = adapter.read(in);

    assertTrue(result instanceof Number);
    assertEquals(123.45d, ((Number) result).doubleValue(), 0.0001);
  }

  @Test
    @Timeout(8000)
  void testRead_beginArray() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.STRING, JsonToken.STRING, JsonToken.END_ARRAY);
    doNothing().when(in).beginArray();
    when(in.hasNext()).thenReturn(true, true, false);
    when(in.peek()).thenReturn(JsonToken.STRING, JsonToken.STRING, JsonToken.END_ARRAY);
    when(in.nextString()).thenReturn("one", "two");
    doNothing().when(in).endArray();

    // Spy adapter to call real readTerminal and tryBeginNesting via reflection
    Object result = adapter.read(in);

    assertTrue(result instanceof List);
    List<?> list = (List<?>) result;
    assertEquals(2, list.size());
    assertEquals("one", list.get(0));
    assertEquals("two", list.get(1));
  }

  @Test
    @Timeout(8000)
  void testRead_beginObject() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT, JsonToken.NAME, JsonToken.STRING, JsonToken.NAME, JsonToken.NUMBER, JsonToken.END_OBJECT);
    doNothing().when(in).beginObject();
    when(in.hasNext()).thenReturn(true, true, false);
    when(in.nextName()).thenReturn("key1", "key2");
    when(in.peek()).thenReturn(JsonToken.STRING, JsonToken.NUMBER);
    when(in.nextString()).thenReturn("value1");
    when(in.nextString()).thenReturn("123"); // For number, nextString returns string "123"
    doNothing().when(in).endObject();

    // Mock gson.getAdapter to return adapter itself for nested calls
    when(gsonMock.getAdapter(Object.class)).thenReturn(adapter);

    Object result = adapter.read(in);

    assertTrue(result instanceof java.util.Map);
    @SuppressWarnings("unchecked")
    java.util.Map<String, Object> map = (java.util.Map<String, Object>) result;
    assertEquals(2, map.size());
    assertEquals("value1", map.get("key1"));
    assertEquals(123d, ((Number) map.get("key2")).doubleValue(), 0.0001);
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
  void testWrite_primitive() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, "stringValue");

    // Should delegate to gson.getAdapter(String.class).write(out, value)
    @SuppressWarnings("unchecked")
    TypeAdapter<String> stringAdapter = mock(TypeAdapter.class);
    when(gsonMock.getAdapter(String.class)).thenReturn(stringAdapter);

    // Recreate adapter with spy gsonMock to inject adapter again
    // But simpler: just verify write called on stringAdapter

    adapter.write(out, "stringValue");

    verify(stringAdapter).write(out, "stringValue");
  }

  @Test
    @Timeout(8000)
  void testPrivateTryBeginNesting_beginArray() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    doNothing().when(in).beginArray();

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    method.setAccessible(true);

    Object result = method.invoke(adapter, in, JsonToken.BEGIN_ARRAY);

    assertTrue(result instanceof List);
  }

  @Test
    @Timeout(8000)
  void testPrivateTryBeginNesting_beginObject() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    doNothing().when(in).beginObject();

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    method.setAccessible(true);

    Object result = method.invoke(adapter, in, JsonToken.BEGIN_OBJECT);

    assertTrue(result instanceof java.util.Map);
  }

  @Test
    @Timeout(8000)
  void testPrivateTryBeginNesting_notNesting() throws Exception {
    JsonReader in = mock(JsonReader.class);

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    method.setAccessible(true);

    Object result = method.invoke(adapter, in, JsonToken.STRING);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testPrivateReadTerminal_null() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    method.setAccessible(true);

    Object result = method.invoke(adapter, in, JsonToken.NULL);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testPrivateReadTerminal_boolean() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);
    when(in.nextBoolean()).thenReturn(true);

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    method.setAccessible(true);

    Object result = method.invoke(adapter, in, JsonToken.BOOLEAN);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
    @Timeout(8000)
  void testPrivateReadTerminal_string() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("abc");

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    method.setAccessible(true);

    Object result = method.invoke(adapter, in, JsonToken.STRING);

    assertEquals("abc", result);
  }

  @Test
    @Timeout(8000)
  void testPrivateReadTerminal_number() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(in.nextString()).thenReturn("456.78");

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    method.setAccessible(true);

    Object result = method.invoke(adapter, in, JsonToken.NUMBER);

    assertTrue(result instanceof Number);
    assertEquals(456.78d, ((Number) result).doubleValue(), 0.0001);
  }
}