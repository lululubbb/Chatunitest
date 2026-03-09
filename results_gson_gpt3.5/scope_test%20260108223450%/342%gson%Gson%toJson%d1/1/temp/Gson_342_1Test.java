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
import org.mockito.MockedStatic;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;

class GsonToJsonTest {

  private Gson gson;
  private JsonWriter writer;
  private JsonElement jsonElement;

  @BeforeEach
  void setUp() {
    gson = new Gson();
    writer = mock(JsonWriter.class);
    jsonElement = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void toJson_shouldSetAndResetWriterPropertiesAndCallStreamsWrite() throws IOException {
    // Arrange
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(false);

    try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
      // Act
      gson.toJson(jsonElement, writer);

      // Assert
      InOrder inOrder = inOrder(writer);
      // Verify properties read and set to true or expected values
      inOrder.verify(writer).isLenient();
      inOrder.verify(writer).setLenient(true);
      inOrder.verify(writer).isHtmlSafe();
      inOrder.verify(writer).setHtmlSafe(true);
      inOrder.verify(writer).getSerializeNulls();
      inOrder.verify(writer).setSerializeNulls(false);

      // Verify Streams.write called
      streamsMockedStatic.verify(() -> Streams.write(jsonElement, writer));

      // Verify properties reset to original values in finally block
      inOrder.verify(writer).setLenient(false);
      inOrder.verify(writer).setHtmlSafe(false);
      inOrder.verify(writer).setSerializeNulls(false);
    }
  }

  @Test
    @Timeout(8000)
  void toJson_shouldWrapIOExceptionInJsonIOException() throws IOException {
    // Arrange
    // Create a JsonWriter using real instance but override flush to throw IOException
    JsonWriter throwingWriter = new JsonWriter(new StringWriter()) {
      @Override
      public void flush() throws IOException {
        throw new IOException("flush error");
      }
    };

    // Use reflection to set private final fields htmlSafe, lenient, serializeNulls to true
    setFinalField(throwingWriter, "htmlSafe", true);
    setFinalField(throwingWriter, "lenient", true);
    setFinalField(throwingWriter, "serializeNulls", true);

    // Create a JsonElement subclass that implements deepCopy()
    JsonElement element = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    // Act & Assert
    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      gson.toJson(element, throwingWriter);
    });
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("flush error", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void toJson_shouldWrapAssertionErrorWithVersionInfo() throws IOException {
    // Arrange
    // Create a JsonWriter using real instance
    JsonWriter throwingWriter = new JsonWriter(new StringWriter());

    // Use reflection to set private final fields htmlSafe, lenient, serializeNulls to true
    setFinalField(throwingWriter, "htmlSafe", true);
    setFinalField(throwingWriter, "lenient", true);
    setFinalField(throwingWriter, "serializeNulls", true);

    // Create a JsonElement subclass that throws AssertionError on deepCopy() and toString()
    JsonElement assertionErrorElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        throw new AssertionError("test assertion");
      }

      @Override
      public String toString() {
        throw new AssertionError("test assertion");
      }
    };

    // Act & Assert
    AssertionError error = assertThrows(AssertionError.class, () -> {
      gson.toJson(assertionErrorElement, throwingWriter);
    });
    assertTrue(error.getMessage().contains("GSON"));
    assertTrue(error.getMessage().contains("test assertion"));
  }

  private static void setFinalField(Object target, String fieldName, boolean value) {
    try {
      Field field = JsonWriter.class.getDeclaredField(fieldName);
      field.setAccessible(true);

      // Remove final modifier if present
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

      field.setBoolean(target, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}