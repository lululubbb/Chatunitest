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
  public void testFloatAdapterReturnsTypeAdaptersFLOAT_whenSerializeSpecialFloatingPointValuesTrue() throws InvocationTargetException, IllegalAccessException {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, true);
    assertNotNull(adapter);
    // TypeAdapters.FLOAT is a singleton, so test class name and behavior
    assertEquals("com.google.gson.internal.bind.TypeAdapters$FloatTypeAdapter", adapter.getClass().getName());
  }

  @Test
    @Timeout(8000)
  public void testFloatAdapterReturnsCustomAdapter_whenSerializeSpecialFloatingPointValuesFalse() throws InvocationTargetException, IllegalAccessException {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);
    assertNotNull(adapter);
    // The returned adapter is an anonymous class, so class name contains Gson$ or similar
    assertTrue(adapter.getClass().getName().contains("Gson$"));
  }

  @Test
    @Timeout(8000)
  public void testReadNullValue_returnsNull() throws Exception {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenReturn(JsonToken.NULL);

    Float result = (Float) adapter.read(mockReader);

    verify(mockReader).nextNull();
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadNonNullValue_returnsFloat() throws Exception {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonReader mockReader = mock(JsonReader.class);
    when(mockReader.peek()).thenReturn(JsonToken.NUMBER);
    when(mockReader.nextDouble()).thenReturn(3.14);

    Float result = (Float) adapter.read(mockReader);

    verify(mockReader).nextDouble();
    assertEquals(3.14f, result);
  }

  @Test
    @Timeout(8000)
  public void testWriteNullValue_callsNullValue() throws Exception {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter mockWriter = mock(JsonWriter.class);

    adapter.write(mockWriter, null);

    verify(mockWriter).nullValue();
  }

  @Test
    @Timeout(8000)
  public void testWriteFloatValue_callsValueWithFloat() throws Exception {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter mockWriter = mock(JsonWriter.class);

    Float value = 1.23f;
    adapter.write(mockWriter, value);

    // Should call out.value(value) because value instanceof Float
    verify(mockWriter).value(value);
  }

  @Test
    @Timeout(8000)
  public void testWriteDoubleValue_callsValueWithFloatValue() throws Exception {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter mockWriter = mock(JsonWriter.class);

    Double value = 2.34;
    adapter.write(mockWriter, value);

    // Should call out.value(floatNumber) where floatNumber is floatValue cast
    verify(mockWriter).value(2.34f);
  }

  @Test
    @Timeout(8000)
  public void testWriteInvalidFloat_throwsException() throws Exception {
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter mockWriter = mock(JsonWriter.class);

    Number invalidValue = Float.NaN;

    IOException thrown = assertThrows(IOException.class, () -> adapter.write(mockWriter, invalidValue));
    // Exception message contains NaN or similar
    assertTrue(thrown.getMessage().contains("NaN") || thrown.getMessage().toLowerCase().contains("invalid"));
  }
}