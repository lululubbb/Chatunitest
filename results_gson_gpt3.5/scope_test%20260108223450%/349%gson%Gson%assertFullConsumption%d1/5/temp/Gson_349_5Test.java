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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class GsonAssertFullConsumptionTest {

  private static Method getAssertFullConsumptionMethod() throws NoSuchMethodException {
    Method method = Gson.class.getDeclaredMethod("assertFullConsumption", Object.class, JsonReader.class);
    method.setAccessible(true);
    return method;
  }

  @Test
    @Timeout(8000)
  public void testAssertFullConsumption_objNull_shouldNotThrow() throws Throwable {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    Method method = getAssertFullConsumptionMethod();
    // obj is null, so no exception expected regardless of reader.peek()
    method.invoke(null, null, reader);
  }

  @Test
    @Timeout(8000)
  public void testAssertFullConsumption_objNotNullAndEndDocument_shouldNotThrow() throws Throwable {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.END_DOCUMENT);

    Method method = getAssertFullConsumptionMethod();
    // obj != null and peek() == END_DOCUMENT, no exception expected
    method.invoke(null, new Object(), reader);
  }

  @Test
    @Timeout(8000)
  public void testAssertFullConsumption_objNotNullAndNotEndDocument_shouldThrowJsonSyntaxException() throws Throwable {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);

    Method method = getAssertFullConsumptionMethod();

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, new Object(), reader);
    });
    // The cause of InvocationTargetException should be JsonSyntaxException
    Throwable cause = thrown.getCause();
    if (!(cause instanceof JsonSyntaxException)) {
      throw thrown;
    }
  }

  @Test
    @Timeout(8000)
  public void testAssertFullConsumption_malformedJsonException_shouldThrowJsonSyntaxException() throws Throwable {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new MalformedJsonException("malformed"));

    Method method = getAssertFullConsumptionMethod();

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, new Object(), reader);
    });
    Throwable cause = thrown.getCause();
    if (!(cause instanceof JsonSyntaxException)) {
      throw thrown;
    }
  }

  @Test
    @Timeout(8000)
  public void testAssertFullConsumption_ioException_shouldThrowJsonIOException() throws Throwable {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new IOException("io exception"));

    Method method = getAssertFullConsumptionMethod();

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(null, new Object(), reader);
    });
    Throwable cause = thrown.getCause();
    if (!(cause instanceof JsonIOException)) {
      throw thrown;
    }
  }
}