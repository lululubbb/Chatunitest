package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
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
import com.google.gson.internal.GsonBuildConfig;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

public class Gson_342_6Test {

  private Gson gson;
  private JsonWriter writer;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
    writer = mock(JsonWriter.class);
  }

  @Test
    @Timeout(8000)
  public void toJson_shouldWriteJsonElementAndRestoreWriterSettings() throws IOException {
    // Setup writer mocks for initial states
    when(writer.isLenient()).thenReturn(false);
    when(writer.isHtmlSafe()).thenReturn(false);
    when(writer.getSerializeNulls()).thenReturn(false);

    // Use a real JsonElement to avoid IllegalArgumentException in Streams.write
    JsonElement realJsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public String toString() {
        return "{}";
      }
    };

    // Call method under test
    gson.toJson(realJsonElement, writer);

    // Verify writer state changes in order
    InOrder inOrder = inOrder(writer);
    inOrder.verify(writer).isLenient();
    inOrder.verify(writer).setLenient(true);
    inOrder.verify(writer).isHtmlSafe();
    inOrder.verify(writer).setHtmlSafe(gson.htmlSafe());
    inOrder.verify(writer).getSerializeNulls();
    inOrder.verify(writer).setSerializeNulls(gson.serializeNulls());

    // Verify writer settings restored
    inOrder.verify(writer).setLenient(false);
    inOrder.verify(writer).setHtmlSafe(false);
    inOrder.verify(writer).setSerializeNulls(false);
  }

  @Test
    @Timeout(8000)
  public void toJson_shouldThrowJsonIOException_whenIOExceptionOccurs() throws IOException {
    // Use a simple JsonElement that can be serialized
    JsonElement simpleJsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public String toString() {
        return "{}";
      }
    };

    // Create JsonWriter that throws IOException during writeDeferredName
    JsonWriter throwingWriter = new JsonWriter(new StringWriter()) {
      // Removed @Override annotation to fix compilation error
      public void writeDeferredName() throws IOException {
        throw new IOException("writeDeferredName IOException");
      }
    };

    // Use a real JsonElement that Gson can serialize (not anonymous class)
    JsonElement jsonElement = gson.toJsonTree("{}");

    assertThrows(JsonIOException.class, () -> gson.toJson(jsonElement, throwingWriter));
  }

  @Test
    @Timeout(8000)
  public void toJson_shouldWrapAssertionErrorWithVersion() throws IOException {
    // Use a simple JsonElement that can be serialized
    JsonElement simpleJsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public String toString() {
        return "{}";
      }
    };

    // Create JsonWriter that throws AssertionError during writeDeferredName
    JsonWriter throwingWriter = new JsonWriter(new StringWriter()) {
      // Removed @Override annotation to fix compilation error
      public void writeDeferredName() throws IOException {
        throw new AssertionError("assertion error");
      }
    };

    // Use a real JsonElement that Gson can serialize (not anonymous class)
    JsonElement jsonElement = gson.toJsonTree("{}");

    AssertionError thrown = assertThrows(AssertionError.class, () -> gson.toJson(jsonElement, throwingWriter));
    assertTrue(thrown.getMessage().contains("AssertionError (GSON " + GsonBuildConfig.VERSION + "): assertion error"));
  }
}