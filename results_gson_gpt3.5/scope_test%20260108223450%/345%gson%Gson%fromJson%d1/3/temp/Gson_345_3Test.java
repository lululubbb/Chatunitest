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
import java.io.Reader;
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
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.lang.reflect.Method;

class GsonFromJsonStringTypeTokenTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_nullJson_returnsNull() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson((String) null, typeToken.getType());
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_validJsonString_returnsObject() {
    String json = "\"test string\"";
    TypeToken<String> typeToken = TypeToken.get(String.class);
    String result = gson.fromJson(json, typeToken.getType());
    assertEquals("test string", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_invalidJson_throwsJsonSyntaxException() {
    String invalidJson = "{unclosed json}";
    TypeToken<Object> typeToken = TypeToken.get(Object.class);
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(invalidJson, typeToken.getType()));
  }

  @Test
    @Timeout(8000)
  void fromJson_emptyJsonString_returnsNullOrThrows() {
    TypeToken<String> typeToken = TypeToken.get(String.class);
    // Empty string is invalid JSON, expect JsonSyntaxException
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson("", typeToken.getType()));
  }

  @Test
    @Timeout(8000)
  void fromJson_invokesPrivateFromJsonReaderMethod() throws Exception {
    String json = "\"reflection test\"";
    TypeToken<String> typeToken = TypeToken.get(String.class);

    // The private method signature is: <T> T fromJson(JsonReader reader, TypeToken<T> typeToken)
    Method fromJsonReaderMethod = Gson.class.getDeclaredMethod("fromJson", JsonReader.class, TypeToken.class);
    fromJsonReaderMethod.setAccessible(true);

    JsonReader jsonReader = new JsonReader(new StringReader(json));

    @SuppressWarnings("unchecked")
    String result = (String) fromJsonReaderMethod.invoke(gson, jsonReader, typeToken);

    assertEquals("reflection test", result);
  }
}