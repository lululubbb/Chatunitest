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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class GsonBuilder_1_6Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testConstructor_DefaultValues() throws Exception {
    // Verify default private fields are set correctly
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonBuilder);
    assertNotNull(excluder);
    assertEquals(Excluder.DEFAULT, excluder);

    Field longPolicyField = GsonBuilder.class.getDeclaredField("longSerializationPolicy");
    longPolicyField.setAccessible(true);
    LongSerializationPolicy longPolicy = (LongSerializationPolicy) longPolicyField.get(gsonBuilder);
    assertEquals(LongSerializationPolicy.DEFAULT, longPolicy);

    Field fieldNamingPolicyField = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    fieldNamingPolicyField.setAccessible(true);
    FieldNamingStrategy fieldNamingStrategy = (FieldNamingStrategy) fieldNamingPolicyField.get(gsonBuilder);
    assertEquals(FieldNamingPolicy.IDENTITY, fieldNamingStrategy);

    Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    @SuppressWarnings("unchecked")
    var instanceCreators = (java.util.Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonBuilder);
    assertNotNull(instanceCreators);
    assertTrue(instanceCreators.isEmpty());

    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);
    assertNotNull(factories);
    assertTrue(factories.isEmpty());

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<TypeAdapterFactory> hierarchyFactories = (List<TypeAdapterFactory>) hierarchyFactoriesField.get(gsonBuilder);
    assertNotNull(hierarchyFactories);
    assertTrue(hierarchyFactories.isEmpty());

    Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNulls = serializeNullsField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_SERIALIZE_NULLS, serializeNulls);

    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    String datePattern = (String) datePatternField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_DATE_PATTERN, datePattern);

    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    int dateStyle = dateStyleField.getInt(gsonBuilder);
    assertEquals(DateFormat.DEFAULT, dateStyle);

    Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    int timeStyle = timeStyleField.getInt(gsonBuilder);
    assertEquals(DateFormat.DEFAULT, timeStyle);

    Field complexMapKeySerializationField = GsonBuilder.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    boolean complexMapKeySerialization = complexMapKeySerializationField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_COMPLEX_MAP_KEYS, complexMapKeySerialization);

    Field serializeSpecialFloatingPointValuesField = GsonBuilder.class.getDeclaredField("serializeSpecialFloatingPointValues");
    serializeSpecialFloatingPointValuesField.setAccessible(true);
    boolean serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValuesField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_SPECIALIZE_FLOAT_VALUES, serializeSpecialFloatingPointValues);

    Field escapeHtmlCharsField = GsonBuilder.class.getDeclaredField("escapeHtmlChars");
    escapeHtmlCharsField.setAccessible(true);
    boolean escapeHtmlChars = escapeHtmlCharsField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_ESCAPE_HTML, escapeHtmlChars);

    Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    boolean prettyPrinting = prettyPrintingField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_PRETTY_PRINT, prettyPrinting);

    Field generateNonExecutableJsonField = GsonBuilder.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    boolean generateNonExecutableJson = generateNonExecutableJsonField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_JSON_NON_EXECUTABLE, generateNonExecutableJson);

    Field lenientField = GsonBuilder.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenient = lenientField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_LENIENT, lenient);

    Field useJdkUnsafeField = GsonBuilder.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    boolean useJdkUnsafe = useJdkUnsafeField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_USE_JDK_UNSAFE, useJdkUnsafe);

    Field objectToNumberStrategyField = GsonBuilder.class.getDeclaredField("objectToNumberStrategy");
    objectToNumberStrategyField.setAccessible(true);
    ToNumberStrategy objectToNumberStrategy = (ToNumberStrategy) objectToNumberStrategyField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY, objectToNumberStrategy);

    Field numberToNumberStrategyField = GsonBuilder.class.getDeclaredField("numberToNumberStrategy");
    numberToNumberStrategyField.setAccessible(true);
    ToNumberStrategy numberToNumberStrategy = (ToNumberStrategy) numberToNumberStrategyField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY, numberToNumberStrategy);

    Field reflectionFiltersField = GsonBuilder.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);
    @SuppressWarnings("unchecked")
    LinkedList<ReflectionAccessFilter> reflectionFilters = (LinkedList<ReflectionAccessFilter>) reflectionFiltersField.get(gsonBuilder);
    assertNotNull(reflectionFilters);
    assertTrue(reflectionFilters.isEmpty());
  }
}