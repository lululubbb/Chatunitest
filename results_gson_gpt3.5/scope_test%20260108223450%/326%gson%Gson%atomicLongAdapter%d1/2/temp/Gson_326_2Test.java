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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

class Gson_atomicLongAdapter_Test {

  private TypeAdapter<Number> mockLongAdapter;
  private TypeAdapter<AtomicLong> atomicLongAdapter;

  @BeforeEach
  @SuppressWarnings("unchecked")
  public void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    mockLongAdapter = mock(TypeAdapter.class);

    Method method = Gson.class.getDeclaredMethod("atomicLongAdapter", TypeAdapter.class);
    method.setAccessible(true);
    atomicLongAdapter = (TypeAdapter<AtomicLong>) method.invoke(null, mockLongAdapter);
  }

  @Test
    @Timeout(8000)
  public void write_shouldDelegateToLongAdapter() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);
    AtomicLong atomicLong = new AtomicLong(123L);

    atomicLongAdapter.write(mockWriter, atomicLong);

    verify(mockLongAdapter).write(mockWriter, 123L);
  }

  @Test
    @Timeout(8000)
  public void write_shouldWriteNullWhenValueIsNull() throws IOException {
    JsonWriter mockWriter = mock(JsonWriter.class);

    atomicLongAdapter.write(mockWriter, null);

    verify(mockLongAdapter).write(mockWriter, null);
  }

  @Test
    @Timeout(8000)
  public void read_shouldDelegateToLongAdapterAndWrapInAtomicLong() throws IOException {
    JsonReader mockReader = mock(JsonReader.class);
    Number numberValue = 456L;
    when(mockLongAdapter.read(mockReader)).thenReturn(numberValue);

    AtomicLong result = atomicLongAdapter.read(mockReader);

    verify(mockLongAdapter).read(mockReader);
    assertNotNull(result);
    assertEquals(456L, result.get());
  }

  @Test
    @Timeout(8000)
  public void read_shouldReturnAtomicLongWithZero_whenLongAdapterReturnsNull() throws IOException {
    JsonReader mockReader = mock(JsonReader.class);
    when(mockLongAdapter.read(mockReader)).thenReturn(null);

    AtomicLong result = atomicLongAdapter.read(mockReader);

    assertNotNull(result);
    assertEquals(0L, result.get());
  }
}