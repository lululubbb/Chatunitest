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
import java.util.List;

class GsonFromJsonJsonElementTypeTokenTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullJsonElement_shouldReturnNull() throws Exception {
    JsonElement json = null;
    TypeToken<String> typeToken = TypeToken.get(String.class);

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    Object result = fromJsonMethod.invoke(gson, json, typeToken);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withJsonElementAndTypeToken_shouldDeserializeCorrectly() throws Exception {
    String jsonString = "{\"name\":\"John\",\"age\":30}";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    TypeToken<Person> typeToken = TypeToken.get(Person.class);

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    Person person = (Person) fromJsonMethod.invoke(gson, jsonElement, typeToken);

    assertNotNull(person);
    assertEquals("John", person.name);
    assertEquals(30, person.age);
  }

  @Test
    @Timeout(8000)
  void fromJson_withJsonPrimitive_shouldDeserializePrimitive() throws Exception {
    JsonElement jsonElement = JsonParser.parseString("123");
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    Integer result = (Integer) fromJsonMethod.invoke(gson, jsonElement, typeToken);

    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withJsonNull_shouldReturnNull() throws Exception {
    JsonElement jsonElement = JsonNull.INSTANCE;
    TypeToken<String> typeToken = TypeToken.get(String.class);

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    Object result = fromJsonMethod.invoke(gson, jsonElement, typeToken);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withJsonArray_shouldDeserializeList() throws Exception {
    String jsonString = "[\"a\",\"b\",\"c\"]";
    JsonElement jsonElement = JsonParser.parseString(jsonString);
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    @SuppressWarnings("unchecked")
    List<String> list = (List<String>) fromJsonMethod.invoke(gson, jsonElement, typeToken);

    assertNotNull(list);
    assertEquals(3, list.size());
    assertEquals("a", list.get(0));
    assertEquals("b", list.get(1));
    assertEquals("c", list.get(2));
  }

  private static class Person {
    String name;
    int age;
  }
}