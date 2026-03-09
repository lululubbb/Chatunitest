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

class GsonBuilder_30_3Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void create_DefaultSettings_ShouldReturnGsonWithExpectedProperties() throws Exception {
    // Arrange
    // Prepare some dummy factories to test list combination and reversal
    TypeAdapterFactory factory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory factory2 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactory2 = mock(TypeAdapterFactory.class);

    // Use reflection to add factories to private lists
    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);
    factories.add(factory1);
    factories.add(factory2);

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    List<TypeAdapterFactory> hierarchyFactories = (List<TypeAdapterFactory>) hierarchyFactoriesField.get(gsonBuilder);
    hierarchyFactories.add(hierarchyFactory1);
    hierarchyFactories.add(hierarchyFactory2);

    // Spy on the GsonBuilder to verify private method call
    GsonBuilder spyBuilder = Mockito.spy(gsonBuilder);

    // We will verify that addTypeAdaptersForDate is called with correct arguments and list
    doNothing().when(spyBuilder).addTypeAdaptersForDate(anyString(), anyInt(), anyInt(), anyList());

    // Act
    Gson gson = spyBuilder.create();

    // Assert
    assertNotNull(gson);

    // Verify addTypeAdaptersForDate called once with correct datePattern, dateStyle, timeStyle and a list containing all factories reversed
    verify(spyBuilder, times(1)).addTypeAdaptersForDate(
        eq((String) getFieldValue(spyBuilder, "datePattern")),
        eq((Integer) getFieldValue(spyBuilder, "dateStyle")),
        eq((Integer) getFieldValue(spyBuilder, "timeStyle")),
        anyList());

    // Validate the combined factories list in the created Gson object
    // Access Gson's factories field via reflection
    Field gsonFactoriesField = Gson.class.getDeclaredField("factories");
    gsonFactoriesField.setAccessible(true);
    List<?> gsonFactories = (List<?>) gsonFactoriesField.get(gson);

    // The combined list size should be original factories + hierarchyFactories + 3 (per create() method)
    int expectedSize = factories.size() + hierarchyFactories.size() + 3;
    assertEquals(expectedSize, gsonFactories.size());

    // Validate that the factories list passed to Gson constructor is as expected
    // The first added factories list is reversed in create()
    List<TypeAdapterFactory> reversedFactories = new ArrayList<>(factories);
    java.util.Collections.reverse(reversedFactories);

    // The hierarchyFactories are reversed and appended
    List<TypeAdapterFactory> reversedHierarchyFactories = new ArrayList<>(hierarchyFactories);
    java.util.Collections.reverse(reversedHierarchyFactories);

    // The combined list passed to Gson constructor is reversedFactories + reversedHierarchyFactories + 3 type adapters for date
    // We cannot directly check the last 3, but check the first two parts are correctly reversed and in order
    assertTrue(gsonFactories.containsAll(reversedFactories));
    assertTrue(gsonFactories.containsAll(reversedHierarchyFactories));

    // Check other fields in Gson match those in GsonBuilder
    assertEquals(getFieldValue(spyBuilder, "excluder"), getFieldValue(gson, "excluder"));
    assertEquals(getFieldValue(spyBuilder, "fieldNamingPolicy"), getFieldValue(gson, "fieldNamingStrategy"));
    assertEquals(getFieldValue(spyBuilder, "serializeNulls"), getFieldValue(gson, "serializeNulls"));
    assertEquals(getFieldValue(spyBuilder, "complexMapKeySerialization"), getFieldValue(gson, "complexMapKeySerialization"));
    assertEquals(getFieldValue(spyBuilder, "generateNonExecutableJson"), getFieldValue(gson, "generateNonExecutableJson"));
    assertEquals(getFieldValue(spyBuilder, "escapeHtmlChars"), getFieldValue(gson, "escapeHtmlChars"));
    assertEquals(getFieldValue(spyBuilder, "prettyPrinting"), getFieldValue(gson, "prettyPrinting"));
    assertEquals(getFieldValue(spyBuilder, "lenient"), getFieldValue(gson, "lenient"));
    assertEquals(getFieldValue(spyBuilder, "serializeSpecialFloatingPointValues"), getFieldValue(gson, "serializeSpecialFloatingPointValues"));
    assertEquals(getFieldValue(spyBuilder, "useJdkUnsafe"), getFieldValue(gson, "useJdkUnsafe"));
    assertEquals(getFieldValue(spyBuilder, "longSerializationPolicy"), getFieldValue(gson, "longSerializationPolicy"));
    assertEquals(getFieldValue(spyBuilder, "datePattern"), getFieldValue(gson, "datePattern"));
    assertEquals(getFieldValue(spyBuilder, "dateStyle"), getFieldValue(gson, "dateStyle"));
    assertEquals(getFieldValue(spyBuilder, "timeStyle"), getFieldValue(gson, "timeStyle"));
    assertEquals(getFieldValue(spyBuilder, "objectToNumberStrategy"), getFieldValue(gson, "objectToNumberStrategy"));
    assertEquals(getFieldValue(spyBuilder, "numberToNumberStrategy"), getFieldValue(gson, "numberToNumberStrategy"));

    // Validate instanceCreators copied correctly
    HashMap<?, ?> gsonInstanceCreators = (HashMap<?, ?>) getFieldValue(gson, "instanceCreators");
    assertTrue(gsonInstanceCreators.isEmpty());

    // Validate that the factories and hierarchyFactories in Gson are copies (new ArrayList)
    List<?> gsonBuilderFactories = (List<?>) getFieldValue(spyBuilder, "factories");
    List<?> gsonBuilderHierarchyFactories = (List<?>) getFieldValue(spyBuilder, "hierarchyFactories");
    List<?> gsonFactoriesList = (List<?>) getFieldValue(gson, "factories");
    List<?> gsonHierarchyFactoriesList = (List<?>) getFieldValue(gson, "hierarchyFactories");
    assertNotSame(gsonBuilderFactories, gsonFactoriesList);
    assertNotSame(gsonBuilderHierarchyFactories, gsonHierarchyFactoriesList);
    assertEquals(gsonBuilderFactories, gsonFactoriesList);
    assertEquals(gsonBuilderHierarchyFactories, gsonHierarchyFactoriesList);

    // Validate reflectionFilters copied as new ArrayList
    LinkedList<?> reflectionFilters = (LinkedList<?>) getFieldValue(spyBuilder, "reflectionFilters");
    List<?> gsonReflectionFilters = (List<?>) getFieldValue(gson, "reflectionFilters");
    assertNotSame(reflectionFilters, gsonReflectionFilters);
    assertEquals(reflectionFilters, new LinkedList<>(gsonReflectionFilters));
  }

  private Object getFieldValue(Object target, String fieldName) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(target);
  }
}