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
import java.io.StringReader;
import java.io.StringWriter;
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
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Gson_atomicLongAdapter_Test {

  private Method atomicLongAdapterMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    atomicLongAdapterMethod = Gson.class.getDeclaredMethod("atomicLongAdapter", TypeAdapter.class);
    atomicLongAdapterMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void atomicLongAdapter_write_and_read() throws Throwable {
    // Arrange
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);

    // Mock longAdapter.write to do nothing
    doNothing().when(longAdapter).write(any(JsonWriter.class), any(Number.class));
    // Mock longAdapter.read to return a Number with longValue 123L
    when(longAdapter.read(any(JsonReader.class))).thenReturn(123L);

    // Invoke private static method atomicLongAdapter
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter =
        (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, longAdapter);

    // Prepare mocks for JsonWriter and JsonReader
    JsonWriter jsonWriter = mock(JsonWriter.class);
    JsonReader jsonReader = mock(JsonReader.class);

    AtomicLong atomicLongValue = new AtomicLong(456L);

    // Act
    atomicLongAdapter.write(jsonWriter, atomicLongValue);
    AtomicLong result = atomicLongAdapter.read(jsonReader);

    // Assert
    // Verify that longAdapter.write was called with jsonWriter and atomicLongValue.get()
    verify(longAdapter).write(eq(jsonWriter), eq(atomicLongValue.get()));

    // Verify that longAdapter.read was called with jsonReader
    verify(longAdapter).read(eq(jsonReader));

    // The result AtomicLong should have the longValue returned by longAdapter.read
    assertNotNull(result);
    assertEquals(123L, result.get());

    // Test nullSafe wrapper: writing null should NOT call longAdapter.write because nullSafe handles nulls
    atomicLongAdapter.write(jsonWriter, null);
    // The underlying adapter's write should NOT be called with null because nullSafe handles nulls internally.
    verify(longAdapter, never()).write(eq(jsonWriter), isNull());

    // Test nullSafe wrapper: reading null should return null
    // To simulate reading null, mock longAdapter.read to return null
    when(longAdapter.read(any(JsonReader.class))).thenReturn(null);
    AtomicLong nullResult = atomicLongAdapter.read(jsonReader);
    assertNull(nullResult);
  }
}