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
import java.lang.reflect.Type;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_withValidJsonAndType_shouldReturnParsedObject() {
    String json = "\"testString\"";
    Type type = String.class;

    String result = gson.fromJson(json, type);

    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_withNullJson_shouldReturnNull() {
    String json = null;
    Type type = String.class;

    String result = gson.fromJson(json, type);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_withEmptyJson_shouldThrowJsonSyntaxException() {
    String json = "";
    Type type = String.class;

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, type));
  }

  @Test
    @Timeout(8000)
  public void fromJson_withInvalidJson_shouldThrowJsonSyntaxException() {
    String json = "{invalid json}";
    Type type = Object.class;

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, type));
  }

  @Test
    @Timeout(8000)
  public void fromJson_invokesPrivateFromJsonWithTypeToken() throws Exception {
    String json = "\"testString\"";
    Type type = String.class;

    // Use reflection to invoke private fromJson(String, TypeToken)
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", String.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    String result = (String) fromJsonMethod.invoke(gson, json, TypeToken.get(type));

    assertEquals("testString", result);
  }
}