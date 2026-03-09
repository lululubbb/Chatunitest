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
import org.mockito.MockedStatic;
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
  void toJson_appendsJsonSuccessfully() throws IOException {
    StringWriter writer = new StringWriter();
    Object src = "testString";
    Type typeOfSrc = String.class;

    // Actual call, no mocking
    gson.toJson(src, typeOfSrc, writer);

    String json = writer.toString();
    assertNotNull(json);
    assertTrue(json.contains("testString"));
  }

  @Test
    @Timeout(8000)
  void toJson_throwsJsonIOExceptionOnIOException() throws IOException {
    Object src = "testString";
    Type typeOfSrc = String.class;

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

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      gson.toJson(src, typeOfSrc, failingAppendable);
    });
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("fail", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void toJson_invokesNewJsonWriterAndToJsonWithJsonWriter() throws Throwable {
    Object src = "testString";
    Type typeOfSrc = String.class;
    StringBuilder appendable = new StringBuilder();

    // Spy on gson to verify calls
    Gson spyGson = Mockito.spy(gson);

    // Use reflection to get private newJsonWriter method
    Method newJsonWriterMethod = Gson.class.getDeclaredMethod("newJsonWriter", java.io.Writer.class);
    newJsonWriterMethod.setAccessible(true);

    // Use reflection to get toJson(Object, Type, JsonWriter) method
    Method toJsonWithWriterMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, JsonWriter.class);
    toJsonWithWriterMethod.setAccessible(true);

    // Mock Streams.writerForAppendable to return a Writer that writes into appendable
    try (MockedStatic<Streams> streamsMockedStatic = mockStatic(Streams.class)) {
      // Provide a Writer that writes directly into appendable
      java.io.Writer writerForAppendable = new java.io.Writer() {
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
          appendable.append(cbuf, off, len);
        }
        @Override
        public void flush() throws IOException {}
        @Override
        public void close() throws IOException {}
      };

      streamsMockedStatic.when(() -> Streams.writerForAppendable(appendable)).thenReturn(writerForAppendable);

      // Spy newJsonWriter to call real method
      JsonWriter jsonWriter = (JsonWriter) newJsonWriterMethod.invoke(spyGson, writerForAppendable);

      // Stub toJson(Object, Type, JsonWriter) on spyGson to call real method to avoid infinite recursion
      doAnswer(invocation -> {
        Object arg0 = invocation.getArgument(0);
        Type arg1 = invocation.getArgument(1);
        JsonWriter arg2 = invocation.getArgument(2);
        // Call real method on original gson instance to avoid recursion
        toJsonWithWriterMethod.invoke(gson, arg0, arg1, arg2);
        return null;
      }).when(spyGson).toJson(any(), any(), any(JsonWriter.class));

      // Call focal method
      spyGson.toJson(src, typeOfSrc, appendable);

      // Verify Streams.writerForAppendable called once with appendable
      streamsMockedStatic.verify(() -> Streams.writerForAppendable(appendable), times(1));

      // Verify toJson(Object, Type, JsonWriter) called once
      verify(spyGson, times(1)).toJson(eq(src), eq(typeOfSrc), any(JsonWriter.class));

      // Verify appendable now contains JSON string
      String result = appendable.toString();
      assertNotNull(result);
      assertTrue(result.contains("testString"));
    }
  }
}