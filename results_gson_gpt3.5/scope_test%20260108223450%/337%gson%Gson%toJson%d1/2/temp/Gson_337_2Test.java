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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
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

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;

class GsonToJsonTest {

  private Gson gson;
  private JsonWriter writer;
  private TypeAdapter<Object> adapter;

  @BeforeEach
  void setUp() throws Exception {
    gson = spy(new Gson());

    // Set private final fields htmlSafe and serializeNulls to false via reflection
    setFinalField(gson, "htmlSafe", false);
    setFinalField(gson, "serializeNulls", false);

    writer = mock(JsonWriter.class);
    adapter = mock(TypeAdapter.class);
  }

  private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(target, value);
  }

  @Test
    @Timeout(8000)
  void testToJson_callsAdapterWrite() throws IOException {
    Object src = new Object();
    Type typeOfSrc = Object.class;

    doReturn(adapter).when(gson).getAdapter(TypeToken.get(typeOfSrc));
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(false);

    gson.toJson(src, typeOfSrc, writer);

    verify(adapter).write(writer, src);
    verify(writer).setLenient(true);
    verify(writer).setHtmlSafe(false);
    verify(writer).setSerializeNulls(false);
    verify(writer).setLenient(false);
    verify(writer).setHtmlSafe(false);
    verify(writer).setSerializeNulls(false);
  }

  @Test
    @Timeout(8000)
  void testToJson_wrapsIOException() throws IOException {
    Object src = new Object();
    Type typeOfSrc = Object.class;

    doReturn(adapter).when(gson).getAdapter(TypeToken.get(typeOfSrc));
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(false);

    doThrow(new IOException("io error")).when(adapter).write(writer, src);

    JsonIOException ex = assertThrows(JsonIOException.class, () -> gson.toJson(src, typeOfSrc, writer));
    assertEquals("java.io.IOException: io error", ex.getCause().toString());
  }

  @Test
    @Timeout(8000)
  void testToJson_wrapsAssertionError() throws IOException {
    Object src = new Object();
    Type typeOfSrc = Object.class;

    doReturn(adapter).when(gson).getAdapter(TypeToken.get(typeOfSrc));
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(false);

    AssertionError error = new AssertionError("assertion failed");
    doThrow(error).when(adapter).write(writer, src);

    AssertionError thrown = assertThrows(AssertionError.class, () -> gson.toJson(src, typeOfSrc, writer));
    assertTrue(thrown.getMessage().contains("AssertionError (GSON"));
    assertSame(error, thrown.getCause());
  }
}