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
import com.google.gson.stream.JsonToken;
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
  void atomicLongArrayAdapter_write_and_read() throws Exception {
    // Arrange
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);

    // Mock write calls to write long values
    doAnswer(invocation -> {
      JsonWriter writer = invocation.getArgument(0);
      Number value = invocation.getArgument(1);
      writer.value(value.longValue());
      return null;
    }).when(longAdapter).write(any(JsonWriter.class), any(Number.class));

    // Mock read calls to return successive long values
    when(longAdapter.read(any(JsonReader.class)))
        .thenAnswer(new org.mockito.stubbing.Answer<Number>() {
          private final long[] values = {10L, 20L, 30L};
          private int index = 0;

          @Override
          public Number answer(org.mockito.invocation.InvocationOnMock invocation) {
            if (index < values.length) {
              return values[index++];
            }
            return 0L;
          }
        });

    // Use reflection to invoke private static method atomicLongArrayAdapter
    Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

    // Prepare AtomicLongArray to write
    AtomicLongArray array = new AtomicLongArray(new long[]{10L, 20L, 30L});

    // Write AtomicLongArray to JSON string
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    adapter.write(jsonWriter, array);
    jsonWriter.close();
    String json = stringWriter.toString();

    // Assert JSON output is an array of numbers [10,20,30]
    assertEquals("[10,20,30]", json);

    // Read AtomicLongArray back from JSON string
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    AtomicLongArray readArray = adapter.read(jsonReader);

    // Assert read AtomicLongArray equals original values
    assertEquals(array.length(), readArray.length());
    for (int i = 0; i < array.length(); i++) {
      assertEquals(array.get(i), readArray.get(i));
    }

    // Verify that longAdapter.write was called exactly 3 times
    verify(longAdapter, times(3)).write(any(JsonWriter.class), any(Number.class));

    // Verify that longAdapter.read was called exactly 3 times (once per value)
    verify(longAdapter, times(3)).read(any(JsonReader.class));
  }

  @Test
    @Timeout(8000)
  void atomicLongArrayAdapter_read_emptyArray() throws Exception {
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);

    // For empty array, read should not be called, so no setup needed

    Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

    String json = "[]";
    JsonReader jsonReader = new JsonReader(new StringReader(json));
    AtomicLongArray readArray = adapter.read(jsonReader);

    assertNotNull(readArray);
    assertEquals(0, readArray.length());

    // Verify that longAdapter.read was never called
    verify(longAdapter, never()).read(any(JsonReader.class));
  }

  @Test
    @Timeout(8000)
  void atomicLongArrayAdapter_write_nullSafe() throws Exception {
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);

    doAnswer(invocation -> {
      JsonWriter writer = invocation.getArgument(0);
      Number value = invocation.getArgument(1);
      writer.value(value.longValue());
      return null;
    }).when(longAdapter).write(any(JsonWriter.class), any(Number.class));

    Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    // The adapter returned is nullSafe, so it should handle null values without throwing
    adapter.write(jsonWriter, null);
    jsonWriter.close();

    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void atomicLongArrayAdapter_read_nullSafe() throws Exception {
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);

    Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

    JsonReader jsonReader = new JsonReader(new StringReader("null"));
    AtomicLongArray result = adapter.read(jsonReader);

    assertNull(result);
  }
}