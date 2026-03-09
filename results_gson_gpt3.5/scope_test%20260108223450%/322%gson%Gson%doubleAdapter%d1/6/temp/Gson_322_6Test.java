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
import java.io.IOException;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Gson_doubleAdapter_Test {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_serializeSpecialFloatingPointValuesTrue_returnsTypeAdaptersDOUBLE() throws Exception {
    Method method = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter =
        (TypeAdapter<Number>) method.invoke(gson, true);

    assertSame(TypeAdapters.DOUBLE, adapter);
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_serializeSpecialFloatingPointValuesFalse_readNull() throws Exception {
    Method method = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter =
        (TypeAdapter<Number>) method.invoke(gson, false);

    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);

    Number result = adapter.read(in);

    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_serializeSpecialFloatingPointValuesFalse_readDouble() throws Exception {
    Method method = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter =
        (TypeAdapter<Number>) method.invoke(gson, false);

    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(in.nextDouble()).thenReturn(123.456);

    Number result = adapter.read(in);

    assertEquals(123.456, result.doubleValue());
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_serializeSpecialFloatingPointValuesFalse_writeNullValue() throws Exception {
    Method method = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter =
        (TypeAdapter<Number>) method.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_serializeSpecialFloatingPointValuesFalse_writeValidDouble() throws Exception {
    Method method = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter =
        (TypeAdapter<Number>) method.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);

    // Use a valid finite double value
    adapter.write(out, 789.012);

    // Verify checkValidFloatingPoint was called via no exception and value passed to out.value
    verify(out).value(789.012);
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_serializeSpecialFloatingPointValuesFalse_writeInvalidDouble_nan_throws() throws Exception {
    Method method = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter =
        (TypeAdapter<Number>) method.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);

    Number nanValue = Double.NaN;

    IOException thrown = assertThrows(IOException.class, () -> adapter.write(out, nanValue));
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  void doubleAdapter_serializeSpecialFloatingPointValuesFalse_writeInvalidDouble_infinity_throws() throws Exception {
    Method method = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    method.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter =
        (TypeAdapter<Number>) method.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);

    Number infValue = Double.POSITIVE_INFINITY;

    IOException thrown = assertThrows(IOException.class, () -> adapter.write(out, infValue));
    assertTrue(thrown.getMessage().contains("Infinity"));
  }
}