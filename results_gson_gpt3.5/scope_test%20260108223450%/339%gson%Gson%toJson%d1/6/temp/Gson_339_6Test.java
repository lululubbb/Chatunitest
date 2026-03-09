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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.io.Writer;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void toJson_writesJsonUsingJsonWriter() throws Exception {
    JsonElement jsonElement = mock(JsonElement.class);
    StringWriter stringWriter = new StringWriter();

    // Spy the Gson instance to mock the newJsonWriter method
    Gson spyGson = spy(gson);

    // Prepare a real JsonWriter wrapping the StringWriter
    JsonWriter jsonWriter = new JsonWriter(stringWriter);

    // Use reflection to get the newJsonWriter(Writer) method and mock it
    Method newJsonWriterMethod = Gson.class.getDeclaredMethod("newJsonWriter", Writer.class);
    newJsonWriterMethod.setAccessible(true);

    // Mock newJsonWriter to return our prepared jsonWriter when called with any Writer
    doReturn(jsonWriter).when(spyGson).newJsonWriter(any(Writer.class));

    // Spy the toJson(JsonElement, JsonWriter) method to verify it is called
    doNothing().when(spyGson).toJson(eq(jsonElement), eq(jsonWriter));

    // Call the method under test
    spyGson.toJson(jsonElement, stringWriter);

    // Verify newJsonWriter called with a Writer wrapping the Appendable
    verify(spyGson).newJsonWriter(any(Writer.class));

    // Verify toJson(JsonElement, JsonWriter) called with correct arguments
    verify(spyGson).toJson(jsonElement, jsonWriter);
  }

  @Test
    @Timeout(8000)
  void toJson_throwsJsonIOException_whenIOExceptionOccurs() {
    JsonElement jsonElement = mock(JsonElement.class);
    Appendable throwingAppendable = new Appendable() {
      @Override
      public Appendable append(CharSequence csq) throws IOException {
        throw new IOException("append failed");
      }

      @Override
      public Appendable append(CharSequence csq, int start, int end) throws IOException {
        throw new IOException("append failed");
      }

      @Override
      public Appendable append(char c) throws IOException {
        throw new IOException("append failed");
      }
    };

    // Spy Gson to call real newJsonWriter but it will fail on Streams.writerForAppendable
    Gson spyGson = spy(gson);

    com.google.gson.JsonIOException thrown = assertThrows(com.google.gson.JsonIOException.class, () -> spyGson.toJson(jsonElement, throwingAppendable));
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("append failed", thrown.getCause().getMessage());
  }
}