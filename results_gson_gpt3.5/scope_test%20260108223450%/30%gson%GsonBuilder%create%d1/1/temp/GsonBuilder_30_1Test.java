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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class GsonBuilder_30_1Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void create_DefaultState_ReturnsGsonWithExpectedProperties() throws Exception {
    // Prepare spies and mocks for internal lists
    // Add mocks to factories and hierarchyFactories lists to test reverse and addAll behavior
    TypeAdapterFactory factory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory factory2 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactory2 = mock(TypeAdapterFactory.class);

    // Add factories
    List<TypeAdapterFactory> factoriesList = getField(gsonBuilder, "factories");
    factoriesList.add(factory1);
    factoriesList.add(factory2);

    // Add hierarchy factories
    List<TypeAdapterFactory> hierarchyFactoriesList = getField(gsonBuilder, "hierarchyFactories");
    hierarchyFactoriesList.add(hierarchyFactory1);
    hierarchyFactoriesList.add(hierarchyFactory2);

    // Spy on gsonBuilder to verify addTypeAdaptersForDate is called with correct args
    GsonBuilder spyBuilder = Mockito.spy(gsonBuilder);

    // Invoke create()
    Gson gson = spyBuilder.create();

    // Verify that addTypeAdaptersForDate was called with correct parameters
    verify(spyBuilder).addTypeAdaptersForDate(
        anyString(),
        anyInt(),
        anyInt(),
        anyList());

    // Check that returned Gson is not null and has expected fields set
    assertNotNull(gson);

    // Validate that factories in Gson include reversed factories and hierarchyFactories and date adapters
    List<TypeAdapterFactory> factories = getField(gson, "factories");
    List<TypeAdapterFactory> hierarchyFactories = getField(gson, "hierarchyFactories");
    List<TypeAdapterFactory> builderFactories = getField(gsonBuilder, "factories");
    List<TypeAdapterFactory> builderHierarchyFactories = getField(gsonBuilder, "hierarchyFactories");

    // The factories list in Gson should contain reversed factories + reversed hierarchyFactories + date adapters (3 more)
    // So total size = builderFactories.size() + builderHierarchyFactories.size() + 3
    List<TypeAdapterFactory> combinedFactories = getField(gson, "factories");
    assertEquals(builderFactories.size() + builderHierarchyFactories.size() + 3, combinedFactories.size());

    // The hierarchyFactories in Gson should be a copy of builder's hierarchyFactories
    assertEquals(builderHierarchyFactories.size(), hierarchyFactories.size());

    // Validate other fields copied correctly
    assertEquals(getField(gsonBuilder, "excluder"), getField(gson, "excluder"));
    assertEquals(getField(gsonBuilder, "fieldNamingPolicy"), getField(gson, "fieldNamingStrategy"));
    assertEquals(getField(gsonBuilder, "serializeNulls"), getField(gson, "serializeNulls"));
    assertEquals(getField(gsonBuilder, "complexMapKeySerialization"), getField(gson, "complexMapKeySerialization"));
    assertEquals(getField(gsonBuilder, "generateNonExecutableJson"), getField(gson, "generateNonExecutableJson"));
    assertEquals(getField(gsonBuilder, "escapeHtmlChars"), getField(gson, "escapeHtmlChars"));
    assertEquals(getField(gsonBuilder, "prettyPrinting"), getField(gson, "prettyPrinting"));
    assertEquals(getField(gsonBuilder, "lenient"), getField(gson, "lenient"));
    assertEquals(getField(gsonBuilder, "serializeSpecialFloatingPointValues"), getField(gson, "serializeSpecialFloatingPointValues"));
    assertEquals(getField(gsonBuilder, "useJdkUnsafe"), getField(gson, "useJdkUnsafe"));
    assertEquals(getField(gsonBuilder, "longSerializationPolicy"), getField(gson, "longSerializationPolicy"));
    assertEquals(getField(gsonBuilder, "datePattern"), getField(gson, "datePattern"));
    assertEquals(getField(gsonBuilder, "dateStyle"), getField(gson, "dateStyle"));
    assertEquals(getField(gsonBuilder, "timeStyle"), getField(gson, "timeStyle"));
    assertEquals(getField(gsonBuilder, "objectToNumberStrategy"), getField(gson, "objectToNumberStrategy"));
    assertEquals(getField(gsonBuilder, "numberToNumberStrategy"), getField(gson, "numberToNumberStrategy"));

    // Validate instanceCreators map copied correctly
    HashMap<?, ?> instanceCreatorsBuilder = getField(gsonBuilder, "instanceCreators");
    HashMap<?, ?> instanceCreatorsGson = getField(gson, "instanceCreators");
    assertEquals(instanceCreatorsBuilder, instanceCreatorsGson);

    // Validate reflectionFilters copied correctly
    LinkedList<?> reflectionFiltersBuilder = getField(gsonBuilder, "reflectionFilters");
    List<?> reflectionFiltersGson = getField(gson, "reflectionFilters");
    assertEquals(reflectionFiltersBuilder.size(), reflectionFiltersGson.size());
  }

  @Test
    @Timeout(8000)
  void create_WithCustomDatePatternAndStyles_AddsDateTypeAdapters() throws Exception {
    String customDatePattern = "yyyy-MM-dd'T'HH:mm:ss";
    int customDateStyle = DateFormat.SHORT;
    int customTimeStyle = DateFormat.MEDIUM;

    setField(gsonBuilder, "datePattern", customDatePattern);
    setField(gsonBuilder, "dateStyle", customDateStyle);
    setField(gsonBuilder, "timeStyle", customTimeStyle);

    GsonBuilder spyBuilder = Mockito.spy(gsonBuilder);

    // We want to capture the factories list passed to addTypeAdaptersForDate
    doAnswer(invocation -> {
      List<TypeAdapterFactory> factories = invocation.getArgument(3);
      // Add a dummy factory to simulate date adapters added
      factories.add(mock(TypeAdapterFactory.class));
      return null;
    }).when(spyBuilder).addTypeAdaptersForDate(anyString(), anyInt(), anyInt(), anyList());

    Gson gson = spyBuilder.create();

    verify(spyBuilder).addTypeAdaptersForDate(eq(customDatePattern), eq(customDateStyle), eq(customTimeStyle), anyList());

    assertNotNull(gson);
  }

  @Test
    @Timeout(8000)
  void create_EmptyFactoriesAndHierarchyFactories_ReturnsGsonWith3DateTypeAdapters() throws Exception {
    // Clear factories and hierarchyFactories
    List<TypeAdapterFactory> factories = getField(gsonBuilder, "factories");
    factories.clear();
    List<TypeAdapterFactory> hierarchyFactories = getField(gsonBuilder, "hierarchyFactories");
    hierarchyFactories.clear();

    Gson gson = gsonBuilder.create();

    List<TypeAdapterFactory> combinedFactories = getField(gson, "factories");

    // Should contain exactly 3 factories (date adapters)
    assertEquals(3, combinedFactories.size());
  }

  // Helper methods to access private fields via reflection
  @SuppressWarnings("unchecked")
  private <T> T getField(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(target);
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}