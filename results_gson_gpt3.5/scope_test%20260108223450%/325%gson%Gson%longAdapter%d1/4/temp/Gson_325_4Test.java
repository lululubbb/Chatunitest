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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Gson_longAdapter_Test {

  private Method longAdapterMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    longAdapterMethod = Gson.class.getDeclaredMethod("longAdapter", LongSerializationPolicy.class);
    longAdapterMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_withDefaultPolicy_returnsTypeAdaptersLong() throws InvocationTargetException, IllegalAccessException {
    Object adapterObj = longAdapterMethod.invoke(null, LongSerializationPolicy.DEFAULT);
    assertNotNull(adapterObj);
    assertSame(TypeAdapters.LONG, adapterObj);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_withNonDefaultPolicy_readNull() throws Exception {
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
  void testLongAdapter_withNonDefaultPolicy_readLong() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NUMBER);
    when(reader.nextLong()).thenReturn(123456789L);

    Number result = adapter.read(reader);

    verify(reader).peek();
    verify(reader).nextLong();
    assertEquals(123456789L, result);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_withNonDefaultPolicy_writeNull() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);
    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(writer).nullValue();
    verifyNoMoreInteractions(writer);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_withNonDefaultPolicy_writeValue() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);
    JsonWriter writer = mock(JsonWriter.class);
    Number value = 987654321L;

    adapter.write(writer, value);

    verify(writer).value(value.toString());
    verifyNoMoreInteractions(writer);
  }
}