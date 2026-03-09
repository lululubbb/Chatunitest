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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

class GsonBuilder_31_4Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withNonEmptyDatePattern_addsFactoriesIncludingSql() throws Exception {
    List<TypeAdapterFactory> factories = new ArrayList<>();
    String datePattern = "yyyy-MM-dd";

    try (MockedStatic<SqlTypesSupport> sqlMock = Mockito.mockStatic(SqlTypesSupport.class);
         MockedStatic<DefaultDateTypeAdapter.DateType> dateTypeMock = Mockito.mockStatic(DefaultDateTypeAdapter.DateType.class)) {

      sqlMock.when(() -> SqlTypesSupport.SUPPORTS_SQL_TYPES).thenReturn(true);
      TypeAdapterFactory dateAdapterFactory = mock(TypeAdapterFactory.class);
      TypeAdapterFactory sqlTimestampAdapterFactory = mock(TypeAdapterFactory.class);
      TypeAdapterFactory sqlDateAdapterFactory = mock(TypeAdapterFactory.class);

      DefaultDateTypeAdapter.DateType dateType = mock(DefaultDateTypeAdapter.DateType.class);
      dateTypeMock.when(() -> DefaultDateTypeAdapter.DateType.DATE).thenReturn(dateType);
      when(dateType.createAdapterFactory(datePattern)).thenReturn(dateAdapterFactory);

      // Use reflection to set final static fields
      setFinalStaticField(SqlTypesSupport.class, "TIMESTAMP_DATE_TYPE", mock(DefaultDateTypeAdapter.DateType.class));
      setFinalStaticField(SqlTypesSupport.class, "DATE_DATE_TYPE", mock(DefaultDateTypeAdapter.DateType.class));

      DefaultDateTypeAdapter.DateType timestampDateType = (DefaultDateTypeAdapter.DateType) getStaticField(SqlTypesSupport.class, "TIMESTAMP_DATE_TYPE");
      DefaultDateTypeAdapter.DateType dateDateType = (DefaultDateTypeAdapter.DateType) getStaticField(SqlTypesSupport.class, "DATE_DATE_TYPE");

      when(timestampDateType.createAdapterFactory(datePattern)).thenReturn(sqlTimestampAdapterFactory);
      when(dateDateType.createAdapterFactory(datePattern)).thenReturn(sqlDateAdapterFactory);

      Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
      method.setAccessible(true);
      method.invoke(gsonBuilder, datePattern, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

      assertEquals(3, factories.size());
      assertSame(dateAdapterFactory, factories.get(0));
      assertSame(sqlTimestampAdapterFactory, factories.get(1));
      assertSame(sqlDateAdapterFactory, factories.get(2));
    }
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withDateAndTimeStyle_addsFactoriesIncludingSql() throws Exception {
    List<TypeAdapterFactory> factories = new ArrayList<>();
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.MEDIUM;

    try (MockedStatic<SqlTypesSupport> sqlMock = Mockito.mockStatic(SqlTypesSupport.class);
         MockedStatic<DefaultDateTypeAdapter.DateType> dateTypeMock = Mockito.mockStatic(DefaultDateTypeAdapter.DateType.class)) {

      sqlMock.when(() -> SqlTypesSupport.SUPPORTS_SQL_TYPES).thenReturn(true);
      TypeAdapterFactory dateAdapterFactory = mock(TypeAdapterFactory.class);
      TypeAdapterFactory sqlTimestampAdapterFactory = mock(TypeAdapterFactory.class);
      TypeAdapterFactory sqlDateAdapterFactory = mock(TypeAdapterFactory.class);

      DefaultDateTypeAdapter.DateType dateType = mock(DefaultDateTypeAdapter.DateType.class);
      dateTypeMock.when(() -> DefaultDateTypeAdapter.DateType.DATE).thenReturn(dateType);
      when(dateType.createAdapterFactory(dateStyle, timeStyle)).thenReturn(dateAdapterFactory);

      setFinalStaticField(SqlTypesSupport.class, "TIMESTAMP_DATE_TYPE", mock(DefaultDateTypeAdapter.DateType.class));
      setFinalStaticField(SqlTypesSupport.class, "DATE_DATE_TYPE", mock(DefaultDateTypeAdapter.DateType.class));

      DefaultDateTypeAdapter.DateType timestampDateType = (DefaultDateTypeAdapter.DateType) getStaticField(SqlTypesSupport.class, "TIMESTAMP_DATE_TYPE");
      DefaultDateTypeAdapter.DateType dateDateType = (DefaultDateTypeAdapter.DateType) getStaticField(SqlTypesSupport.class, "DATE_DATE_TYPE");

      when(timestampDateType.createAdapterFactory(dateStyle, timeStyle)).thenReturn(sqlTimestampAdapterFactory);
      when(dateDateType.createAdapterFactory(dateStyle, timeStyle)).thenReturn(sqlDateAdapterFactory);

      Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
      method.setAccessible(true);
      method.invoke(gsonBuilder, null, dateStyle, timeStyle, factories);

      assertEquals(3, factories.size());
      assertSame(dateAdapterFactory, factories.get(0));
      assertSame(sqlTimestampAdapterFactory, factories.get(1));
      assertSame(sqlDateAdapterFactory, factories.get(2));
    }
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withNullPatternAndDefaultStyles_doesNotAddFactories() throws Exception {
    List<TypeAdapterFactory> factories = new ArrayList<>();

    Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);
    method.invoke(gsonBuilder, null, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    assertTrue(factories.isEmpty());
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withEmptyPatternAndDefaultStyles_doesNotAddFactories() throws Exception {
    List<TypeAdapterFactory> factories = new ArrayList<>();

    Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);
    method.invoke(gsonBuilder, "   ", DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    assertTrue(factories.isEmpty());
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withSqlTypesNotSupported_addsOnlyDateAdapterFactory() throws Exception {
    List<TypeAdapterFactory> factories = new ArrayList<>();
    String datePattern = "yyyy-MM-dd";

    try (MockedStatic<SqlTypesSupport> sqlMock = Mockito.mockStatic(SqlTypesSupport.class);
         MockedStatic<DefaultDateTypeAdapter.DateType> dateTypeMock = Mockito.mockStatic(DefaultDateTypeAdapter.DateType.class)) {

      sqlMock.when(() -> SqlTypesSupport.SUPPORTS_SQL_TYPES).thenReturn(false);
      TypeAdapterFactory dateAdapterFactory = mock(TypeAdapterFactory.class);

      DefaultDateTypeAdapter.DateType dateType = mock(DefaultDateTypeAdapter.DateType.class);
      dateTypeMock.when(() -> DefaultDateTypeAdapter.DateType.DATE).thenReturn(dateType);
      when(dateType.createAdapterFactory(datePattern)).thenReturn(dateAdapterFactory);

      Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
      method.setAccessible(true);
      method.invoke(gsonBuilder, datePattern, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

      assertEquals(1, factories.size());
      assertSame(dateAdapterFactory, factories.get(0));
    }
  }

  // Helper method to set final static fields via reflection
  private static void setFinalStaticField(Class<?> clazz, String fieldName, Object newValue) throws Exception {
    java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier
    java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    field.set(null, newValue);
  }

  // Helper method to get static field value via reflection
  private static Object getStaticField(Class<?> clazz, String fieldName) throws Exception {
    java.lang.reflect.Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(null);
  }
}