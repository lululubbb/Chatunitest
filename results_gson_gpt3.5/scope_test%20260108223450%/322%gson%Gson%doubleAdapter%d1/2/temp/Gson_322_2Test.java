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

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
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
  void testDoubleAdapterReturnsTypeAdaptersDOUBLE_whenSerializeSpecialFloatingPointValuesTrue() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, true);

    assertNotNull(adapter);

    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NUMBER);
    when(reader.nextDouble()).thenReturn(1.23);
    assertEquals(1.23, adapter.read(reader));

    JsonWriter writer = mock(JsonWriter.class);
    adapter.write(writer, 2.34);
    verify(writer).value(2.34);
  }

  @Test
    @Timeout(8000)
  void testDoubleAdapterCustomAdapterReadNull() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(reader).nextNull();

    Number result = adapter.read(reader);
    assertNull(result);
    verify(reader).nextNull();
  }

  @Test
    @Timeout(8000)
  void testDoubleAdapterCustomAdapterReadDouble() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.NUMBER);
    when(reader.nextDouble()).thenReturn(3.1415);

    Number result = adapter.read(reader);
    assertEquals(3.1415, result);
  }

  @Test
    @Timeout(8000)
  void testDoubleAdapterCustomAdapterWriteNull() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonWriter writer = mock(JsonWriter.class);
    adapter.write(writer, null);
    verify(writer).nullValue();
  }

  @Test
    @Timeout(8000)
  void testDoubleAdapterCustomAdapterWriteValidDouble() throws Throwable {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonWriter writer = mock(JsonWriter.class);

    Method checkValidFloatingPoint = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    checkValidFloatingPoint.setAccessible(true);

    double validValue = 1.23;
    // invoke checkValidFloatingPoint directly, unwrap InvocationTargetException if any
    try {
      checkValidFloatingPoint.invoke(null, validValue);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    adapter.write(writer, validValue);
    verify(writer).value(validValue);
  }

  @Test
    @Timeout(8000)
  void testDoubleAdapterCustomAdapterWriteInvalidDoubleNaN() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonWriter writer = mock(JsonWriter.class);

    Method checkValidFloatingPoint = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    checkValidFloatingPoint.setAccessible(true);

    double nanValue = Double.NaN;
    InvocationTargetException thrownInvoke = assertThrows(InvocationTargetException.class, () -> checkValidFloatingPoint.invoke(null, nanValue));
    assertTrue(thrownInvoke.getCause() instanceof IllegalArgumentException);
    assertTrue(thrownInvoke.getCause().getMessage().contains("NaN"));

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> adapter.write(writer, nanValue));
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  void testDoubleAdapterCustomAdapterWriteInvalidDoubleInfinite() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);

    JsonWriter writer = mock(JsonWriter.class);

    double posInf = Double.POSITIVE_INFINITY;
    IllegalArgumentException thrownPos = assertThrows(IllegalArgumentException.class, () -> adapter.write(writer, posInf));
    assertTrue(thrownPos.getMessage().contains("Infinity"));

    double negInf = Double.NEGATIVE_INFINITY;
    IllegalArgumentException thrownNeg = assertThrows(IllegalArgumentException.class, () -> adapter.write(writer, negInf));
    assertTrue(thrownNeg.getMessage().contains("Infinity"));
  }
}