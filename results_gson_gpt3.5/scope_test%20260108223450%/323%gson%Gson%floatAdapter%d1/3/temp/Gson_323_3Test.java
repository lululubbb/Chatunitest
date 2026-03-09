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
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Gson_floatAdapter_Test {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void floatAdapter_returnsTypeAdapters_FLOAT_whenSerializeSpecialFloatingPointValuesTrue()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, true);

    assertSame(TypeAdapters.FLOAT, adapter);
  }

  @Test
    @Timeout(8000)
  void floatAdapter_adapterRead_returnsNullWhenJsonTokenIsNull()
      throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NULL);

    Float result = (Float) adapter.read(in);
    verify(in).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void floatAdapter_adapterRead_returnsFloatValueFromNextDouble()
      throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(in.nextDouble()).thenReturn(3.14);

    Float result = (Float) adapter.read(in);
    assertEquals(3.14f, result);
  }

  @Test
    @Timeout(8000)
  void floatAdapter_adapterWrite_writesNullValueWhenValueIsNull()
      throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);
    adapter.write(out, null);
    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  void floatAdapter_adapterWrite_writesFloatValueForFloatInstance() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);
    Float value = 1.23f;

    adapter.write(out, value);

    // checkValidFloatingPoint is called internally - no exception means float is valid
    ArgumentCaptor<Number> captor = ArgumentCaptor.forClass(Number.class);
    verify(out).value(captor.capture());
    Number captured = captor.getValue();
    assertTrue(captured instanceof Float);
    assertEquals(value, captured);
  }

  @Test
    @Timeout(8000)
  void floatAdapter_adapterWrite_writesFloatValueForNonFloatInstance() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);
    Number value = Double.valueOf(2.34);

    adapter.write(out, value);

    ArgumentCaptor<Number> captor = ArgumentCaptor.forClass(Number.class);
    verify(out).value(captor.capture());
    Number usedValue = captor.getValue();
    assertTrue(usedValue instanceof Float);
    assertEquals(value.floatValue(), usedValue.floatValue());
  }

  @Test
    @Timeout(8000)
  void floatAdapter_adapterWrite_throwsOnInvalidFloat() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);

    Number value = Float.NaN;

    // Reflection to access private static checkValidFloatingPoint(double)
    Method checkValidFloatingPointMethod = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    checkValidFloatingPointMethod.setAccessible(true);

    // Sanity check that checkValidFloatingPoint throws for NaN
    IllegalArgumentException thrownByCheck = assertThrows(IllegalArgumentException.class,
        () -> {
          try {
            checkValidFloatingPointMethod.invoke(null, (double) Float.NaN);
          } catch (InvocationTargetException e) {
            throw e.getCause();
          }
        });

    assertTrue(thrownByCheck.getMessage().contains("NaN"));

    // The adapter.write should throw IllegalArgumentException for NaN float values
    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> adapter.write(out, value));
    assertTrue(thrown.getMessage().contains("NaN"));
  }
}