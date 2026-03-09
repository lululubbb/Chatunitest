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

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Gson_FromJson_String_TypeToken_Test {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_nullJson_returnsNull() throws Exception {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson((String) null, typeToken);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_validJson_returnsObject() throws Exception {
    String json = "\"testString\"";
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson(json, typeToken);
    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_emptyJson_throwsJsonSyntaxException() throws Exception {
    String json = "";
    TypeToken<String> typeToken = TypeToken.get(String.class);
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, typeToken));
  }

  @Test
    @Timeout(8000)
  void fromJson_invalidJson_throwsJsonSyntaxException() throws Exception {
    String json = "{invalidJson}";
    TypeToken<String> typeToken = TypeToken.get(String.class);
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, typeToken));
  }

  @Test
    @Timeout(8000)
  void fromJson_jsonArrayToList_returnsList() throws Exception {
    String json = "[\"one\", \"two\"]";
    TypeToken<java.util.List<String>> typeToken = new TypeToken<java.util.List<String>>() {};
    java.util.List<String> result = gson.fromJson(json, typeToken);
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("one", result.get(0));
    assertEquals("two", result.get(1));
  }

  @Test
    @Timeout(8000)
  void fromJson_jsonNumberToInteger_returnsInteger() throws Exception {
    String json = "123";
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);
    Integer result = gson.fromJson(json, typeToken);
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  void fromJson_jsonBooleanToBoolean_returnsBoolean() throws Exception {
    String json = "true";
    TypeToken<Boolean> typeToken = TypeToken.get(Boolean.class);
    Boolean result = gson.fromJson(json, typeToken);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_privateFromJsonInvokedViaReflection() throws Exception {
    // Use reflection to invoke private fromJson(Reader, TypeToken) indirectly
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", java.io.Reader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);
    String json = "\"reflectionTest\"";
    TypeToken<String> typeToken = TypeToken.get(String.class);
    java.io.Reader reader = new java.io.StringReader(json);
    @SuppressWarnings("unchecked")
    String result = (String) fromJsonMethod.invoke(gson, reader, typeToken);
    assertEquals("reflectionTest", result);
  }
}