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

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

class Gson_atomicLongAdapter_Test {

  private Method atomicLongAdapterMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    atomicLongAdapterMethod = Gson.class.getDeclaredMethod("atomicLongAdapter", TypeAdapter.class);
    atomicLongAdapterMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void atomicLongAdapter_writeAndRead() throws Throwable {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);

    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter =
        (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, longAdapter);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    JsonReader jsonReader = mock(JsonReader.class);

    AtomicLong atomicLongValue = new AtomicLong(123456789L);

    atomicLongAdapter.write(jsonWriter, atomicLongValue);
    verify(longAdapter).write(jsonWriter, atomicLongValue.get());

    Number numberValue = 987654321L;
    when(longAdapter.read(jsonReader)).thenReturn(numberValue);

    AtomicLong result = atomicLongAdapter.read(jsonReader);
    assertNotNull(result);
    assertEquals(numberValue.longValue(), result.get());
  }

  @Test
    @Timeout(8000)
  void atomicLongAdapter_write_nullSafe() throws Throwable {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter =
        (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, longAdapter);

    JsonWriter jsonWriter = mock(JsonWriter.class);

    atomicLongAdapter.write(jsonWriter, null);

    verify(longAdapter, never()).write(any(), anyLong());
  }

  @Test
    @Timeout(8000)
  void atomicLongAdapter_read_nullSafe() throws Throwable {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);
    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter =
        (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, longAdapter);

    JsonReader jsonReader = mock(JsonReader.class);

    when(longAdapter.read(jsonReader)).thenReturn(null);

    AtomicLong result = atomicLongAdapter.read(jsonReader);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void atomicLongAdapter_write_throwsIOException() throws Throwable {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);
    doThrow(new IOException("write error")).when(longAdapter).write(any(JsonWriter.class), anyLong());

    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter =
        (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, longAdapter);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    AtomicLong atomicLongValue = new AtomicLong(1L);

    IOException thrown = assertThrows(IOException.class, () -> atomicLongAdapter.write(jsonWriter, atomicLongValue));
    assertEquals("write error", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void atomicLongAdapter_read_throwsIOException() throws Throwable {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);
    when(longAdapter.read(any(JsonReader.class))).thenThrow(new IOException("read error"));

    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter =
        (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, longAdapter);

    JsonReader jsonReader = mock(JsonReader.class);

    IOException thrown = assertThrows(IOException.class, () -> atomicLongAdapter.read(jsonReader));
    assertEquals("read error", thrown.getMessage());
  }
}