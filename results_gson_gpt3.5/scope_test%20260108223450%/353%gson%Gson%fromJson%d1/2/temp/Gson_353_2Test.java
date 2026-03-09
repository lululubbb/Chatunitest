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

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void fromJson_withJsonElementAndType_returnsExpectedObject() throws Exception {
    // Arrange
    String jsonString = "\"test\""; // JSON string representing a String literal
    Type type = String.class;

    // Create a JsonElement by parsing the JSON string using Gson's fromJson(String, JsonElement.class)
    JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);

    // Act
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);
    Object result = fromJsonMethod.invoke(gson, jsonElement, TypeToken.get(type));

    // Assert
    assertNotNull(result);
    assertTrue(result instanceof String);
    assertEquals("test", result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withNullJsonElement_returnsNull() throws Exception {
    // Arrange
    JsonElement jsonElement = null;
    Type type = String.class;

    // Act
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);
    Object result = fromJsonMethod.invoke(gson, jsonElement, TypeToken.get(type));

    // Assert
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void fromJson_withInvalidJson_throwsJsonSyntaxException() throws Exception {
    // Arrange
    String invalidJson = "invalid json";
    Type type = String.class;

    // Parse invalid JSON string into JsonElement using fromJson(String, JsonElement.class)
    JsonElement jsonElement = gson.fromJson(invalidJson, JsonElement.class);

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    // Act & Assert
    try {
      fromJsonMethod.invoke(gson, jsonElement, TypeToken.get(type));
      fail("Expected JsonSyntaxException");
    } catch (Exception e) {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof JsonSyntaxException, "Expected cause to be JsonSyntaxException");
    }
  }

  @Test
    @Timeout(8000)
  void fromJson_withComplexTypeToken_returnsExpectedObject() throws Exception {
    // Arrange
    String jsonString = "[\"one\", \"two\"]";
    TypeToken<java.util.List<String>> typeToken = new TypeToken<java.util.List<String>>() {};

    // Parse JSON string into JsonElement using fromJson(String, JsonElement.class)
    JsonElement jsonElement = gson.fromJson(jsonString, JsonElement.class);

    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    // Act
    Object result = fromJsonMethod.invoke(gson, jsonElement, typeToken);

    // Assert
    assertNotNull(result);
    assertTrue(result instanceof java.util.List);
    @SuppressWarnings("unchecked")
    java.util.List<String> list = (java.util.List<String>) result;
    assertEquals(2, list.size());
    assertEquals("one", list.get(0));
    assertEquals("two", list.get(1));
  }
}