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
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonBuilder_25_3Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapterFactory_shouldAddFactoryToFactoriesList_andReturnThis() throws Exception {
    // Arrange
    TypeAdapterFactory mockFactory = mock(TypeAdapterFactory.class);
    // Access private field 'factories' before registering
    Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<TypeAdapterFactory> factoriesBefore = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);
    int initialSize = factoriesBefore.size();

    // Act
    GsonBuilder returned = gsonBuilder.registerTypeAdapterFactory(mockFactory);

    // Assert
    assertSame(gsonBuilder, returned, "registerTypeAdapterFactory should return this");
    @SuppressWarnings("unchecked")
    List<TypeAdapterFactory> factoriesAfter = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);
    assertEquals(initialSize + 1, factoriesAfter.size(), "Factories list size should increase by 1");
    assertTrue(factoriesAfter.contains(mockFactory), "Factories list should contain the registered factory");
  }

  @Test
    @Timeout(8000)
  void registerTypeAdapterFactory_shouldThrowNullPointerException_whenFactoryIsNull() {
    assertThrows(NullPointerException.class, () -> gsonBuilder.registerTypeAdapterFactory(null));
  }
}