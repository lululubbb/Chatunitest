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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.LinkedList;

class GsonBuilder_2_1Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testDefaultConstructor_initialState() throws Exception {
    // Validate default fields set by constructor
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonBuilder);
    assertEquals(Excluder.DEFAULT, excluder);

    Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNulls = (boolean) serializeNullsField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_SERIALIZE_NULLS, serializeNulls);

    Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    boolean prettyPrinting = (boolean) prettyPrintingField.get(gsonBuilder);
    assertEquals(Gson.DEFAULT_PRETTY_PRINT, prettyPrinting);

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
    var instanceCreators = (java.util.Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonBuilder);
    assertNotNull(instanceCreators);
    assertTrue(instanceCreators.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testCopyConstructor_copiesAllFields() throws Exception {
    // Prepare a Gson mock with custom fields
    Gson gsonMock = mock(Gson.class, RETURNS_DEEP_STUBS);

    Excluder excluder = Excluder.DEFAULT;
    when(gsonMock.excluder).thenReturn(excluder);
    when(gsonMock.fieldNamingStrategy).thenReturn(FieldNamingPolicy.IDENTITY);
    when(gsonMock.serializeNulls).thenReturn(true);
    when(gsonMock.complexMapKeySerialization).thenReturn(true);
    when(gsonMock.generateNonExecutableJson).thenReturn(true);
    when(gsonMock.htmlSafe).thenReturn(true);
    when(gsonMock.prettyPrinting).thenReturn(true);
    when(gsonMock.lenient).thenReturn(true);
    when(gsonMock.serializeSpecialFloatingPointValues).thenReturn(true);
    when(gsonMock.longSerializationPolicy).thenReturn(LongSerializationPolicy.DEFAULT);
    when(gsonMock.datePattern).thenReturn("yyyy-MM-dd");
    when(gsonMock.dateStyle).thenReturn(DateFormat.SHORT);
    when(gsonMock.timeStyle).thenReturn(DateFormat.LONG);
    when(gsonMock.builderFactories).thenReturn(List.of(TypeAdapters.STRING_FACTORY));
    when(gsonMock.builderHierarchyFactories).thenReturn(List.of(TypeAdapters.STRING_FACTORY));
    when(gsonMock.useJdkUnsafe).thenReturn(true);
    when(gsonMock.objectToNumberStrategy).thenReturn(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    when(gsonMock.numberToNumberStrategy).thenReturn(ToNumberPolicy.LAZILY_PARSED_NUMBER);
    when(gsonMock.reflectionFilters).thenReturn(new LinkedList<>());

    // Use reflection to set instanceCreators map in gsonMock
    Field instanceCreatorsField = Gson.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    java.util.Map<Type, InstanceCreator<?>> instanceCreatorsMap = new java.util.HashMap<>();
    instanceCreatorsMap.put(String.class, (InstanceCreator<String>) type -> "test");
    instanceCreatorsField.set(gsonMock, instanceCreatorsMap);

    // Create GsonBuilder copy
    GsonBuilder copy = new GsonBuilder(gsonMock);

    // Validate fields copied
    Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    assertSame(excluder, excluderField.get(copy));

    Field fieldNamingPolicyField = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
    fieldNamingPolicyField.setAccessible(true);
    assertEquals(FieldNamingPolicy.IDENTITY, fieldNamingPolicyField.get(copy));

    Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    assertTrue((Boolean) serializeNullsField.get(copy));

    Field complexMapKeySerializationField = GsonBuilder.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    assertTrue((Boolean) complexMapKeySerializationField.get(copy));

    Field generateNonExecutableJsonField = GsonBuilder.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    assertTrue((Boolean) generateNonExecutableJsonField.get(copy));

    Field escapeHtmlCharsField = GsonBuilder.class.getDeclaredField("escapeHtmlChars");
    escapeHtmlCharsField.setAccessible(true);
    assertTrue((Boolean) escapeHtmlCharsField.get(copy));

    Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    assertTrue((Boolean) prettyPrintingField.get(copy));

    Field lenientField = GsonBuilder.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    assertTrue((Boolean) lenientField.get(copy));

    Field serializeSpecialFloatingPointValuesField = GsonBuilder.class.getDeclaredField("serializeSpecialFloatingPointValues");
    serializeSpecialFloatingPointValuesField.setAccessible(true);
    assertTrue((Boolean) serializeSpecialFloatingPointValuesField.get(copy));

    Field longSerializationPolicyField = GsonBuilder.class.getDeclaredField("longSerializationPolicy");
    longSerializationPolicyField.setAccessible(true);
    assertEquals(LongSerializationPolicy.DEFAULT, longSerializationPolicyField.get(copy));

    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    assertEquals("yyyy-MM-dd", datePatternField.get(copy));

    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    assertEquals(DateFormat.SHORT, dateStyleField.get(copy));

    Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    assertEquals(DateFormat.LONG, timeStyleField.get(copy));

    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<?> factories = (List<?>) factoriesField.get(copy);
    assertEquals(1, factories.size());

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    List<?> hierarchyFactories = (List<?>) hierarchyFactoriesField.get(copy);
    assertEquals(1, hierarchyFactories.size());

    Field useJdkUnsafeField = GsonBuilder.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    assertTrue((Boolean) useJdkUnsafeField.get(copy));

    Field objectToNumberStrategyField = GsonBuilder.class.getDeclaredField("objectToNumberStrategy");
    objectToNumberStrategyField.setAccessible(true);
    assertEquals(ToNumberPolicy.LAZILY_PARSED_NUMBER, objectToNumberStrategyField.get(copy));

    Field numberToNumberStrategyField = GsonBuilder.class.getDeclaredField("numberToNumberStrategy");
    numberToNumberStrategyField.setAccessible(true);
    assertEquals(ToNumberPolicy.LAZILY_PARSED_NUMBER, numberToNumberStrategyField.get(copy));

    Field reflectionFiltersField = GsonBuilder.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);
    LinkedList<?> reflectionFilters = (LinkedList<?>) reflectionFiltersField.get(copy);
    assertNotNull(reflectionFilters);
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_invokesFactories() throws Exception {
    // Prepare factories list
    List<TypeAdapterFactory> factories = new java.util.ArrayList<>();

    // Access private method addTypeAdaptersForDate
    Method addTypeAdaptersForDateMethod = GsonBuilder.class.getDeclaredMethod(
        "addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    addTypeAdaptersForDateMethod.setAccessible(true);

    // Invoke with null pattern and default styles
    addTypeAdaptersForDateMethod.invoke(gsonBuilder, null, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    // Expect factories list to be non-empty with date adapters
    assertFalse(factories.isEmpty());

    boolean hasDateAdapter = factories.stream().anyMatch(factory -> {
      try {
        Method createMethod = factory.getClass().getMethod("create", Gson.class, TypeToken.class);
        return createMethod != null;
      } catch (NoSuchMethodException e) {
        return false;
      }
    });
    assertTrue(hasDateAdapter);
  }
}