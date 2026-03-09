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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.List;
import java.util.LinkedList;

class GsonBuilder_2_2Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testGsonBuilder_DefaultConstructor_InitializesDefaults() throws Exception {
    // Use reflection to check private fields initialized to default values
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonBuilder);
    assertSame(Excluder.DEFAULT, excluder);

    Field longSerializationPolicyField = GsonBuilder.class.getDeclaredField("longSerializationPolicy");
    longSerializationPolicyField.setAccessible(true);
    LongSerializationPolicy longSerializationPolicy = (LongSerializationPolicy) longSerializationPolicyField.get(gsonBuilder);
    assertEquals(LongSerializationPolicy.DEFAULT, longSerializationPolicy);

    Field fieldNamingPolicyField = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    fieldNamingPolicyField.setAccessible(true);
    FieldNamingStrategy fieldNamingStrategy = (FieldNamingStrategy) fieldNamingPolicyField.get(gsonBuilder);
    assertEquals(FieldNamingPolicy.IDENTITY, fieldNamingStrategy);

    Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNulls = serializeNullsField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_SERIALIZE_NULLS, serializeNulls);

    Field complexMapKeySerializationField = GsonBuilder.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    boolean complexMapKeySerialization = complexMapKeySerializationField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_COMPLEX_MAP_KEYS, complexMapKeySerialization);

    Field generateNonExecutableJsonField = GsonBuilder.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    boolean generateNonExecutableJson = generateNonExecutableJsonField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_JSON_NON_EXECUTABLE, generateNonExecutableJson);

    Field escapeHtmlCharsField = GsonBuilder.class.getDeclaredField("escapeHtmlChars");
    escapeHtmlCharsField.setAccessible(true);
    boolean escapeHtmlChars = escapeHtmlCharsField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_ESCAPE_HTML, escapeHtmlChars);

    Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    boolean prettyPrinting = prettyPrintingField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_PRETTY_PRINT, prettyPrinting);

    Field lenientField = GsonBuilder.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenient = lenientField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_LENIENT, lenient);

    Field serializeSpecialFloatingPointValuesField = GsonBuilder.class.getDeclaredField("serializeSpecialFloatingPointValues");
    serializeSpecialFloatingPointValuesField.setAccessible(true);
    boolean serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValuesField.getBoolean(gsonBuilder);
    assertEquals(Gson.DEFAULT_SPECIALIZE_FLOAT_VALUES, serializeSpecialFloatingPointValues);

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
    LinkedList<?> reflectionFilters = (LinkedList<?>) reflectionFiltersField.get(gsonBuilder);
    assertNotNull(reflectionFilters);
    assertTrue(reflectionFilters.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testGsonBuilder_CopyConstructor_CopiesFields() throws Exception {
    Gson mockGson = mock(Gson.class, RETURNS_DEEP_STUBS);
    Excluder excluder = Excluder.DEFAULT;
    FieldNamingStrategy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
    ToNumberStrategy objectToNumberStrategy = Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY;
    ToNumberStrategy numberToNumberStrategy = Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY;

    when(mockGson.excluder).thenReturn(excluder);
    when(mockGson.fieldNamingStrategy).thenReturn(fieldNamingStrategy);
    when(mockGson.serializeNulls).thenReturn(true);
    when(mockGson.complexMapKeySerialization).thenReturn(true);
    when(mockGson.generateNonExecutableJson).thenReturn(true);
    when(mockGson.htmlSafe).thenReturn(true);
    when(mockGson.prettyPrinting).thenReturn(true);
    when(mockGson.lenient).thenReturn(true);
    when(mockGson.serializeSpecialFloatingPointValues).thenReturn(true);
    when(mockGson.longSerializationPolicy).thenReturn(LongSerializationPolicy.DEFAULT);
    when(mockGson.datePattern).thenReturn("yyyy-MM-dd");
    when(mockGson.dateStyle).thenReturn(DateFormat.SHORT);
    when(mockGson.timeStyle).thenReturn(DateFormat.MEDIUM);
    when(mockGson.useJdkUnsafe).thenReturn(false);
    when(mockGson.objectToNumberStrategy).thenReturn(objectToNumberStrategy);
    when(mockGson.numberToNumberStrategy).thenReturn(numberToNumberStrategy);

    when(mockGson.instanceCreators).thenReturn(new HashMap<>());
    when(mockGson.builderFactories).thenReturn(Collections.emptyList());
    when(mockGson.builderHierarchyFactories).thenReturn(Collections.emptyList());
    when(mockGson.reflectionFilters).thenReturn(new LinkedList<>());

    GsonBuilder copyBuilder = new GsonBuilder(mockGson);

    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder copiedExcluder = (Excluder) excluderField.get(copyBuilder);
    assertSame(excluder, copiedExcluder);

    Field fieldNamingPolicyField = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    fieldNamingPolicyField.setAccessible(true);
    FieldNamingStrategy copiedFieldNamingPolicy = (FieldNamingStrategy) fieldNamingPolicyField.get(copyBuilder);
    assertSame(fieldNamingStrategy, copiedFieldNamingPolicy);

    Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    assertTrue(serializeNullsField.getBoolean(copyBuilder));

    Field complexMapKeySerializationField = GsonBuilder.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    assertTrue(complexMapKeySerializationField.getBoolean(copyBuilder));

    Field generateNonExecutableJsonField = GsonBuilder.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    assertTrue(generateNonExecutableJsonField.getBoolean(copyBuilder));

    Field escapeHtmlCharsField = GsonBuilder.class.getDeclaredField("escapeHtmlChars");
    escapeHtmlCharsField.setAccessible(true);
    assertTrue(escapeHtmlCharsField.getBoolean(copyBuilder));

    Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    assertTrue(prettyPrintingField.getBoolean(copyBuilder));

    Field lenientField = GsonBuilder.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    assertTrue(lenientField.getBoolean(copyBuilder));

    Field serializeSpecialFloatingPointValuesField = GsonBuilder.class.getDeclaredField("serializeSpecialFloatingPointValues");
    serializeSpecialFloatingPointValuesField.setAccessible(true);
    assertTrue(serializeSpecialFloatingPointValuesField.getBoolean(copyBuilder));

    Field longSerializationPolicyField = GsonBuilder.class.getDeclaredField("longSerializationPolicy");
    longSerializationPolicyField.setAccessible(true);
    assertEquals(LongSerializationPolicy.DEFAULT, longSerializationPolicyField.get(copyBuilder));

    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    assertEquals("yyyy-MM-dd", datePatternField.get(copyBuilder));

    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    assertEquals(DateFormat.SHORT, dateStyleField.getInt(copyBuilder));

    Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    assertEquals(DateFormat.MEDIUM, timeStyleField.getInt(copyBuilder));

    Field useJdkUnsafeField = GsonBuilder.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    assertFalse(useJdkUnsafeField.getBoolean(copyBuilder));

    Field objectToNumberStrategyField = GsonBuilder.class.getDeclaredField("objectToNumberStrategy");
    objectToNumberStrategyField.setAccessible(true);
    assertSame(objectToNumberStrategy, objectToNumberStrategyField.get(copyBuilder));

    Field numberToNumberStrategyField = GsonBuilder.class.getDeclaredField("numberToNumberStrategy");
    numberToNumberStrategyField.setAccessible(true);
    assertSame(numberToNumberStrategy, numberToNumberStrategyField.get(copyBuilder));

    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<?> factories = (List<?>) factoriesField.get(copyBuilder);
    assertNotNull(factories);
    assertTrue(factories.isEmpty());

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    List<?> hierarchyFactories = (List<?>) hierarchyFactoriesField.get(copyBuilder);
    assertNotNull(hierarchyFactories);
    assertTrue(hierarchyFactories.isEmpty());

    Field reflectionFiltersField = GsonBuilder.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);
    LinkedList<?> reflectionFilters = (LinkedList<?>) reflectionFiltersField.get(copyBuilder);
    assertNotNull(reflectionFilters);
    assertTrue(reflectionFilters.isEmpty());
  }
}