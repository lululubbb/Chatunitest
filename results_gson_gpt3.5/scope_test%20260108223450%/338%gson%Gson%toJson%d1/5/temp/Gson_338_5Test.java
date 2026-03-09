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

import com.google.gson.stream.JsonWriter;
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
  void toJson_withNullJsonElement_returnsJsonNullString() {
    JsonElement jsonElement = JsonNull.INSTANCE;
    String json = gson.toJson(jsonElement);
    assertEquals("null", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withJsonPrimitive_returnsPrimitiveAsString() {
    JsonElement jsonElement = new JsonPrimitive("test");
    String json = gson.toJson(jsonElement);
    assertEquals("\"test\"", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withJsonObject_returnsJsonObjectString() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("key", "value");
    String json = gson.toJson(jsonObject);
    assertEquals("{\"key\":\"value\"}", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withJsonArray_returnsJsonArrayString() {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));
    String json = gson.toJson(jsonArray);
    assertEquals("[1,2]", json);
  }

  @Test
    @Timeout(8000)
  void toJson_invokesToJsonJsonElementWriter() throws Exception {
    JsonPrimitive primitive = new JsonPrimitive("test");
    JsonElement element = primitive;

    StringWriter stringWriter = spy(new StringWriter());
    Gson spyGson = spy(gson);

    JsonWriter jsonWriter = spyGson.newJsonWriter(stringWriter);

    // Use reflection to invoke private toJson(JsonElement, JsonWriter)
    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class, JsonWriter.class);
    toJsonMethod.setAccessible(true);
    toJsonMethod.invoke(spyGson, element, jsonWriter);

    // Verify that the internal 'value' method of JsonWriter was called with "test"
    verify(jsonWriter, atLeastOnce()).value("test");

    String json = stringWriter.toString();
    assertNotNull(json);
  }

  @Test
    @Timeout(8000)
  void toJson_withEmptyJsonObject_returnsEmptyBraces() {
    JsonObject jsonObject = new JsonObject();
    String json = gson.toJson(jsonObject);
    assertEquals("{}", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withJsonPrimitiveNumber_returnsNumberString() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);
    String json = gson.toJson(jsonPrimitive);
    assertEquals("123", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withJsonPrimitiveBoolean_returnsBooleanString() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    String json = gson.toJson(jsonPrimitive);
    assertEquals("true", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withJsonPrimitiveNull_returnsNullString() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("null");
    String json = gson.toJson(jsonPrimitive);
    assertEquals("\"null\"", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withComplexJsonElement_returnsCorrectJson() {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("string", "value");
    jsonObject.addProperty("number", 10);
    jsonObject.addProperty("bool", true);
    JsonArray jsonArray = new JsonArray();
    jsonArray.add(new JsonPrimitive(1));
    jsonArray.add(new JsonPrimitive(2));
    jsonObject.add("array", jsonArray);

    String json = gson.toJson(jsonObject);
    assertEquals("{\"string\":\"value\",\"number\":10,\"bool\":true,\"array\":[1,2]}", json);
  }
}