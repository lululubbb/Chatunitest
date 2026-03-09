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

import java.io.StringWriter;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testToJson_withNullObject_returnsJsonNull() {
    String json = gson.toJson(null, Object.class);
    assertEquals("null", json);
  }

  @Test
    @Timeout(8000)
  public void testToJson_withStringObject_returnsJsonString() {
    String input = "test";
    String json = gson.toJson(input, String.class);
    assertEquals("\"test\"", json);
  }

  @Test
    @Timeout(8000)
  public void testToJson_withIntegerObject_returnsJsonNumber() {
    Integer input = 123;
    String json = gson.toJson(input, Integer.class);
    assertEquals("123", json);
  }

  @Test
    @Timeout(8000)
  public void testToJson_withCustomTypeToken_returnsJson() {
    Type type = new TypeToken<Integer>() {}.getType();
    Integer input = 456;
    String json = gson.toJson(input, type);
    assertEquals("456", json);
  }

  @Test
    @Timeout(8000)
  public void testToJson_invokesPrivateToJsonWithWriter() throws Exception {
    String input = "privateTest";
    Type type = String.class;

    // Use reflection to invoke private method: toJson(Object src, Type typeOfSrc, Appendable writer)
    Method privateToJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, Appendable.class);
    privateToJsonMethod.setAccessible(true);

    StringWriter writer = new StringWriter();
    privateToJsonMethod.invoke(gson, input, type, writer);

    assertEquals("\"privateTest\"", writer.toString());
  }

  @Test
    @Timeout(8000)
  public void testToJson_withComplexObject_returnsJson() {
    class Person {
      String name = "John";
      int age = 30;
    }
    Person person = new Person();
    String json = gson.toJson(person, Person.class);
    assertTrue(json.contains("\"name\":\"John\""));
    assertTrue(json.contains("\"age\":30"));
  }

  @Test
    @Timeout(8000)
  public void testToJson_withArray_returnsJsonArray() {
    Integer[] numbers = {1, 2, 3};
    String json = gson.toJson(numbers, Integer[].class);
    assertEquals("[1,2,3]", json);
  }

  @Test
    @Timeout(8000)
  public void testToJson_withList_returnsJsonArray() {
    java.util.List<String> list = java.util.Arrays.asList("a", "b", "c");
    Type type = new TypeToken<java.util.List<String>>() {}.getType();
    String json = gson.toJson(list, type);
    assertEquals("[\"a\",\"b\",\"c\"]", json);
  }

  @Test
    @Timeout(8000)
  public void testToJson_withMap_returnsJsonObject() {
    java.util.Map<String, Integer> map = new java.util.HashMap<>();
    map.put("one", 1);
    map.put("two", 2);
    Type type = new TypeToken<java.util.Map<String, Integer>>() {}.getType();
    String json = gson.toJson(map, type);
    assertTrue(json.contains("\"one\":1"));
    assertTrue(json.contains("\"two\":2"));
  }
}