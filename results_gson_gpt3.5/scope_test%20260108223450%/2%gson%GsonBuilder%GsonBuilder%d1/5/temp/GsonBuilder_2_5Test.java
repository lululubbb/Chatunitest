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
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.LinkedList;
import java.util.List;

class GsonBuilder_2_5Test {

  private GsonBuilder gsonBuilder;

  @BeforeEach
  void setUp() {
    gsonBuilder = new GsonBuilder();
  }

  @Test
    @Timeout(8000)
  void testConstructor_withGsonCopiesAllFields() throws Exception {
    // Create a real Gson instance and set fields via reflection
    Gson gson = new Gson();

    // Setup fields on gson via reflection
    setField(gson, "excluder", Excluder.DEFAULT);
    setField(gson, "fieldNamingStrategy", FieldNamingPolicy.IDENTITY);
    setField(gson, "serializeNulls", true);
    setField(gson, "complexMapKeySerialization", true);
    setField(gson, "generateNonExecutableJson", true);
    setField(gson, "htmlSafe", true);
    setField(gson, "prettyPrinting", true);
    setField(gson, "lenient", true);
    setField(gson, "serializeSpecialFloatingPointValues", true);
    setField(gson, "longSerializationPolicy", LongSerializationPolicy.STRING);
    setField(gson, "datePattern", "yyyy-MM-dd");
    setField(gson, "dateStyle", DateFormat.SHORT);
    setField(gson, "timeStyle", DateFormat.MEDIUM);
    setField(gson, "useJdkUnsafe", true);
    setField(gson, "objectToNumberStrategy", ToNumberPolicy.LAZILY_PARSED_NUMBER);
    setField(gson, "numberToNumberStrategy", ToNumberPolicy.LONG_OR_DOUBLE);

    List<TypeAdapterFactory> builderFactories = List.of(TypeAdapters.STRING_FACTORY);
    List<TypeAdapterFactory> builderHierarchyFactories = List.of(TypeAdapters.INTEGER_FACTORY);
    setField(gson, "builderFactories", builderFactories);
    setField(gson, "builderHierarchyFactories", builderHierarchyFactories);

    LinkedList<ReflectionAccessFilter> reflectionFilters = new LinkedList<>();
    setField(gson, "reflectionFilters", reflectionFilters);

    GsonBuilder builder = new GsonBuilder(gson);

    assertSame(Excluder.DEFAULT, getField(builder, "excluder"));
    assertEquals(FieldNamingPolicy.IDENTITY, getField(builder, "fieldNamingPolicy"));
    assertTrue((Boolean) getField(builder, "serializeNulls"));
    assertTrue((Boolean) getField(builder, "complexMapKeySerialization"));
    assertTrue((Boolean) getField(builder, "generateNonExecutableJson"));
    assertTrue((Boolean) getField(builder, "escapeHtmlChars"));
    assertTrue((Boolean) getField(builder, "prettyPrinting"));
    assertTrue((Boolean) getField(builder, "lenient"));
    assertTrue((Boolean) getField(builder, "serializeSpecialFloatingPointValues"));
    assertEquals(LongSerializationPolicy.STRING, getField(builder, "longSerializationPolicy"));
    assertEquals("yyyy-MM-dd", getField(builder, "datePattern"));
    assertEquals(DateFormat.SHORT, getField(builder, "dateStyle"));
    assertEquals(DateFormat.MEDIUM, getField(builder, "timeStyle"));
    assertTrue((Boolean) getField(builder, "useJdkUnsafe"));
    assertEquals(ToNumberPolicy.LAZILY_PARSED_NUMBER, getField(builder, "objectToNumberStrategy"));
    assertEquals(ToNumberPolicy.LONG_OR_DOUBLE, getField(builder, "numberToNumberStrategy"));

    List<?> factories = (List<?>) getField(builder, "factories");
    assertTrue(factories.containsAll(builderFactories));

    List<?> hierarchyFactories = (List<?>) getField(builder, "hierarchyFactories");
    assertTrue(hierarchyFactories.containsAll(builderHierarchyFactories));

    LinkedList<?> reflectionFiltersBuilder = (LinkedList<?>) getField(builder, "reflectionFilters");
    assertTrue(reflectionFiltersBuilder.containsAll(reflectionFilters));
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  private static Object getField(Object target, String fieldName) throws Exception {
    Field field = getFieldFromClassHierarchy(target.getClass(), fieldName);
    field.setAccessible(true);
    return field.get(target);
  }

  private static Field getFieldFromClassHierarchy(Class<?> clazz, String fieldName) throws NoSuchFieldException {
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
}