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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void toJson_writesJsonSuccessfully() throws Exception {
    StringWriter writer = new StringWriter();
    Object src = "test string";
    Type type = String.class;

    // Spy on gson to verify toJson(Object, Type, JsonWriter) call
    Gson spyGson = Mockito.spy(gson);

    // We need to do partial mocking of the private method newJsonWriter(Writer)
    // so that the JsonWriter passed to toJson is the one wrapping our StringWriter.
    Method newJsonWriterMethod = Gson.class.getDeclaredMethod("newJsonWriter", java.io.Writer.class);
    newJsonWriterMethod.setAccessible(true);

    // Stub newJsonWriter to call the real method with our StringWriter to ensure correct JsonWriter
    doAnswer(invocation -> {
      Object arg = invocation.getArgument(0);
      return newJsonWriterMethod.invoke(spyGson, arg);
    }).when(spyGson).newJsonWriter(any(java.io.Writer.class));

    // Call the focal method
    spyGson.toJson(src, type, writer);

    // Capture the JsonWriter argument passed to toJson(Object, Type, JsonWriter)
    ArgumentCaptor<JsonWriter> captor = ArgumentCaptor.forClass(JsonWriter.class);
    verify(spyGson).toJson(eq(src), eq(type), captor.capture());

    // Verify that the JsonWriter wraps a Writer that ultimately writes to the StringWriter
    JsonWriter capturedJsonWriter = captor.getValue();
    assertNotNull(capturedJsonWriter);

    // The writer should contain the JSON string of "test string"
    String jsonOutput = writer.toString();
    assertTrue(jsonOutput.contains("test string"));
  }

  @Test
    @Timeout(8000)
  void toJson_throwsJsonIOExceptionOnIOException() throws Exception {
    Object src = "test";
    Type type = String.class;

    Appendable failingAppendable = new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException("fail");
      }
      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException("fail");
      }
      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException("fail");
      }
    };

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> gson.toJson(src, type, failingAppendable));
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("fail", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void toJson_invokesNewJsonWriterViaReflection() throws Exception {
    StringWriter writer = new StringWriter();
    Object src = "reflection test";
    Type type = String.class;

    // Use reflection to invoke private newJsonWriter(Writer)
    Method newJsonWriterMethod = Gson.class.getDeclaredMethod("newJsonWriter", java.io.Writer.class);
    newJsonWriterMethod.setAccessible(true);
    JsonWriter jsonWriter = (JsonWriter) newJsonWriterMethod.invoke(gson, writer);

    assertNotNull(jsonWriter);

    // Use reflection to invoke private toJson(Object, Type, JsonWriter)
    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, JsonWriter.class);
    toJsonMethod.setAccessible(true);
    toJsonMethod.invoke(gson, src, type, jsonWriter);

    String jsonOutput = writer.toString();
    assertTrue(jsonOutput.contains("reflection test"));
  }
}