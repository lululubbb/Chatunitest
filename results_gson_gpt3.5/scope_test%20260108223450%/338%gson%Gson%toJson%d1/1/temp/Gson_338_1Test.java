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
import com.google.gson.JsonNull;
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
  void toJson_withJsonNull_returnsJsonNullString() {
    JsonElement jsonNull = JsonNull.INSTANCE;
    String json = gson.toJson(jsonNull);
    assertEquals("null", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withJsonPrimitiveString_returnsQuotedString() {
    JsonElement jsonPrimitive = new JsonPrimitive("test");
    String json = gson.toJson(jsonPrimitive);
    assertEquals("\"test\"", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withJsonPrimitiveNumber_returnsNumberString() {
    JsonElement jsonPrimitive = new JsonPrimitive(123);
    String json = gson.toJson(jsonPrimitive);
    assertEquals("123", json);
  }

  @Test
    @Timeout(8000)
  void toJson_withJsonPrimitiveBoolean_returnsBooleanString() {
    JsonElement jsonPrimitive = new JsonPrimitive(true);
    String json = gson.toJson(jsonPrimitive);
    assertEquals("true", json);
  }

  @Test
    @Timeout(8000)
  void toJson_reflectionInvoke_privateToJsonJsonElementWriter() throws Exception {
    JsonElement element = new JsonPrimitive("reflect");
    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class, com.google.gson.stream.JsonWriter.class);

    toJsonMethod.setAccessible(true);

    StringWriter stringWriter = new StringWriter();
    com.google.gson.stream.JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    // invoke private method
    toJsonMethod.invoke(gson, element, jsonWriter);
    jsonWriter.close();

    assertEquals("\"reflect\"", stringWriter.toString());
  }

  @Test
    @Timeout(8000)
  void toJson_withEmptyJsonElement_returnsEmptyJson() {
    JsonElement jsonElement = new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }

      @Override
      public void write(com.google.gson.stream.JsonWriter writer) {
        // Write nothing to simulate empty JsonElement
      }
    };
    // This is a dummy JsonElement that does not override toString,
    // so it will trigger default behavior.
    String json = gson.toJson(jsonElement);
    // The actual output depends on JsonElement implementation,
    // but it should not be null or empty.
    assertNotNull(json);
    assertFalse(json.isEmpty());
  }

}