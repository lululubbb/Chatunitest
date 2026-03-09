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
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.List;

class GsonBuilder_1_3Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testCreate_DefaultConfiguration() throws Exception {
    // Act
    Gson gson = gsonBuilder.create();

    // Assert
    assertNotNull(gson);
  }

  @Test
    @Timeout(8000)
  void testCreate_WithCustomSettings() throws Exception {
    // Arrange
    gsonBuilder.serializeNulls()
        .enableComplexMapKeySerialization()
        .setPrettyPrinting()
        .setLenient()
        .disableHtmlEscaping()
        .setDateFormat("yyyy-MM-dd")
        .setLongSerializationPolicy(LongSerializationPolicy.STRING)
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        .setNumberToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE)
        .disableJdkUnsafe();

    // Act
    Gson gson = gsonBuilder.create();

    // Assert
    assertNotNull(gson);
  }

  @Test
    @Timeout(8000)
  void testCreate_ReflectionAccessFilters() throws Exception {
    // Arrange
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    gsonBuilder.addReflectionAccessFilter(filter);

    // Act
    Gson gson = gsonBuilder.create();

    // Assert
    assertNotNull(gson);
  }

  @Test
    @Timeout(8000)
  void testPrivateAddTypeAdaptersForDate() throws Exception {
    // Arrange
    Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);
    List<TypeAdapterFactory> factories = new java.util.ArrayList<>();

    // Act
    method.invoke(gsonBuilder, "yyyy-MM-dd", DateFormat.SHORT, DateFormat.LONG, factories);

    // Assert
    assertFalse(factories.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testCreate_WithInstanceCreatorsAndFactories() throws Exception {
    // Arrange
    gsonBuilder.registerTypeAdapter(String.class, (InstanceCreator<String>) type -> "created")
        .registerTypeAdapterFactory(TypeAdapters.STRING_FACTORY);

    // Act
    Gson gson = gsonBuilder.create();

    // Assert
    assertNotNull(gson);
  }
}