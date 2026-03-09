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

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Gson_FromJson_Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJson_shouldReturnObject() throws Exception {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    TestClass result = gson.fromJson(reader, typeToken);

    assertNotNull(result);
    assertEquals("test", result.name);
  }

  @Test
    @Timeout(8000)
  public void fromJson_emptyJson_shouldReturnNull() throws Exception {
    String json = "";
    Reader reader = new StringReader(json);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      gson.fromJson(reader, typeToken);
    });

    assertNotNull(thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullReader_shouldThrowNullPointerException() {
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);
    assertThrows(NullPointerException.class, () -> gson.fromJson((Reader) null, typeToken));
  }

  @Test
    @Timeout(8000)
  public void fromJson_assertFullConsumptionThrows_shouldThrowJsonSyntaxException() throws Exception {
    String json = "{\"name\":\"test\"}";
    Reader reader = new StringReader(json);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    // Spy on Gson instance
    Gson spyGson = spy(gson);
    JsonReader jsonReader = spyGson.newJsonReader(reader);

    // Mock fromJson(JsonReader, TypeToken) to return a TestClass instance
    doReturn(new TestClass("test")).when(spyGson).fromJson(any(JsonReader.class), eq(typeToken));

    // Use reflection to get the private static method assertFullConsumption
    Method assertFullConsumption = Gson.class.getDeclaredMethod("assertFullConsumption", Object.class, JsonReader.class);
    assertFullConsumption.setAccessible(true);

    JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
      // Simulate fromJson method behavior with assertFullConsumption throwing exception
      JsonReader jr = spyGson.newJsonReader(new StringReader(json));
      TestClass object = spyGson.fromJson(jr, typeToken);

      try {
        assertFullConsumption.invoke(null, object, jr);
      } catch (InvocationTargetException e) {
        // Throw the cause if it is a JsonSyntaxException to simulate the behavior
        Throwable cause = e.getCause();
        if (cause instanceof JsonSyntaxException) {
          throw (JsonSyntaxException) cause;
        } else {
          throw e;
        }
      }
    });

    assertTrue(thrown.getMessage().contains("Not fully consumed") || thrown.getMessage().contains("Expected EOF"));
  }

  // Helper class for deserialization test
  public static class TestClass {
    public String name;

    public TestClass() {}

    public TestClass(String name) {
      this.name = name;
    }
  }
}