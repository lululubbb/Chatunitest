package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.*;
import java.lang.reflect.*;
import java.util.Collections;

public class Gson_315_3Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testDefaultConstructorCreatesNonNullGson() {
    assertNotNull(gson);
    // Check default fields via reflection
    try {
      Field excluderField = Gson.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      Excluder excluder = (Excluder) excluderField.get(gson);
      assertNotNull(excluder);

      Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
      serializeNullsField.setAccessible(true);
      boolean serializeNulls = serializeNullsField.getBoolean(gson);
      assertFalse(serializeNulls);

      Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
      htmlSafeField.setAccessible(true);
      boolean htmlSafe = htmlSafeField.getBoolean(gson);
      assertTrue(htmlSafe);

      Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
      prettyPrintingField.setAccessible(true);
      boolean prettyPrinting = prettyPrintingField.getBoolean(gson);
      assertFalse(prettyPrinting);

      Field lenientField = Gson.class.getDeclaredField("lenient");
      lenientField.setAccessible(true);
      boolean lenient = lenientField.getBoolean(gson);
      assertFalse(lenient);

      Field complexMapKeySerializationField = Gson.class.getDeclaredField("complexMapKeySerialization");
      complexMapKeySerializationField.setAccessible(true);
      boolean complexMapKeySerialization = complexMapKeySerializationField.getBoolean(gson);
      assertFalse(complexMapKeySerialization);

      Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
      generateNonExecutableJsonField.setAccessible(true);
      boolean generateNonExecutableJson = generateNonExecutableJsonField.getBoolean(gson);
      assertFalse(generateNonExecutableJson);

      Field serializeSpecialFloatingPointValuesField = Gson.class.getDeclaredField("serializeSpecialFloatingPointValues");
      serializeSpecialFloatingPointValuesField.setAccessible(true);
      boolean serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValuesField.getBoolean(gson);
      assertFalse(serializeSpecialFloatingPointValues);

      Field useJdkUnsafeField = Gson.class.getDeclaredField("useJdkUnsafe");
      useJdkUnsafeField.setAccessible(true);
      boolean useJdkUnsafe = useJdkUnsafeField.getBoolean(gson);
      assertTrue(useJdkUnsafe);

      Field datePatternField = Gson.class.getDeclaredField("datePattern");
      datePatternField.setAccessible(true);
      assertNull(datePatternField.get(gson));

      Field longSerializationPolicyField = Gson.class.getDeclaredField("longSerializationPolicy");
      longSerializationPolicyField.setAccessible(true);
      Object longSerializationPolicy = longSerializationPolicyField.get(gson);
      assertNotNull(longSerializationPolicy);
      assertEquals(LongSerializationPolicy.DEFAULT, longSerializationPolicy);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection error: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testPrivateLongAdapterMethod() throws Exception {
    Method longAdapterMethod = Gson.class.getDeclaredMethod("longAdapter", LongSerializationPolicy.class);
    longAdapterMethod.setAccessible(true);
    Object adapter = longAdapterMethod.invoke(null, LongSerializationPolicy.DEFAULT);
    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void testPrivateAtomicLongAdapterMethod() throws Exception {
    Method longAdapterMethod = Gson.class.getDeclaredMethod("longAdapter", LongSerializationPolicy.class);
    longAdapterMethod.setAccessible(true);
    Object longAdapter = longAdapterMethod.invoke(null, LongSerializationPolicy.DEFAULT);

    Method atomicLongAdapterMethod = Gson.class.getDeclaredMethod("atomicLongAdapter", TypeAdapter.class);
    atomicLongAdapterMethod.setAccessible(true);

    Object atomicLongAdapter = atomicLongAdapterMethod.invoke(null, longAdapter);
    assertNotNull(atomicLongAdapter);
    assertTrue(atomicLongAdapter instanceof TypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void testPrivateAtomicLongArrayAdapterMethod() throws Exception {
    Method longAdapterMethod = Gson.class.getDeclaredMethod("longAdapter", LongSerializationPolicy.class);
    longAdapterMethod.setAccessible(true);
    Object longAdapter = longAdapterMethod.invoke(null, LongSerializationPolicy.DEFAULT);

    Method atomicLongArrayAdapterMethod = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
    atomicLongArrayAdapterMethod.setAccessible(true);

    Object atomicLongArrayAdapter = atomicLongArrayAdapterMethod.invoke(null, longAdapter);
    assertNotNull(atomicLongArrayAdapter);
    assertTrue(atomicLongArrayAdapter instanceof TypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void testPrivateDoubleAdapterMethod() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);
    Object adapter = doubleAdapterMethod.invoke(gson, false);
    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void testPrivateFloatAdapterMethod() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);
    Object adapter = floatAdapterMethod.invoke(gson, false);
    assertNotNull(adapter);
    assertTrue(adapter instanceof TypeAdapter);
  }

  @Test
    @Timeout(8000)
  public void testCheckValidFloatingPoint() throws Exception {
    Method checkValidFloatingPointMethod = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    checkValidFloatingPointMethod.setAccessible(true);

    // Valid values
    checkValidFloatingPointMethod.invoke(null, 0.0);
    checkValidFloatingPointMethod.invoke(null, Double.POSITIVE_INFINITY);
    checkValidFloatingPointMethod.invoke(null, Double.NaN);

    // NaN and Infinite should throw IllegalArgumentException
    assertThrows(InvocationTargetException.class, () -> {
      try {
        checkValidFloatingPointMethod.invoke(null, Double.NaN);
      } catch (InvocationTargetException e) {
        if (e.getCause() instanceof IllegalArgumentException) throw e;
        throw e;
      }
    });

    assertThrows(InvocationTargetException.class, () -> {
      try {
        checkValidFloatingPointMethod.invoke(null, Double.POSITIVE_INFINITY);
      } catch (InvocationTargetException e) {
        if (e.getCause() instanceof IllegalArgumentException) throw e;
        throw e;
      }
    });

    assertThrows(InvocationTargetException.class, () -> {
      try {
        checkValidFloatingPointMethod.invoke(null, Double.NEGATIVE_INFINITY);
      } catch (InvocationTargetException e) {
        if (e.getCause() instanceof IllegalArgumentException) throw e;
        throw e;
      }
    });
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
  public void testGetAdapterReturnsTypeAdapter() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetAdapterWithClass() {
    TypeAdapter<String> adapter = gson.getAdapter(String.class);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetDelegateAdapterReturnsAdapter() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapterFactory skipPast = mock(TypeAdapterFactory.class);
    TypeAdapter<String> adapter = gson.getDelegateAdapter(skipPast, stringTypeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testToJsonAndFromJsonRoundTrip() {
    String src = "hello";
    String json = gson.toJson(src);
    assertNotNull(json);
    String deserialized = gson.fromJson(json, String.class);
    assertEquals(src, deserialized);
  }

  @Test
    @Timeout(8000)
  public void testToJsonTreeAndFromJsonTree() {
    String src = "hello";
    JsonElement jsonElement = gson.toJsonTree(src);
    assertNotNull(jsonElement);
    String deserialized = gson.fromJson(jsonElement, String.class);
    assertEquals(src, deserialized);
  }

  @Test
    @Timeout(8000)
  public void testToJsonWithWriter() throws IOException {
    String src = "hello";
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);
    gson.toJson(src, String.class, jsonWriter);
    jsonWriter.close();
    String json = stringWriter.toString();
    assertNotNull(json);
    String deserialized = gson.fromJson(json, String.class);
    assertEquals(src, deserialized);
  }

  @Test
    @Timeout(8000)
  public void testFromJsonWithReader() throws IOException {
    String src = "hello";
    String json = gson.toJson(src);
    StringReader stringReader = new StringReader(json);
    String deserialized = gson.fromJson(stringReader, String.class);
    assertEquals(src, deserialized);
  }

  @Test
    @Timeout(8000)
  public void testFromJsonWithJsonReader() throws IOException {
    String src = "hello";
    String json = gson.toJson(src);
    JsonReader jsonReader = gson.newJsonReader(new StringReader(json));
    String deserialized = gson.fromJson(jsonReader, String.class);
    assertEquals(src, deserialized);
  }

  @Test
    @Timeout(8000)
  public void testNewJsonWriterAndReaderNotNull() {
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);
    assertNotNull(jsonWriter);

    StringReader stringReader = new StringReader("");
    JsonReader jsonReader = gson.newJsonReader(stringReader);
    assertNotNull(jsonReader);
  }

  @Test
    @Timeout(8000)
  public void testToJsonElementWithNull() {
    JsonElement element = gson.toJsonTree(null);
    assertTrue(element.isJsonNull());
  }

  @Test
    @Timeout(8000)
  public void testToJsonWithJsonElement() throws IOException {
    JsonElement element = gson.toJsonTree("test");
    StringWriter writer = new StringWriter();
    gson.toJson(element, writer);
    String json = writer.toString();
    assertTrue(json.contains("test"));
  }

  @Test
    @Timeout(8000)
  public void testToJsonWithJsonElementAndJsonWriter() throws IOException {
    JsonElement element = gson.toJsonTree("test");
    StringWriter stringWriter = new StringWriter();
    JsonWriter jsonWriter = gson.newJsonWriter(stringWriter);
    gson.toJson(element, jsonWriter);
    jsonWriter.close();
    String json = stringWriter.toString();
    assertTrue(json.contains("test"));
  }

  @Test
    @Timeout(8000)
  public void testFromJsonWithEmptyStringThrows() {
    assertThrows(JsonSyntaxException.class, () -> {
      gson.fromJson("", String.class);
    });
  }
}