package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

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
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class ObjectTypeAdapter_550_4Test {

  private Gson gsonMock;
  private ToNumberStrategy toNumberStrategyMock;
  private ObjectTypeAdapter adapter;

  @BeforeEach
  public void setUp() throws Exception {
    gsonMock = mock(Gson.class);
    toNumberStrategyMock = ToNumberPolicy.DOUBLE;
    Constructor<ObjectTypeAdapter> constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(gsonMock, toNumberStrategyMock);
  }

  @Test
    @Timeout(8000)
  public void testRead_null() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Object result = adapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testRead_booleanTrue() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);
    when(in.nextBoolean()).thenReturn(true);

    Object result = adapter.read(in);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
    @Timeout(8000)
  public void testRead_booleanFalse() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);
    when(in.nextBoolean()).thenReturn(false);

    Object result = adapter.read(in);

    assertEquals(Boolean.FALSE, result);
  }

  @Test
    @Timeout(8000)
  public void testRead_string() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("testString");

    Object result = adapter.read(in);

    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  public void testRead_number() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(in.nextString()).thenReturn("123.45");

    // toNumberStrategyMock is DOUBLE, so expect Double value
    Object result = adapter.read(in);

    assertTrue(result instanceof Number);
    assertEquals(123.45d, ((Number) result).doubleValue());
  }

  @Test
    @Timeout(8000)
  public void testRead_beginArray_empty() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.END_ARRAY);
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();

    Object result = adapter.read(in);

    assertTrue(result instanceof java.util.List);
    assertTrue(((java.util.List<?>) result).isEmpty());
    InOrder inOrder = inOrder(in);
    inOrder.verify(in).beginArray();
    inOrder.verify(in).peek();
    inOrder.verify(in).endArray();
  }

  @Test
    @Timeout(8000)
  public void testRead_beginArray_withValues() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.NUMBER, JsonToken.NUMBER, JsonToken.END_ARRAY);
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();
    when(in.nextString()).thenReturn("1", "2");

    // Spy adapter to call real readTerminal for numbers
    ObjectTypeAdapter spyAdapter = spy(adapter);
    doReturn(1.0d).when(spyAdapter).readTerminal(in, JsonToken.NUMBER);
    doReturn(2.0d).when(spyAdapter).readTerminal(in, JsonToken.NUMBER);

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof java.util.List);
    java.util.List<?> list = (java.util.List<?>) result;
    assertEquals(2, list.size());
    assertEquals(1.0d, list.get(0));
    assertEquals(2.0d, list.get(1));
  }

  @Test
    @Timeout(8000)
  public void testRead_beginObject_empty() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT, JsonToken.END_OBJECT);
    doNothing().when(in).beginObject();
    doNothing().when(in).endObject();

    Object result = adapter.read(in);

    assertTrue(result instanceof Map);
    assertTrue(((Map<?, ?>) result).isEmpty());
    InOrder inOrder = inOrder(in);
    inOrder.verify(in).beginObject();
    inOrder.verify(in).peek();
    inOrder.verify(in).endObject();
  }

  @Test
    @Timeout(8000)
  public void testRead_beginObject_withValues() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT, JsonToken.NAME, JsonToken.NUMBER, JsonToken.END_OBJECT);
    doNothing().when(in).beginObject();
    doNothing().when(in).endObject();
    when(in.nextName()).thenReturn("key");
    when(in.nextString()).thenReturn("123");

    // Spy adapter to call real readTerminal for number
    ObjectTypeAdapter spyAdapter = spy(adapter);
    doReturn(123.0d).when(spyAdapter).readTerminal(in, JsonToken.NUMBER);

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof Map);
    Map<?, ?> map = (Map<?, ?>) result;
    assertEquals(1, map.size());
    assertEquals(123.0d, map.get("key"));
  }

  @Test
    @Timeout(8000)
  public void testTryBeginNesting_beginArray() throws Exception {
    JsonReader in = mock(JsonReader.class);
    doNothing().when(in).beginArray();

    Method tryBeginNesting = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    tryBeginNesting.setAccessible(true);

    Object result = tryBeginNesting.invoke(adapter, in, JsonToken.BEGIN_ARRAY);

    assertTrue(result instanceof java.util.List);
    verify(in).beginArray();
  }

  @Test
    @Timeout(8000)
  public void testTryBeginNesting_beginObject() throws Exception {
    JsonReader in = mock(JsonReader.class);
    doNothing().when(in).beginObject();

    Method tryBeginNesting = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    tryBeginNesting.setAccessible(true);

    Object result = tryBeginNesting.invoke(adapter, in, JsonToken.BEGIN_OBJECT);

    assertTrue(result instanceof Map);
    verify(in).beginObject();
  }

  @Test
    @Timeout(8000)
  public void testTryBeginNesting_notNesting() throws Exception {
    JsonReader in = mock(JsonReader.class);

    Method tryBeginNesting = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    tryBeginNesting.setAccessible(true);

    Object result = tryBeginNesting.invoke(adapter, in, JsonToken.STRING);

    assertNull(result);
    verify(in, never()).beginArray();
    verify(in, never()).beginObject();
  }

  @Test
    @Timeout(8000)
  public void testReadTerminal_null() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Method readTerminal = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminal.setAccessible(true);

    Object result = readTerminal.invoke(adapter, in, JsonToken.NULL);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadTerminal_boolean() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);
    when(in.nextBoolean()).thenReturn(true);

    Method readTerminal = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminal.setAccessible(true);

    Object result = readTerminal.invoke(adapter, in, JsonToken.BOOLEAN);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
    @Timeout(8000)
  public void testReadTerminal_string() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("abc");

    Method readTerminal = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminal.setAccessible(true);

    Object result = readTerminal.invoke(adapter, in, JsonToken.STRING);

    assertEquals("abc", result);
  }

  @Test
    @Timeout(8000)
  public void testWrite_null() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  public void testWrite_primitive() throws IOException {
    JsonWriter out = mock(JsonWriter.class);
    Integer value = 42;

    TypeAdapter<Integer> intAdapter = mock(TypeAdapter.class);
    when(gsonMock.getAdapter(TypeToken.get(Integer.class))).thenReturn(intAdapter);

    adapter.write(out, value);

    verify(gsonMock).getAdapter(TypeToken.get(Integer.class));
    verify(intAdapter).write(out, value);
  }

  @Test
    @Timeout(8000)
  public void testGetFactory_returnsFactory() {
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertNotNull(factory);
  }
}