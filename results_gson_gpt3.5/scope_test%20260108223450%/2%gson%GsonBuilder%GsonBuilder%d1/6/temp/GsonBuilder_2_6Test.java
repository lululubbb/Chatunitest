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
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class GsonBuilder_2_6Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testDefaultConstructor_initializesDefaults() throws Exception {
    // Use reflection to verify private fields are initialized with defaults
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonBuilder);
    assertNotNull(excluder);
    assertEquals(Excluder.DEFAULT, excluder);

    Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNulls = (boolean) serializeNullsField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_SERIALIZE_NULLS, serializeNulls);

    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    String datePattern = (String) datePatternField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_DATE_PATTERN, datePattern);

    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    int dateStyle = (int) dateStyleField.get(gsonBuilder);
    assertEquals(DateFormat.DEFAULT, dateStyle);

    Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    int timeStyle = (int) timeStyleField.get(gsonBuilder);
    assertEquals(DateFormat.DEFAULT, timeStyle);

    Field complexMapKeySerializationField = GsonBuilder.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    boolean complexMapKeySerialization = (boolean) complexMapKeySerializationField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_COMPLEX_MAP_KEYS, complexMapKeySerialization);

    Field escapeHtmlCharsField = GsonBuilder.class.getDeclaredField("escapeHtmlChars");
    escapeHtmlCharsField.setAccessible(true);
    boolean escapeHtmlChars = (boolean) escapeHtmlCharsField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_ESCAPE_HTML, escapeHtmlChars);

    Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    boolean prettyPrinting = (boolean) prettyPrintingField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_PRETTY_PRINT, prettyPrinting);

    Field lenientField = GsonBuilder.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenient = (boolean) lenientField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_LENIENT, lenient);
  }

  @Test
    @Timeout(8000)
  void testConstructorWithGsonCopiesFields() throws Exception {
    Gson mockGson = mock(Gson.class);

    Excluder excluder = Excluder.DEFAULT;
    FieldNamingStrategy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();
    boolean serializeNulls = true;
    boolean complexMapKeySerialization = true;
    boolean generateNonExecutableJson = true;
    boolean escapeHtmlChars = true;
    boolean prettyPrinting = true;
    boolean lenient = true;
    boolean serializeSpecialFloatingPointValues = true;
    LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;
    String datePattern = "yyyy-MM-dd";
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.SHORT;
    List<TypeAdapterFactory> builderFactories = List.of(TypeAdapters.STRING_FACTORY);
    List<TypeAdapterFactory> builderHierarchyFactories = List.of(TypeAdapters.STRING_FACTORY);
    boolean useJdkUnsafe = true;
    ToNumberStrategy objectToNumberStrategy = ToNumberStrategy.DEFAULT;
    ToNumberStrategy numberToNumberStrategy = ToNumberStrategy.DEFAULT;
    LinkedList<ReflectionAccessFilter> reflectionFilters = new LinkedList<>();

    when(mockGson.excluder).thenReturn(excluder);
    when(mockGson.fieldNamingStrategy).thenReturn(fieldNamingStrategy);
    when(mockGson.instanceCreators).thenReturn(instanceCreators);
    when(mockGson.serializeNulls).thenReturn(serializeNulls);
    when(mockGson.complexMapKeySerialization).thenReturn(complexMapKeySerialization);
    when(mockGson.generateNonExecutableJson).thenReturn(generateNonExecutableJson);
    when(mockGson.htmlSafe).thenReturn(escapeHtmlChars);
    when(mockGson.prettyPrinting).thenReturn(prettyPrinting);
    when(mockGson.lenient).thenReturn(lenient);
    when(mockGson.serializeSpecialFloatingPointValues).thenReturn(serializeSpecialFloatingPointValues);
    when(mockGson.longSerializationPolicy).thenReturn(longSerializationPolicy);
    when(mockGson.datePattern).thenReturn(datePattern);
    when(mockGson.dateStyle).thenReturn(dateStyle);
    when(mockGson.timeStyle).thenReturn(timeStyle);
    when(mockGson.builderFactories).thenReturn(builderFactories);
    when(mockGson.builderHierarchyFactories).thenReturn(builderHierarchyFactories);
    when(mockGson.useJdkUnsafe).thenReturn(useJdkUnsafe);
    when(mockGson.objectToNumberStrategy).thenReturn(objectToNumberStrategy);
    when(mockGson.numberToNumberStrategy).thenReturn(numberToNumberStrategy);
    when(mockGson.reflectionFilters).thenReturn(reflectionFilters);

    GsonBuilder copyBuilder = new GsonBuilder(mockGson);

    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    assertSame(excluder, excluderField.get(copyBuilder));

    Field fieldNamingPolicyField = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    fieldNamingPolicyField.setAccessible(true);
    assertSame(fieldNamingStrategy, fieldNamingPolicyField.get(copyBuilder));

    Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<?, ?> instanceCreatorsMap = (Map<?, ?>) instanceCreatorsField.get(copyBuilder);
    assertSame(instanceCreators, instanceCreatorsMap);

    Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    assertEquals(serializeNulls, serializeNullsField.get(copyBuilder));

    Field complexMapKeySerializationField = GsonBuilder.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    assertEquals(complexMapKeySerialization, complexMapKeySerializationField.get(copyBuilder));

    Field generateNonExecutableJsonField = GsonBuilder.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    assertEquals(generateNonExecutableJson, generateNonExecutableJsonField.get(copyBuilder));

    Field escapeHtmlCharsField = GsonBuilder.class.getDeclaredField("escapeHtmlChars");
    escapeHtmlCharsField.setAccessible(true);
    assertEquals(escapeHtmlChars, escapeHtmlCharsField.get(copyBuilder));

    Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    assertEquals(prettyPrinting, prettyPrintingField.get(copyBuilder));

    Field lenientField = GsonBuilder.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    assertEquals(lenient, lenientField.get(copyBuilder));

    Field serializeSpecialFloatingPointValuesField = GsonBuilder.class.getDeclaredField("serializeSpecialFloatingPointValues");
    serializeSpecialFloatingPointValuesField.setAccessible(true);
    assertEquals(serializeSpecialFloatingPointValues, serializeSpecialFloatingPointValuesField.get(copyBuilder));

    Field longSerializationPolicyField = GsonBuilder.class.getDeclaredField("longSerializationPolicy");
    longSerializationPolicyField.setAccessible(true);
    assertSame(longSerializationPolicy, longSerializationPolicyField.get(copyBuilder));

    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    assertEquals(datePattern, datePatternField.get(copyBuilder));

    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    assertEquals(dateStyle, dateStyleField.get(copyBuilder));

    Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    assertEquals(timeStyle, timeStyleField.get(copyBuilder));

    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<?> factories = (List<?>) factoriesField.get(copyBuilder);
    assertTrue(factories.containsAll(builderFactories));

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    List<?> hierarchyFactories = (List<?>) hierarchyFactoriesField.get(copyBuilder);
    assertTrue(hierarchyFactories.containsAll(builderHierarchyFactories));

    Field useJdkUnsafeField = GsonBuilder.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    assertEquals(useJdkUnsafe, useJdkUnsafeField.get(copyBuilder));

    Field objectToNumberStrategyField = GsonBuilder.class.getDeclaredField("objectToNumberStrategy");
    objectToNumberStrategyField.setAccessible(true);
    assertSame(objectToNumberStrategy, objectToNumberStrategyField.get(copyBuilder));

    Field numberToNumberStrategyField = GsonBuilder.class.getDeclaredField("numberToNumberStrategy");
    numberToNumberStrategyField.setAccessible(true);
    assertSame(numberToNumberStrategy, numberToNumberStrategyField.get(copyBuilder));

    Field reflectionFiltersField = GsonBuilder.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);
    LinkedList<?> reflectionFiltersList = (LinkedList<?>) reflectionFiltersField.get(copyBuilder);
    assertSame(reflectionFilters, reflectionFiltersList);
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_invokesWithoutException() throws Exception {
    // Prepare parameters
    String pattern = "yyyy-MM-dd'T'HH:mm:ss";
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.MEDIUM;
    List<TypeAdapterFactory> factories = new java.util.ArrayList<>();

    // Use reflection to invoke private method addTypeAdaptersForDate
    Method method = GsonBuilder.class.getDeclaredMethod(
        "addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);

    method.invoke(gsonBuilder, pattern, dateStyle, timeStyle, factories);

    // After invocation, factories should contain some date type adapters
    assertFalse(factories.isEmpty());
  }
}