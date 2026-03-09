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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLong;

class GsonAtomicLongAdapterTest {

  private TypeAdapter<Number> longAdapterMock;
  private TypeAdapter<AtomicLong> atomicLongAdapter;

  @BeforeEach
  public void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    longAdapterMock = mock(TypeAdapter.class);

    Method atomicLongAdapterMethod = Gson.class.getDeclaredMethod("atomicLongAdapter", TypeAdapter.class);
    atomicLongAdapterMethod.setAccessible(true);
    atomicLongAdapter = (TypeAdapter<AtomicLong>) atomicLongAdapterMethod.invoke(null, longAdapterMock);
  }

  @Test
    @Timeout(8000)
  public void testWriteDelegatesToLongAdapter() throws IOException {
    AtomicLong atomicLong = new AtomicLong(123L);
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    atomicLongAdapter.write(jsonWriter, atomicLong);

    verify(longAdapterMock).write(eq(jsonWriter), eq(123L));
  }

  @Test
    @Timeout(8000)
  public void testWriteWithNullValue() throws IOException {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    atomicLongAdapter.write(jsonWriter, null);

    // Because nullSafe() is called, writing null should produce JSON null
    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testReadDelegatesToLongAdapter() throws IOException {
    AtomicLong expectedAtomicLong = new AtomicLong(456L);

    JsonReader jsonReader = new JsonReader(new StringReader("456"));
    when(longAdapterMock.read(any(JsonReader.class))).thenReturn(456);

    AtomicLong result = atomicLongAdapter.read(jsonReader);

    verify(longAdapterMock).read(any(JsonReader.class));
    assertNotNull(result);
    assertEquals(expectedAtomicLong.get(), result.get());
  }

  @Test
    @Timeout(8000)
  public void testReadWithNullValue() throws IOException {
    JsonReader jsonReader = new JsonReader(new StringReader("null"));
    when(longAdapterMock.read(any(JsonReader.class))).thenReturn(null);

    AtomicLong result = atomicLongAdapter.read(jsonReader);

    verify(longAdapterMock).read(any(JsonReader.class));
    assertNotNull(result);
    assertEquals(0L, result.get());
  }
}