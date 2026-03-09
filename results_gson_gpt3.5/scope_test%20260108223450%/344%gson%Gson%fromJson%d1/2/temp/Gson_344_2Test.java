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
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Type;

class GsonFromJsonStringTypeTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_withValidJsonAndType_shouldReturnParsedObject() {
    String json = "{\"name\":\"test\"}";
    Type type = new TypeToken<TestClass>() {}.getType();

    TestClass result = gson.fromJson(json, type);

    assertNotNull(result);
    assertEquals("test", result.name);
  }

  @Test
    @Timeout(8000)
  public void fromJson_withNullJson_shouldReturnNull() {
    Type type = new TypeToken<TestClass>() {}.getType();

    TestClass result = gson.fromJson((String) null, type);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_withEmptyJson_shouldThrowJsonSyntaxException() {
    String json = "";

    Type type = new TypeToken<TestClass>() {}.getType();

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, type));
  }

  @Test
    @Timeout(8000)
  public void fromJson_withMalformedJson_shouldThrowJsonSyntaxException() {
    String json = "{name:\"test\""; // missing closing brace

    Type type = new TypeToken<TestClass>() {}.getType();

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, type));
  }

  @Test
    @Timeout(8000)
  public void fromJson_withPrimitiveType_shouldReturnParsedValue() {
    String json = "123";
    Type type = Integer.class;

    Integer result = gson.fromJson(json, type);

    assertNotNull(result);
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_callsFromJsonWithTypeToken() {
    String json = "{\"name\":\"test\"}";
    Type type = new TypeToken<TestClass>() {}.getType();

    Gson spyGson = Mockito.spy(gson);

    TypeToken<TestClass> typeToken = new TypeToken<TestClass>() {};
    doReturn(new TestClass("mocked")).when(spyGson).fromJson(json, typeToken);

    TestClass result = spyGson.fromJson(json, type);

    assertNotNull(result);
    assertEquals("mocked", result.name);

    verify(spyGson).fromJson(json, typeToken);
  }

  // Helper test class for deserialization
  static class TestClass {
    String name;

    public TestClass() {
    }

    public TestClass(String name) {
      this.name = name;
    }
  }
}