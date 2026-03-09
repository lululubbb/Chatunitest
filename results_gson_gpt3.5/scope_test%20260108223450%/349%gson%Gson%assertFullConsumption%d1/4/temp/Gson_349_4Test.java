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

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GsonAssertFullConsumptionTest {

  private static void invokeAssertFullConsumption(Object obj, JsonReader reader) throws Throwable {
    Method method = Gson.class.getDeclaredMethod("assertFullConsumption", Object.class, JsonReader.class);
    method.setAccessible(true);
    try {
      method.invoke(null, obj, reader);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }

  @Test
    @Timeout(8000)
  void testNullObjectDoesNotThrow() throws Throwable {
    JsonReader reader = mock(JsonReader.class);
    // For null obj, no peek call should be made, but if it is, it should not throw
    invokeAssertFullConsumption(null, reader);
    // verify peek was never called
    verify(reader, never()).peek();
  }

  @Test
    @Timeout(8000)
  void testObjectNotNullAndReaderAtEndDocumentDoesNotThrow() throws Throwable {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.END_DOCUMENT);
    Object obj = new Object();
    invokeAssertFullConsumption(obj, reader);
    verify(reader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  void testObjectNotNullAndReaderNotAtEndDocumentThrowsJsonSyntaxException() throws Throwable {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenReturn(JsonToken.BEGIN_OBJECT);
    Object obj = new Object();

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      invokeAssertFullConsumption(obj, reader);
    });
    assertEquals("JSON document was not fully consumed.", thrown.getMessage());
    verify(reader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  void testMalformedJsonExceptionIsWrapped() throws Throwable {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new MalformedJsonException("malformed"));
    Object obj = new Object();

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      invokeAssertFullConsumption(obj, reader);
    });
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof MalformedJsonException);
    verify(reader, times(1)).peek();
  }

  @Test
    @Timeout(8000)
  void testIOExceptionIsWrapped() throws Throwable {
    JsonReader reader = mock(JsonReader.class);
    when(reader.peek()).thenThrow(new IOException("io error"));
    Object obj = new Object();

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      invokeAssertFullConsumption(obj, reader);
    });
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IOException);
    verify(reader, times(1)).peek();
  }
}