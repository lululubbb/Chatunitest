package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLongArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Gson_atomicLongArrayAdapter_Test {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void write_and_read_should_serialize_and_deserialize_atomicLongArray() throws Exception {
    // Arrange
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);
    AtomicLongArray array = new AtomicLongArray(3);
    array.set(0, 10L);
    array.set(1, 20L);
    array.set(2, 30L);

    // Prepare JsonWriter to capture output
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    // Mock longAdapter.write to write numbers to JsonWriter
    doAnswer(invocation -> {
      JsonWriter out = invocation.getArgument(0);
      Number value = invocation.getArgument(1);
      out.value(value.longValue());
      return null;
    }).when(longAdapter).write(any(JsonWriter.class), any());

    // Use reflection to access private static method atomicLongArrayAdapter
    Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

    // Act: write AtomicLongArray to JSON
    adapter.write(jsonWriter, array);
    jsonWriter.close();
    String jsonOutput = stringWriter.toString();

    // Assert JSON output format
    assertEquals("[10,20,30]", jsonOutput);

    // Prepare JsonReader to read from JSON
    StringReader stringReader = new StringReader(jsonOutput);
    JsonReader jsonReader = new JsonReader(stringReader);

    // Mock longAdapter.read to read numbers from JsonReader
    doAnswer(invocation -> {
      JsonReader in = invocation.getArgument(0);
      if (in.peek() == JsonToken.NUMBER) {
        return in.nextLong();
      }
      throw new IOException("Expected NUMBER token");
    }).when(longAdapter).read(any(JsonReader.class));

    // Act: read AtomicLongArray from JSON
    AtomicLongArray result = adapter.read(jsonReader);

    // Assert the deserialized AtomicLongArray matches original
    assertNotNull(result);
    assertEquals(array.length(), result.length());
    for (int i = 0; i < array.length(); i++) {
      assertEquals(array.get(i), result.get(i));
    }
  }

  @Test
    @Timeout(8000)
  void read_should_return_empty_atomicLongArray_for_empty_json_array() throws Exception {
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);

    Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

    String json = "[]";
    StringReader stringReader = new StringReader(json);
    JsonReader jsonReader = new JsonReader(stringReader);

    // Mock longAdapter.read should not be called since array is empty
    // But just in case, throw if called
    when(longAdapter.read(any(JsonReader.class))).thenThrow(new AssertionError("Should not be called"));

    AtomicLongArray result = adapter.read(jsonReader);

    assertNotNull(result);
    assertEquals(0, result.length());
  }

  @Test
    @Timeout(8000)
  void write_should_handle_empty_atomicLongArray() throws Exception {
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);
    AtomicLongArray array = new AtomicLongArray(0);

    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    doAnswer(invocation -> {
      JsonWriter out = invocation.getArgument(0);
      Number value = invocation.getArgument(1);
      out.value(value.longValue());
      return null;
    }).when(longAdapter).write(any(JsonWriter.class), any());

    Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

    adapter.write(jsonWriter, array);
    jsonWriter.close();

    assertEquals("[]", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void read_should_throw_on_malformed_json() throws Exception {
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);

    Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

    // Malformed JSON (missing end array)
    String json = "[1,2,3";
    StringReader stringReader = new StringReader(json);
    JsonReader jsonReader = new JsonReader(stringReader);

    // Mock longAdapter.read to return values 1,2,3
    when(longAdapter.read(any(JsonReader.class))).thenAnswer(invocation -> {
      JsonReader in = invocation.getArgument(0);
      if (in.peek() == JsonToken.NUMBER) {
        return in.nextLong();
      }
      throw new IOException("Expected NUMBER token");
    });

    assertThrows(IOException.class, () -> adapter.read(jsonReader));
  }
}