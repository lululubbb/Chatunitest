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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GsonToJsonTest {

  private Gson gson;
  private JsonElement jsonElement;
  private Appendable appendable;

  @BeforeEach
  public void setup() {
    gson = new Gson();
    jsonElement = mock(JsonElement.class);
    appendable = new StringBuilder();
  }

  @Test
    @Timeout(8000)
  public void testToJson_success() throws IOException {
    // Spy on gson to mock newJsonWriter and toJson(JsonElement, JsonWriter)
    Gson spyGson = spy(gson);

    JsonWriter jsonWriter = mock(JsonWriter.class);
    // Mock Streams.writerForAppendable to return a Writer wrapping appendable
    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.writerForAppendable(appendable))
          .thenReturn(new java.io.StringWriter());

      doReturn(jsonWriter).when(spyGson).newJsonWriter(any());
      doNothing().when(spyGson).toJson(eq(jsonElement), eq(jsonWriter));

      // Call the method under test
      spyGson.toJson(jsonElement, appendable);

      // Verify interactions
      streamsMock.verify(() -> Streams.writerForAppendable(appendable));
      verify(spyGson).newJsonWriter(any());
      verify(spyGson).toJson(jsonElement, jsonWriter);
    }
  }

  @Test
    @Timeout(8000)
  public void testToJson_throwsIOException() throws IOException {
    Gson spyGson = spy(gson);
    Appendable brokenAppendable = mock(Appendable.class);
    IOException ioException = new IOException("write error");

    try (MockedStatic<Streams> streamsMock = Mockito.mockStatic(Streams.class)) {
      streamsMock.when(() -> Streams.writerForAppendable(brokenAppendable))
          .thenThrow(ioException);

      JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
        spyGson.toJson(jsonElement, brokenAppendable);
      });
      assertEquals(ioException, thrown.getCause());
    }
  }

  @Test
    @Timeout(8000)
  public void testToJson_privateToJsonInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    // Use reflection to invoke private toJson(JsonElement, JsonWriter) method
    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class, JsonWriter.class);
    toJsonMethod.setAccessible(true);

    JsonWriter jsonWriter = mock(JsonWriter.class);

    // Call private method, expecting no exception
    toJsonMethod.invoke(gson, jsonElement, jsonWriter);
  }
}