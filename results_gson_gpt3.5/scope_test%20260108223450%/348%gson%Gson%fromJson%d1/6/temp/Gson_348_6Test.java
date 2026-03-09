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

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void fromJson_validJson_shouldReturnObject() throws Exception {
    String json = "{\"value\":123}";
    Reader reader = new StringReader(json);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    // Spy on gson to mock methods
    Gson spyGson = Mockito.spy(gson);

    // Prepare a JsonReader to be passed to fromJson(JsonReader, TypeToken)
    JsonReader jsonReader = new JsonReader(new StringReader(json));

    // Mock newJsonReader(Reader) to return our prepared JsonReader
    doReturn(jsonReader).when(spyGson).newJsonReader(any(Reader.class));

    // Mock fromJson(JsonReader, TypeToken) to return a TestClass instance
    TestClass expected = new TestClass(123);
    doReturn(expected).when(spyGson).fromJson(eq(jsonReader), eq(typeToken));

    // Use reflection to invoke private static method assertFullConsumption
    Method assertFullConsumptionMethod = Gson.class.getDeclaredMethod("assertFullConsumption", Object.class, JsonReader.class);
    assertFullConsumptionMethod.setAccessible(true);

    // Call the real fromJson(Reader, TypeToken) method on spyGson
    TestClass actual = spyGson.fromJson(reader, typeToken);

    // Verify the returned object
    assertNotNull(actual);
    assertEquals(123, actual.value);

    // Verify that assertFullConsumption was called without exception
    assertDoesNotThrow(() -> assertFullConsumptionMethod.invoke(null, actual, jsonReader));
  }

  @Test
    @Timeout(8000)
  public void fromJson_invalidJson_shouldThrowJsonSyntaxException() {
    String json = "{invalid json}";
    Reader reader = new StringReader(json);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, typeToken));
  }

  @Test
    @Timeout(8000)
  public void fromJson_emptyJson_shouldThrowJsonIOException() {
    String json = "";
    Reader reader = new StringReader(json);
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    assertThrows(JsonIOException.class, () -> gson.fromJson(reader, typeToken));
  }

  // Helper test class
  static class TestClass {
    int value;

    public TestClass(int value) {
      this.value = value;
    }
  }
}