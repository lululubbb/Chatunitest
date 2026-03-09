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
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void toJson_givenValidJsonElementAndAppendable_invokesToJsonWithJsonWriter() throws Exception {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Appendable appendable = new StringBuilder();

    // Spy on gson to mock newJsonWriter and toJson(JsonElement, JsonWriter)
    Gson spyGson = Mockito.spy(gson);
    JsonWriter jsonWriter = mock(JsonWriter.class);

    try (MockedStatic<Streams> streamsStatic = mockStatic(Streams.class)) {
      // Mock Streams.writerForAppendable to return a Writer wrapping the Appendable
      streamsStatic.when(() -> Streams.writerForAppendable(appendable))
          .thenReturn(new java.io.StringWriter());

      doReturn(jsonWriter).when(spyGson).newJsonWriter(any());

      // We want to verify that toJson(JsonElement, JsonWriter) is called with correct args
      doNothing().when(spyGson).toJson(eq(jsonElement), eq(jsonWriter));

      // Act
      spyGson.toJson(jsonElement, appendable);

      // Assert
      verify(spyGson).newJsonWriter(any());
      verify(spyGson).toJson(jsonElement, jsonWriter);
    }
  }

  @Test
    @Timeout(8000)
  public void toJson_whenIOExceptionThrown_throwsJsonIOException() throws Exception {
    // Arrange
    JsonElement jsonElement = mock(JsonElement.class);
    Appendable appendable = mock(Appendable.class);

    Gson spyGson = Mockito.spy(gson);

    // Mock Streams.writerForAppendable to throw IOException
    try (MockedStatic<Streams> streamsStatic = mockStatic(Streams.class)) {
      streamsStatic.when(() -> Streams.writerForAppendable(appendable))
          .thenThrow(new IOException("IO error"));

      // Act & Assert
      JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
        spyGson.toJson(jsonElement, appendable);
      });
      assertNotNull(thrown.getCause());
      assertEquals(IOException.class, thrown.getCause().getClass());
      assertEquals("IO error", thrown.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void toJson_privateMethod_newJsonWriter_invocation() throws Exception {
    // Arrange
    Appendable appendable = new StringBuilder();

    // Use reflection to get private method newJsonWriter
    Method newJsonWriterMethod = Gson.class.getDeclaredMethod("newJsonWriter", java.io.Writer.class);
    newJsonWriterMethod.setAccessible(true);

    // Use Streams.writerForAppendable to get Writer
    java.io.Writer writer = Streams.writerForAppendable(appendable);

    // Act
    JsonWriter jsonWriter = (JsonWriter) newJsonWriterMethod.invoke(gson, writer);

    // Assert
    assertNotNull(jsonWriter);
  }
}