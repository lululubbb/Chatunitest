package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.internal.bind.TypeAdapterRuntimeTypeWrapper;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.TypeAdapters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

class GsonConstructorTest {

  private Excluder excluder;
  private FieldNamingStrategy fieldNamingStrategy;
  private Map<Type, InstanceCreator<?>> instanceCreators;
  private List<TypeAdapterFactory> builderFactories;
  private List<TypeAdapterFactory> builderHierarchyFactories;
  private List<TypeAdapterFactory> factoriesToBeAdded;
  private ToNumberStrategy objectToNumberStrategy;
  private ToNumberStrategy numberToNumberStrategy;
  private List<ReflectionAccessFilter> reflectionFilters;

  @BeforeEach
  void setUp() {
    excluder = mock(Excluder.class);
    fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
    instanceCreators = new HashMap<>();
    builderFactories = new ArrayList<>();
    builderHierarchyFactories = new ArrayList<>();
    factoriesToBeAdded = new ArrayList<>();
    objectToNumberStrategy = ToNumberPolicy.DOUBLE;
    numberToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    reflectionFilters = new ArrayList<>();
  }

  @Test
    @Timeout(8000)
  void testGsonConstructor_DefaultBehavior() throws Exception {
    LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;
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
        longSerializationPolicy,
        null,
        DateFormat.DEFAULT,
        DateFormat.DEFAULT,
        builderFactories,
        builderHierarchyFactories,
        factoriesToBeAdded,
        objectToNumberStrategy,
        numberToNumberStrategy,
        reflectionFilters);

    // Verify fields set correctly
    Field excluderField = Gson.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    assertSame(excluder, excluderField.get(gson));

    Field fieldNamingStrategyField = Gson.class.getDeclaredField("fieldNamingStrategy");
    fieldNamingStrategyField.setAccessible(true);
    assertSame(fieldNamingStrategy, fieldNamingStrategyField.get(gson));

    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    assertFalse((Boolean) serializeNullsField.get(gson));

    Field complexMapKeySerializationField = Gson.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    assertFalse((Boolean) complexMapKeySerializationField.get(gson));

    Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    assertFalse((Boolean) generateNonExecutableJsonField.get(gson));

    Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    assertTrue((Boolean) htmlSafeField.get(gson));

    Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    assertFalse((Boolean) prettyPrintingField.get(gson));

    Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    assertFalse((Boolean) lenientField.get(gson));

    Field serializeSpecialFloatingPointValuesField = Gson.class.getDeclaredField("serializeSpecialFloatingPointValues");
    serializeSpecialFloatingPointValuesField.setAccessible(true);
    assertFalse((Boolean) serializeSpecialFloatingPointValuesField.get(gson));

    Field useJdkUnsafeField = Gson.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    assertTrue((Boolean) useJdkUnsafeField.get(gson));

    Field longSerializationPolicyField = Gson.class.getDeclaredField("longSerializationPolicy");
    longSerializationPolicyField.setAccessible(true);
    assertSame(longSerializationPolicy, longSerializationPolicyField.get(gson));

    Field datePatternField = Gson.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    assertNull(datePatternField.get(gson));

    Field dateStyleField = Gson.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    assertEquals(DateFormat.DEFAULT, dateStyleField.get(gson));

    Field timeStyleField = Gson.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    assertEquals(DateFormat.DEFAULT, timeStyleField.get(gson));

    Field builderFactoriesField = Gson.class.getDeclaredField("builderFactories");
    builderFactoriesField.setAccessible(true);
    assertSame(builderFactories, builderFactoriesField.get(gson));

    Field builderHierarchyFactoriesField = Gson.class.getDeclaredField("builderHierarchyFactories");
    builderHierarchyFactoriesField.setAccessible(true);
    assertSame(builderHierarchyFactories, builderHierarchyFactoriesField.get(gson));

    Field objectToNumberStrategyField = Gson.class.getDeclaredField("objectToNumberStrategy");
    objectToNumberStrategyField.setAccessible(true);
    assertSame(objectToNumberStrategy, objectToNumberStrategyField.get(gson));

    Field numberToNumberStrategyField = Gson.class.getDeclaredField("numberToNumberStrategy");
    numberToNumberStrategyField.setAccessible(true);
    assertSame(numberToNumberStrategy, numberToNumberStrategyField.get(gson));

    Field reflectionFiltersField = Gson.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);
    assertSame(reflectionFilters, reflectionFiltersField.get(gson));

    // factories list is unmodifiable and contains expected factories
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gson);
    assertNotNull(factories);
    assertTrue(factories.contains(TypeAdapters.JSON_ELEMENT_FACTORY));
    assertTrue(factories.contains(ObjectTypeAdapter.getFactory(objectToNumberStrategy)));
    assertTrue(factories.contains(excluder));
    assertTrue(factories.contains(TypeAdapters.STRING_FACTORY));
    assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("CollectionTypeAdapterFactory")));
    assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("MapTypeAdapterFactory")));
    assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("ReflectiveTypeAdapterFactory")));
  }

  @Test
    @Timeout(8000)
  void testGsonConstructor_WithFactoriesToBeAdded() throws Exception {
    TypeAdapterFactory customFactory = mock(TypeAdapterFactory.class);
    factoriesToBeAdded.add(customFactory);

    Gson gson = new Gson(
        excluder,
        fieldNamingStrategy,
        instanceCreators,
        true,
        true,
        true,
        false,
        true,
        true,
        true,
        false,
        LongSerializationPolicy.DEFAULT,
        "yyyy-MM-dd",
        DateFormat.SHORT,
        DateFormat.MEDIUM,
        builderFactories,
        builderHierarchyFactories,
        factoriesToBeAdded,
        objectToNumberStrategy,
        numberToNumberStrategy,
        reflectionFilters);

    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gson);
    assertTrue(factories.contains(customFactory));
  }

  @Test
    @Timeout(8000)
  void testGsonConstructor_ReflectionFilters() throws Exception {
    ReflectionAccessFilter filter = mock(ReflectionAccessFilter.class);
    reflectionFilters.add(filter);

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
        reflectionFilters);

    Field reflectionFiltersField = Gson.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);
    List<ReflectionAccessFilter> filters = (List<ReflectionAccessFilter>) reflectionFiltersField.get(gson);
    assertTrue(filters.contains(filter));
  }
}