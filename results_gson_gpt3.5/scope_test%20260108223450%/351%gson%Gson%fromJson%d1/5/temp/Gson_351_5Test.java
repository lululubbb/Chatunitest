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
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.EOFException;
import java.io.IOException;

class Gson_FromJson_Test {

  private Gson gson;
  private JsonReader reader;

  @BeforeEach
  void setUp() {
    gson = new Gson();
    reader = mock(JsonReader.class);
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldReturnNull_whenEmptyDocument() throws Exception {
    when(reader.isLenient()).thenReturn(false);
    // peek throws EOFException on empty document
    doThrow(new EOFException()).when(reader).peek();

    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson(reader, typeToken);

    assertNull(result);
    InOrder inOrder = inOrder(reader);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(reader).setLenient(false);
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldThrowJsonSyntaxException_onEOFExceptionNotEmpty() throws Exception {
    when(reader.isLenient()).thenReturn(false);
    doNothing().when(reader).peek();
    // Simulate typeAdapter.read throws EOFException (simulate mid-read EOF)
    Gson spyGson = Mockito.spy(gson);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    var adapter = mock(com.google.gson.TypeAdapter.class);
    doReturn(adapter).when(spyGson).getAdapter(typeToken);
    when(adapter.read(reader)).thenThrow(new EOFException());

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> {
      spyGson.fromJson(reader, typeToken);
    });

    assertTrue(ex.getCause() instanceof EOFException);
    InOrder inOrder = inOrder(reader);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(adapter).read(reader);
    inOrder.verify(reader).setLenient(false);
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldThrowJsonSyntaxException_onIllegalStateException() throws Exception {
    when(reader.isLenient()).thenReturn(false);
    doNothing().when(reader).peek();
    Gson spyGson = Mockito.spy(gson);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    var adapter = mock(com.google.gson.TypeAdapter.class);
    doReturn(adapter).when(spyGson).getAdapter(typeToken);
    when(adapter.read(reader)).thenThrow(new IllegalStateException("bad state"));

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> {
      spyGson.fromJson(reader, typeToken);
    });

    assertTrue(ex.getCause() instanceof IllegalStateException);
    InOrder inOrder = inOrder(reader);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(adapter).read(reader);
    inOrder.verify(reader).setLenient(false);
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldThrowJsonSyntaxException_onIOException() throws Exception {
    when(reader.isLenient()).thenReturn(false);
    doNothing().when(reader).peek();
    Gson spyGson = Mockito.spy(gson);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    var adapter = mock(com.google.gson.TypeAdapter.class);
    doReturn(adapter).when(spyGson).getAdapter(typeToken);
    when(adapter.read(reader)).thenThrow(new IOException("io error"));

    JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> {
      spyGson.fromJson(reader, typeToken);
    });

    assertTrue(ex.getCause() instanceof IOException);
    InOrder inOrder = inOrder(reader);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(adapter).read(reader);
    inOrder.verify(reader).setLenient(false);
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldThrowAssertionError_withVersionMessage() throws Exception {
    when(reader.isLenient()).thenReturn(false);
    doNothing().when(reader).peek();
    Gson spyGson = Mockito.spy(gson);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    var adapter = mock(com.google.gson.TypeAdapter.class);
    doReturn(adapter).when(spyGson).getAdapter(typeToken);
    AssertionError original = new AssertionError("assertion failed");
    when(adapter.read(reader)).thenThrow(original);

    AssertionError thrown = assertThrows(AssertionError.class, () -> {
      spyGson.fromJson(reader, typeToken);
    });

    assertTrue(thrown.getMessage().contains("GSON"));
    assertSame(original, thrown.getCause());
    InOrder inOrder = inOrder(reader);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(adapter).read(reader);
    inOrder.verify(reader).setLenient(false);
  }

  @Test
    @Timeout(8000)
  void fromJson_shouldReturnValue_whenReadSucceeds() throws Exception {
    when(reader.isLenient()).thenReturn(false);
    doNothing().when(reader).peek();
    Gson spyGson = Mockito.spy(gson);
    TypeToken<String> typeToken = TypeToken.get(String.class);
    @SuppressWarnings("unchecked")
    var adapter = mock(com.google.gson.TypeAdapter.class);
    doReturn(adapter).when(spyGson).getAdapter(typeToken);
    when(adapter.read(reader)).thenReturn("value");

    String result = spyGson.fromJson(reader, typeToken);

    assertEquals("value", result);
    InOrder inOrder = inOrder(reader);
    inOrder.verify(reader).isLenient();
    inOrder.verify(reader).setLenient(true);
    inOrder.verify(reader).peek();
    inOrder.verify(adapter).read(reader);
    inOrder.verify(reader).setLenient(false);
  }
}