package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonToken;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

class GsonConstructorTest {

  private Excluder excluder;
  private FieldNamingStrategy fieldNamingStrategy;
  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<TypeAdapterFactory> builderFactories;
  private List<TypeAdapterFactory> builderHierarchyFactories;
  private List<TypeAdapterFactory> factoriesToBeAdded;
  private List<ReflectionAccessFilter> reflectionFilters;
  private ToNumberStrategy objectToNumberStrategy;
  private ToNumberStrategy numberToNumberStrategy;
  private LongSerializationPolicy longSerializationPolicy;

  @BeforeEach
  void setUp() {
    excluder = mock(Excluder.class);
    fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
    instanceCreators = new HashMap<>();
    builderFactories = new ArrayList<>();
    builderHierarchyFactories = new ArrayList<>();
    factoriesToBeAdded = new ArrayList<>();
    reflectionFilters = new ArrayList<>();
    objectToNumberStrategy = ToNumberPolicy.DOUBLE;
    numberToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    longSerializationPolicy = LongSerializationPolicy.DEFAULT;
  }

  @Test
    @Timeout(8000)
  void testGsonConstructor_DefaultsAndFactories() throws Exception {
    Gson gson = new Gson(
        excluder,
        fieldNamingStrategy,
        instanceCreators,
        true, // serializeNulls
        true, // complexMapKeySerialization
        true, // generateNonExecutableGson
        false, // htmlSafe
        true, // prettyPrinting
        true, // lenient
        true, // serializeSpecialFloatingPointValues
        false, // useJdkUnsafe
        longSerializationPolicy,
        "yyyy-MM-dd",
        DateFormat.SHORT,
        DateFormat.MEDIUM,
        builderFactories,
        builderHierarchyFactories,
        factoriesToBeAdded,
        objectToNumberStrategy,
        numberToNumberStrategy,
        reflectionFilters);

    // Validate fields set correctly
    assertSame(excluder, getField(gson, "excluder"));
    assertSame(fieldNamingStrategy, getField(gson, "fieldNamingStrategy"));
    assertSame(instanceCreators, getField(gson, "instanceCreators"));
    assertEquals(true, getField(gson, "serializeNulls"));
    assertEquals(true, getField(gson, "complexMapKeySerialization"));
    assertEquals(true, getField(gson, "generateNonExecutableJson"));
    assertEquals(false, getField(gson, "htmlSafe"));
    assertEquals(true, getField(gson, "prettyPrinting"));
    assertEquals(true, getField(gson, "lenient"));
    assertEquals(true, getField(gson, "serializeSpecialFloatingPointValues"));
    assertEquals(false, getField(gson, "useJdkUnsafe"));
    assertEquals(longSerializationPolicy, getField(gson, "longSerializationPolicy"));
    assertEquals("yyyy-MM-dd", getField(gson, "datePattern"));
    assertEquals(DateFormat.SHORT, getField(gson, "dateStyle"));
    assertEquals(DateFormat.MEDIUM, getField(gson, "timeStyle"));
    assertSame(builderFactories, getField(gson, "builderFactories"));
    assertSame(builderHierarchyFactories, getField(gson, "builderHierarchyFactories"));
    assertSame(objectToNumberStrategy, getField(gson, "objectToNumberStrategy"));
    assertSame(numberToNumberStrategy, getField(gson, "numberToNumberStrategy"));
    assertSame(reflectionFilters, getField(gson, "reflectionFilters"));

    // Validate constructorConstructor is not null
    Object constructorConstructor = getField(gson, "constructorConstructor");
    assertNotNull(constructorConstructor);

    // Validate jsonAdapterFactory is not null
    Object jsonAdapterFactory = getField(gson, "jsonAdapterFactory");
    assertNotNull(jsonAdapterFactory);

    // Validate factories list is unmodifiable and contains expected adapters
    List<?> factories = (List<?>) getField(gson, "factories");
    assertNotNull(factories);
    assertTrue(factories.contains(TypeAdapters.JSON_ELEMENT_FACTORY));
    assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("Excluder")));
    assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("CollectionTypeAdapterFactory")));
    assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("MapTypeAdapterFactory")));
    assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("ReflectiveTypeAdapterFactory")));
    assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("JsonAdapterAnnotationTypeAdapterFactory")));

    // Validate that the list is unmodifiable
    assertThrows(UnsupportedOperationException.class, () -> factories.add(null));
  }

  @Test
    @Timeout(8000)
  void testGsonConstructor_WithEmptyFactoriesToBeAdded() throws Exception {
    // factoriesToBeAdded is empty, ensure no NPE and defaults are set
    Gson gson = new Gson(
        excluder,
        fieldNamingStrategy,
        instanceCreators,
        false,
        false,
        false,
        true,
        false,
        false,
        false,
        true,
        LongSerializationPolicy.DEFAULT,
        null,
        DateFormat.DEFAULT,
        DateFormat.DEFAULT,
        builderFactories,
        builderHierarchyFactories,
        Collections.emptyList(),
        objectToNumberStrategy,
        numberToNumberStrategy,
        reflectionFilters);

    List<?> factories = (List<?>) getField(gson, "factories");
    assertNotNull(factories);
    assertTrue(factories.contains(TypeAdapters.JSON_ELEMENT_FACTORY));
  }

  @Test
    @Timeout(8000)
  void testGsonConstructor_ReflectionFiltersNotNull() throws Exception {
    List<ReflectionAccessFilter> filters = new ArrayList<>();
    filters.add(mock(ReflectionAccessFilter.class));
    Gson gson = new Gson(
        excluder,
        fieldNamingStrategy,
        instanceCreators,
        false,
        false,
        false,
        true,
        false,
        false,
        false,
        true,
        LongSerializationPolicy.DEFAULT,
        null,
        DateFormat.DEFAULT,
        DateFormat.DEFAULT,
        builderFactories,
        builderHierarchyFactories,
        factoriesToBeAdded,
        objectToNumberStrategy,
        numberToNumberStrategy,
        filters);

    List<?> factories = (List<?>) getField(gson, "factories");
    assertNotNull(factories);
    // Ensure ReflectiveTypeAdapterFactory is present and constructed with filters
    boolean reflectiveFound = factories.stream()
        .anyMatch(f -> f.getClass().getSimpleName().equals("ReflectiveTypeAdapterFactory"));
    assertTrue(reflectiveFound);
  }

  // Helper to access private fields via reflection
  private Object getField(Object instance, String fieldName) throws Exception {
    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }
}