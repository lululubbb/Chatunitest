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
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testToJson_nullInput_returnsJsonNull() throws Exception {
    // Use reflection to invoke public toJson(JsonElement) method
    Method toJsonJsonElement = Gson.class.getMethod("toJson", JsonElement.class);

    String result = (String) toJsonJsonElement.invoke(gson, JsonNull.INSTANCE);
    assertNotNull(result);
    assertEquals("null", result.trim());
  }

  @Test
    @Timeout(8000)
  public void testToJson_nonNullInput_returnsJsonString() {
    String src = "testString";
    String json = gson.toJson(src);
    assertNotNull(json);
    assertTrue(json.startsWith("\""));
    assertTrue(json.endsWith("\""));
    assertTrue(json.contains("testString"));
  }

  @Test
    @Timeout(8000)
  public void testToJson_integerInput_returnsJsonNumber() {
    Integer src = 123;
    String json = gson.toJson(src);
    assertNotNull(json);
    assertEquals("123", json);
  }

  @Test
    @Timeout(8000)
  public void testToJson_objectInput_returnsJsonObject() {
    TestClass src = new TestClass("value", 10);
    String json = gson.toJson(src);
    assertNotNull(json);
    assertTrue(json.contains("\"field1\":\"value\""));
    assertTrue(json.contains("\"field2\":10"));
  }

  @Test
    @Timeout(8000)
  public void testToJson_arrayInput_returnsJsonArray() {
    String[] src = {"a", "b", "c"};
    String json = gson.toJson(src);
    assertNotNull(json);
    assertTrue(json.startsWith("["));
    assertTrue(json.endsWith("]"));
    assertTrue(json.contains("\"a\""));
    assertTrue(json.contains("\"b\""));
    assertTrue(json.contains("\"c\""));
  }

  private static class TestClass {
    String field1;
    int field2;

    TestClass(String field1, int field2) {
      this.field1 = field1;
      this.field2 = field2;
    }
  }
}