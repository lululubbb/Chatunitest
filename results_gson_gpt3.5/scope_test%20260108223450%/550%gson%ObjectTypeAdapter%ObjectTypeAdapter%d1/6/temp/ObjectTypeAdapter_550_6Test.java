package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ObjectTypeAdapter_550_6Test {

  private Gson gsonMock;
  private ToNumberStrategy toNumberStrategyMock;
  private ObjectTypeAdapter adapter;

  @BeforeEach
  public void setUp() throws Exception {
    gsonMock = mock(Gson.class);
    toNumberStrategyMock = mock(ToNumberStrategy.class);

    Constructor<ObjectTypeAdapter> constructor =
        ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(gsonMock, toNumberStrategyMock);
  }

  @Test
    @Timeout(8000)
  public void testRead_NullToken() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Object result = adapter.read(in);

    verify(in).peek();
    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testRead_BooleanToken() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);
    when(in.nextBoolean()).thenReturn(true);

    Object result = adapter.read(in);

    verify(in).peek();
    verify(in).nextBoolean();
    assertEquals(Boolean.TRUE, result);
  }

  @Test
    @Timeout(8000)
  public void testRead_StringToken() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("testString");

    Object result = adapter.read(in);

    verify(in).peek();
    verify(in).nextString();
    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  public void testRead_NumberToken() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(toNumberStrategyMock.readNumber(in)).thenReturn(123.45);

    Object result = adapter.read(in);

    verify(in).peek();
    verify(toNumberStrategyMock).readNumber(in);
    assertEquals(123.45, result);
  }

  @Test
    @Timeout(8000)
  public void testRead_BeginArrayToken() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.STRING, JsonToken.END_ARRAY);
    when(gsonMock.getAdapter(Object.class)).thenReturn(adapter);
    when(in.nextString()).thenReturn("element");
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();

    // We need to simulate the nested peeks inside the array reading loop
    // The adapter.read(in) calls peek multiple times, so we need to set up the sequence accordingly
    // The sequence: BEGIN_ARRAY (already set), then inside loop STRING, then END_ARRAY
    // This is already set by when(in.peek()).thenReturn(BEGIN_ARRAY, STRING, END_ARRAY);

    Object result = adapter.read(in);

    verify(in).peek();
    verify(in).beginArray();
    verify(in, atLeastOnce()).peek();
    verify(in).nextString();
    verify(in).endArray();
    assertTrue(result instanceof java.util.List);
    @SuppressWarnings("unchecked")
    java.util.List<Object> list = (java.util.List<Object>) result;
    assertEquals(1, list.size());
    assertEquals("element", list.get(0));
  }

  @Test
    @Timeout(8000)
  public void testRead_BeginObjectToken() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT, JsonToken.NAME, JsonToken.STRING, JsonToken.END_OBJECT);
    when(gsonMock.getAdapter(Object.class)).thenReturn(adapter);
    doNothing().when(in).beginObject();
    doNothing().when(in).endObject();
    when(in.nextName()).thenReturn("key");
    when(in.nextString()).thenReturn("value");

    Object result = adapter.read(in);

    verify(in).peek();
    verify(in).beginObject();
    verify(in, atLeastOnce()).peek();
    verify(in).nextName();
    verify(in).nextString();
    verify(in).endObject();
    assertTrue(result instanceof java.util.Map);
    @SuppressWarnings("unchecked")
    java.util.Map<String, Object> map = (java.util.Map<String, Object>) result;
    assertEquals(1, map.size());
    assertEquals("value", map.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testWrite_NullValue() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  public void testWrite_PrimitiveValue() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<String> stringAdapterMock = mock(TypeAdapter.class);

    when(gsonMock.getAdapter(String.class)).thenReturn(stringAdapterMock);

    adapter.write(out, "stringValue");

    verify(gsonMock).getAdapter(String.class);
    verify(stringAdapterMock).write(out, "stringValue");
  }

  @Test
    @Timeout(8000)
  public void testTryBeginNesting_BeginArray() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    doNothing().when(in).beginArray();

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.BEGIN_ARRAY);

    verify(in).beginArray();
    assertTrue(result instanceof java.util.List);
  }

  @Test
    @Timeout(8000)
  public void testTryBeginNesting_BeginObject() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    doNothing().when(in).beginObject();

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.BEGIN_OBJECT);

    verify(in).beginObject();
    assertTrue(result instanceof java.util.Map);
  }

  @Test
    @Timeout(8000)
  public void testTryBeginNesting_NotNesting() throws Exception {
    JsonReader in = mock(JsonReader.class);

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.STRING);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadTerminal_Null() throws Exception {
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
  public void testReadTerminal_Boolean() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);
    when(in.nextBoolean()).thenReturn(true);

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.BOOLEAN);

    verify(in).nextBoolean();
    assertEquals(Boolean.TRUE, result);
  }

  @Test
    @Timeout(8000)
  public void testReadTerminal_String() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("abc");

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.STRING);

    verify(in).nextString();
    assertEquals("abc", result);
  }

  @Test
    @Timeout(8000)
  public void testReadTerminal_Number() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(toNumberStrategyMock.readNumber(in)).thenReturn(42);

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.NUMBER);

    verify(toNumberStrategyMock).readNumber(in);
    assertEquals(42, result);
  }
}