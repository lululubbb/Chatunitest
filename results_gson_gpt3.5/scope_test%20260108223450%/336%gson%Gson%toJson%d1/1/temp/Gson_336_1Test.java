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
  void toJson_writesJsonSuccessfully() throws IOException {
    StringWriter stringWriter = new StringWriter();
    String src = "test string";
    Type typeOfSrc = String.class;

    // Call the method under test
    assertDoesNotThrow(() -> gson.toJson(src, typeOfSrc, stringWriter));

    // The stringWriter should contain valid JSON string (quoted string)
    String json = stringWriter.toString();
    assertTrue(json.startsWith("\""));
    assertTrue(json.endsWith("\""));
    assertTrue(json.contains("test string"));
  }

  @Test
    @Timeout(8000)
  void toJson_throwsJsonIOExceptionOnIOException() throws IOException {
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

    String src = "test";
    Type typeOfSrc = String.class;

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> gson.toJson(src, typeOfSrc, failingAppendable));
    assertNotNull(thrown.getCause());
    assertEquals(IOException.class, thrown.getCause().getClass());
    assertEquals("fail", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void toJson_invokesNewJsonWriterAndToJson() throws Throwable {
    StringWriter stringWriter = new StringWriter();
    Type typeOfSrc = String.class;
    String src = "hello";

    // Use reflection to access private newJsonWriter method
    Method newJsonWriterMethod = Gson.class.getDeclaredMethod("newJsonWriter", java.io.Writer.class);
    newJsonWriterMethod.setAccessible(true);

    // Spy on gson to verify calls to newJsonWriter and toJson(Object, Type, JsonWriter)
    Gson spyGson = Mockito.spy(gson);

    // Mock Streams.writerForAppendable to return the StringWriter
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.writerForAppendable(stringWriter)).thenReturn(stringWriter);

      // When newJsonWriter is called on spyGson, invoke the real private method via reflection
      doAnswer(invocation -> newJsonWriterMethod.invoke(spyGson, invocation.getArgument(0)))
          .when(spyGson).newJsonWriter(any());

      // Call the method under test
      spyGson.toJson(src, typeOfSrc, stringWriter);

      // Verify newJsonWriter called once with any Writer (StringWriter)
      verify(spyGson, times(1)).newJsonWriter(any());

      // Verify toJson(Object, Type, JsonWriter) called once
      verify(spyGson, times(1)).toJson(eq(src), eq(typeOfSrc), any(JsonWriter.class));
    }
  }
}