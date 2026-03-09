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
import static org.mockito.Mockito.*;

import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

class GsonBuilder_31_2Test {
  private GsonBuilder gsonBuilder;
  private Method addTypeAdaptersForDateMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    gsonBuilder = new GsonBuilder();
    addTypeAdaptersForDateMethod = GsonBuilder.class.getDeclaredMethod(
        "addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    addTypeAdaptersForDateMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withDatePattern_andSqlTypesSupported() throws Throwable {
    try (MockedStatic<SqlTypesSupport> sqlTypesSupportMockedStatic = Mockito.mockStatic(SqlTypesSupport.class)) {
      sqlTypesSupportMockedStatic.when(() -> { return SqlTypesSupport.SUPPORTS_SQL_TYPES; }).thenReturn(true);

      String datePattern = "yyyy-MM-dd";
      int dateStyle = DateFormat.DEFAULT;
      int timeStyle = DateFormat.DEFAULT;
      List<Object> factories = new ArrayList<>();

      // Invoke private method
      addTypeAdaptersForDateMethod.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

      // Assertions
      assertFalse(factories.isEmpty());
      Object firstFactory = factories.get(0);
      assertNotNull(firstFactory);
      assertEquals(3, factories.size());
      assertNotNull(factories.get(1));
      assertNotNull(factories.get(2));
    }
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withDatePattern_andSqlTypesNotSupported() throws Throwable {
    try (MockedStatic<SqlTypesSupport> sqlTypesSupportMockedStatic = Mockito.mockStatic(SqlTypesSupport.class)) {
      sqlTypesSupportMockedStatic.when(() -> { return SqlTypesSupport.SUPPORTS_SQL_TYPES; }).thenReturn(false);

      String datePattern = "yyyy-MM-dd";
      int dateStyle = DateFormat.DEFAULT;
      int timeStyle = DateFormat.DEFAULT;
      List<Object> factories = new ArrayList<>();

      addTypeAdaptersForDateMethod.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

      assertEquals(1, factories.size());
      assertNotNull(factories.get(0));
    }
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withDateAndTimeStyle_andSqlTypesSupported() throws Throwable {
    try (MockedStatic<SqlTypesSupport> sqlTypesSupportMockedStatic = Mockito.mockStatic(SqlTypesSupport.class)) {
      sqlTypesSupportMockedStatic.when(() -> { return SqlTypesSupport.SUPPORTS_SQL_TYPES; }).thenReturn(true);

      String datePattern = null;
      int dateStyle = DateFormat.SHORT;
      int timeStyle = DateFormat.MEDIUM;
      List<Object> factories = new ArrayList<>();

      addTypeAdaptersForDateMethod.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

      assertEquals(3, factories.size());
      assertNotNull(factories.get(0));
      assertNotNull(factories.get(1));
      assertNotNull(factories.get(2));
    }
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withDateAndTimeStyle_andSqlTypesNotSupported() throws Throwable {
    try (MockedStatic<SqlTypesSupport> sqlTypesSupportMockedStatic = Mockito.mockStatic(SqlTypesSupport.class)) {
      sqlTypesSupportMockedStatic.when(() -> { return SqlTypesSupport.SUPPORTS_SQL_TYPES; }).thenReturn(false);

      String datePattern = null;
      int dateStyle = DateFormat.SHORT;
      int timeStyle = DateFormat.MEDIUM;
      List<Object> factories = new ArrayList<>();

      addTypeAdaptersForDateMethod.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

      assertEquals(1, factories.size());
      assertNotNull(factories.get(0));
    }
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withBlankDatePatternAndDefaultStyles() throws Throwable {
    String datePattern = "   ";
    int dateStyle = DateFormat.DEFAULT;
    int timeStyle = DateFormat.DEFAULT;
    List<Object> factories = new ArrayList<>();

    addTypeAdaptersForDateMethod.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

    assertTrue(factories.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withNullDatePatternAndDefaultStyles() throws Throwable {
    String datePattern = null;
    int dateStyle = DateFormat.DEFAULT;
    int timeStyle = DateFormat.DEFAULT;
    List<Object> factories = new ArrayList<>();

    addTypeAdaptersForDateMethod.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

    assertTrue(factories.isEmpty());
  }
}