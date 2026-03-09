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

import com.google.gson.*;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

class Gson_315_2Test {

  private Gson gson;

  @BeforeEach
  void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  void testDefaultConstructor_initializesFields() throws Exception {
    // Using reflection to check private final fields are initialized correctly

    Field excluderField = Gson.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gson);
    assertNotNull(excluder);
    assertEquals(Excluder.DEFAULT, excluder);

    Field fieldNamingStrategyField = Gson.class.getDeclaredField("fieldNamingStrategy");
    fieldNamingStrategyField.setAccessible(true);
    FieldNamingStrategy fieldNamingStrategy = (FieldNamingStrategy) fieldNamingStrategyField.get(gson);
    assertNotNull(fieldNamingStrategy);
    assertEquals(FieldNamingPolicy.IDENTITY, fieldNamingStrategy);

    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNulls = serializeNullsField.getBoolean(gson);
    assertFalse(serializeNulls);

    Field complexMapKeySerializationField = Gson.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    boolean complexMapKeySerialization = complexMapKeySerializationField.getBoolean(gson);
    assertFalse(complexMapKeySerialization);

    Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    boolean generateNonExecutableJson = generateNonExecutableJsonField.getBoolean(gson);
    assertFalse(generateNonExecutableJson);

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

    Field serializeSpecialFloatingPointValuesField = Gson.class.getDeclaredField("serializeSpecialFloatingPointValues");
    serializeSpecialFloatingPointValuesField.setAccessible(true);
    boolean serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValuesField.getBoolean(gson);
    assertFalse(serializeSpecialFloatingPointValues);

    Field useJdkUnsafeField = Gson.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    boolean useJdkUnsafe = useJdkUnsafeField.getBoolean(gson);
    assertTrue(useJdkUnsafe);

    Field longSerializationPolicyField = Gson.class.getDeclaredField("longSerializationPolicy");
    longSerializationPolicyField.setAccessible(true);
    LongSerializationPolicy longSerializationPolicy = (LongSerializationPolicy) longSerializationPolicyField.get(gson);
    assertEquals(LongSerializationPolicy.DEFAULT, longSerializationPolicy);

    Field datePatternField = Gson.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    String datePattern = (String) datePatternField.get(gson);
    assertNull(datePattern);

    Field dateStyleField = Gson.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    int dateStyle = dateStyleField.getInt(gson);
    assertEquals(DateFormat.DEFAULT, dateStyle);

    Field timeStyleField = Gson.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    int timeStyle = timeStyleField.getInt(gson);
    assertEquals(DateFormat.DEFAULT, timeStyle);

    Field instanceCreatorsField = Gson.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<?, ?> instanceCreators = (Map<?, ?>) instanceCreatorsField.get(gson);
    assertNotNull(instanceCreators);
    assertTrue(instanceCreators.isEmpty());

    Field objectToNumberStrategyField = Gson.class.getDeclaredField("objectToNumberStrategy");
    objectToNumberStrategyField.setAccessible(true);
    ToNumberStrategy objectToNumberStrategy = (ToNumberStrategy) objectToNumberStrategyField.get(gson);
    assertEquals(ToNumberPolicy.DOUBLE, objectToNumberStrategy);

    Field numberToNumberStrategyField = Gson.class.getDeclaredField("numberToNumberStrategy");
    numberToNumberStrategyField.setAccessible(true);
    ToNumberStrategy numberToNumberStrategy = (ToNumberStrategy) numberToNumberStrategyField.get(gson);
    assertEquals(ToNumberPolicy.LAZILY_PARSED_NUMBER, numberToNumberStrategy);

    Field reflectionFiltersField = Gson.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);
    List<?> reflectionFilters = (List<?>) reflectionFiltersField.get(gson);
    assertNotNull(reflectionFilters);
    assertTrue(reflectionFilters.isEmpty());

    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<?> factories = (List<?>) factoriesField.get(gson);
    assertNotNull(factories);
  }

  @Test
    @Timeout(8000)
  void testToJsonAndFromJson_cycle() {
    // Test that serializing and deserializing a simple object works correctly

    class Person {
      String name;
      int age;

      Person() {}

      Person(String name, int age) {
        this.name = name;
        this.age = age;
      }

      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Person)) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
      }

      @Override
      public int hashCode() {
        return Objects.hash(name, age);
      }
    }

    Person original = new Person("Alice", 30);
    String json = gson.toJson(original);
    assertNotNull(json);
    Person deserialized = gson.fromJson(json, Person.class);
    assertEquals(original, deserialized);
  }

  @Test
    @Timeout(8000)
  void testGetAdapter_returnsNonNullAdapter() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testGetDelegateAdapter_skipsFactory() {
    TypeAdapterFactory mockFactory = mock(TypeAdapterFactory.class);
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = gson.getDelegateAdapter(mockFactory, stringTypeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testToJsonTree_andFromJsonTree() {
    Map<String, Integer> map = new HashMap<>();
    map.put("one", 1);
    map.put("two", 2);

    JsonElement jsonElement = gson.toJsonTree(map);
    assertNotNull(jsonElement);
    Map<?, ?> deserialized = gson.fromJson(jsonElement, Map.class);
    assertEquals(map, deserialized);
  }

  @Test
    @Timeout(8000)
  void testToString_containsClassName() {
    String str = gson.toString();
    assertTrue(str.contains("Gson"));
  }

  @Test
    @Timeout(8000)
  void testPrivateDoubleAdapter_reflection() throws Exception {
    Method doubleAdapterMethod = Gson.class.getDeclaredMethod("doubleAdapter", boolean.class);
    doubleAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) doubleAdapterMethod.invoke(gson, false);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testPrivateFloatAdapter_reflection() throws Exception {
    Method floatAdapterMethod = Gson.class.getDeclaredMethod("floatAdapter", boolean.class);
    floatAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) floatAdapterMethod.invoke(gson, false);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testPrivateLongAdapter_reflection() throws Exception {
    Method longAdapterMethod = Gson.class.getDeclaredMethod("longAdapter", LongSerializationPolicy.class);
    longAdapterMethod.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeAdapter<Number> adapter = (TypeAdapter<Number>) longAdapterMethod.invoke(null, LongSerializationPolicy.DEFAULT);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  void testPrivateAtomicLongAdapter_reflection() throws Exception {
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
  void testPrivateAtomicLongArrayAdapter_reflection() throws Exception {
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
  void testCheckValidFloatingPoint_acceptsValidValues() throws Exception {
    Method checkValidFloatingPoint = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    checkValidFloatingPoint.setAccessible(true);

    // Should not throw for normal double values
    checkValidFloatingPoint.invoke(null, 1.0);
    checkValidFloatingPoint.invoke(null, -1.0);
    checkValidFloatingPoint.invoke(null, 0.0);
    checkValidFloatingPoint.invoke(null, Double.MAX_VALUE);
    checkValidFloatingPoint.invoke(null, Double.MIN_VALUE);
  }

  @Test
    @Timeout(8000)
  void testCheckValidFloatingPoint_throwsOnNaNAndInfinite() throws Exception {
    Method checkValidFloatingPoint = Gson.class.getDeclaredMethod("checkValidFloatingPoint", double.class);
    checkValidFloatingPoint.setAccessible(true);

    try {
      checkValidFloatingPoint.invoke(null, Double.NaN);
      fail("Expected IllegalArgumentException for NaN");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    try {
      checkValidFloatingPoint.invoke(null, Double.POSITIVE_INFINITY);
      fail("Expected IllegalArgumentException for POSITIVE_INFINITY");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof IllegalArgumentException);
    }

    try {
      checkValidFloatingPoint.invoke(null, Double.NEGATIVE_INFINITY);
      fail("Expected IllegalArgumentException for NEGATIVE_INFINITY");
    } catch (Exception e) {
      assertTrue(e.getCause() instanceof IllegalArgumentException);
    }
  }
}