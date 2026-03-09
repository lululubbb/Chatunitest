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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.lang.reflect.Method;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testToJson_withNullJsonElement_returnsNullJson() {
    // JsonElement can be null, but Gson's toJson(JsonElement) does not accept null,
    // so this test assumes passing a JsonElement representing null JSON value.
    JsonElement jsonElement = gson.toJsonTree(null);
    String json = gson.toJson(jsonElement);
    assertEquals("null", json);
  }

  @Test
    @Timeout(8000)
  public void testToJson_withJsonObject_returnsJsonString() {
    JsonElement jsonElement = gson.toJsonTree(new TestObject("test", 123));
    String json = gson.toJson(jsonElement);
    assertEquals("{\"name\":\"test\",\"value\":123}", json);
  }

  @Test
    @Timeout(8000)
  public void testToJson_invokesPrivateToJsonJsonWriter() throws Exception {
    // Use reflection to invoke private toJson(JsonElement, JsonWriter) to ensure coverage
    JsonElement jsonElement = gson.toJsonTree(new TestObject("ref", 456));
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class, JsonWriter.class);
    toJsonMethod.setAccessible(true);
    toJsonMethod.invoke(gson, jsonElement, jsonWriter);

    // flush instead of close to avoid closing the underlying StringWriter
    jsonWriter.flush();
    assertEquals("{\"name\":\"ref\",\"value\":456}", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  public void testToJson_withEmptyJsonElement_returnsEmptyJson() {
    JsonElement jsonElement = gson.toJsonTree(new EmptyObject());
    String json = gson.toJson(jsonElement);
    assertEquals("{}", json);
  }

  // Helper test classes
  static class TestObject {
    String name;
    int value;

    TestObject(String name, int value) {
      this.name = name;
      this.value = value;
    }
  }

  static class EmptyObject {
  }
}