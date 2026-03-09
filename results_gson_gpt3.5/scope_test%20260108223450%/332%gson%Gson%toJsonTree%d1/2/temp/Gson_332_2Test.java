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

import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class Gson_332_2Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testToJsonTree_withNullSrc() {
    JsonElement result = gson.toJsonTree(null, Object.class);
    assertNotNull(result);
    assertTrue(result.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testToJsonTree_withSimpleObject() {
    String src = "test";
    JsonElement result = gson.toJsonTree(src, String.class);
    assertNotNull(result);
    assertTrue(result.isJsonPrimitive());
    assertEquals("test", result.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testToJsonTree_withGenericType() {
    Type typeOfSrc = new TypeToken<java.util.List<String>>() {}.getType();
    java.util.List<String> src = java.util.Arrays.asList("one", "two");
    JsonElement result = gson.toJsonTree(src, typeOfSrc);
    assertNotNull(result);
    assertTrue(result.isJsonArray());
    assertEquals(2, result.getAsJsonArray().size());
    assertEquals("one", result.getAsJsonArray().get(0).getAsString());
    assertEquals("two", result.getAsJsonArray().get(1).getAsString());
  }

  @Test
    @Timeout(8000)
  public void testToJsonTree_invokesToJsonWithJsonTreeWriter() throws Exception {
    // Use reflection to invoke private method toJson(Object, Type, JsonWriter)
    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class, Type.class, JsonWriter.class);
    toJsonMethod.setAccessible(true);

    JsonTreeWriter writer = new JsonTreeWriter();
    String src = "reflectionTest";

    toJsonMethod.invoke(gson, src, String.class, writer);

    JsonElement element = writer.get();
    assertNotNull(element);
    assertTrue(element.isJsonPrimitive());
    assertEquals("reflectionTest", element.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testToJsonTree_withCustomTypeAdapter() {
    // Create a Gson instance with a custom TypeAdapter for Integer
    TypeAdapter<Integer> intAdapter = new TypeAdapter<Integer>() {
      @Override
      public void write(JsonWriter out, Integer value) {
        try {
          out.value(value == null ? 0 : value * 2);
        } catch (Exception e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public Integer read(JsonReader in) {
        throw new UnsupportedOperationException();
      }
    };

    Gson gsonWithAdapter = new GsonBuilder()
        .registerTypeAdapter(Integer.class, intAdapter)
        .create();

    Integer src = 5;
    JsonElement result = gsonWithAdapter.toJsonTree(src, Integer.class);
    assertNotNull(result);
    assertTrue(result.isJsonPrimitive());
    assertEquals(10, result.getAsInt());
  }
}