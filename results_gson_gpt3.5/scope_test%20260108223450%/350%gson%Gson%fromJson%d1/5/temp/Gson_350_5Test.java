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
import java.io.Reader;
import java.io.StringWriter;
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

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withValidJsonReaderAndType_returnsObject() throws Exception {
    String json = "{\"name\":\"test\"}";
    JsonReader reader = new JsonReader(new StringReader(json));
    Type type = String.class;

    // Use reflection to invoke private fromJson(JsonReader, TypeToken)
    Method fromJsonTypeTokenMethod = Gson.class.getDeclaredMethod("fromJson", JsonReader.class, TypeToken.class);
    fromJsonTypeTokenMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeToken<String> typeToken = (TypeToken<String>) TypeToken.get(type);

    // Call the focal method
    String result = gson.fromJson(reader, type);

    // Reset the reader for reflection call because the first call consumes it
    reader = new JsonReader(new StringReader(json));

    // Also call private method via reflection for coverage
    Object reflectedResult = fromJsonTypeTokenMethod.invoke(gson, reader, typeToken);

    assertNotNull(result);
    assertEquals(result, reflectedResult);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withNullType_throwsNullPointerException() {
    JsonReader reader = new JsonReader(new StringReader("{}"));
    assertThrows(NullPointerException.class, () -> gson.fromJson(reader, (Type) null));
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withMalformedJson_throwsJsonSyntaxException() {
    String malformedJson = "{name:\"missing quotes}";
    JsonReader reader = new JsonReader(new StringReader(malformedJson));
    Type type = Object.class;

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, type));
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withIOException_throwsJsonIOException() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    Type type = Object.class;

    doThrow(new IOException("IO error")).when(reader).peek();

    assertThrows(JsonIOException.class, () -> gson.fromJson(reader, type));
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withEOFException_returnsNull() throws Exception {
    JsonReader reader = mock(JsonReader.class);
    Type type = Object.class;

    doThrow(new java.io.EOFException()).when(reader).peek();

    Object result = gson.fromJson(reader, type);
    assertNull(result);
  }
}