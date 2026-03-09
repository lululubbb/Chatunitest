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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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
    TypeAdapter<?> adapter = (TypeAdapter<?>) longAdapterMethod.invoke(null, LongSerializationPolicy.DEFAULT);
    assertSame(TypeAdapters.LONG, adapter);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_withNonDefaultPolicy_readNull() throws Exception {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);

    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);

    Number result = adapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_withNonDefaultPolicy_readLong() throws Exception {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);

    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(in.nextLong()).thenReturn(123456789L);

    Number result = adapter.read(in);

    verify(in, never()).nextNull();
    verify(in).nextLong();
    assertEquals(123456789L, result);
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_withNonDefaultPolicy_writeNull() throws Exception {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);

    JsonWriter out = mock(JsonWriter.class);
    adapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void testLongAdapter_withNonDefaultPolicy_writeNumberValue() throws Exception {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.STRING);

    JsonWriter out = mock(JsonWriter.class);
    Number value = 987654321L;

    adapter.write(out, value);

    verify(out).value(value.toString());
  }
}