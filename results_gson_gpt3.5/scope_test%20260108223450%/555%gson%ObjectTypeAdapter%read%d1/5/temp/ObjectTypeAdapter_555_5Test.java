package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.ToNumberStrategy;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class ObjectTypeAdapter_555_5Test {

  private ObjectTypeAdapter adapter;
  private Gson gson;
  private ToNumberStrategy toNumberStrategy;

  @BeforeEach
  void setUp() throws Exception {
    gson = mock(Gson.class);
    toNumberStrategy = mock(ToNumberStrategy.class);
    // Use reflection to invoke private constructor
    var constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
    constructor.setAccessible(true);
    adapter = (ObjectTypeAdapter) constructor.newInstance(gson, toNumberStrategy);
  }

  @Test
    @Timeout(8000)
  void read_terminalValue_returnsExpected() throws Exception {
    JsonReader in = mock(JsonReader.class);
    // Setup peek to return a token that tryBeginNesting returns null for
    when(in.peek()).thenReturn(JsonToken.STRING);
    // tryBeginNesting returns null -> readTerminal is called
    // Use reflection to invoke private readTerminal method for expected value
    Method readTerminalMethod = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminalMethod.setAccessible(true);
    Object expected = readTerminalMethod.invoke(adapter, in, JsonToken.STRING);

    Object result = adapter.read(in);

    assertEquals(expected, result);
    verify(in).peek();
  }

  @Test
    @Timeout(8000)
  void read_listWithNestedValues_readsCorrectly() throws Exception {
    JsonReader in = mock(JsonReader.class);

    // Setup peek sequence to simulate JSON array with nested array and primitives
    when(in.peek())
        .thenReturn(JsonToken.BEGIN_ARRAY)  // initial peek -> tryBeginNesting returns List
        .thenReturn(JsonToken.NUMBER)       // peek inside array element 1 -> terminal
        .thenReturn(JsonToken.BEGIN_ARRAY)  // peek inside array element 2 -> nested list
        .thenReturn(JsonToken.NUMBER)       // nested array element 1
        .thenReturn(JsonToken.NUMBER)       // nested array element 2
        .thenReturn(JsonToken.END_ARRAY)    // end nested array
        .thenReturn(JsonToken.END_ARRAY);   // end outer array

    // Setup hasNext for outer and inner lists
    when(in.hasNext())
        .thenReturn(true, true, false) // outer array has 2 elements then ends
        .thenReturn(true, true, false); // inner array has 2 elements then ends

    // Setup nextName to never be called for List
    // Setup endArray calls
    doNothing().when(in).endArray();

    // Setup readTerminal to return values for NUMBER tokens
    Method readTerminalMethod = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminalMethod.setAccessible(true);

    // We cannot mock private methods directly, so we rely on the real method readTerminal inside read
    // Setup in.peek() to return NUMBER for terminal reads
    // We simulate readTerminal by mocking JsonReader behavior:
    // Since readTerminal uses JsonReader to read values, but we cannot mock internals easily,
    // we will mock readTerminal via reflection to return simple values when called

    // Instead, we spy adapter and mock tryBeginNesting and readTerminal to control behavior
    ObjectTypeAdapter spyAdapter = spy(adapter);

    // tryBeginNesting returns List for BEGIN_ARRAY, null otherwise
    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.BEGIN_ARRAY) {
        return new java.util.ArrayList<>();
      }
      if (token == JsonToken.BEGIN_OBJECT) {
        return new java.util.LinkedHashMap<String, Object>();
      }
      return null;
    }).when(spyAdapter).tryBeginNesting(any(JsonReader.class), any(JsonToken.class));

    // readTerminal returns fixed values for NUMBER tokens
    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.NUMBER) {
        return 42;
      }
      return null;
    }).when(spyAdapter).readTerminal(any(JsonReader.class), any(JsonToken.class));

    // Setup JsonReader mock for hasNext and endArray
    when(in.hasNext())
        .thenReturn(true, true, false) // outer array: 2 elements then end
        .thenReturn(true, true, false); // inner array: 2 elements then end

    doNothing().when(in).endArray();

    when(in.peek())
        .thenReturn(JsonToken.BEGIN_ARRAY)  // initial peek for outer list
        .thenReturn(JsonToken.NUMBER)       // first element terminal
        .thenReturn(JsonToken.BEGIN_ARRAY)  // second element nested list
        .thenReturn(JsonToken.NUMBER)       // nested first element
        .thenReturn(JsonToken.NUMBER)       // nested second element
        .thenReturn(JsonToken.END_ARRAY)    // end nested list
        .thenReturn(JsonToken.END_ARRAY);   // end outer list

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof List);
    List<?> outerList = (List<?>) result;
    assertEquals(2, outerList.size());
    assertEquals(42, outerList.get(0));
    assertTrue(outerList.get(1) instanceof List);
    List<?> innerList = (List<?>) outerList.get(1);
    assertEquals(2, innerList.size());
    assertEquals(42, innerList.get(0));
    assertEquals(42, innerList.get(1));

    InOrder inOrder = inOrder(in);
    inOrder.verify(in).peek();
    inOrder.verify(in, times(3)).hasNext();
    inOrder.verify(in, times(2)).endArray();
  }

  @Test
    @Timeout(8000)
  void read_mapWithNestedValues_readsCorrectly() throws Exception {
    JsonReader in = mock(JsonReader.class);

    // Setup peek sequence to simulate JSON object with nested object and primitives
    when(in.peek())
        .thenReturn(JsonToken.BEGIN_OBJECT) // initial peek -> tryBeginNesting returns Map
        .thenReturn(JsonToken.STRING)       // value for key1 terminal
        .thenReturn(JsonToken.BEGIN_OBJECT) // value for key2 nested object
        .thenReturn(JsonToken.STRING)       // nested value for nestedKey terminal
        .thenReturn(JsonToken.END_OBJECT)   // end nested object
        .thenReturn(JsonToken.END_OBJECT);  // end outer object

    // Setup hasNext for outer and inner objects
    when(in.hasNext())
        .thenReturn(true, true, false)  // outer object has 2 entries then ends
        .thenReturn(true, false);        // inner object has 1 entry then ends

    // Setup nextName calls for outer and inner objects
    when(in.nextName())
        .thenReturn("key1", "key2", "nestedKey");

    // Setup endObject calls
    doNothing().when(in).endObject();

    // Spy adapter to mock tryBeginNesting and readTerminal
    ObjectTypeAdapter spyAdapter = spy(adapter);

    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.BEGIN_OBJECT) {
        return new java.util.LinkedHashMap<String, Object>();
      }
      if (token == JsonToken.BEGIN_ARRAY) {
        return new java.util.ArrayList<>();
      }
      return null;
    }).when(spyAdapter).tryBeginNesting(any(JsonReader.class), any(JsonToken.class));

    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.STRING) {
        return "value";
      }
      return null;
    }).when(spyAdapter).readTerminal(any(JsonReader.class), any(JsonToken.class));

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof Map);
    Map<?, ?> outerMap = (Map<?, ?>) result;
    assertEquals(2, outerMap.size());
    assertEquals("value", outerMap.get("key1"));
    assertTrue(outerMap.get("key2") instanceof Map);
    Map<?, ?> innerMap = (Map<?, ?>) outerMap.get("key2");
    assertEquals(1, innerMap.size());
    assertEquals("value", innerMap.get("nestedKey"));

    InOrder inOrder = inOrder(in);
    inOrder.verify(in).peek();
    inOrder.verify(in, times(3)).hasNext();
    inOrder.verify(in, times(2)).nextName();
    inOrder.verify(in, times(2)).endObject();
  }

  @Test
    @Timeout(8000)
  void read_emptyArray_returnsEmptyList() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.END_ARRAY);
    when(in.hasNext()).thenReturn(false);
    doNothing().when(in).endArray();

    ObjectTypeAdapter spyAdapter = spy(adapter);
    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.BEGIN_ARRAY) {
        return new java.util.ArrayList<>();
      }
      return null;
    }).when(spyAdapter).tryBeginNesting(any(JsonReader.class), any(JsonToken.class));

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof List);
    List<?> list = (List<?>) result;
    assertTrue(list.isEmpty());

    verify(in).endArray();
  }

  @Test
    @Timeout(8000)
  void read_emptyObject_returnsEmptyMap() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.BEGIN_OBJECT, JsonToken.END_OBJECT);
    when(in.hasNext()).thenReturn(false);
    doNothing().when(in).endObject();

    ObjectTypeAdapter spyAdapter = spy(adapter);
    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.BEGIN_OBJECT) {
        return new java.util.LinkedHashMap<String, Object>();
      }
      return null;
    }).when(spyAdapter).tryBeginNesting(any(JsonReader.class), any(JsonToken.class));

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof Map);
    Map<?, ?> map = (Map<?, ?>) result;
    assertTrue(map.isEmpty());

    verify(in).endObject();
  }
}