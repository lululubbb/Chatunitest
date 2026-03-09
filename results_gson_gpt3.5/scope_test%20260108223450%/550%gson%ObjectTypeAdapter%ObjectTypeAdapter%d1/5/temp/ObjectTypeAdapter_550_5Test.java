package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayDeque;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class ObjectTypeAdapter_550_5Test {

  private Gson gsonMock;
  private ToNumberStrategy toNumberStrategyMock;
  private ObjectTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    gsonMock = mock(Gson.class);
    toNumberStrategyMock = ToNumberPolicy.DOUBLE; // Use a real strategy for coverage
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

    verify(in).peek();
    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_boolean() throws IOException {
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
  void testRead_string() throws IOException {
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
  void testRead_number() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(in.nextDouble()).thenReturn(123.45);

    // Using real ToNumberStrategy.DOUBLE returns Double
    Object result = adapter.read(in);

    verify(in).peek();
    verify(in).nextDouble();
    assertTrue(result instanceof Number);
    assertEquals(123.45, ((Number) result).doubleValue());
  }

  @Test
    @Timeout(8000)
  void testRead_beginArray() throws Exception {
    JsonReader in = mock(JsonReader.class);
    // Adjusted to simulate peek calls properly
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.STRING, JsonToken.END_ARRAY);
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();
    when(in.nextString()).thenReturn("elem");

    // We spy adapter to call real read for nested elements
    ObjectTypeAdapter spyAdapter = spy(adapter);
    // Remove any stubbing of spyAdapter.read to allow real calls
    doCallRealMethod().when(spyAdapter).read(any(JsonReader.class));

    Object result = spyAdapter.read(in);

    InOrder inOrder = inOrder(in);
    inOrder.verify(in).peek();
    inOrder.verify(in).beginArray();
    inOrder.verify(in).peek();
    inOrder.verify(in).nextString();
    inOrder.verify(in).peek();
    inOrder.verify(in).endArray();

    assertTrue(result instanceof ArrayList);
    assertEquals(1, ((ArrayList<?>) result).size());
    assertEquals("elem", ((ArrayList<?>) result).get(0));
  }

  @Test
    @Timeout(8000)
  void testRead_beginObject() throws Exception {
    JsonReader in = mock(JsonReader.class);
    // Adjusted to simulate peek calls properly
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT, JsonToken.NAME, JsonToken.STRING, JsonToken.END_OBJECT);
    doNothing().when(in).beginObject();
    doNothing().when(in).endObject();
    when(in.nextName()).thenReturn("key");
    when(in.nextString()).thenReturn("value");

    ObjectTypeAdapter spyAdapter = spy(adapter);
    // Remove any stubbing of spyAdapter.read to allow real calls
    doCallRealMethod().when(spyAdapter).read(any(JsonReader.class));

    Object result = spyAdapter.read(in);

    InOrder inOrder = inOrder(in);
    inOrder.verify(in).peek();
    inOrder.verify(in).beginObject();
    inOrder.verify(in).peek();
    inOrder.verify(in).nextName();
    inOrder.verify(in).peek();
    inOrder.verify(in).nextString();
    inOrder.verify(in).peek();
    inOrder.verify(in).endObject();

    assertTrue(result instanceof java.util.Map);
    assertEquals("value", ((java.util.Map<?, ?>) result).get("key"));
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
    // Mock gson.toJsonTree to avoid NullPointerException in adapter.write
    when(gsonMock.toJsonTree(any())).thenReturn(mock(com.google.gson.JsonElement.class));
    // Also mock gson.getAdapter to avoid NullPointerException when adapter.write calls it
    when(gsonMock.getAdapter(any(TypeToken.class))).thenReturn(mock(com.google.gson.TypeAdapter.class));
    adapter.write(out, "stringValue");

    // Since real write delegates to gson.toJsonTree etc,
    // just verify no null calls and no exceptions thrown
    verify(out, never()).nullValue();
  }

  @Test
    @Timeout(8000)
  void testTryBeginNesting_beginArray() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    doNothing().when(in).beginArray();

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.BEGIN_ARRAY);

    verify(in).beginArray();
    assertTrue(result instanceof ArrayDeque);
  }

  @Test
    @Timeout(8000)
  void testTryBeginNesting_beginObject() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    doNothing().when(in).beginObject();

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.BEGIN_OBJECT);

    verify(in).beginObject();
    assertTrue(result instanceof LinkedTreeMap);
  }

  @Test
    @Timeout(8000)
  void testTryBeginNesting_other() throws Exception {
    JsonReader in = mock(JsonReader.class);

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.NAME);

    assertNull(result);
    verify(in, never()).beginArray();
    verify(in, never()).beginObject();
  }

  @Test
    @Timeout(8000)
  void testReadTerminal_null() throws Exception {
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
  void testReadTerminal_boolean() throws Exception {
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
  void testReadTerminal_string() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("stringVal");

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.STRING);

    verify(in).nextString();
    assertEquals("stringVal", result);
  }

  @Test
    @Timeout(8000)
  void testReadTerminal_number() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(in.nextDouble()).thenReturn(42.0);

    Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    Object result = method.invoke(adapter, in, JsonToken.NUMBER);

    verify(in).nextDouble();
    assertTrue(result instanceof Number);
    assertEquals(42.0, ((Number) result).doubleValue());
  }
}