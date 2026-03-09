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
  public void fromJson_validJson_returnsExpectedObject() {
    String json = "{\"name\":\"test\"}";
    Type type = new TypeToken<TestClass>() {}.getType();

    TestClass result = gson.fromJson(json, type);

    assertNotNull(result);
    assertEquals("test", result.name);
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullJson_returnsNull() {
    String json = null;
    Type type = new TypeToken<TestClass>() {}.getType();

    TestClass result = gson.fromJson(json, type);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_emptyJson_throwsJsonSyntaxException() {
    String json = "";
    Type type = new TypeToken<TestClass>() {}.getType();

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, type));
  }

  @Test
    @Timeout(8000)
  public void fromJson_invalidJson_throwsJsonSyntaxException() {
    String json = "{invalid json}";
    Type type = new TypeToken<TestClass>() {}.getType();

    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, type));
  }

  @Test
    @Timeout(8000)
  public void fromJson_callsFromJsonWithTypeToken() throws Exception {
    String json = "{\"name\":\"test\"}";
    Type type = new TypeToken<TestClass>() {}.getType();

    // Spy on gson to verify delegation to fromJson(String, TypeToken)
    Gson spyGson = Mockito.spy(gson);

    // Create a real TypeToken instance for the given type
    @SuppressWarnings("unchecked")
    TypeToken<TestClass> typeToken = (TypeToken<TestClass>) TypeToken.get(type);

    // Use Mockito's doReturn to stub the fromJson(String, TypeToken) method
    TestClass expected = new TestClass();
    doReturn(expected).when(spyGson).fromJson(eq(json), eq(typeToken));

    TestClass actual = spyGson.fromJson(json, type);

    assertSame(expected, actual);
    verify(spyGson).fromJson(eq(json), eq(typeToken));
  }

  // Helper class for testing
  static class TestClass {
    String name;
  }
}