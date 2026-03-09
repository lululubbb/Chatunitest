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
import com.google.gson.internal.bind.TypeAdapters;
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
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.LinkedList;
import java.util.List;

public class GsonBuilder_2_3Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  public void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  public void testGsonBuilder_DefaultConstructor_InitializesDefaults() throws Exception {
    // Using reflection to verify private fields are initialized to expected defaults

    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonBuilder);
    assertNotNull(excluder);
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
    LinkedList<?> reflectionFilters = (LinkedList<?>) reflectionFiltersField.get(gsonBuilder);
    assertNotNull(reflectionFilters);
    assertTrue(reflectionFilters.isEmpty());

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

    Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    assertNotNull(instanceCreatorsField.get(gsonBuilder));
  }

  @Test
    @Timeout(8000)
  public void testGsonBuilder_CopyConstructor_CopiesFields() throws Exception {
    // Prepare a mock Gson with specific fields set
    Gson mockGson = mock(Gson.class);

    Excluder excluder = Excluder.DEFAULT;
    when(mockGson.excluder).thenReturn(excluder);

    FieldNamingStrategy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
    when(mockGson.fieldNamingStrategy).thenReturn(fieldNamingStrategy);

    // instanceCreators map
    when(mockGson.instanceCreators).thenReturn(new java.util.HashMap<>());

    when(mockGson.serializeNulls).thenReturn(true);
    when(mockGson.complexMapKeySerialization).thenReturn(true);
    when(mockGson.generateNonExecutableJson).thenReturn(true);
    when(mockGson.htmlSafe).thenReturn(true);
    when(mockGson.prettyPrinting).thenReturn(true);
    when(mockGson.lenient).thenReturn(true);
    when(mockGson.serializeSpecialFloatingPointValues).thenReturn(true);
    when(mockGson.longSerializationPolicy).thenReturn(LongSerializationPolicy.DEFAULT);
    when(mockGson.datePattern).thenReturn("pattern");
    when(mockGson.dateStyle).thenReturn(1);
    when(mockGson.timeStyle).thenReturn(2);
    when(mockGson.builderFactories).thenReturn(new java.util.ArrayList<>());
    when(mockGson.builderHierarchyFactories).thenReturn(new java.util.ArrayList<>());
    when(mockGson.useJdkUnsafe).thenReturn(true);
    when(mockGson.objectToNumberStrategy).thenReturn(Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY);
    when(mockGson.numberToNumberStrategy).thenReturn(Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY);
    when(mockGson.reflectionFilters).thenReturn(new LinkedList<>());

    // Use real GsonBuilder(Gson) constructor
    GsonBuilder copyBuilder = new GsonBuilder(mockGson);

    // Verify fields copied correctly using reflection
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    assertSame(excluder, excluderField.get(copyBuilder));

    Field fieldNamingPolicyField = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    fieldNamingPolicyField.setAccessible(true);
    assertSame(fieldNamingStrategy, fieldNamingPolicyField.get(copyBuilder));

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
    assertEquals("pattern", datePatternField.get(copyBuilder));

    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    assertEquals(1, dateStyleField.getInt(copyBuilder));

    Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    assertEquals(2, timeStyleField.getInt(copyBuilder));

    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    assertNotNull(factoriesField.get(copyBuilder));
    assertTrue(((java.util.List<?>) factoriesField.get(copyBuilder)).isEmpty());

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    assertNotNull(hierarchyFactoriesField.get(copyBuilder));
    assertTrue(((java.util.List<?>) hierarchyFactoriesField.get(copyBuilder)).isEmpty());

    Field useJdkUnsafeField = GsonBuilder.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    assertTrue(useJdkUnsafeField.getBoolean(copyBuilder));

    Field objectToNumberStrategyField = GsonBuilder.class.getDeclaredField("objectToNumberStrategy");
    objectToNumberStrategyField.setAccessible(true);
    assertEquals(Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY, objectToNumberStrategyField.get(copyBuilder));

    Field numberToNumberStrategyField = GsonBuilder.class.getDeclaredField("numberToNumberStrategy");
    numberToNumberStrategyField.setAccessible(true);
    assertEquals(Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY, numberToNumberStrategyField.get(copyBuilder));

    Field reflectionFiltersField = GsonBuilder.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);
    assertNotNull(reflectionFiltersField.get(copyBuilder));
    assertTrue(((LinkedList<?>) reflectionFiltersField.get(copyBuilder)).isEmpty());
  }

  @Test
    @Timeout(8000)
  public void testAddTypeAdaptersForDate_PrivateMethod() throws Exception {
    // Arrange
    Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);

    List<TypeAdapterFactory> factories = new java.util.ArrayList<>();

    // Act
    method.invoke(gsonBuilder, "yyyy-MM-dd", DateFormat.SHORT, DateFormat.LONG, factories);

    // Assert
    assertFalse(factories.isEmpty());
  }
}