package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.util.ArrayDeque;
import java.util.Deque;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

class ObjectTypeAdapter_555_6Test {

  private Gson gson;
  private ToNumberStrategy toNumberStrategy;
  private ObjectTypeAdapter adapter;

  @BeforeEach
  void setUp() throws Exception {
    gson = mock(Gson.class);
    toNumberStrategy = ToNumberPolicy.DOUBLE; // Use existing policy for simplicity
    // Use reflection to invoke private constructor
    var ctor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
    ctor.setAccessible(true);
    adapter = (ObjectTypeAdapter) ctor.newInstance(gson, toNumberStrategy);
  }

  @Test
    @Timeout(8000)
  void read_PrimitiveTerminalValue() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.hasNext()).thenReturn(false);
    when(in.peek()).thenReturn(JsonToken.STRING);
    when(in.nextName()).thenThrow(new IllegalStateException("Should not call nextName for terminal"));
    // Mock readTerminal to return a terminal value
    Method readTerminal = ObjectTypeAdapter.class.getDeclaredMethod("readTerminal", JsonReader.class, JsonToken.class);
    readTerminal.setAccessible(true);
    // Spy adapter to mock readTerminal
    ObjectTypeAdapter spyAdapter = spy(adapter);
    doReturn("terminalValue").when(spyAdapter).readTerminal(in, JsonToken.STRING);
    Object result = spyAdapter.read(in);
    assertEquals("terminalValue", result);
  }

  @Test
    @Timeout(8000)
  void read_ListWithNestedElements() throws Exception {
    JsonReader in = mock(JsonReader.class);
    // Setup JsonReader to simulate JSON array [ "a", { "key": "value" } ]
    when(in.peek())
        .thenReturn(JsonToken.BEGIN_ARRAY) // initial peek
        .thenReturn(JsonToken.STRING) // first element
        .thenReturn(JsonToken.BEGIN_OBJECT) // second element start
        .thenReturn(JsonToken.STRING) // object member value
        .thenReturn(JsonToken.END_OBJECT) // end object
        .thenReturn(JsonToken.END_ARRAY); // end array

    when(in.hasNext())
        .thenReturn(true)  // first element
        .thenReturn(true)  // second element
        .thenReturn(false); // end of array elements

    when(in.nextName()).thenThrow(new IllegalStateException("Should not call nextName inside array"));

    // Spy adapter to mock tryBeginNesting and readTerminal
    ObjectTypeAdapter spyAdapter = spy(adapter);

    // tryBeginNesting returns List for BEGIN_ARRAY, Map for BEGIN_OBJECT, null otherwise
    Method tryBeginNesting = ObjectTypeAdapter.class.getDeclaredMethod("tryBeginNesting", JsonReader.class, JsonToken.class);
    tryBeginNesting.setAccessible(true);

    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.BEGIN_ARRAY) {
        return new ArrayList<>();
      } else if (token == JsonToken.BEGIN_OBJECT) {
        return new HashMap<String, Object>();
      } else {
        return null;
      }
    }).when(spyAdapter).tryBeginNesting(any(JsonReader.class), any(JsonToken.class));

    // readTerminal returns string values for STRING tokens
    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.STRING) {
        return "stringValue";
      }
      return null;
    }).when(spyAdapter).readTerminal(any(JsonReader.class), any(JsonToken.class));

    // Mock JsonReader endArray and endObject to do nothing
    doNothing().when(in).endArray();
    doNothing().when(in).endObject();

    // Call read
    Object result = spyAdapter.read(in);

    assertTrue(result instanceof List);
    List<?> list = (List<?>) result;
    assertEquals(2, list.size());
    assertEquals("stringValue", list.get(0));
    assertTrue(list.get(1) instanceof Map);
    Map<?, ?> map = (Map<?, ?>) list.get(1);
    assertTrue(map.isEmpty()); // Because nextName is never called, map stays empty
  }

  @Test
    @Timeout(8000)
  void read_MapWithNestedElements() throws Exception {
    JsonReader in = mock(JsonReader.class);
    // Setup JsonReader to simulate JSON object {"a": "valueA", "b": [1, 2]}
    when(in.peek())
        .thenReturn(JsonToken.BEGIN_OBJECT) // initial peek
        .thenReturn(JsonToken.STRING)       // value for "a"
        .thenReturn(JsonToken.BEGIN_ARRAY)  // value for "b"
        .thenReturn(JsonToken.NUMBER)       // first element in array
        .thenReturn(JsonToken.NUMBER)       // second element in array
        .thenReturn(JsonToken.END_ARRAY)    // end array
        .thenReturn(JsonToken.END_OBJECT);  // end object

    when(in.hasNext())
        .thenReturn(true)  // for "a"
        .thenReturn(true)  // for "b"
        .thenReturn(false); // end object members

    when(in.nextName())
        .thenReturn("a")
        .thenReturn("b");

    // Spy adapter to mock tryBeginNesting and readTerminal
    ObjectTypeAdapter spyAdapter = spy(adapter);

    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.BEGIN_OBJECT) {
        return new HashMap<String, Object>();
      } else if (token == JsonToken.BEGIN_ARRAY) {
        return new ArrayList<>();
      } else {
        return null;
      }
    }).when(spyAdapter).tryBeginNesting(any(JsonReader.class), any(JsonToken.class));

    doAnswer(invocation -> {
      JsonToken token = invocation.getArgument(1);
      if (token == JsonToken.STRING) {
        return "valueA";
      } else if (token == JsonToken.NUMBER) {
        return 42; // Arbitrary number for test
      }
      return null;
    }).when(spyAdapter).readTerminal(any(JsonReader.class), any(JsonToken.class));

    doNothing().when(in).endArray();
    doNothing().when(in).endObject();

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof Map);
    Map<?, ?> map = (Map<?, ?>) result;
    assertEquals(2, map.size());
    assertEquals("valueA", map.get("a"));
    assertTrue(map.get("b") instanceof List);
    List<?> list = (List<?>) map.get("b");
    assertEquals(2, list.size());
    assertEquals(42, list.get(0));
    assertEquals(42, list.get(1));
  }

  @Test
    @Timeout(8000)
  void read_EmptyArray() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek())
        .thenReturn(JsonToken.BEGIN_ARRAY)
        .thenReturn(JsonToken.END_ARRAY);
    when(in.hasNext()).thenReturn(false);
    doNothing().when(in).endArray();

    ObjectTypeAdapter spyAdapter = spy(adapter);
    doReturn(new ArrayList<>()).when(spyAdapter).tryBeginNesting(any(JsonReader.class), eq(JsonToken.BEGIN_ARRAY));

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof List);
    List<?> list = (List<?>) result;
    assertTrue(list.isEmpty());
  }

  @Test
    @Timeout(8000)
  void read_EmptyObject() throws Exception {
    JsonReader in = mock(JsonReader.class);
    when(in.peek())
        .thenReturn(JsonToken.BEGIN_OBJECT)
        .thenReturn(JsonToken.END_OBJECT);
    when(in.hasNext()).thenReturn(false);
    when(in.nextName()).thenThrow(new IllegalStateException("No next name"));
    doNothing().when(in).endObject();

    ObjectTypeAdapter spyAdapter = spy(adapter);
    doReturn(new HashMap<String, Object>()).when(spyAdapter).tryBeginNesting(any(JsonReader.class), eq(JsonToken.BEGIN_OBJECT));

    Object result = spyAdapter.read(in);

    assertTrue(result instanceof Map);
    Map<?, ?> map = (Map<?, ?>) result;
    assertTrue(map.isEmpty());
  }
}