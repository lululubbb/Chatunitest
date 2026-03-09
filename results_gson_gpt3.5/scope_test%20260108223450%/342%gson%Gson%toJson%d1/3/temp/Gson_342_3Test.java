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

import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;

class GsonToJsonTest {

  private Gson gson;
  private JsonWriter writer;
  private JsonElement jsonElement;
  private MockedStatic<Streams> streamsMock;

  @BeforeEach
  void setUp() throws Exception {
    gson = new Gson();

    // Mock writer and jsonElement
    writer = mock(JsonWriter.class);
    jsonElement = mock(JsonElement.class);

    // Set default behavior for the writer
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(true);
    when(writer.getSerializeNulls()).thenReturn(false);

    // Since gson.htmlSafe and gson.serializeNulls are private final fields, set them to known values for testing
    setFinalField(gson, "htmlSafe", true);
    setFinalField(gson, "serializeNulls", false);

    // Mock Streams.write to avoid actual write and IOException during normal tests
    streamsMock = Mockito.mockStatic(Streams.class);
    streamsMock.when(() -> Streams.write(any(JsonElement.class), any(JsonWriter.class))).thenAnswer(invocation -> null);
  }

  private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    field.set(target, value);
  }

  @Test
    @Timeout(8000)
  void toJson_shouldSetAndResetWriterPropertiesAndCallStreamsWrite() throws Exception {
    // Act
    gson.toJson(jsonElement, writer);

    // Assert
    InOrder inOrder = inOrder(writer);

    // Verify writer properties read and set before Streams.write
    inOrder.verify(writer).isLenient();
    inOrder.verify(writer).setLenient(true);
    inOrder.verify(writer).isHtmlSafe();
    inOrder.verify(writer).setHtmlSafe(true);
    inOrder.verify(writer).getSerializeNulls();
    inOrder.verify(writer).setSerializeNulls(false);

    // Verify writer properties reset to original values in finally block
    inOrder.verify(writer).setLenient(false);
    inOrder.verify(writer).setHtmlSafe(true);
    inOrder.verify(writer).setSerializeNulls(false);

    // Verify Streams.write was called once
    streamsMock.verify(() -> Streams.write(jsonElement, writer));
  }

  @Test
    @Timeout(8000)
  void toJson_shouldThrowJsonIOExceptionWhenIOExceptionOccurs() throws Exception {
    // Arrange
    streamsMock.close();
    streamsMock = Mockito.mockStatic(Streams.class);
    streamsMock.when(() -> Streams.write(any(JsonElement.class), any(JsonWriter.class)))
        .thenThrow(new IOException("IO error"));

    // Act & Assert
    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      gson.toJson(jsonElement, writer);
    });
    assertEquals("java.io.IOException: IO error", thrown.getCause().toString());
  }

  @Test
    @Timeout(8000)
  void toJson_shouldWrapAssertionErrorWithVersionInfo() throws Exception {
    // Arrange
    streamsMock.close();
    streamsMock = Mockito.mockStatic(Streams.class);
    streamsMock.when(() -> Streams.write(any(JsonElement.class), any(JsonWriter.class)))
        .thenThrow(new AssertionError("assertion failed"));

    // Act & Assert
    AssertionError thrown = assertThrows(AssertionError.class, () -> {
      gson.toJson(jsonElement, writer);
    });
    String version = Gson.class.getPackage().getImplementationVersion();
    if (version == null) {
      version = "null";
    }
    assertTrue(thrown.getMessage().contains("AssertionError (GSON " + version + "): assertion failed"));
    assertTrue(thrown.getCause() instanceof AssertionError);
  }
}