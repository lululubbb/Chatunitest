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
  void floatAdapter_returnsFloatTypeAdapter_whenSerializeSpecialFloatingPointValuesTrue() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, true);

    assertSame(TypeAdapters.FLOAT, adapter);
  }

  @Test
    @Timeout(8000)
  void floatAdapter_returnsCustomTypeAdapter_whenSerializeSpecialFloatingPointValuesFalse() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    assertNotNull(adapter);

    JsonReader readerMock = mock(JsonReader.class);
    JsonWriter writerMock = mock(JsonWriter.class);

    // Test read() when JsonToken.NULL
    when(readerMock.peek()).thenReturn(JsonToken.NULL);
    doNothing().when(readerMock).nextNull();

    Float resultNull = (Float) adapter.read(readerMock);
    assertNull(resultNull);
    verify(readerMock).nextNull();

    // Test read() when JsonToken is NUMBER (simulate nextDouble)
    when(readerMock.peek()).thenReturn(JsonToken.NUMBER);
    when(readerMock.nextDouble()).thenReturn(3.14d);

    Float resultNumber = (Float) adapter.read(readerMock);
    assertEquals(3.14f, resultNumber);

    // Test write() when value is null
    adapter.write(writerMock, null);
    verify(writerMock).nullValue();

    // Test write() when value is Float instance
    Float floatValue = 1.23f;
    adapter.write(writerMock, floatValue);
    ArgumentCaptor<Number> captor = ArgumentCaptor.forClass(Number.class);
    verify(writerMock, atLeastOnce()).value(captor.capture());
    assertTrue(captor.getValue() instanceof Float);

    // Test write() when value is other Number subclass (e.g. Double)
    reset(writerMock);
    Number doubleValue = Double.valueOf(2.34);
    adapter.write(writerMock, doubleValue);
    // The adapter converts the doubleValue to float before writing
    verify(writerMock).value(2.34f);
  }

  @Test
    @Timeout(8000)
  void floatAdapter_write_throwsOnInvalidFloatingPoint() throws Throwable {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);

    JsonWriter writerMock = mock(JsonWriter.class);

    // Prepare a Number with NaN float value to cause checkValidFloatingPoint to throw
    Number nanNumber = new Number() {
      @Override
      public int intValue() { return 0; }
      @Override
      public long longValue() { return 0L; }
      @Override
      public float floatValue() { return Float.NaN; }
      @Override
      public double doubleValue() { return Double.NaN; }
    };

    IOException thrown = assertThrows(IOException.class, () -> {
      try {
        adapter.write(writerMock, nanNumber);
      } catch (InvocationTargetException e) {
        // Unwrap the cause if the adapter is a proxy or uses reflection internally
        Throwable cause = e.getCause();
        if (cause instanceof IOException) {
          throw (IOException) cause;
        } else {
          throw e;
        }
      }
    });
    assertTrue(thrown.getCause() instanceof IllegalArgumentException);
    assertTrue(thrown.getCause().getMessage().contains("NaN"));
  }
}