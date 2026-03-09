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
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class Gson_atomicLongAdapter_Test {

  private Method atomicLongAdapterMethod;

  @BeforeEach
  void setup() throws NoSuchMethodException {
    atomicLongAdapterMethod = Gson.class.getDeclaredMethod("atomicLongAdapter", TypeAdapter.class);
    atomicLongAdapterMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testAtomicLongAdapter_write_and_read() throws Throwable {
    // Prepare a mock TypeAdapter<Number>
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> mockLongAdapter = mock(TypeAdapter.class);

    // Stub the read method to return a Number
    when(mockLongAdapter.read(any(JsonReader.class))).thenReturn(123L);

    // Invoke atomicLongAdapter to get TypeAdapter<AtomicLong>
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter =
        (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, mockLongAdapter);

    // Verify that nullSafe() returns a non-null adapter
    assertNotNull(atomicLongAdapter);

    // Prepare JsonWriter and AtomicLong for write test
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    AtomicLong atomicLongValue = new AtomicLong(456L);

    // Call write and verify that mockLongAdapter.write was called with correct value
    atomicLongAdapter.write(jsonWriter, atomicLongValue);
    jsonWriter.flush();

    ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
    verify(mockLongAdapter, times(1)).write(any(JsonWriter.class), captor.capture());
    assertEquals(456L, captor.getValue());

    // Prepare JsonReader for read test
    StringReader stringReader = new StringReader("123");
    JsonReader jsonReader = new JsonReader(stringReader);

    // Call read and verify returned AtomicLong value
    AtomicLong result = atomicLongAdapter.read(jsonReader);
    assertNotNull(result);
    assertEquals(123L, result.get());

    // Verify that mockLongAdapter.read was called once (only during actual read)
    verify(mockLongAdapter, times(1)).read(any(JsonReader.class));
  }

  @Test
    @Timeout(8000)
  void testAtomicLongAdapter_write_null() throws Throwable {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> mockLongAdapter = mock(TypeAdapter.class);
    when(mockLongAdapter.read(any(JsonReader.class))).thenReturn(0L);

    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter =
        (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, mockLongAdapter);

    // The adapter is nullSafe, so writing null should not throw and should write JSON null
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    atomicLongAdapter.write(jsonWriter, null);
    jsonWriter.flush();

    assertEquals("null", stringWriter.toString());

    // Verify that no write call was made to the mockLongAdapter when writing null
    verify(mockLongAdapter, never()).write(any(JsonWriter.class), any());
  }

  @Test
    @Timeout(8000)
  void testAtomicLongAdapter_read_null() throws Throwable {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> mockLongAdapter = mock(TypeAdapter.class);

    // The wrapped adapter read(null) returns null
    when(mockLongAdapter.read(any(JsonReader.class))).thenReturn(null);

    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter =
        (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, mockLongAdapter);

    // Provide JSON "null"
    StringReader stringReader = new StringReader("null");
    JsonReader jsonReader = new JsonReader(stringReader);

    // Because of nullSafe, read(null) returns null AtomicLong
    AtomicLong result = atomicLongAdapter.read(jsonReader);
    assertNull(result);

    // Verify that mockLongAdapter.read was called once
    verify(mockLongAdapter, times(1)).read(any(JsonReader.class));
  }

  @Test
    @Timeout(8000)
  void testAtomicLongAdapter_read_invalid_throws() throws Throwable {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> mockLongAdapter = mock(TypeAdapter.class);

    // Simulate the wrapped adapter throwing IOException on read
    when(mockLongAdapter.read(any(JsonReader.class))).thenThrow(new IOException("mock exception"));

    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter =
        (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, mockLongAdapter);

    StringReader stringReader = new StringReader("123");
    JsonReader jsonReader = new JsonReader(stringReader);

    IOException thrown = assertThrows(IOException.class, () -> atomicLongAdapter.read(jsonReader));
    assertEquals("mock exception", thrown.getMessage());

    // Verify that mockLongAdapter.read was called once
    verify(mockLongAdapter, times(1)).read(any(JsonReader.class));
  }
}