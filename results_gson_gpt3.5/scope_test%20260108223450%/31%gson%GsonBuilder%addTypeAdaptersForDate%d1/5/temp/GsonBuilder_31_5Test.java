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

import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.internal.bind.TypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

class GsonBuilder_31_5Test {

  private GsonBuilder gsonBuilder;
  private List<TypeAdapterFactory> factories;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
    factories = new ArrayList<>();
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withValidDatePattern_addsFactoriesIncludingSql() throws Exception {
    String datePattern = "yyyy-MM-dd";
    int dateStyle = DateFormat.DEFAULT;
    int timeStyle = DateFormat.DEFAULT;

    try (MockedStatic<SqlTypesSupport> sqlSupportMock = Mockito.mockStatic(SqlTypesSupport.class);
         MockedStatic<DefaultDateTypeAdapter.DateType> dateTypeMock = Mockito.mockStatic(DefaultDateTypeAdapter.DateType.class)) {

      // Mock SqlTypesSupport.SUPPORTS_SQL_TYPES to true
      sqlSupportMock.when(() -> SqlTypesSupport.SUPPORTS_SQL_TYPES).thenReturn(true);

      // Mock factory creation for DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(String)
      TypeAdapterFactory dateAdapterFactoryMock = mock(TypeAdapterFactory.class);
      dateTypeMock.when(() -> DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(datePattern))
          .thenReturn(dateAdapterFactoryMock);

      // Mock factory creation for SqlTypesSupport.TIMESTAMP_DATE_TYPE.createAdapterFactory(String)
      TypeAdapterFactory timestampAdapterFactoryMock = mock(TypeAdapterFactory.class);
      TypeAdapterFactory dateSqlAdapterFactoryMock = mock(TypeAdapterFactory.class);

      sqlSupportMock.when(() -> SqlTypesSupport.TIMESTAMP_DATE_TYPE.createAdapterFactory(datePattern))
          .thenReturn(timestampAdapterFactoryMock);
      sqlSupportMock.when(() -> SqlTypesSupport.DATE_DATE_TYPE.createAdapterFactory(datePattern))
          .thenReturn(dateSqlAdapterFactoryMock);

      // Use reflection to invoke private method
      Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
      method.setAccessible(true);
      method.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

      // Verify factories added
      assertEquals(3, factories.size());
      assertTrue(factories.contains(dateAdapterFactoryMock));
      assertTrue(factories.contains(timestampAdapterFactoryMock));
      assertTrue(factories.contains(dateSqlAdapterFactoryMock));
    }
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withValidDateAndTimeStyle_addsFactoriesIncludingSql() throws Exception {
    String datePattern = null;
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.MEDIUM;

    try (MockedStatic<SqlTypesSupport> sqlSupportMock = Mockito.mockStatic(SqlTypesSupport.class);
         MockedStatic<DefaultDateTypeAdapter.DateType> dateTypeMock = Mockito.mockStatic(DefaultDateTypeAdapter.DateType.class)) {

      // Mock SqlTypesSupport.SUPPORTS_SQL_TYPES to true
      sqlSupportMock.when(() -> SqlTypesSupport.SUPPORTS_SQL_TYPES).thenReturn(true);

      // Mock factory creation for DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(int,int)
      TypeAdapterFactory dateAdapterFactoryMock = mock(TypeAdapterFactory.class);
      dateTypeMock.when(() -> DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(dateStyle, timeStyle))
          .thenReturn(dateAdapterFactoryMock);

      // Mock factory creation for SqlTypesSupport.TIMESTAMP_DATE_TYPE.createAdapterFactory(int,int)
      TypeAdapterFactory timestampAdapterFactoryMock = mock(TypeAdapterFactory.class);
      TypeAdapterFactory dateSqlAdapterFactoryMock = mock(TypeAdapterFactory.class);

      sqlSupportMock.when(() -> SqlTypesSupport.TIMESTAMP_DATE_TYPE.createAdapterFactory(dateStyle, timeStyle))
          .thenReturn(timestampAdapterFactoryMock);
      sqlSupportMock.when(() -> SqlTypesSupport.DATE_DATE_TYPE.createAdapterFactory(dateStyle, timeStyle))
          .thenReturn(dateSqlAdapterFactoryMock);

      // Use reflection to invoke private method
      Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
      method.setAccessible(true);
      method.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

      // Verify factories added
      assertEquals(3, factories.size());
      assertTrue(factories.contains(dateAdapterFactoryMock));
      assertTrue(factories.contains(timestampAdapterFactoryMock));
      assertTrue(factories.contains(dateSqlAdapterFactoryMock));
    }
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withEmptyDatePatternAndDefaultStyles_doesNotAddFactories() throws Exception {
    String datePattern = "   ";
    int dateStyle = DateFormat.DEFAULT;
    int timeStyle = DateFormat.DEFAULT;

    // Use reflection to invoke private method
    Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);
    method.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

    // Should not add any factories
    assertTrue(factories.isEmpty());
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withNullDatePatternAndDefaultStyles_doesNotAddFactories() throws Exception {
    String datePattern = null;
    int dateStyle = DateFormat.DEFAULT;
    int timeStyle = DateFormat.DEFAULT;

    // Use reflection to invoke private method
    Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);
    method.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

    // Should not add any factories
    assertTrue(factories.isEmpty());
  }

  @Test
    @Timeout(8000)
  void addTypeAdaptersForDate_withSqlTypesNotSupported_addsOnlyDateAdapterFactory() throws Exception {
    String datePattern = "yyyy-MM-dd";
    int dateStyle = DateFormat.DEFAULT;
    int timeStyle = DateFormat.DEFAULT;

    try (MockedStatic<SqlTypesSupport> sqlSupportMock = Mockito.mockStatic(SqlTypesSupport.class);
         MockedStatic<DefaultDateTypeAdapter.DateType> dateTypeMock = Mockito.mockStatic(DefaultDateTypeAdapter.DateType.class)) {

      // Mock SqlTypesSupport.SUPPORTS_SQL_TYPES to false
      sqlSupportMock.when(() -> SqlTypesSupport.SUPPORTS_SQL_TYPES).thenReturn(false);

      // Mock factory creation for DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(String)
      TypeAdapterFactory dateAdapterFactoryMock = mock(TypeAdapterFactory.class);
      dateTypeMock.when(() -> DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(datePattern))
          .thenReturn(dateAdapterFactoryMock);

      // Use reflection to invoke private method
      Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
      method.setAccessible(true);
      method.invoke(gsonBuilder, datePattern, dateStyle, timeStyle, factories);

      // Verify only dateAdapterFactory added
      assertEquals(1, factories.size());
      assertTrue(factories.contains(dateAdapterFactoryMock));
    }
  }
}