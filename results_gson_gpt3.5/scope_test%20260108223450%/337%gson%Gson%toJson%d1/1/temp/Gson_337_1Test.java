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

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;

class GsonToJsonTest {

  private Gson gson;
  private JsonWriter writer;
  private TypeAdapter<Object> adapter;

  @BeforeEach
  public void setup() {
    gson = new Gson();
    writer = mock(JsonWriter.class);
    adapter = mock(TypeAdapter.class);
  }

  @Test
    @Timeout(8000)
  public void testToJson_normalFlow() throws IOException {
    Object src = new Object();
    Type typeOfSrc = Object.class;

    Gson spyGson = Mockito.spy(gson);
    doReturn(adapter).when(spyGson).getAdapter(TypeToken.get(typeOfSrc));

    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(false);

    spyGson.toJson(src, typeOfSrc, writer);

    InOrder inOrder = inOrder(writer, adapter);
    inOrder.verify(writer).isLenient();
    inOrder.verify(writer).setLenient(true);
    inOrder.verify(writer).isHtmlSafe();
    inOrder.verify(writer).setHtmlSafe(spyGson.htmlSafe());
    inOrder.verify(writer).getSerializeNulls();
    inOrder.verify(writer).setSerializeNulls(spyGson.serializeNulls());
    inOrder.verify(adapter).write(writer, src);
    inOrder.verify(writer).setLenient(false);
    inOrder.verify(writer).setHtmlSafe(false);
    inOrder.verify(writer).setSerializeNulls(false);
  }

  @Test
    @Timeout(8000)
  public void testToJson_ioExceptionIsWrapped() throws IOException {
    Object src = new Object();
    Type typeOfSrc = Object.class;

    Gson spyGson = Mockito.spy(gson);
    doReturn(adapter).when(spyGson).getAdapter(TypeToken.get(typeOfSrc));

    when(writer.isLenient()).thenReturn(true);
    when(writer.isHtmlSafe()).thenReturn(true);
    when(writer.getSerializeNulls()).thenReturn(true);

    doThrow(new IOException("io error")).when(adapter).write(writer, src);

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      spyGson.toJson(src, typeOfSrc, writer);
    });

    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("io error", thrown.getCause().getMessage());

    InOrder inOrder = inOrder(writer);
    inOrder.verify(writer).isLenient();
    inOrder.verify(writer).setLenient(true);
    inOrder.verify(writer).isHtmlSafe();
    inOrder.verify(writer).setHtmlSafe(spyGson.htmlSafe());
    inOrder.verify(writer).getSerializeNulls();
    inOrder.verify(writer).setSerializeNulls(spyGson.serializeNulls());
    // The finally block restores the old settings
    inOrder.verify(writer).setLenient(true);
    inOrder.verify(writer).setHtmlSafe(true);
    inOrder.verify(writer).setSerializeNulls(true);
  }

  @Test
    @Timeout(8000)
  public void testToJson_assertionErrorIsWrapped() throws IOException {
    Object src = new Object();
    Type typeOfSrc = Object.class;

    Gson spyGson = Mockito.spy(gson);
    doReturn(adapter).when(spyGson).getAdapter(TypeToken.get(typeOfSrc));

    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(false);

    AssertionError assertionError = new AssertionError("assertion failed");
    doThrow(assertionError).when(adapter).write(writer, src);

    AssertionError thrown = assertThrows(AssertionError.class, () -> {
      spyGson.toJson(src, typeOfSrc, writer);
    });

    assertTrue(thrown.getMessage().contains("AssertionError (GSON "));
    assertEquals(assertionError, thrown.getCause());

    InOrder inOrder = inOrder(writer);
    inOrder.verify(writer).isLenient();
    inOrder.verify(writer).setLenient(true);
    inOrder.verify(writer).isHtmlSafe();
    inOrder.verify(writer).setHtmlSafe(spyGson.htmlSafe());
    inOrder.verify(writer).getSerializeNulls();
    inOrder.verify(writer).setSerializeNulls(spyGson.serializeNulls());
    inOrder.verify(writer).setLenient(false);
    inOrder.verify(writer).setHtmlSafe(false);
    inOrder.verify(writer).setSerializeNulls(false);
  }

  @Test
    @Timeout(8000)
  public void testToJson_restoresWriterSettingsOnException() throws IOException {
    Object src = new Object();
    Type typeOfSrc = Object.class;

    Gson spyGson = Mockito.spy(gson);
    doReturn(adapter).when(spyGson).getAdapter(TypeToken.get(typeOfSrc));

    when(writer.isLenient()).thenReturn(true);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(true);

    doThrow(new IOException("io error")).when(adapter).write(writer, src);

    try {
      spyGson.toJson(src, typeOfSrc, writer);
    } catch (JsonIOException ignored) {
    }

    InOrder inOrder = inOrder(writer);
    inOrder.verify(writer).isLenient();
    inOrder.verify(writer).setLenient(true);
    inOrder.verify(writer).isHtmlSafe();
    inOrder.verify(writer).setHtmlSafe(spyGson.htmlSafe());
    inOrder.verify(writer).getSerializeNulls();
    inOrder.verify(writer).setSerializeNulls(spyGson.serializeNulls());
    inOrder.verify(writer).setLenient(true);
    inOrder.verify(writer).setHtmlSafe(false);
    inOrder.verify(writer).setSerializeNulls(true);
  }
}