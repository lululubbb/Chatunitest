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

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.io.StringWriter;
import java.lang.reflect.Method;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonToJsonTest {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void testToJson_withJsonPrimitive() {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("testString");
    String json = gson.toJson(jsonPrimitive);
    assertEquals("\"testString\"", json);
  }

  @Test
    @Timeout(8000)
  void testToJson_withJsonElement_null() {
    String json = gson.toJson((JsonElement) null);
    assertEquals("null", json);
  }

  @Test
    @Timeout(8000)
  void testToJson_invokesPrivateToJsonJsonElementWriter() throws Exception {
    JsonElement jsonElement = new JsonPrimitive("privateTest");
    StringWriter stringWriter = new StringWriter();

    // The private method toJson(JsonElement, JsonWriter) is the one to invoke
    Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", JsonElement.class, com.google.gson.stream.JsonWriter.class);
    toJsonMethod.setAccessible(true);

    // Create JsonWriter wrapping the StringWriter
    com.google.gson.stream.JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);

    toJsonMethod.invoke(gson, jsonElement, jsonWriter);

    // Flush and close the JsonWriter to ensure content is written to the StringWriter
    jsonWriter.flush();
    jsonWriter.close();

    assertEquals("\"privateTest\"", stringWriter.toString());
  }
}