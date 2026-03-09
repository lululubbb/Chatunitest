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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
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

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class GsonFromJsonStringTypeTokenTest {
  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullJson_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson((String) null, typeToken.getType());
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_emptyJson_returnsNullOrThrows() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    // empty string is not null, so it tries to parse
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson("", typeToken.getType()));
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJson_returnsParsedObject() {
    String json = "\"hello\"";
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson(json, typeToken.getType());
    assertEquals("hello", result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJsonNumber_returnsParsedNumber() {
    String json = "123";
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    Integer result = gson.fromJson(json, typeToken.getType());
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_invalidJson_throwsJsonSyntaxException() {
    String json = "{invalid json}";
    TypeToken<Object> typeToken = TypeToken.get(Object.class);
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, typeToken.getType()));
  }

  @Test
    @Timeout(8000)
  public void fromJson_privateMethodInvocation_withReflection() throws Exception {
    // Use reflection to access the private fromJson(Reader, TypeToken<T>) method
    Method fromJsonReaderMethod = Gson.class.getDeclaredMethod("fromJson", java.io.Reader.class, TypeToken.class);
    fromJsonReaderMethod.setAccessible(true);

    String json = "\"reflection test\"";
    TypeToken<String> typeToken = TypeToken.get(String.class);

    java.io.StringReader reader = new java.io.StringReader(json);
    @SuppressWarnings("unchecked")
    String result = (String) fromJsonReaderMethod.invoke(gson, reader, typeToken);
    assertEquals("reflection test", result);
  }
}