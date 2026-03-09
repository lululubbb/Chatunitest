package com.google.gson;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.Gson.DEFAULT_COMPLEX_MAP_KEYS;
import static com.google.gson.Gson.DEFAULT_DATE_PATTERN;
import static com.google.gson.Gson.DEFAULT_ESCAPE_HTML;
import static com.google.gson.Gson.DEFAULT_JSON_NON_EXECUTABLE;
import static com.google.gson.Gson.DEFAULT_LENIENT;
import static com.google.gson.Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY;
import static com.google.gson.Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY;
import static com.google.gson.Gson.DEFAULT_PRETTY_PRINT;
import static com.google.gson.Gson.DEFAULT_SERIALIZE_NULLS;
import static com.google.gson.Gson.DEFAULT_SPECIALIZE_FLOAT_VALUES;
import static com.google.gson.Gson.DEFAULT_USE_JDK_UNSAFE;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.sql.SqlTypesSupport;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.List;
import java.util.Map;

class GsonBuilder_1_4Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testDefaultFieldValuesAfterConstructor() throws Exception {
    // Verify default field values set by constructor
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(Excluder.DEFAULT, excluder);

    Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    assertEquals(Gson.DEFAULT_SERIALIZE_NULLS, serializeNullsField.getBoolean(gsonBuilder));

    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    assertEquals(Gson.DEFAULT_DATE_PATTERN, datePatternField.get(gsonBuilder));

    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    assertEquals(DateFormat.DEFAULT, dateStyleField.getInt(gsonBuilder));

    Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    assertEquals(DateFormat.DEFAULT, timeStyleField.getInt(gsonBuilder));

    Field complexMapKeySerializationField = GsonBuilder.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    assertEquals(Gson.DEFAULT_COMPLEX_MAP_KEYS, complexMapKeySerializationField.getBoolean(gsonBuilder));

    Field serializeSpecialFloatingPointValuesField = GsonBuilder.class.getDeclaredField("serializeSpecialFloatingPointValues");
    serializeSpecialFloatingPointValuesField.setAccessible(true);
    assertEquals(Gson.DEFAULT_SPECIALIZE_FLOAT_VALUES, serializeSpecialFloatingPointValuesField.getBoolean(gsonBuilder));

    Field escapeHtmlCharsField = GsonBuilder.class.getDeclaredField("escapeHtmlChars");
    escapeHtmlCharsField.setAccessible(true);
    assertEquals(Gson.DEFAULT_ESCAPE_HTML, escapeHtmlCharsField.getBoolean(gsonBuilder));

    Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    assertEquals(Gson.DEFAULT_PRETTY_PRINT, prettyPrintingField.getBoolean(gsonBuilder));

    Field generateNonExecutableJsonField = GsonBuilder.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    assertEquals(Gson.DEFAULT_JSON_NON_EXECUTABLE, generateNonExecutableJsonField.getBoolean(gsonBuilder));

    Field lenientField = GsonBuilder.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    assertEquals(Gson.DEFAULT_LENIENT, lenientField.getBoolean(gsonBuilder));

    Field useJdkUnsafeField = GsonBuilder.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    assertEquals(Gson.DEFAULT_USE_JDK_UNSAFE, useJdkUnsafeField.getBoolean(gsonBuilder));

    Field objectToNumberStrategyField = GsonBuilder.class.getDeclaredField("objectToNumberStrategy");
    objectToNumberStrategyField.setAccessible(true);
    assertSame(Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY, objectToNumberStrategyField.get(gsonBuilder));

    Field numberToNumberStrategyField = GsonBuilder.class.getDeclaredField("numberToNumberStrategy");
    numberToNumberStrategyField.setAccessible(true);
    assertSame(Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY, numberToNumberStrategyField.get(gsonBuilder));

    Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<?, ?> instanceCreators = (Map<?, ?>) instanceCreatorsField.get(gsonBuilder);
    assertNotNull(instanceCreators);
    assertTrue(instanceCreators.isEmpty());

    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<?> factories = (List<?>) factoriesField.get(gsonBuilder);
    assertNotNull(factories);
    assertTrue(factories.isEmpty());

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    List<?> hierarchyFactories = (List<?>) hierarchyFactoriesField.get(gsonBuilder);
    assertNotNull(hierarchyFactories);
    assertTrue(hierarchyFactories.isEmpty());

    Field reflectionFiltersField = GsonBuilder.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);
    List<?> reflectionFilters = (List<?>) reflectionFiltersField.get(gsonBuilder);
    assertNotNull(reflectionFilters);
    assertTrue(reflectionFilters.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testCreateReturnsNonNullGson() {
    Gson gson = gsonBuilder.create();
    assertNotNull(gson);
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDateViaReflection() throws Exception {
    // Prepare parameters for private method addTypeAdaptersForDate
    String pattern = "yyyy-MM-dd";
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.MEDIUM;

    // Access private method
    Method addTypeAdaptersForDateMethod = GsonBuilder.class.getDeclaredMethod(
        "addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    addTypeAdaptersForDateMethod.setAccessible(true);

    // Prepare factories list to pass in and verify modification
    List<TypeAdapterFactory> factories = new java.util.ArrayList<>();

    // Invoke private method
    addTypeAdaptersForDateMethod.invoke(gsonBuilder, pattern, dateStyle, timeStyle, factories);

    // Verify factories list has been added to
    assertFalse(factories.isEmpty());

    // Factories should contain some TypeAdapterFactory instances
    boolean containsDateTypeAdapter = factories.stream().anyMatch(f -> f != null);
    assertTrue(containsDateTypeAdapter);
  }
}