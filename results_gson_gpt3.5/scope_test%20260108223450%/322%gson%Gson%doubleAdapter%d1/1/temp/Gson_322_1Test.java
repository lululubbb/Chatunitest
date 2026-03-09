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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GsonDoubleAdapterTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_returnsTypeAdaptersDouble_whenSerializeSpecialFloatingPointValuesTrue()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) method.invoke(gson, true);

    assertSame(TypeAdapters.DOUBLE, adapter);
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_read_returnsNullOnJsonNull() throws Exception {
    TypeAdapter<Number> adapter = getDoubleAdapter(false);

    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);

    Number result = adapter.read(reader);

    verify(reader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_read_returnsDoubleValue() throws Exception {
    TypeAdapter<Number> adapter = getDoubleAdapter(false);

    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NUMBER);
    when(reader.nextDouble()).thenReturn(123.456);

    Number result = adapter.read(reader);

    assertEquals(123.456, result.doubleValue());
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_write_writesNullValue() throws Exception {
    TypeAdapter<Number> adapter = getDoubleAdapter(false);

    JsonWriter writer = mock(JsonWriter.class);

    adapter.write(writer, null);

    verify(writer).nullValue();
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_write_writesDoubleValue() throws Exception {
    TypeAdapter<Number> adapter = getDoubleAdapter(false);

    JsonWriter writer = mock(JsonWriter.class);

    // Use a valid double value
    Double value = 42.42;

    adapter.write(writer, value);

    verify(writer).value(value);
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_write_throwsOnNaN() throws Exception {
    TypeAdapter<Number> adapter = getDoubleAdapter(false);

    JsonWriter writer = mock(JsonWriter.class);

    Number nanValue = Double.NaN;

    IOException thrown = assertThrows(IOException.class, () -> adapter.write(writer, nanValue));
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_write_throwsOnPositiveInfinity() throws Exception {
    TypeAdapter<Number> adapter = getDoubleAdapter(false);

    JsonWriter writer = mock(JsonWriter.class);

    Number infValue = Double.POSITIVE_INFINITY;

    IOException thrown = assertThrows(IOException.class, () -> adapter.write(writer, infValue));
    assertTrue(thrown.getMessage().contains("Infinity"));
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_write_throwsOnNegativeInfinity() throws Exception {
    TypeAdapter<Number> adapter = getDoubleAdapter(false);

    JsonWriter writer = mock(JsonWriter.class);

    Number negInfValue = Double.NEGATIVE_INFINITY;

    IOException thrown = assertThrows(IOException.class, () -> adapter.write(writer, negInfValue));
    assertTrue(thrown.getMessage().contains("Infinity"));
  }

  private TypeAdapter<Number> getDoubleAdapter(boolean serializeSpecialFloatingPointValues)
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    method.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) method.invoke(gson, serializeSpecialFloatingPointValues);
    return adapter;
  }
}