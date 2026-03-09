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
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

class GsonFromJsonReaderTypeTokenTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_withValidJson_shouldReturnExpectedObject() throws Exception {
    String json = "{\"value\":42}";
    Reader reader = new StringReader(json);
    TypeToken<Dummy> typeToken = TypeToken.get(Dummy.class);

    Dummy result = gson.fromJson(reader, typeToken);

    assertNotNull(result);
    assertEquals(42, result.value);
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullJson_shouldReturnNull() throws Exception {
    Reader reader = new StringReader("null");
    TypeToken<Dummy> typeToken = TypeToken.get(Dummy.class);

    Dummy result = gson.fromJson(reader, typeToken);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withMalformedJson_shouldThrowJsonSyntaxException() {
    String json = "{invalidJson:";
    Reader reader = new StringReader(json);
    TypeToken<Dummy> typeToken = TypeToken.get(Dummy.class);

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, typeToken));
  }

  @Test
    @Timeout(8000)
  void fromJson_withIOExceptionDuringReading_shouldThrowJsonIOException() throws Exception {
    Reader mockReader = mock(Reader.class);
    when(mockReader.read(any(char[].class), anyInt(), anyInt())).thenThrow(new java.io.IOException());

    TypeToken<Dummy> typeToken = TypeToken.get(Dummy.class);

    assertThrows(JsonIOException.class, () -> gson.fromJson(mockReader, typeToken));
  }

  @Test
    @Timeout(8000)
  void fromJson_assertFullConsumption_invoked() throws Exception {
    // Use reflection to invoke private static assertFullConsumption method
    Method assertFullConsumption = Gson.class.getDeclaredMethod("assertFullConsumption", Object.class, JsonReader.class);
    assertFullConsumption.setAccessible(true);

    String json = "null";
    Reader reader = new StringReader(json);
    JsonReader jsonReader = gson.newJsonReader(reader);

    Object obj = null;

    // Should not throw exception
    assertFullConsumption.invoke(null, obj, jsonReader);

    // Prepare JsonReader that has extra tokens after deserialization
    String jsonWithExtra = "true false";
    Reader readerExtra = new StringReader(jsonWithExtra);
    JsonReader jsonReaderExtra = gson.newJsonReader(readerExtra);
    jsonReaderExtra.setLenient(true);
    jsonReaderExtra.nextBoolean(); // consume true

    // Now invoking assertFullConsumption with leftover tokens should throw JsonSyntaxException
    Exception ex = assertThrows(Exception.class, () -> assertFullConsumption.invoke(null, true, jsonReaderExtra));
    Throwable cause = ex.getCause();
    assertTrue(cause instanceof JsonSyntaxException);
  }

  // Dummy class for testing deserialization
  static class Dummy {
    int value;
  }
}