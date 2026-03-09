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
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.*;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

class GsonBuilder_31_3Test {

  private GsonBuilder gsonBuilder;
  private List<TypeAdapterFactory> factories;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
    factories = new ArrayList<>();
  }

  private void invokeAddTypeAdaptersForDate(String datePattern, int dateStyle, int timeStyle, List<TypeAdapterFactory> factories)
      throws Exception {
    Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);
    method.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withNonEmptyDatePattern_andSqlTypesSupported() throws Exception {
    // Arrange
    setStaticFinalField(SqlTypesSupport.class, "SUPPORTS_SQL_TYPES", true);

    // Act
    invokeAddTypeAdaptersForDate("yyyy-MM-dd", DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    // Assert
    assertEquals(3, factories.size());
    assertTrue(factories.get(0).getClass().getName().contains("DefaultDateTypeAdapter"));
    assertNotNull(factories.get(1));
    assertNotNull(factories.get(2));
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withNonEmptyDatePattern_andSqlTypesNotSupported() throws Exception {
    // Arrange
    setStaticFinalField(SqlTypesSupport.class, "SUPPORTS_SQL_TYPES", false);

    // Act
    invokeAddTypeAdaptersForDate("yyyy-MM-dd", DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    // Assert
    assertEquals(1, factories.size());
    assertTrue(factories.get(0).getClass().getName().contains("DefaultDateTypeAdapter"));
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withDateStyleAndTimeStyle_andSqlTypesSupported() throws Exception {
    // Arrange
    setStaticFinalField(SqlTypesSupport.class, "SUPPORTS_SQL_TYPES", true);

    // Act
    invokeAddTypeAdaptersForDate(null, DateFormat.SHORT, DateFormat.MEDIUM, factories);

    // Assert
    assertEquals(3, factories.size());
    assertTrue(factories.get(0).getClass().getName().contains("DefaultDateTypeAdapter"));
    assertNotNull(factories.get(1));
    assertNotNull(factories.get(2));
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withDateStyleAndTimeStyle_andSqlTypesNotSupported() throws Exception {
    // Arrange
    setStaticFinalField(SqlTypesSupport.class, "SUPPORTS_SQL_TYPES", false);

    // Act
    invokeAddTypeAdaptersForDate(null, DateFormat.SHORT, DateFormat.MEDIUM, factories);

    // Assert
    assertEquals(1, factories.size());
    assertTrue(factories.get(0).getClass().getName().contains("DefaultDateTypeAdapter"));
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withEmptyDatePattern_andDefaultStyles() throws Exception {
    // Arrange
    setStaticFinalField(SqlTypesSupport.class, "SUPPORTS_SQL_TYPES", true);

    // Act
    invokeAddTypeAdaptersForDate("   ", DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    // Assert
    assertTrue(factories.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withNullDatePattern_andDefaultStyles() throws Exception {
    // Arrange
    setStaticFinalField(SqlTypesSupport.class, "SUPPORTS_SQL_TYPES", true);

    // Act
    invokeAddTypeAdaptersForDate(null, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    // Assert
    assertTrue(factories.isEmpty());
  }

  private static void setStaticFinalField(Class<?> clazz, String fieldName, Object value) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier from field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    // For static final fields, set the value via Unsafe if needed
    // But here, reflection set is enough for test environment

    field.set(null, value);
  }
}