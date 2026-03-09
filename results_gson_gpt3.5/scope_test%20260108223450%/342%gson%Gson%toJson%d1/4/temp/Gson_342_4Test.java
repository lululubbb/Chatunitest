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

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.stream.JsonWriter;
import com.google.gson.internal.Streams;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

class GsonToJsonTest {

  Gson gson;
  JsonWriter writer;
  JsonElement jsonElement;

  @BeforeEach
  void setUp() throws Exception {
    gson = new Gson();

    // Use reflection to set htmlSafe and serializeNulls fields to default values used in the test
    Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    htmlSafeField.setBoolean(gson, true);

    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    serializeNullsField.setBoolean(gson, false);

    writer = mock(JsonWriter.class);
    jsonElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void toJson_shouldSetAndRestoreWriterSettings_andCallStreamsWrite() throws IOException {
    // Arrange
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(true);
    when(writer.getSerializeNulls()).thenReturn(false);

    // Act
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      gson.toJson(jsonElement, writer);

      // Assert
      InOrder inOrder = inOrder(writer);
      // verify old states retrieved
      inOrder.verify(writer).isLenient();
      inOrder.verify(writer).setLenient(true);
      inOrder.verify(writer).isHtmlSafe();
      inOrder.verify(writer).setHtmlSafe(true);
      inOrder.verify(writer).getSerializeNulls();
      inOrder.verify(writer).setSerializeNulls(false);
      // verify Streams.write called with same jsonElement and writer
      streamsMockedStatic.verify(() -> Streams.write(jsonElement, writer));
      // verify settings restored in finally block
      inOrder.verify(writer).setLenient(false);
      inOrder.verify(writer).setHtmlSafe(true);
      inOrder.verify(writer).setSerializeNulls(false);
    }
  }

  @Test
    @Timeout(8000)
  void toJson_shouldThrowJsonIOException_whenStreamsWriteThrowsIOException() throws IOException {
    // Arrange
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(true);
    when(writer.getSerializeNulls()).thenReturn(false);

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(jsonElement, writer))
          .thenThrow(new IOException("io error"));

      // Act & Assert
      JsonIOException ex = assertThrows(JsonIOException.class, () -> gson.toJson(jsonElement, writer));
      assertEquals("java.io.IOException: io error", ex.getCause().toString());

      // verify settings restored
      verify(writer).setLenient(false);
      verify(writer).setHtmlSafe(true);
      verify(writer).setSerializeNulls(false);
    }
  }

  @Test
    @Timeout(8000)
  void toJson_shouldThrowAssertionErrorWithVersion_whenStreamsWriteThrowsAssertionError() throws IOException {
    // Arrange
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(true);
    when(writer.getSerializeNulls()).thenReturn(false);

    AssertionError error = new AssertionError("assertion failed");

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.write(jsonElement, writer))
          .thenThrow(error);

      // Act & Assert
      AssertionError thrown = assertThrows(AssertionError.class, () -> gson.toJson(jsonElement, writer));
      String expectedMessageStart = "AssertionError (GSON ";
      assertTrue(thrown.getMessage().startsWith(expectedMessageStart));
      assertTrue(thrown.getMessage().contains(": assertion failed"));
      assertSame(error, thrown.getCause());

      // verify settings restored
      verify(writer).setLenient(false);
      verify(writer).setHtmlSafe(true);
      verify(writer).setSerializeNulls(false);
    }
  }
}