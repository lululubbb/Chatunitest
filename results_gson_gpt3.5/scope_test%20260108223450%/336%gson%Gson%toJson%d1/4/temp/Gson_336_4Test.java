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

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void toJson_withValidSrcAndWriter_callsJsonWriterAndToJson() throws Exception {
    Object src = new Object();
    Type typeOfSrc = Object.class;
    StringWriter stringWriter = new StringWriter();

    // Spy on Gson to mock newJsonWriter and toJson(src, typeOfSrc, jsonWriter)
    Gson spyGson = Mockito.spy(gson);

    JsonWriter mockJsonWriter = mock(JsonWriter.class);

    // Mock Streams.writerForAppendable to return StringWriter
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.writerForAppendable(stringWriter)).thenReturn(stringWriter);

      // Mock newJsonWriter to return mockJsonWriter
      doReturn(mockJsonWriter).when(spyGson).newJsonWriter(stringWriter);

      // Mock toJson(src, typeOfSrc, jsonWriter) to do nothing
      doNothing().when(spyGson).toJson(src, typeOfSrc, mockJsonWriter);

      // Call the focal method
      spyGson.toJson(src, typeOfSrc, stringWriter);

      // Verify interactions
      streamsMockedStatic.verify(() -> Streams.writerForAppendable(stringWriter));
      verify(spyGson).newJsonWriter(stringWriter);
      verify(spyGson).toJson(src, typeOfSrc, mockJsonWriter);
    }
  }

  @Test
    @Timeout(8000)
  void toJson_whenIOExceptionThrown_throwsJsonIOException() throws Exception {
    Object src = new Object();
    Type typeOfSrc = Object.class;

    Appendable throwingAppendable = mock(Appendable.class);
    // Make Streams.writerForAppendable return a Writer that throws IOException on write
    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.writerForAppendable(throwingAppendable))
          .thenThrow(new IOException("forced IO exception"));

      JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
        gson.toJson(src, typeOfSrc, throwingAppendable);
      });
      assertNotNull(thrown.getCause());
      assertTrue(thrown.getCause() instanceof IOException);
      assertEquals("forced IO exception", thrown.getCause().getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void toJson_privateNewJsonWriter_invocation() throws Exception {
    StringWriter stringWriter = new StringWriter();

    // Use reflection to invoke private newJsonWriter(Writer)
    Method newJsonWriterMethod = Gson.class.getDeclaredMethod("newJsonWriter", java.io.Writer.class);
    newJsonWriterMethod.setAccessible(true);

    JsonWriter jsonWriter = (JsonWriter) newJsonWriterMethod.invoke(gson, stringWriter);

    assertNotNull(jsonWriter);
  }
}