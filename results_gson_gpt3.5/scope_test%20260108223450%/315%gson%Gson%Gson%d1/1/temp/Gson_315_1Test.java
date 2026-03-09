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
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.*;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Gson_315_1Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testDefaultConstructorCreatesInstance() {
    assertNotNull(gson);
    // Check default config values via reflection
    try {
      var excluderField = Gson.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      Excluder excluder = (Excluder) excluderField.get(gson);
      assertNotNull(excluder);

      var serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
      serializeNullsField.setAccessible(true);
      boolean serializeNulls = serializeNullsField.getBoolean(gson);
      assertFalse(serializeNulls);

      var prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
      prettyPrintingField.setAccessible(true);
      boolean prettyPrinting = prettyPrintingField.getBoolean(gson);
      assertFalse(prettyPrinting);

      var htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
      htmlSafeField.setAccessible(true);
      boolean htmlSafe = htmlSafeField.getBoolean(gson);
      assertTrue(htmlSafe);

      var lenientField = Gson.class.getDeclaredField("lenient");
      lenientField.setAccessible(true);
      boolean lenient = lenientField.getBoolean(gson);
      assertFalse(lenient);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testGetAdapterReturnsTypeAdapter() {
    TypeToken<String> stringType = TypeToken.get(String.class);
    TypeAdapter<String> adapter = gson.getAdapter(stringType);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetDelegateAdapterSkipsFactory() {
    TypeToken<String> stringType = TypeToken.get(String.class);
    TypeAdapterFactory skipFactory = mock(TypeAdapterFactory.class);
    TypeAdapter<String> adapter = gson.getDelegateAdapter(skipFactory, stringType);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testToJsonAndFromJsonCycle() {
    String original = "test string";
    String json = gson.toJson(original);
    assertNotNull(json);
    String deserialized = gson.fromJson(json, String.class);
    assertEquals(original, deserialized);
  }

  @Test
    @Timeout(8000)
  public void testToJsonTreeAndFromJsonElement() {
    String original = "hello";
    JsonElement jsonElement = gson.toJsonTree(original);
    assertNotNull(jsonElement);
    String deserialized = gson.fromJson(jsonElement, String.class);
    assertEquals(original, deserialized);
  }

  @Test
    @Timeout(8000)
  public void testToJsonWithAppendable() throws IOException {
    String original = "appendable test";
    StringWriter writer = new StringWriter();
    gson.toJson(original, writer);
    String json = writer.toString();
    assertNotNull(json);
    assertTrue(json.contains(original));
  }

  @Test
    @Timeout(8000)
  public void testToJsonWithJsonWriter() throws IOException {
    String original = "jsonwriter test";
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);
    gson.toJson(original, String.class, jsonWriter);
    jsonWriter.close();
    String json = stringWriter.toString();
    assertNotNull(json);
    assertTrue(json.contains(original));
  }

  @Test
    @Timeout(8000)
  public void testFromJsonWithReader() throws IOException {
    String json = "\"reader test\"";
    StringReader reader = new StringReader(json);
    String result = gson.fromJson(reader, String.class);
    assertEquals("reader test", result);
  }

  @Test
    @Timeout(8000)
  public void testFromJsonWithJsonReader() throws IOException {
    String json = "\"jsonreader test\"";
    JsonReader jsonReader = gson.newJsonReader(new StringReader(json));
    String result = gson.fromJson(jsonReader, String.class);
    assertEquals("jsonreader test", result);
  }

  @Test
    @Timeout(8000)
  public void testPrivateDoubleAdapterMethod() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);
    assertNotNull(adapter);
    // Check serialization of normal double
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    adapter.write(jsonWriter, 1.23);
    jsonWriter.close();
    assertEquals("1.23", writer.toString());
  }

  @Test
    @Timeout(8000)
  public void testPrivateFloatAdapterMethod() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);
    assertNotNull(adapter);
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    adapter.write(jsonWriter, 4.56f);
    jsonWriter.close();
    assertEquals("4.56", writer.toString());
  }

  @Test
    @Timeout(8000)
  public void testPrivateLongAdapterMethod() throws Exception {
    Method longAdapterMethod = Gson.class.getDeclaredMethod("longAdapter", LongSerializationPolicy.class);
    longAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.DEFAULT);
    assertNotNull(adapter);
    StringWriter writer = new StringWriter();
    JsonWriter jsonWriter = new JsonWriter(writer);
    adapter.write(jsonWriter, 123L);
    jsonWriter.close();
    assertEquals("123", writer.toString());
  }

  @Test
    @Timeout(8000)
  public void testAtomicLongAdapter() throws Exception {
    Method longAdapterMethod = Gson.class.getDeclaredMethod("longAdapter", LongSerializationPolicy.class);
    longAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> longAdapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.DEFAULT);
    Method atomicLongAdapterMethod = Gson.class.getDeclaredMethod("atomicLongAdapter", TypeAdapter.class);
    atomicLongAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<?> atomicLongAdapter = (TypeAdapter<?>) atomicLongAdapterMethod.invoke(null, longAdapter);
    assertNotNull(atomicLongAdapter);
  }

  @Test
    @Timeout(8000)
  public void testAtomicLongArrayAdapter() throws Exception {
    Method longAdapterMethod = Gson.class.getDeclaredMethod("longAdapter", LongSerializationPolicy.class);
    longAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> longAdapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.DEFAULT);
    Method atomicLongArrayAdapterMethod = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    atomicLongArrayAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<?> atomicLongArrayAdapter = (TypeAdapter<?>) atomicLongArrayAdapterMethod.invoke(null, longAdapter);
    assertNotNull(atomicLongArrayAdapter);
  }

  @Test
    @Timeout(8000)
  public void testCheckValidFloatingPoint() throws Exception {
    Method checkValidFloatingPoint = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    checkValidFloatingPoint.setAccessible(true);
    // Valid values
    checkValidFloatingPoint.invoke(null, 0.0);
    checkValidFloatingPoint.invoke(null, 1.0);
    checkValidFloatingPoint.invoke(null, -1.0);
    // Invalid values should throw IllegalArgumentException
    assertThrows(InvocationTargetException.class, () -> checkValidFloatingPoint.invoke(null, Double.NaN));
    assertThrows(InvocationTargetException.class, () -> checkValidFloatingPoint.invoke(null, Double.POSITIVE_INFINITY));
    assertThrows(InvocationTargetException.class, () -> checkValidFloatingPoint.invoke(null, Double.NEGATIVE_INFINITY));
  }

  @Test
    @Timeout(8000)
  public void testToStringContainsClassName() {
    String str = gson.toString();
    assertNotNull(str);
    assertTrue(str.contains("Gson"));
  }

  @Test
    @Timeout(8000)
  public void testNewBuilderReturnsGsonBuilder() {
    GsonBuilder builder = gson.newBuilder();
    assertNotNull(builder);
  }

  @Test
    @Timeout(8000)
  public void testExcluderMethod() {
    Excluder excluder = gson.excluder();
    assertNotNull(excluder);
  }

  @Test
    @Timeout(8000)
  public void testFieldNamingStrategyMethod() {
    FieldNamingStrategy strategy = gson.fieldNamingStrategy();
    assertNotNull(strategy);
  }

  @Test
    @Timeout(8000)
  public void testSerializeNullsMethod() {
    boolean serializeNulls = gson.serializeNulls();
    assertFalse(serializeNulls);
  }

  @Test
    @Timeout(8000)
  public void testHtmlSafeMethod() {
    boolean htmlSafe = gson.htmlSafe();
    assertTrue(htmlSafe);
  }
}