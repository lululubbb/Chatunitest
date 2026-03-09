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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.Reader;
import java.io.StringReader;
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
  public void testFromJson_withValidJson_shouldReturnObject() throws Exception {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    TestClass result = gson.fromJson(reader, typeToken);

    assertNotNull(result);
    assertEquals("test", result.name);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withEmptyJson_shouldReturnObject() throws Exception {
    String json = "{}";
    Reader reader = new StringReader(json);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    TestClass result = gson.fromJson(reader, typeToken);

    assertNotNull(result);
    assertNull(result.name);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withMalformedJson_shouldThrowJsonSyntaxException() {
    String json = "{name:}";
    Reader reader = new StringReader(json);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, typeToken));
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withNullReader_shouldThrowNullPointerException() {
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
    assertThrows(NullPointerException.class, () -> gson.fromJson((Reader) null, typeToken));
  }

  @Test
    @Timeout(8000)
  public void testAssertFullConsumption_withExtraTokens_shouldThrowJsonSyntaxException() throws Exception {
    String json = "{\"name\":\"test\"} extra";
    Reader reader = new StringReader(json);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    // Use reflection to invoke private static method assertFullConsumption
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", Reader.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    assertThrows(JsonSyntaxException.class, () -> {
      fromJsonMethod.invoke(gson, reader, typeToken);
    });
  }

  @Test
    @Timeout(8000)
  public void testFromJson_invokesFromJsonJsonReader() throws Exception {
    Reader reader = new StringReader("{\"name\":\"mock\"}");
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    // Spy on Gson to verify interaction with fromJson(JsonReader, TypeToken)
    Gson spyGson = Mockito.spy(new Gson());

    TestClass result = spyGson.fromJson(reader, typeToken);

    assertNotNull(result);
    assertEquals("mock", result.name);

    // Verify fromJson(JsonReader, TypeToken) called internally
    verify(spyGson).fromJson(any(JsonReader.class), eq(typeToken));
  }

  static class TestClass {
    String name;
  }
}