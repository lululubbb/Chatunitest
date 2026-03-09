package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ObjectTypeAdapter_550_2Test {

  private Gson gsonMock;
  private ToNumberStrategy toNumberStrategyMock;
  private ObjectTypeAdapter objectTypeAdapter;

  @BeforeEach
  public void setUp() throws Exception {
    gsonMock = mock(Gson.class);
    toNumberStrategyMock = mock(ToNumberStrategy.class);

    Constructor<ObjectTypeAdapter> constructor =
        ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
    constructor.setAccessible(true);
    objectTypeAdapter = constructor.newInstance(gsonMock, toNumberStrategyMock);
  }

  @Test
    @Timeout(8000)
  public void testRead_Null() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Object result = objectTypeAdapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testRead_Boolean() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);
    when(in.nextBoolean()).thenReturn(true);

    Object result = objectTypeAdapter.read(in);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
    @Timeout(8000)
  public void testRead_String() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("testString");

    Object result = objectTypeAdapter.read(in);

    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  public void testRead_Number() throws IOException {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);

    // Mock toNumberStrategy to return a Number
    when(toNumberStrategyMock.readNumber(in)).thenReturn(12345L);

    Object result = objectTypeAdapter.read(in);

    assertEquals(12345L, result);
  }

  @Test
    @Timeout(8000)
  public void testRead_BeginArray() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.STRING, JsonToken.END_ARRAY);
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();
    when(in.nextString()).thenReturn("elem1");

    // Spy on objectTypeAdapter to mock recursive read calls
    ObjectTypeAdapter spyAdapter = Mockito.spy(objectTypeAdapter);
    doReturn("elem1").when(spyAdapter).read(in);

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof java.util.List);
    java.util.List<?> list = (java.util.List<?>) result;
    assertEquals(1, list.size());
    assertEquals("elem1", list.get(0));

    verify(in).beginArray();
    verify(in).endArray();
  }

  @Test
    @Timeout(8000)
  public void testRead_BeginObject() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT, JsonToken.NAME, JsonToken.STRING, JsonToken.END_OBJECT);
    doNothing().when(in).beginObject();
    doNothing().when(in).endObject();
    when(in.nextName()).thenReturn("key1");
    when(in.nextString()).thenReturn("value1");

    // Spy on objectTypeAdapter to mock recursive read calls
    ObjectTypeAdapter spyAdapter = Mockito.spy(objectTypeAdapter);
    doReturn("value1").when(spyAdapter).read(in);

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof java.util.Map);
    java.util.Map<?, ?> map = (java.util.Map<?, ?>) result;
    assertEquals(1, map.size());
    assertEquals("value1", map.get("key1"));

    verify(in).beginObject();
    verify(in).endObject();
  }

  @Test
    @Timeout(8000)
  public void testTryBeginNesting_BeginArray() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.STRING, JsonToken.END_ARRAY);
    doNothing().when(in).beginArray();
    doNothing().when(in).endArray();
    when(in.nextString()).thenReturn("arrayElem");

    Method tryBeginNesting = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    tryBeginNesting.setAccessible(true);

    // Spy on objectTypeAdapter to mock recursive read calls
    ObjectTypeAdapter spyAdapter = Mockito.spy(objectTypeAdapter);
    doReturn("arrayElem").when(spyAdapter).read(in);

    Object result = tryBeginNesting.invoke(spyAdapter, in, JsonToken.BEGIN_ARRAY);

    assertTrue(result instanceof java.util.List);
    java.util.List<?> list = (java.util.List<?>) result;
    assertEquals(1, list.size());
    assertEquals("arrayElem", list.get(0));
  }

  @Test
    @Timeout(8000)
  public void testTryBeginNesting_BeginObject() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT, JsonToken.NAME, JsonToken.STRING, JsonToken.END_OBJECT);
    doNothing().when(in).beginObject();
    doNothing().when(in).endObject();
    when(in.nextName()).thenReturn("k");
    when(in.nextString()).thenReturn("v");

    Method tryBeginNesting = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    tryBeginNesting.setAccessible(true);

    ObjectTypeAdapter spyAdapter = Mockito.spy(objectTypeAdapter);
    doReturn("v").when(spyAdapter).read(in);

    Object result = tryBeginNesting.invoke(spyAdapter, in, JsonToken.BEGIN_OBJECT);

    assertTrue(result instanceof java.util.Map);
    java.util.Map<?, ?> map = (java.util.Map<?, ?>) result;
    assertEquals(1, map.size());
    assertEquals("v", map.get("k"));
  }

  @Test
    @Timeout(8000)
  public void testTryBeginNesting_NotBeginArrayOrObject() throws Exception {
    JsonReader in = mock(JsonReader.class);

    Method tryBeginNesting = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    tryBeginNesting.setAccessible(true);

    Object result = tryBeginNesting.invoke(objectTypeAdapter, in, JsonToken.STRING);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadTerminal_Boolean() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BOOLEAN);
    when(in.nextBoolean()).thenReturn(true);

    Method readTerminal = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminal.setAccessible(true);

    Object result = readTerminal.invoke(objectTypeAdapter, in, JsonToken.BOOLEAN);

    assertEquals(Boolean.TRUE, result);
  }

  @Test
    @Timeout(8000)
  public void testReadTerminal_Null() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).nextNull();

    Method readTerminal = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminal.setAccessible(true);

    Object result = readTerminal.invoke(objectTypeAdapter, in, JsonToken.NULL);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadTerminal_Number() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);

    when(toNumberStrategyMock.readNumber(in)).thenReturn(42);

    Method readTerminal = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminal.setAccessible(true);

    Object result = readTerminal.invoke(objectTypeAdapter, in, JsonToken.NUMBER);

    assertEquals(42, result);
  }

  @Test
    @Timeout(8000)
  public void testReadTerminal_String() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextString()).thenReturn("stringVal");

    Method readTerminal = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminal.setAccessible(true);

    Object result = readTerminal.invoke(objectTypeAdapter, in, JsonToken.STRING);

    assertEquals("stringVal", result);
  }

  @Test
    @Timeout(8000)
  public void testWrite_Null() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    objectTypeAdapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  public void testWrite_Primitive() throws IOException {
    JsonWriter out = mock(JsonWriter.class);

    // Mock gson.getAdapter to return a dummy adapter that writes a string
    TypeAdapter<String> stringAdapter = mock(TypeAdapter.class);
    doAnswer(invocation -> {
      JsonWriter writer = invocation.getArgument(0);
      writer.value("written");
      return null;
    }).when(stringAdapter).write(any(JsonWriter.class), any());

    when(gsonMock.getAdapter(String.class)).thenReturn(stringAdapter);

    objectTypeAdapter.write(out, "testValue");

    verify(stringAdapter).write(out, "testValue");
  }

  @Test
    @Timeout(8000)
  public void testGetFactory() {
    TypeAdapterFactory factory = ObjectTypeAdapter.getFactory(ToNumberPolicy.DOUBLE);
    assertNotNull(factory);
  }

  @Test
    @Timeout(8000)
  public void testNewFactory_StaticMethod() throws Exception {
    Method newFactory = ObjectTypeAdapter.class.getDeclaredMethod("newFactory", ToNumberStrategy.class);
    newFactory.setAccessible(true);

    Object factory = newFactory.invoke(null, ToNumberPolicy.DOUBLE);
    assertNotNull(factory);
  }
}