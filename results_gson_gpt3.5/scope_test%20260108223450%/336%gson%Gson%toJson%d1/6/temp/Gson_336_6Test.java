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

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
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
  void toJson_writesJsonSuccessfully() throws Exception {
    StringWriter writer = new StringWriter();
    Object src = "testString";
    Type type = String.class;

    // We spy on gson to verify internal calls
    Gson spyGson = Mockito.spy(gson);

    // Mock Streams.writerForAppendable to return the StringWriter
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.writerForAppendable(writer)).thenReturn(writer);

      // Use reflection to access private toJson(Object, Type, JsonWriter)
      Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, JsonWriter.class);
      toJsonMethod.setAccessible(true);

      // Mock newJsonWriter to return a JsonWriter wrapping the StringWriter
      JsonWriter jsonWriter = new JsonWriter(writer);
      doReturn(jsonWriter).when(spyGson).newJsonWriter(writer);

      // Call the public toJson method under test
      spyGson.toJson(src, type, writer);

      // Verify newJsonWriter called
      verify(spyGson).newJsonWriter(writer);

      // Verify that the internal toJson(Object, Type, JsonWriter) was called by invoking it directly
      // (already tested indirectly by above)

      // Flush to ensure all output is written
      jsonWriter.flush();

      // Assert that output JSON string is as expected
      assertEquals("\"testString\"", writer.toString());
    }
  }

  @Test
    @Timeout(8000)
  void toJson_throwsJsonIOException_whenIOExceptionOccurs() throws IOException {
    StringWriter writer = new StringWriter();
    Object src = "testString";
    Type type = String.class;

    Gson spyGson = Mockito.spy(gson);

    // Mock Streams.writerForAppendable to throw IOException
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.writerForAppendable(writer)).thenThrow(new IOException("IO error"));

      JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
        spyGson.toJson(src, type, writer);
      });

      assertTrue(thrown.getCause() instanceof IOException);
      assertEquals("IO error", thrown.getCause().getMessage());
    }
  }
}