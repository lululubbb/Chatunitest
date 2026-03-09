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
import com.google.gson.JsonSyntaxException;
import com.google.gson.JsonIOException;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_withValidJsonAndType_shouldReturnObject() {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);
    Type type = Object.class;

    Object result = gson.fromJson(reader, type);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullReader_shouldThrowNullPointerException() {
    Reader reader = null;
    Type type = Object.class;

    assertThrows(NullPointerException.class, () -> gson.fromJson(reader, type));
  }

  @Test
    @Timeout(8000)
  void fromJson_withMalformedJson_shouldThrowJsonSyntaxException() {
    String json = "{name:\"test\""; // malformed JSON (missing closing brace)
    Reader reader = new StringReader(json);
    Type type = Object.class;

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, type));
  }

  @Test
    @Timeout(8000)
  void fromJson_withJsonIOException_shouldThrowJsonIOException() throws Exception {
    Reader reader = mock(Reader.class);
    Type type = Object.class;

    // Spy on gson
    Gson spyGson = Mockito.spy(gson);

    @SuppressWarnings("rawtypes")
    TypeToken rawTypeToken = TypeToken.get(type);

    // Use reflection to get the fromJson(Reader, TypeToken) method
    java.lang.reflect.Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", Reader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    // Mock the fromJson(Reader, TypeToken) method on the spy using doAnswer
    Mockito.doAnswer(invocation -> {
      Reader argReader = invocation.getArgument(0);
      TypeToken argTypeToken = invocation.getArgument(1);
      if (argReader == reader && argTypeToken.equals(rawTypeToken)) {
        throw new JsonIOException("IO error");
      }
      return invocation.callRealMethod();
    }).when(spyGson).fromJson(any(Reader.class), any(TypeToken.class));

    assertThrows(JsonIOException.class, () -> spyGson.fromJson(reader, type));
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullType_shouldThrowNullPointerException() {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);
    Type type = null;

    assertThrows(NullPointerException.class, () -> gson.fromJson(reader, type));
  }
}