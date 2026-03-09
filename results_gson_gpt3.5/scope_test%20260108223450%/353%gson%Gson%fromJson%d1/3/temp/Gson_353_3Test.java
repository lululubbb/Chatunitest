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
import java.util.Map;

class GsonFromJsonTest {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withJsonElementAndType() throws Exception {
    // Prepare a JsonElement (JsonPrimitive) and a Type (String.class)
    JsonElement jsonElement = new JsonPrimitive("testString");
    Type type = String.class;

    // Call fromJson(JsonElement, Type) - the focal method
    String result = gson.fromJson(jsonElement, type);

    // Assert the result equals the original string
    assertEquals("testString", result);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withJsonElementAndTypeToken() throws Exception {
    // Use reflection to access private fromJson(JsonElement, TypeToken) method
    Method fromJsonMethod = Gson.class.getDeclaredMethod("fromJson", JsonElement.class, TypeToken.class);
    fromJsonMethod.setAccessible(true);

    // Prepare JsonElement and TypeToken
    JsonElement jsonElement = new JsonPrimitive(123);
    TypeToken<Integer> typeToken = TypeToken.get(Integer.class);

    // Invoke private method
    Object result = fromJsonMethod.invoke(gson, jsonElement, typeToken);

    // Assert result equals 123 (Integer)
    assertEquals(123, ((Number) result).intValue());
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withJsonElementNull() throws Exception {
    // fromJson(JsonElement, Type) with null JsonElement should return null
    String result = gson.fromJson((JsonElement) null, String.class);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  public void testFromJson_withJsonElementAndComplexType() throws Exception {
    // Prepare a JsonElement representing a Map<String, Integer>
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key1", 1);
    jsonObject.addProperty("key2", 2);
    Type type = new TypeToken<Map<String, Integer>>() {}.getType();

    Map<String, Integer> result = gson.fromJson(jsonObject, type);

    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals(1, result.get("key1"));
    assertEquals(2, result.get("key2"));
  }
}