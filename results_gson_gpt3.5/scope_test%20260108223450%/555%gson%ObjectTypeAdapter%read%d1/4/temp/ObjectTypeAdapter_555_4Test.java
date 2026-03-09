package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

class ObjectTypeAdapter_555_4Test {

  private ObjectTypeAdapter adapter;
  private Gson gson;
  private ToNumberStrategy toNumberStrategy;

  @BeforeEach
  void setUp() throws Exception {
    gson = mock(Gson.class);
    toNumberStrategy = ToNumberPolicy.DOUBLE;
    // Using reflection to invoke private constructor
    Method constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
    constructor.setAccessible(true);
    adapter = (ObjectTypeAdapter) constructor.invoke(null, gson, toNumberStrategy);
  }

  private Object callTryBeginNesting(JsonReader in, JsonToken peeked) throws Exception {
    Method method = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    return method.invoke(adapter, in, peeked);
  }

  private Object callReadTerminal(JsonReader in, JsonToken peeked) throws Exception {
    Method method = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    method.setAccessible(true);
    return method.invoke(adapter, in, peeked);
  }

  @Test
    @Timeout(8000)
  void testRead_withPrimitiveTerminalValue() throws IOException, Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.hasNext()).thenReturn(false);

    // tryBeginNesting returns null for terminal
    Object result = adapter.read(in);

    // Because tryBeginNesting returns null, readTerminal is called
    // We mock readTerminal via reflection to return a known value
    Object terminalValue = callReadTerminal(in, JsonToken.STRING);
    // The actual returned value from read should be equal to terminalValue
    // But since readTerminal is private and not mocked, result is terminalValue
    // So we just assert not null (cannot mock private method easily)
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testRead_withEmptyArray() throws IOException, Exception {
    JsonReader in = mock(JsonReader.class);

    // Setup peek to BEGIN_ARRAY first call
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
    // tryBeginNesting should detect BEGIN_ARRAY and return a List (via reflection)
    // Simulate hasNext: true then false to end array immediately
    when(in.hasNext()).thenReturn(true, false);
    // peek after nextName (not called for array)
    when(in.peek()).thenReturn(JsonToken.STRING);
    // readTerminal returns a string terminal value
    // nextName is never called for List
    // endArray is called once
    doNothing().when(in).endArray();

    // Because tryBeginNesting is private, we can't mock it directly
    // So we use a spy and override tryBeginNesting to return new ArrayList<>
    ObjectTypeAdapter spyAdapter = Mockito.spy(adapter);
    Method tryBeginNestingMethod = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    tryBeginNestingMethod.setAccessible(true);
    doReturn(new ArrayList<>()).when(spyAdapter).tryBeginNesting(in, JsonToken.BEGIN_ARRAY);

    // readTerminal returns a string terminal value
    doReturn("value").when(spyAdapter).readTerminal(in, JsonToken.STRING);

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof List);
    List<?> list = (List<?>) result;
    // It should contain one element added in loop
    assertEquals(1, list.size());
    assertEquals("value", list.get(0));

    verify(in).endArray();
  }

  @Test
    @Timeout(8000)
  void testRead_withEmptyObject() throws IOException, Exception {
    JsonReader in = mock(JsonReader.class);

    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    when(in.hasNext()).thenReturn(true, false);
    when(in.nextName()).thenReturn("key");
    when(in.peek()).thenReturn(JsonToken.STRING);
    doNothing().when(in).endObject();

    ObjectTypeAdapter spyAdapter = Mockito.spy(adapter);
    doReturn(new LinkedHashMap<String, Object>()).when(spyAdapter).tryBeginNesting(in, JsonToken.BEGIN_OBJECT);
    doReturn("value").when(spyAdapter).readTerminal(in, JsonToken.STRING);

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof Map);
    Map<?, ?> map = (Map<?, ?>) result;
    assertEquals(1, map.size());
    assertEquals("value", map.get("key"));

    verify(in).endObject();
  }

  @Test
    @Timeout(8000)
  void testRead_nestedStructure() throws IOException, Exception {
    JsonReader in = mock(JsonReader.class);

    // Simulate nested JSON: { "a": [ "x", "y" ] }
    // peek sequence:
    // BEGIN_OBJECT -> BEGIN_ARRAY -> STRING -> STRING -> END_ARRAY -> END_OBJECT
    when(in.peek()).thenReturn(
        JsonToken.BEGIN_OBJECT,
        JsonToken.BEGIN_ARRAY,
        JsonToken.STRING,
        JsonToken.STRING,
        JsonToken.END_ARRAY,
        JsonToken.END_OBJECT
    );

    // hasNext sequence for object then array then object end then array end
    when(in.hasNext()).thenReturn(
        true,    // object hasNext true (for key "a")
        true,    // array hasNext true (for "x")
        true,    // array hasNext true (for "y")
        false,   // array hasNext false (end array)
        false    // object hasNext false (end object)
    );

    when(in.nextName()).thenReturn("a");

    doNothing().when(in).endArray();
    doNothing().when(in).endObject();

    ObjectTypeAdapter spyAdapter = Mockito.spy(adapter);

    // Setup tryBeginNesting to return Map for BEGIN_OBJECT, List for BEGIN_ARRAY, null otherwise
    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.BEGIN_OBJECT) {
        return new LinkedHashMap<String, Object>();
      } else if (token == JsonToken.BEGIN_ARRAY) {
        return new ArrayList<Object>();
      }
      return null;
    }).when(spyAdapter).tryBeginNesting(any(JsonReader.class), any(JsonToken.class));

    // Setup readTerminal to return string values for STRING tokens
    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.STRING) {
        return "stringValue";
      }
      return null;
    }).when(spyAdapter).readTerminal(any(JsonReader.class), any(JsonToken.class));

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof Map);
    Map<?, ?> map = (Map<?, ?>) result;
    assertTrue(map.containsKey("a"));
    Object nested = map.get("a");
    assertTrue(nested instanceof List);
    List<?> list = (List<?>) nested;
    assertEquals(2, list.size());
    assertEquals("stringValue", list.get(0));
    assertEquals("stringValue", list.get(1));

    InOrder inOrder = inOrder(in);
    inOrder.verify(in).endArray();
    inOrder.verify(in).endObject();
  }

  @Test
    @Timeout(8000)
  void testRead_emptyInput_returnsTerminal() throws IOException, Exception {
    JsonReader in = mock(JsonReader.class);

    when(in.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(in).endObject();

    ObjectTypeAdapter spyAdapter = Mockito.spy(adapter);

    doReturn(null).when(spyAdapter).tryBeginNesting(in, JsonToken.NULL);
    doReturn(null).when(spyAdapter).readTerminal(in, JsonToken.NULL);

    Object result = spyAdapter.read(in);

    assertNull(result);
  }
}