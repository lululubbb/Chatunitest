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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Gson_floatAdapter_Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapter_serializeSpecialFloatingPointValues_true_returnsTypeAdaptersFLOAT() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, true);

    assertSame(TypeAdapters.FLOAT, adapter);
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapter_serializeSpecialFloatingPointValues_false_read_null() throws Exception {
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
  public void testFloatAdapter_serializeSpecialFloatingPointValues_false_read_nonNull() throws Exception {
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
  public void testFloatAdapter_serializeSpecialFloatingPointValues_false_write_null() throws Exception {
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
  public void testFloatAdapter_serializeSpecialFloatingPointValues_false_write_floatValue() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);

    // We use reflection to access private static checkValidFloatingPoint method
    Method checkValidFloatingPointMethod = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    checkValidFloatingPointMethod.setAccessible(true);

    // Spy on Gson class static method checkValidFloatingPoint
    try (MockedStatic<Gson> gsonStatic = Mockito.mockStatic(Gson.class, invocation -> {
      if (invocation.getMethod().equals(checkValidFloatingPointMethod)) {
        // call real method
        try {
          return checkValidFloatingPointMethod.invoke(null, invocation.getArgument(0));
        } catch (InvocationTargetException e) {
          throw e.getCause();
        }
      }
      return invocation.callRealMethod();
    })) {
      // Float instance value
      Float floatValue = 1.23f;
      adapter.write(out, floatValue);

      ArgumentCaptor<Number> captor = ArgumentCaptor.forClass(Number.class);
      verify(out).value(captor.capture());
      assertEquals(floatValue, captor.getValue());

      // Double instance value (should be converted to float primitive)
      Double doubleValue = 2.34;
      adapter.write(out, doubleValue);

      verify(out, times(2)).value(captor.capture());
      Number lastValue = captor.getAllValues().get(1);
      assertTrue(lastValue instanceof Float);
      assertEquals(doubleValue.floatValue(), lastValue.floatValue());
    }
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapter_serializeSpecialFloatingPointValues_false_write_invalidNaN_throws() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);

    Float nanValue = Float.NaN;

    // Use mockStatic to allow checkValidFloatingPoint to throw exception properly
    Method checkValidFloatingPointMethod = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    checkValidFloatingPointMethod.setAccessible(true);

    try (MockedStatic<Gson> gsonStatic = Mockito.mockStatic(Gson.class, invocation -> {
      if (invocation.getMethod().equals(checkValidFloatingPointMethod)) {
        try {
          return checkValidFloatingPointMethod.invoke(null, invocation.getArgument(0));
        } catch (InvocationTargetException e) {
          throw e.getCause();
        }
      }
      return invocation.callRealMethod();
    })) {
      IOException thrown = assertThrows(IOException.class, () -> adapter.write(out, nanValue));
      assertTrue(thrown.getMessage().contains("NaN"));
    }
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapter_serializeSpecialFloatingPointValues_false_write_invalidInfinity_throws() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);

    Float posInf = Float.POSITIVE_INFINITY;
    Float negInf = Float.NEGATIVE_INFINITY;

    Method checkValidFloatingPointMethod = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    checkValidFloatingPointMethod.setAccessible(true);

    try (MockedStatic<Gson> gsonStatic = Mockito.mockStatic(Gson.class, invocation -> {
      if (invocation.getMethod().equals(checkValidFloatingPointMethod)) {
        try {
          return checkValidFloatingPointMethod.invoke(null, invocation.getArgument(0));
        } catch (InvocationTargetException e) {
          throw e.getCause();
        }
      }
      return invocation.callRealMethod();
    })) {
      IOException thrown1 = assertThrows(IOException.class, () -> adapter.write(out, posInf));
      assertTrue(thrown1.getMessage().contains("Infinity"));

      IOException thrown2 = assertThrows(IOException.class, () -> adapter.write(out, negInf));
      assertTrue(thrown2.getMessage().contains("Infinity"));
    }
  }
}