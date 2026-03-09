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
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
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

import com.google.gson.stream.JsonWriter;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void toJson_withNullObject_returnsJsonNull() {
    String json = gson.toJson(null, Object.class);
    assertEquals("null", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withSimpleObject_returnsJsonString() {
    String src = "test";
    String json = gson.toJson(src, String.class);
    assertEquals("\"test\"", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withInteger_returnsJsonNumber() {
    Integer src = 123;
    String json = gson.toJson(src, Integer.class);
    assertEquals("123", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withCustomType_returnsJsonObject() {
    class Custom {
      int a = 1;
      String b = "b";
    }
    Custom src = new Custom();
    String json = gson.toJson(src, Custom.class);
    assertTrue(json.contains("\"a\":1"));
    assertTrue(json.contains("\"b\":\"b\""));
  }

  @Test
    @Timeout(8000)
  void toJson_invokesPrivateToJsonMethodUsingReflection() throws Throwable {
    String src = "reflectionTest";
    Type type = String.class;
    StringWriter writer = new StringWriter();

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, JsonWriter.class);
    toJsonMethod.setAccessible(true);

    JsonWriter jsonWriter = gson.newJsonWriter(writer);

    try {
      toJsonMethod.invoke(gson, src, type, jsonWriter);
      jsonWriter.flush();
      jsonWriter.close();
    } catch (Exception e) {
      Throwable cause = e.getCause();
      if (cause != null) {
        throw cause;
      } else {
        throw e;
      }
    }

    String json = writer.toString();
    assertEquals("\"reflectionTest\"", json);
  }
}