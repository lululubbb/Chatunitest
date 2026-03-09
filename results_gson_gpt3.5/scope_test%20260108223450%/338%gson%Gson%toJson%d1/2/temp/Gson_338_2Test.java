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
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
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
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringWriter;
import java.lang.reflect.Method;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void testToJson_withJsonPrimitive() {
    JsonElement jsonElement = new JsonPrimitive("test");
    String json = gson.toJson(jsonElement);
    assertEquals("\"test\"", json);
  }

  @Test
    @Timeout(8000)
  void testToJson_withJsonNull() {
    JsonElement jsonElement = com.google.gson.JsonNull.INSTANCE;
    String json = gson.toJson(jsonElement);
    assertEquals("null", json);
  }

  @Test
    @Timeout(8000)
  void testToJson_withJsonObject() {
    com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
    jsonObject.addProperty("key", "value");
    String json = gson.toJson(jsonObject);
    assertEquals("{\"key\":\"value\"}", json);
  }

  @Test
    @Timeout(8000)
  void testToJson_withJsonArray() {
    com.google.gson.JsonArray jsonArray = new com.google.gson.JsonArray();
    jsonArray.add(new JsonPrimitive("value1"));
    jsonArray.add(new JsonPrimitive("value2"));
    String json = gson.toJson(jsonArray);
    assertEquals("[\"value1\",\"value2\"]", json);
  }

  @Test
    @Timeout(8000)
  void testToJson_invokesPrivateToJsonJsonWriter() throws Exception {
    // Use reflection to invoke private toJson(JsonElement, JsonWriter) method for branch coverage
    JsonElement jsonElement = new JsonPrimitive("reflectionTest");
    StringWriter stringWriter = new StringWriter();
    com.google.gson.stream.JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class, com.google.gson.stream.JsonWriter.class);
    toJsonMethod.setAccessible(true);
    toJsonMethod.invoke(gson, jsonElement, jsonWriter);
    jsonWriter.close();

    assertEquals("\"reflectionTest\"", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void testToJson_invokesToJsonJsonWriter_withEmptyJsonElement() throws Exception {
    // Test with JsonNull to cover branch
    JsonElement jsonElement = com.google.gson.JsonNull.INSTANCE;
    StringWriter stringWriter = new StringWriter();
    com.google.gson.stream.JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class, com.google.gson.stream.JsonWriter.class);
    toJsonMethod.setAccessible(true);
    toJsonMethod.invoke(gson, jsonElement, jsonWriter);
    jsonWriter.close();

    assertEquals("null", stringWriter.toString());
  }
}