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
  void toJson_withValidObject_andType_callsJsonWriter() throws Exception {
    StringWriter stringWriter = new StringWriter();
    Appendable appendable = stringWriter;
    Object src = "test string";
    Type typeOfSrc = String.class;

    // Spy on gson to verify internal calls
    Gson spyGson = Mockito.spy(gson);

    // Use reflection to get the private toJson(Object, Type, JsonWriter) method
    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, JsonWriter.class);
    toJsonMethod.setAccessible(true);

    // Mock newJsonWriter to return a JsonWriter wrapping our StringWriter
    JsonWriter jsonWriter = new JsonWriter(stringWriter);
    doReturn(jsonWriter).when(spyGson).newJsonWriter(any());

    // Call the public toJson method that internally calls newJsonWriter and the private toJson method
    spyGson.toJson(src, typeOfSrc, appendable);

    // Verify that the private toJson method was invoked with correct arguments
    verify(spyGson, times(1)).toJson(eq(src), eq(typeOfSrc), any(JsonWriter.class));

    String jsonOutput = stringWriter.toString();
    assertNotNull(jsonOutput);
    assertTrue(jsonOutput.contains("test string"));
  }

  @Test
    @Timeout(8000)
  void toJson_withIOException_throwsJsonIOException() throws Exception {
    Appendable appendable = mock(Appendable.class);
    when(appendable.append(any(CharSequence.class))).thenThrow(new IOException("mock IO exception"));
    Object src = "test";
    Type typeOfSrc = String.class;

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> gson.toJson(src, typeOfSrc, appendable));
    assertNotNull(thrown.getCause());
    assertTrue(thrown.getCause() instanceof IOException);
    assertEquals("mock IO exception", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void toJson_invokesNewJsonWriterWithStreamsWriterForAppendable() throws Exception {
    StringWriter stringWriter = new StringWriter();
    Appendable appendable = stringWriter;
    Object src = 123;
    Type typeOfSrc = Integer.class;

    try (MockedStatic<Streams> streamsMockedStatic = Mockito.mockStatic(Streams.class)) {
      streamsMockedStatic.when(() -> Streams.writerForAppendable(appendable)).thenReturn(stringWriter);

      Gson spyGson = Mockito.spy(new Gson());

      // Mock newJsonWriter to return JsonWriter wrapping stringWriter
      JsonWriter jsonWriter = new JsonWriter(stringWriter);
      doReturn(jsonWriter).when(spyGson).newJsonWriter(any());

      spyGson.toJson(src, typeOfSrc, appendable);

      streamsMockedStatic.verify(() -> Streams.writerForAppendable(appendable), times(1));
      verify(spyGson, times(1)).toJson(eq(src), eq(typeOfSrc), any(JsonWriter.class));

      String result = stringWriter.toString();
      assertNotNull(result);
      assertTrue(result.contains("123"));
    }
  }

  @Test
    @Timeout(8000)
  void toJson_privateMethodInvocation_viaReflection() throws Exception {
    StringWriter stringWriter = new StringWriter();
    Appendable appendable = stringWriter;
    Object src = "reflection test";
    Type typeOfSrc = String.class;

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, Appendable.class);
    toJsonMethod.setAccessible(true);

    // Invoke the public toJson method directly
    toJsonMethod.invoke(gson, src, typeOfSrc, appendable);

    String jsonOutput = stringWriter.toString();
    assertNotNull(jsonOutput);
    assertTrue(jsonOutput.contains("reflection test"));
  }
}