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
import com.google.gson.internal.bind.DefaultDateTypeAdapter.DateType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

class GsonBuilder_31_1Test {

  private GsonBuilder gsonBuilder;
  private List<TypeAdapterFactory> factories;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
    factories = new ArrayList<>();
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withValidDatePattern_andSqlTypesSupported() throws Exception {
    try (MockedStatic<SqlTypesSupport> sqlTypesSupportMockedStatic = Mockito.mockStatic(SqlTypesSupport.class)) {
      sqlTypesSupportMockedStatic.when(() -> SqlTypesSupport.SUPPORTS_SQL_TYPES).thenReturn(true);

      try (MockedStatic<DefaultDateTypeAdapter.DateType> dateTypeMockedStatic = Mockito.mockStatic(DefaultDateTypeAdapter.DateType.class);
           MockedStatic<SqlTypesSupport.TIMESTAMP_DATE_TYPE> tsDateTypeMockedStatic = Mockito.mockStatic(SqlTypesSupport.TIMESTAMP_DATE_TYPE.getClass());
           MockedStatic<SqlTypesSupport.DATE_DATE_TYPE> dateDateTypeMockedStatic = Mockito.mockStatic(SqlTypesSupport.DATE_DATE_TYPE.getClass())) {

        String pattern = "yyyy-MM-dd";

        TypeAdapterFactory dateAdapterFactoryMock = mock(TypeAdapterFactory.class);
        TypeAdapterFactory sqlTimestampAdapterFactoryMock = mock(TypeAdapterFactory.class);
        TypeAdapterFactory sqlDateAdapterFactoryMock = mock(TypeAdapterFactory.class);

        dateTypeMockedStatic.when(() -> DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(pattern))
            .thenReturn(dateAdapterFactoryMock);
        sqlTimestampAdapterFactoryMock = mock(TypeAdapterFactory.class);
        sqlDateAdapterFactoryMock = mock(TypeAdapterFactory.class);

        // We can't mock SqlTypesSupport.TIMESTAMP_DATE_TYPE and DATE_DATE_TYPE fields directly because they are final static.
        // Instead, we mock their createAdapterFactory methods via spy on the objects.
        TypeAdapterFactory tsFactory = mock(TypeAdapterFactory.class);
        TypeAdapterFactory dFactory = mock(TypeAdapterFactory.class);

        // We use spy on the SqlTypesSupport.TIMESTAMP_DATE_TYPE and DATE_DATE_TYPE objects to mock createAdapterFactory
        // But since they are static final fields, we can't spy easily, so we mock them by reflection.
        // Instead, we use reflection to replace these fields temporarily.

        var timestampDateTypeField = SqlTypesSupport.class.getDeclaredField("TIMESTAMP_DATE_TYPE");
        timestampDateTypeField.setAccessible(true);
        var originalTimestampDateType = timestampDateTypeField.get(null);
        var dateDateTypeField = SqlTypesSupport.class.getDeclaredField("DATE_DATE_TYPE");
        dateDateTypeField.setAccessible(true);
        var originalDateDateType = dateDateTypeField.get(null);

        Object timestampDateTypeSpy = Mockito.spy(originalTimestampDateType);
        Object dateDateTypeSpy = Mockito.spy(originalDateDateType);

        // Mock createAdapterFactory(String) methods on spies
        Method createAdapterFactoryStringMethod = timestampDateTypeSpy.getClass().getMethod("createAdapterFactory", String.class);
        Method createAdapterFactoryStringMethodDate = dateDateTypeSpy.getClass().getMethod("createAdapterFactory", String.class);

        Mockito.doReturn(sqlTimestampAdapterFactoryMock).when(timestampDateTypeSpy).createAdapterFactory(pattern);
        Mockito.doReturn(sqlDateAdapterFactoryMock).when(dateDateTypeSpy).createAdapterFactory(pattern);

        // Replace static final fields temporarily
        setFinalStatic(timestampDateTypeField, timestampDateTypeSpy);
        setFinalStatic(dateDateTypeField, dateDateTypeSpy);

        try {
          Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
          method.setAccessible(true);
          method.invoke(gsonBuilder, pattern, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

          assertEquals(3, factories.size());
          assertSame(dateAdapterFactoryMock, factories.get(0));
          assertSame(sqlTimestampAdapterFactoryMock, factories.get(1));
          assertSame(sqlDateAdapterFactoryMock, factories.get(2));
        } finally {
          // Restore original static final fields
          setFinalStatic(timestampDateTypeField, originalTimestampDateType);
          setFinalStatic(dateDateTypeField, originalDateDateType);
        }
      }
    }
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withDatePatternNull_andValidStyles_andSqlTypesSupported() throws Exception {
    try (MockedStatic<SqlTypesSupport> sqlTypesSupportMockedStatic = Mockito.mockStatic(SqlTypesSupport.class)) {
      sqlTypesSupportMockedStatic.when(() -> SqlTypesSupport.SUPPORTS_SQL_TYPES).thenReturn(true);

      try (MockedStatic<DefaultDateTypeAdapter.DateType> dateTypeMockedStatic = Mockito.mockStatic(DefaultDateTypeAdapter.DateType.class)) {
        int dateStyle = DateFormat.SHORT;
        int timeStyle = DateFormat.MEDIUM;

        TypeAdapterFactory dateAdapterFactoryMock = mock(TypeAdapterFactory.class);
        TypeAdapterFactory sqlTimestampAdapterFactoryMock = mock(TypeAdapterFactory.class);
        TypeAdapterFactory sqlDateAdapterFactoryMock = mock(TypeAdapterFactory.class);

        dateTypeMockedStatic.when(() -> DefaultDateTypeAdapter.DateType.DATE.createAdapterFactory(dateStyle, timeStyle))
            .thenReturn(dateAdapterFactoryMock);

        var timestampDateTypeField = SqlTypesSupport.class.getDeclaredField("TIMESTAMP_DATE_TYPE");
        timestampDateTypeField.setAccessible(true);
        var originalTimestampDateType = timestampDateTypeField.get(null);
        var dateDateTypeField = SqlTypesSupport.class.getDeclaredField("DATE_DATE_TYPE");
        dateDateTypeField.setAccessible(true);
        var originalDateDateType = dateDateTypeField.get(null);

        Object timestampDateTypeSpy = Mockito.spy(originalTimestampDateType);
        Object dateDateTypeSpy = Mockito.spy(originalDateDateType);

        Mockito.doReturn(sqlTimestampAdapterFactoryMock).when(timestampDateTypeSpy).createAdapterFactory(dateStyle, timeStyle);
        Mockito.doReturn(sqlDateAdapterFactoryMock).when(dateDateTypeSpy).createAdapterFactory(dateStyle, timeStyle);

        setFinalStatic(timestampDateTypeField, timestampDateTypeSpy);
        setFinalStatic(dateDateTypeField, dateDateTypeSpy);

        try {
          Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
          method.setAccessible(true);
          method.invoke(gsonBuilder, null, dateStyle, timeStyle, factories);

          assertEquals(3, factories.size());
          assertSame(dateAdapterFactoryMock, factories.get(0));
          assertSame(sqlTimestampAdapterFactoryMock, factories.get(1));
          assertSame(sqlDateAdapterFactoryMock, factories.get(2));
        } finally {
          setFinalStatic(timestampDateTypeField, originalTimestampDateType);
          setFinalStatic(dateDateTypeField, originalDateDateType);
        }
      }
    }
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withBlankDatePattern_andDefaultStyles() throws Exception {
    Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);

    method.invoke(gsonBuilder, "  ", DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    // Should not add any factories
    assertTrue(factories.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testAddTypeAdaptersForDate_withNullDatePattern_andDefaultStyles() throws Exception {
    Method method = GsonBuilder.class.getDeclaredMethod("addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
    method.setAccessible(true);

    method.invoke(gsonBuilder, null, DateFormat.DEFAULT, DateFormat.DEFAULT, factories);

    // Should not add any factories
    assertTrue(factories.isEmpty());
  }

  private static void setFinalStatic(java.lang.reflect.Field field, Object newValue) throws Exception {
    field.setAccessible(true);

    java.lang.reflect.Field modifiersField = java.lang.reflect.Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    field.set(null, newValue);
  }
}