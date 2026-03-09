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
import com.google.gson.stream.JsonWriter;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GsonAssertFullConsumptionTest {

  private static Method getAssertFullConsumptionMethod() throws NoSuchMethodException {
    Method method = Gson.class.getDeclaredMethod("assertFullConsumption", Object.class, JsonReader.class);
    method.setAccessible(true);
    return method;
  }

  @Test
    @Timeout(8000)
  void testAssertFullConsumption_objNull_shouldNotThrow() throws Throwable {
    Method method = getAssertFullConsumptionMethod();
    JsonReader reader = mock(JsonReader.class);
    // No need to stub peek() because obj is null and peek() should not be called.

    // obj is null, so no exception regardless of peek()
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, null, reader);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });

    verify(reader, never()).peek();
  }

  @Test
    @Timeout(8000)
  void testAssertFullConsumption_objNotNull_andPeekIsEndDocument_shouldNotThrow() throws Throwable {
    Method method = getAssertFullConsumptionMethod();
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    // obj not null and peek is END_DOCUMENT, no exception
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, new Object(), reader);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });

    verify(reader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  void testAssertFullConsumption_objNotNull_andPeekNotEndDocument_shouldThrowJsonSyntaxException() throws Throwable {
    Method method = getAssertFullConsumptionMethod();
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      try {
        method.invoke(null, new Object(), reader);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertEquals("JSON document was not fully consumed.", thrown.getMessage());

    verify(reader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  void testAssertFullConsumption_malformedJsonException_shouldThrowJsonSyntaxException() throws Throwable {
    Method method = getAssertFullConsumptionMethod();
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new com.google.gson.stream.MalformedJsonException("malformed"));

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      try {
        method.invoke(null, new Object(), reader);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertTrue(thrown.getCause() instanceof com.google.gson.stream.MalformedJsonException);
    assertEquals("malformed", thrown.getCause().getMessage());

    verify(reader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  void testAssertFullConsumption_ioException_shouldThrowJsonIOException() throws Throwable {
    Method method = getAssertFullConsumptionMethod();
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new IOException("io error"));

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      try {
        method.invoke(null, new Object(), reader);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("io error", thrown.getCause().getMessage());

    verify(reader, times(1)).peek();
  }
}