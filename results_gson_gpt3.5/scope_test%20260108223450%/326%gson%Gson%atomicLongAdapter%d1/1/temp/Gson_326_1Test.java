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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Gson_atomicLongAdapter_Test {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void atomicLongAdapter_writeAndRead_shouldWriteAndReadAtomicLong() throws Exception {
    // Arrange
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);
    doAnswer(invocation -> {
      JsonWriter out = invocation.getArgument(0);
      Number value = invocation.getArgument(1);
      out.value(value.longValue());
      return null;
    }).when(longAdapter).write(any(JsonWriter.class), any(Number.class));
    when(longAdapter.read(any(JsonReader.class))).thenAnswer(invocation -> 123L);

    Method atomicLongAdapterMethod = Gson.class.getDeclaredMethod("atomicLongAdapter", TypeAdapter.class);
    atomicLongAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter = (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, longAdapter);

    AtomicLong atomicLong = new AtomicLong(42L);

    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    // Act - write
    atomicLongAdapter.write(jsonWriter, atomicLong);
    jsonWriter.close();

    String jsonWritten = stringWriter.toString();

    // Prepare to read
    StringReader stringReader = new StringReader(jsonWritten);
    JsonReader jsonReader = new JsonReader(stringReader);

    // Act - read
    AtomicLong readAtomicLong = atomicLongAdapter.read(jsonReader);

    // Assert
    verify(longAdapter).write(any(JsonWriter.class), eq(42L));
    verify(longAdapter).read(any(JsonReader.class));
    assertNotNull(readAtomicLong);
    assertEquals(123L, readAtomicLong.get());
  }

  @Test
    @Timeout(8000)
  void atomicLongAdapter_nullSafe_shouldHandleNullsGracefully() throws Exception {
    // Arrange
    TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);
    when(longAdapter.read(any(JsonReader.class))).thenReturn(null);

    Method atomicLongAdapterMethod = Gson.class.getDeclaredMethod("atomicLongAdapter", TypeAdapter.class);
    atomicLongAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<AtomicLong> atomicLongAdapter = (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, longAdapter);

    // The adapter returned is nullSafe, so it should handle nulls
    String jsonNull = "null";
    JsonReader jsonReader = new JsonReader(new StringReader(jsonNull));

    // Act
    AtomicLong result = atomicLongAdapter.read(jsonReader);

    // Assert
    assertNull(result);
  }
}