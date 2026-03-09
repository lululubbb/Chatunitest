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
import com.google.gson.TypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

class GsonBuilder_31_6Test {

  private GsonBuilder gsonBuilder;
  private List<TypeAdapterFactory> factories;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
    factories = new ArrayList<>();
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withNonEmptyDatePattern_andSqlTypesSupported() throws Exception {
    setSqlTypesSupport(true);

    String datePattern = "yyyy-MM-dd";

    invokeAddTypeAdaptersForDate(datePattern, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    assertEquals(3, factories.size());
    assertTrue(factories.get(0).getClass().getName().contains("DefaultDateTypeAdapter"));
    assertNotNull(factories.get(1));
    assertNotNull(factories.get(2));
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withNonEmptyDatePattern_andSqlTypesNotSupported() throws Exception {
    setSqlTypesSupport(false);

    String datePattern = "yyyy-MM-dd";

    invokeAddTypeAdaptersForDate(datePattern, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    assertEquals(1, factories.size());
    assertTrue(factories.get(0).getClass().getName().contains("DefaultDateTypeAdapter"));
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withDateStyleAndTimeStyle_andSqlTypesSupported() throws Exception {
    setSqlTypesSupport(true);

    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.MEDIUM;

    invokeAddTypeAdaptersForDate(null, dateStyle, timeStyle, factories);

    assertEquals(3, factories.size());
    assertTrue(factories.get(0).getClass().getName().contains("DefaultDateTypeAdapter"));
    assertNotNull(factories.get(1));
    assertNotNull(factories.get(2));
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withDateStyleAndTimeStyle_andSqlTypesNotSupported() throws Exception {
    setSqlTypesSupport(false);

    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.MEDIUM;

    invokeAddTypeAdaptersForDate(null, dateStyle, timeStyle, factories);

    assertEquals(1, factories.size());
    assertTrue(factories.get(0).getClass().getName().contains("DefaultDateTypeAdapter"));
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withEmptyDatePattern_andDefaultStyles() throws Exception {
    setSqlTypesSupport(true);

    String datePattern = "  ";

    invokeAddTypeAdaptersForDate(datePattern, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    assertTrue(factories.isEmpty());
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withNullDatePattern_andDefaultStyles() throws Exception {
    setSqlTypesSupport(true);

    invokeAddTypeAdaptersForDate(null, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    assertTrue(factories.isEmpty());
  }

  private void setSqlTypesSupport(boolean supported) throws Exception {
    Field field = SqlTypesSupport.class.getDeclaredField("SUPPORTS_SQL_TYPES");
    field.setAccessible(true);

    // Remove final modifier if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(null, supported);

    // Reset cached fields in SqlTypesSupport if any (optional, depending on implementation)
  }

  private void invokeAddTypeAdaptersForDate(String datePattern, int dateStyle, int timeStyle,
      List<TypeAdapterFactory> factories) throws Exception {
    Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);
    method.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);
  }
}