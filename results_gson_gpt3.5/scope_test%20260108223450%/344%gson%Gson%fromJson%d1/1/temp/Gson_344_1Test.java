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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

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
  public void fromJson_validJson_shouldReturnExpectedObject() throws Exception {
    String json = "{\"name\":\"test\"}";
    TypeToken<TestClass> typeToken = TypeToken.get(TestClass.class);

    // Spy on Gson to mock fromJson(String, TypeToken) call
    Gson spyGson = Mockito.spy(gson);
    TestClass expected = new TestClass("test");

    // Use doAnswer to mock fromJson(String, TypeToken) method
    doAnswer(invocation -> {
      String argJson = invocation.getArgument(0);
      TypeToken<?> argTypeToken = invocation.getArgument(1);
      if (json.equals(argJson) && typeToken.equals(argTypeToken)) {
        return expected;
      }
      // Call real method for other cases
      return invocation.callRealMethod();
    }).when(spyGson).fromJson(anyString(), any(TypeToken.class));

    // Call the method under test
    TestClass actual = spyGson.fromJson(json, TestClass.class);

    assertNotNull(actual);
    assertEquals(expected.name, actual.name);
  }

  @Test
    @Timeout(8000)
  public void fromJson_nullJson_shouldReturnNull() {
    String json = null;
    TestClass result = gson.fromJson(json, TestClass.class);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_emptyJson_shouldThrowJsonSyntaxException() {
    String json = "";
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, TestClass.class));
  }

  @Test
    @Timeout(8000)
  public void fromJson_invalidJson_shouldThrowJsonSyntaxException() {
    String json = "{invalid json}";
    assertThrows(JsonSyntaxException.class, () -> gson.fromJson(json, TestClass.class));
  }

  @Test
    @Timeout(8000)
  public void fromJson_primitiveType_shouldReturnPrimitive() {
    String json = "123";
    Integer result = gson.fromJson(json, Integer.class);
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  public void fromJson_genericType_shouldReturnExpectedObject() {
    String json = "[\"a\",\"b\"]";
    Type type = new TypeToken<java.util.List<String>>() {}.getType();

    java.util.List<String> result = gson.fromJson(json, type);
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("a", result.get(0));
    assertEquals("b", result.get(1));
  }

  // Helper class for testing
  static class TestClass {
    String name;

    public TestClass() {}

    public TestClass(String name) {
      this.name = name;
    }
  }
}