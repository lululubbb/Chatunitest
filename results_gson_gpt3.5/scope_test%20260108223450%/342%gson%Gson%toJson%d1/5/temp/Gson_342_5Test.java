package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
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

import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.Streams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

class Gson_toJson_Test {

  private Gson gson;
  private JsonWriter writer;
  private JsonElement jsonElement;

  @BeforeEach
  void setUp() throws Exception {
    gson = new Gson();

    // Use reflection to set private final fields htmlSafe and serializeNulls in Gson instance
    setFinalField(gson, "htmlSafe", true);
    setFinalField(gson, "serializeNulls", true);

    writer = mock(JsonWriter.class);
    jsonElement = mock(JsonElement.class);
  }

  private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    field.set(target, value);
  }

  @Test
    @Timeout(8000)
  void toJson_shouldWriteJsonElementAndRestoreWriterSettings() throws IOException {
    // Setup writer initial states
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(false);

    try (var mockedStreams = Mockito.mockStatic(Streams.class)) {
      mockedStreams.when(() -> Streams.write(jsonElement, writer)).thenAnswer(invocation -> null);

      gson.toJson(jsonElement, writer);

      InOrder inOrder = inOrder(writer);
      // Verify initial state retrieval and setup calls in order
      inOrder.verify(writer).isLenient();
      inOrder.verify(writer).setLenient(true);
      inOrder.verify(writer).isHtmlSafe();
      inOrder.verify(writer).setHtmlSafe(true);
      inOrder.verify(writer).getSerializeNulls();
      inOrder.verify(writer).setSerializeNulls(true);

      // Verify Streams.write called with correct args
      mockedStreams.verify(() -> Streams.write(jsonElement, writer));

      // Verify finally block restores original states
      inOrder.verify(writer).setLenient(false);
      inOrder.verify(writer).setHtmlSafe(false);
      inOrder.verify(writer).setSerializeNulls(false);
    }
  }

  @Test
    @Timeout(8000)
  void toJson_shouldThrowJsonIOException_whenStreamsWriteThrowsIOException() throws IOException {
    when(writer.isLenient()).thenReturn(true);
    when(writer.isHtmlSafe()).thenReturn(true);
    when(writer.getSerializeNulls()).thenReturn(true);

    try (var mockedStreams = Mockito.mockStatic(Streams.class)) {
      mockedStreams.when(() -> Streams.write(jsonElement, writer)).thenThrow(new IOException("io error"));

      JsonIOException thrown = assertThrows(JsonIOException.class, () -> gson.toJson(jsonElement, writer));
      assertEquals("java.io.IOException: io error", thrown.getCause().toString());

      // Verify writer states restored after exception
      verify(writer).setLenient(true);
      verify(writer).setHtmlSafe(true);
      verify(writer).setSerializeNulls(true);
    }
  }

  @Test
    @Timeout(8000)
  void toJson_shouldThrowAssertionErrorWithVersion_whenStreamsWriteThrowsAssertionError() throws IOException {
    when(writer.isLenient()).thenReturn(true);
    when(writer.isHtmlSafe()).thenReturn(true);
    when(writer.getSerializeNulls()).thenReturn(true);

    AssertionError error = new AssertionError("test error");
    try (var mockedStreams = Mockito.mockStatic(Streams.class)) {
      mockedStreams.when(() -> Streams.write(jsonElement, writer)).thenThrow(error);

      AssertionError thrown = assertThrows(AssertionError.class, () -> gson.toJson(jsonElement, writer));
      assertTrue(thrown.getMessage().startsWith("AssertionError (GSON "));
      assertTrue(thrown.getMessage().contains("test error"));
      assertSame(error, thrown.getCause());

      // Verify writer states restored after exception
      verify(writer).setLenient(true);
      verify(writer).setHtmlSafe(true);
      verify(writer).setSerializeNulls(true);
    }
  }

}