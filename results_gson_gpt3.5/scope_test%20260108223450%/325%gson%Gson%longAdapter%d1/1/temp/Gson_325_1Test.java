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
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.Reader;
import java.io.StringReader;
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
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Gson_longAdapter_Test {

  private Method longAdapterMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    longAdapterMethod = Gson.class.getDeclaredMethod("longAdapter", LongSerializationPolicy.class);
    longAdapterMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_returnsLongTypeAdapter_forDefaultPolicy() throws InvocationTargetException, IllegalAccessException, NoSuchFieldException {
    TypeAdapter<?> adapter = (TypeAdapter<?>) longAdapterMethod.invoke(null, LongSerializationPolicy.DEFAULT);
    assertNotNull(adapter);

    // Directly access TypeAdapters.LONG field since it's public static final
    Field longField = TypeAdapters.class.getDeclaredField("LONG");
    longField.setAccessible(true);
    Object longAdapter = longField.get(null);

    assertSame(longAdapter, adapter);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_customAdapter_readNull() throws IOException, InvocationTargetException, IllegalAccessException {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);

    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    Number result = adapter.read(reader);

    verify(reader).peek();
    verify(reader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_customAdapter_readLong() throws IOException, InvocationTargetException, IllegalAccessException {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);

    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NUMBER);
    when(reader.nextLong()).thenReturn(123L);

    Number result = adapter.read(reader);

    verify(reader).peek();
    verify(reader).nextLong();
    assertEquals(123L, result);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_customAdapter_writeNull() throws IOException, InvocationTargetException, IllegalAccessException {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);

    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    adapter.write(writer, null);
    writer.flush();

    assertEquals("null", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_customAdapter_writeNumber() throws IOException, InvocationTargetException, IllegalAccessException {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);

    StringWriter stringWriter = new StringWriter();
    JsonWriter writer = new JsonWriter(stringWriter);
    adapter.write(writer, 456L);
    writer.flush();

    assertEquals("\"456\"", stringWriter.toString());
  }
}