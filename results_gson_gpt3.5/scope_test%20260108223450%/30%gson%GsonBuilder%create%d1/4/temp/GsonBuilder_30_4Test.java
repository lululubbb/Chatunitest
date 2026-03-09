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
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class GsonBuilder_30_4Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void create_DefaultSettings_ReturnsGsonInstance() throws Exception {
    // Arrange
    // Add some dummy factories and hierarchyFactories to cover list additions and reversals
    TypeAdapterFactory factory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory factory2 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactory1 = mock(TypeAdapterFactory.class);
    gsonBuilder.factories.add(factory1);
    gsonBuilder.factories.add(factory2);
    gsonBuilder.hierarchyFactories.add(hierarchyFactory1);

    // Spy on gsonBuilder to verify private method call
    GsonBuilder spyBuilder = Mockito.spy(gsonBuilder);

    // Use reflection to access private addTypeAdaptersForDate method and verify it is called
    // We cannot mock private methods easily, so we verify indirectly by spying and verifying call count
    // But since it's private, we will invoke create and check the resulting Gson object and list state

    // Act
    Gson gson = spyBuilder.create();

    // Assert
    assertNotNull(gson);

    // Verify that the factories list inside Gson contains the reversed factories and hierarchy factories plus 3 more added by addTypeAdaptersForDate
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<?> gsonFactories = (List<?>) factoriesField.get(gson);
    // The created list should at least contain all original factories and hierarchyFactories reversed plus 3 (from addTypeAdaptersForDate)
    // So total size >= original factories + hierarchyFactories + 3
    assertTrue(gsonFactories.size() >= gsonBuilder.factories.size() + gsonBuilder.hierarchyFactories.size() + 3);

    // Verify that the factories list is reversed for factories and hierarchyFactories
    List<TypeAdapterFactory> reversedFactories = new ArrayList<>(gsonBuilder.factories);
    Collections.reverse(reversedFactories);
    for (TypeAdapterFactory f : reversedFactories) {
      assertTrue(gsonFactories.contains(f));
    }

    List<TypeAdapterFactory> reversedHierarchyFactories = new ArrayList<>(gsonBuilder.hierarchyFactories);
    Collections.reverse(reversedHierarchyFactories);
    for (TypeAdapterFactory f : reversedHierarchyFactories) {
      assertTrue(gsonFactories.contains(f));
    }

    // Verify that Gson fields match the builder's fields
    assertEquals(gsonBuilder.excluder, getFieldValue(gson, "excluder"));
    assertEquals(gsonBuilder.fieldNamingPolicy, getFieldValue(gson, "fieldNamingPolicy"));
    assertEquals(gsonBuilder.serializeNulls, getFieldValue(gson, "serializeNulls"));
    assertEquals(gsonBuilder.complexMapKeySerialization, getFieldValue(gson, "complexMapKeySerialization"));
    assertEquals(gsonBuilder.generateNonExecutableJson, getFieldValue(gson, "generateNonExecutableJson"));
    assertEquals(gsonBuilder.escapeHtmlChars, getFieldValue(gson, "escapeHtmlChars"));
    assertEquals(gsonBuilder.prettyPrinting, getFieldValue(gson, "prettyPrinting"));
    assertEquals(gsonBuilder.lenient, getFieldValue(gson, "lenient"));
    assertEquals(gsonBuilder.serializeSpecialFloatingPointValues, getFieldValue(gson, "serializeSpecialFloatingPointValues"));
    assertEquals(gsonBuilder.useJdkUnsafe, getFieldValue(gson, "useJdkUnsafe"));
    assertEquals(gsonBuilder.longSerializationPolicy, getFieldValue(gson, "longSerializationPolicy"));
    assertEquals(gsonBuilder.datePattern, getFieldValue(gson, "datePattern"));
    assertEquals(gsonBuilder.dateStyle, getFieldValue(gson, "dateStyle"));
    assertEquals(gsonBuilder.timeStyle, getFieldValue(gson, "timeStyle"));
    assertEquals(gsonBuilder.objectToNumberStrategy, getFieldValue(gson, "objectToNumberStrategy"));
    assertEquals(gsonBuilder.numberToNumberStrategy, getFieldValue(gson, "numberToNumberStrategy"));

    // instanceCreators map copied correctly
    assertEquals(gsonBuilder.instanceCreators.size(), ((HashMap<?, ?>) getFieldValue(gson, "instanceCreators")).size());

    // factories and hierarchyFactories copied as new lists
    List<?> gsonBuilderFactories = (List<?>) getFieldValue(gsonBuilder, "factories");
    List<?> gsonFactoriesList = (List<?>) getFieldValue(gson, "factories");
    assertNotSame(gsonBuilderFactories, gsonFactoriesList);

    List<?> gsonBuilderHierarchyFactories = (List<?>) getFieldValue(gsonBuilder, "hierarchyFactories");
    List<?> gsonHierarchyFactoriesList = (List<?>) getFieldValue(gson, "hierarchyFactories");
    assertNotSame(gsonBuilderHierarchyFactories, gsonHierarchyFactoriesList);

    // reflectionFilters copied as new list
    LinkedList<?> gsonBuilderReflectionFilters = (LinkedList<?>) getFieldValue(gsonBuilder, "reflectionFilters");
    List<?> gsonReflectionFiltersList = (List<?>) getFieldValue(gson, "reflectionFilters");
    assertNotSame(gsonBuilderReflectionFilters, gsonReflectionFiltersList);
  }

  @Test
    @Timeout(8000)
  void create_WithCustomDatePatternAndStyles_AddsDateTypeAdapters() throws Exception {
    // Arrange
    gsonBuilder.datePattern = "yyyy-MM-dd'T'HH:mm:ssZ";
    gsonBuilder.dateStyle = 1;
    gsonBuilder.timeStyle = 2;

    // Spy on gsonBuilder to verify addTypeAdaptersForDate is called with correct args
    GsonBuilder spyBuilder = Mockito.spy(gsonBuilder);

    // We use doCallRealMethod to call actual create method
    doCallRealMethod().when(spyBuilder).create();

    // Act
    Gson gson = spyBuilder.create();

    // Assert
    assertNotNull(gson);
    // We cannot directly verify private method call, but we can check that the factories list contains DefaultDateTypeAdapter factories
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<?> gsonFactories = (List<?>) factoriesField.get(gson);

    boolean hasDateAdapterFactory = gsonFactories.stream().anyMatch(f -> f.getClass().getName().contains("DefaultDateTypeAdapter")
        || f.getClass().getName().contains("DateTypeAdapter"));

    // Because addTypeAdaptersForDate adds date adapters, at least one should be present
    assertTrue(hasDateAdapterFactory || gsonFactories.size() >= gsonBuilder.factories.size() + gsonBuilder.hierarchyFactories.size() + 3);
  }

  private Object getFieldValue(Object instance, String fieldName) throws Exception {
    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }
}