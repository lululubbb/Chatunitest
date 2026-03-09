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

class GsonFloatAdapterTest {

  private Gson gson;
  private Method floatAdapterMethod;

  @BeforeEach
  public void setUp() throws NoSuchMethodException {
    gson = new Gson();
    floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapter_serializeSpecialFloatingPointValuesTrue_returnsTypeAdaptersFloat() throws InvocationTargetException, IllegalAccessException {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, true);
    assertNotNull(adapter);
    // TypeAdapters.FLOAT is a singleton, so we can check class name or toString
    assertEquals("com.google.gson.internal.bind.TypeAdapters$FloatTypeAdapter", adapter.getClass().getName());
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapter_serializeSpecialFloatingPointValuesFalse_readNull() throws Exception {
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
  public void testFloatAdapter_serializeSpecialFloatingPointValuesFalse_readNonNull() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);
    JsonReader in = mock(JsonReader.class);
    when(in.peek()).thenReturn(JsonToken.NUMBER);
    when(in.nextDouble()).thenReturn(123.456);

    Float result = (Float) adapter.read(in);

    assertEquals(123.456f, result);
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapter_serializeSpecialFloatingPointValuesFalse_writeNull() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);
    JsonWriter out = mock(JsonWriter.class);

    adapter.write(out, null);

    verify(out).nullValue();
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapter_serializeSpecialFloatingPointValuesFalse_writeFloatInstance() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);
    Number value = Float.valueOf(1.23f);

    adapter.write(out, value);

    verify(out).value(value);
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapter_serializeSpecialFloatingPointValuesFalse_writeNonFloatInstance() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);
    Number value = Double.valueOf(2.34);

    adapter.write(out, value);

    verify(out).value(value.floatValue());
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapter_serializeSpecialFloatingPointValuesFalse_writeInvalidFloatingPoint_throwsException() throws Exception {
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter out = mock(JsonWriter.class);
    Number value = Float.valueOf(Float.NaN);

    IOException thrown = assertThrows(IOException.class, () -> adapter.write(out, value));
    assertTrue(thrown.getMessage().contains("NaN"));
  }
}