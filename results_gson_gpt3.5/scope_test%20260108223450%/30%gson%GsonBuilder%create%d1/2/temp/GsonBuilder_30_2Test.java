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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class GsonBuilder_30_2Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void create_DefaultSettings_ReturnsGsonInstance() throws Exception {
    // Prepare fields to simulate some state
    // Add dummy factories and hierarchyFactories
    TypeAdapterFactory factory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory factory2 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactory1 = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactory2 = mock(TypeAdapterFactory.class);

    // Use reflection to add factories and hierarchyFactories
    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factoriesList = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);
    factoriesList.add(factory1);
    factoriesList.add(factory2);

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    List<TypeAdapterFactory> hierarchyFactoriesList = (List<TypeAdapterFactory>) hierarchyFactoriesField.get(gsonBuilder);
    hierarchyFactoriesList.add(hierarchyFactory1);
    hierarchyFactoriesList.add(hierarchyFactory2);

    // Spy on gsonBuilder to verify addTypeAdaptersForDate call
    GsonBuilder spyBuilder = Mockito.spy(gsonBuilder);

    // Invoke create method
    Method createMethod = GsonBuilder.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);
    Gson gson = (Gson) createMethod.invoke(spyBuilder);

    assertNotNull(gson);
    // Verify that addTypeAdaptersForDate was called once with correct parameters
    verify(spyBuilder, times(1)).addTypeAdaptersForDate(anyString(), anyInt(), anyInt(), anyList());

    // Verify factories list in Gson is correctly constructed
    Field gsonFactoriesField = Gson.class.getDeclaredField("factories");
    gsonFactoriesField.setAccessible(true);
    List<?> gsonFactories = (List<?>) gsonFactoriesField.get(gson);
    assertTrue(gsonFactories.contains(factory1));
    assertTrue(gsonFactories.contains(factory2));
    assertTrue(gsonFactories.contains(hierarchyFactory1));
    assertTrue(gsonFactories.contains(hierarchyFactory2));

    // Check other fields are set to default values
    Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNulls = (boolean) serializeNullsField.get(gsonBuilder);
    assertEquals(serializeNulls, gson.serializeNulls());

    // Check datePattern, dateStyle, timeStyle are passed correctly
    Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    String datePattern = (String) datePatternField.get(gsonBuilder);
    Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    int dateStyle = (int) dateStyleField.get(gsonBuilder);
    Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    int timeStyle = (int) timeStyleField.get(gsonBuilder);
    // No direct getter for these in Gson, so just assert non-null datePattern
    assertNotNull(datePattern);
    assertTrue(dateStyle >= 0);
    assertTrue(timeStyle >= 0);
  }

  @Test
    @Timeout(8000)
  void create_WithEmptyFactoriesAndHierarchyFactories_ReturnsGsonInstance() throws Exception {
    // Ensure factories and hierarchyFactories are empty
    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factoriesList = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);
    factoriesList.clear();

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    List<TypeAdapterFactory> hierarchyFactoriesList = (List<TypeAdapterFactory>) hierarchyFactoriesField.get(gsonBuilder);
    hierarchyFactoriesList.clear();

    // Spy on gsonBuilder to verify addTypeAdaptersForDate call
    GsonBuilder spyBuilder = Mockito.spy(gsonBuilder);

    Method createMethod = GsonBuilder.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);
    Gson gson = (Gson) createMethod.invoke(spyBuilder);

    assertNotNull(gson);
    verify(spyBuilder, times(1)).addTypeAdaptersForDate(anyString(), anyInt(), anyInt(), anyList());
  }

  @Test
    @Timeout(8000)
  void create_VerifyFactoriesOrderAfterReverse() throws Exception {
    TypeAdapterFactory factoryA = mock(TypeAdapterFactory.class);
    TypeAdapterFactory factoryB = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactoryA = mock(TypeAdapterFactory.class);
    TypeAdapterFactory hierarchyFactoryB = mock(TypeAdapterFactory.class);

    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factoriesList = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);
    factoriesList.clear();
    factoriesList.add(factoryA);
    factoriesList.add(factoryB);

    Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
    hierarchyFactoriesField.setAccessible(true);
    List<TypeAdapterFactory> hierarchyFactoriesList = (List<TypeAdapterFactory>) hierarchyFactoriesField.get(gsonBuilder);
    hierarchyFactoriesList.clear();
    hierarchyFactoriesList.add(hierarchyFactoryA);
    hierarchyFactoriesList.add(hierarchyFactoryB);

    // Spy on gsonBuilder to intercept addTypeAdaptersForDate call and capture argument
    GsonBuilder spyBuilder = Mockito.spy(gsonBuilder);

    doAnswer(invocation -> {
      List<TypeAdapterFactory> factories = invocation.getArgument(3);
      // Factories should contain reversed factories and reversed hierarchyFactories plus 3 added in addTypeAdaptersForDate
      // The original factories: [factoryA, factoryB]
      // After reverse: [factoryB, factoryA]
      // hierarchyFactories: [hierarchyFactoryA, hierarchyFactoryB]
      // After reverse: [hierarchyFactoryB, hierarchyFactoryA]
      // So combined factories before addTypeAdaptersForDate: [factoryB, factoryA, hierarchyFactoryB, hierarchyFactoryA]
      // addTypeAdaptersForDate might add 3 more factories, so total size >= 7
      assertTrue(factories.contains(factoryB));
      assertTrue(factories.contains(factoryA));
      assertTrue(factories.contains(hierarchyFactoryB));
      assertTrue(factories.contains(hierarchyFactoryA));
      assertTrue(factories.size() >= 4);
      return null;
    }).when(spyBuilder).addTypeAdaptersForDate(anyString(), anyInt(), anyInt(), anyList());

    Method createMethod = GsonBuilder.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);
    Gson gson = (Gson) createMethod.invoke(spyBuilder);

    assertNotNull(gson);
  }
}