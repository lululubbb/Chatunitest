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
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

class GsonBuilder_2_4Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testConstructor_withGsonCopiesAllFields() throws Exception {
    // Arrange
    Gson gsonMock = mock(Gson.class);
    Excluder excluder = Excluder.DEFAULT;
    FieldNamingStrategy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
    LinkedList<ReflectionAccessFilter> reflectionFilters = new LinkedList<>();
    reflectionFilters.add(mock(ReflectionAccessFilter.class));

    // Because fields are public or package-private, we cannot stub fields with Mockito.
    // Instead, use a spy or a real instance with reflection to set fields.
    // We'll create a real Gson instance and set fields by reflection.

    Gson realGson = mock(Gson.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));

    // Set fields on the realGson instance via reflection
    setField(realGson, "excluder", excluder);
    setField(realGson, "fieldNamingStrategy", fieldNamingStrategy);
    setField(realGson, "serializeNulls", true);
    setField(realGson, "complexMapKeySerialization", true);
    setField(realGson, "generateNonExecutableJson", true);
    setField(realGson, "htmlSafe", true);
    setField(realGson, "prettyPrinting", true);
    setField(realGson, "lenient", true);
    setField(realGson, "serializeSpecialFloatingPointValues", true);
    setField(realGson, "longSerializationPolicy", LongSerializationPolicy.DEFAULT);
    setField(realGson, "datePattern", "datePattern");
    setField(realGson, "dateStyle", DateFormat.SHORT);
    setField(realGson, "timeStyle", DateFormat.MEDIUM);
    setField(realGson, "builderFactories", List.of(mock(TypeAdapterFactory.class)));
    setField(realGson, "builderHierarchyFactories", List.of(mock(TypeAdapterFactory.class)));
    setField(realGson, "useJdkUnsafe", true);

    Field defaultObjectToNumberStrategyField = getStaticField(Gson.class, "DEFAULT_OBJECT_TO_NUMBER_STRATEGY");
    ToNumberStrategy defaultObjectToNumberStrategy = (ToNumberStrategy) defaultObjectToNumberStrategyField.get(null);

    Field defaultNumberToNumberStrategyField = getStaticField(Gson.class, "DEFAULT_NUMBER_TO_NUMBER_STRATEGY");
    ToNumberStrategy defaultNumberToNumberStrategy = (ToNumberStrategy) defaultNumberToNumberStrategyField.get(null);

    setField(realGson, "objectToNumberStrategy", defaultObjectToNumberStrategy);
    setField(realGson, "numberToNumberStrategy", defaultNumberToNumberStrategy);

    setField(realGson, "reflectionFilters", reflectionFilters);

    setField(realGson, "instanceCreators", new java.util.HashMap<>());

    // Act
    GsonBuilder builder = new GsonBuilder(realGson);

    // Assert
    assertFieldEquals(builder, "excluder", excluder);
    assertFieldEquals(builder, "fieldNamingPolicy", fieldNamingStrategy);
    assertFieldEquals(builder, "serializeNulls", true);
    assertFieldEquals(builder, "complexMapKeySerialization", true);
    assertFieldEquals(builder, "generateNonExecutableJson", true);
    assertFieldEquals(builder, "escapeHtmlChars", true);
    assertFieldEquals(builder, "prettyPrinting", true);
    assertFieldEquals(builder, "lenient", true);
    assertFieldEquals(builder, "serializeSpecialFloatingPointValues", true);
    assertFieldEquals(builder, "longSerializationPolicy", LongSerializationPolicy.DEFAULT);
    assertFieldEquals(builder, "datePattern", "datePattern");
    assertFieldEquals(builder, "dateStyle", DateFormat.SHORT);
    assertFieldEquals(builder, "timeStyle", DateFormat.MEDIUM);

    List<?> factories = (List<?>) getField(builder, "factories");
    assertEquals(1, factories.size());

    List<?> hierarchyFactories = (List<?>) getField(builder, "hierarchyFactories");
    assertEquals(1, hierarchyFactories.size());

    assertFieldEquals(builder, "useJdkUnsafe", true);
    assertFieldEquals(builder, "objectToNumberStrategy", defaultObjectToNumberStrategy);
    assertFieldEquals(builder, "numberToNumberStrategy", defaultNumberToNumberStrategy);

    LinkedList<?> reflectionFiltersList = (LinkedList<?>) getField(builder, "reflectionFilters");
    assertEquals(1, reflectionFiltersList.size());
  }

  private static Field getStaticField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        Field field = current.getDeclaredField(fieldName);
        if ((field.getModifiers() & java.lang.reflect.Modifier.STATIC) != 0) {
          field.setAccessible(true);
          return field;
        }
      } catch (NoSuchFieldException e) {
        // continue search in superclass
      }
      current = current.getSuperclass();
    }
    throw new NoSuchFieldException(fieldName);
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = getField(target.getClass(), fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static Object getField(Object target, String fieldName) throws Exception {
    Field field = getField(target.getClass(), fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private static Field getField(Class<?> clazz, String fieldName) throws NoSuchFieldException {
    Class<?> current = clazz;
    while (current != null) {
      try {
        return current.getDeclaredField(fieldName);
      } catch (NoSuchFieldException e) {
        current = current.getSuperclass();
      }
    }
    throw new NoSuchFieldException(fieldName);
  }

  private static void assertFieldEquals(Object target, String fieldName, Object expected) throws Exception {
    Object actual = getField(target, fieldName);
    if (expected instanceof Boolean) {
      assertEquals(expected, actual);
    } else {
      assertSame(expected, actual);
    }
  }
}