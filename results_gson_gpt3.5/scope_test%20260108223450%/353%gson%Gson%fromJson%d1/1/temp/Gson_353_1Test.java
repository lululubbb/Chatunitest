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
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class GsonFromJsonJsonElementTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withValidJsonElementAndClass_returnsObject() {
    JsonElement jsonElement = gson.toJsonTree(new TestClass("test", 42));
    TestClass result = gson.fromJson(jsonElement, TestClass.class);
    assertNotNull(result);
    assertEquals("test", result.name);
    assertEquals(42, result.value);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withValidJsonElementAndTypeToken_returnsObject() {
    JsonElement jsonElement = gson.toJsonTree(new TestClass("hello", 100));
    TestClass result = gson.fromJson(jsonElement, new TypeToken<TestClass>(){}.getType());
    assertNotNull(result);
    assertEquals("hello", result.name);
    assertEquals(100, result.value);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withNullJsonElement_returnsNull() {
    TestClass result = gson.fromJson((JsonElement) null, TestClass.class);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withPrimitiveJsonElement_returnsPrimitive() {
    JsonElement jsonElement = new JsonPrimitive(123);
    Integer result = gson.fromJson(jsonElement, Integer.class);
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_invokesPrivateFromJsonJsonElementTypeToken() throws Exception {
    JsonElement jsonElement = gson.toJsonTree(new TestClass("private", 7));
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TestClass result = (TestClass) fromJsonMethod.invoke(gson, jsonElement, TypeToken.get(TestClass.class));
    assertNotNull(result);
    assertEquals("private", result.name);
    assertEquals(7, result.value);
  }

  static class TestClass {
    String name;
    int value;

    TestClass(String name, int value) {
      this.name = name;
      this.value = value;
    }
  }
}