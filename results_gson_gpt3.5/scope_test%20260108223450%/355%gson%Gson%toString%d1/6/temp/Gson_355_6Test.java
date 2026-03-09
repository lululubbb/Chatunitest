package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
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
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.InstanceCreator;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class GsonToStringTest {

  private Gson gson;

  @BeforeEach
  public void setUp() throws Exception {
    // Prepare dependencies for Gson constructor
    Excluder excluder = Excluder.DEFAULT;
    FieldNamingStrategy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
    Map<Type, InstanceCreator<?>> instanceCreators = Collections.emptyMap();
    boolean serializeNulls = true;
    boolean complexMapKeySerialization = false;
    boolean generateNonExecutableGson = false;
    boolean htmlSafe = true;
    boolean prettyPrinting = false;
    boolean lenient = false;
    boolean serializeSpecialFloatingPointValues = false;
    boolean useJdkUnsafe = true;
    LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;
    String datePattern = null;
    int dateStyle = 0;
    int timeStyle = 0;
    List<TypeAdapterFactory> builderFactories = Collections.emptyList();
    List<TypeAdapterFactory> builderHierarchyFactories = Collections.emptyList();
    List<TypeAdapterFactory> factoriesToBeAdded = Collections.emptyList();

    // Use reflection to get ToNumberStrategy default values from Gson class
    Class<?> gsonClass = Gson.class;
    Object objectToNumberStrategy = null;
    Object numberToNumberStrategy = null;
    Class<?> toNumberStrategyClass = null;
    try {
      toNumberStrategyClass = Class.forName("com.google.gson.ToNumberStrategy");
    } catch (ClassNotFoundException e) {
      toNumberStrategyClass = null;
    }
    try {
      Field defaultObjectToNumberStrategy = gsonClass.getDeclaredField("DEFAULT_OBJECT_TO_NUMBER_STRATEGY");
      defaultObjectToNumberStrategy.setAccessible(true);
      objectToNumberStrategy = defaultObjectToNumberStrategy.get(null);
      Field defaultNumberToNumberStrategy = gsonClass.getDeclaredField("DEFAULT_NUMBER_TO_NUMBER_STRATEGY");
      defaultNumberToNumberStrategy.setAccessible(true);
      numberToNumberStrategy = defaultNumberToNumberStrategy.get(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      // fallback to null if not found
      objectToNumberStrategy = null;
      numberToNumberStrategy = null;
    }

    // Use reflection to load ReflectionAccessFilter class and create empty list of that type
    Class<?> reflectionAccessFilterClass = null;
    try {
      reflectionAccessFilterClass = Class.forName("com.google.gson.ReflectionAccessFilter");
    } catch (ClassNotFoundException e) {
      reflectionAccessFilterClass = null;
    }
    List<?> reflectionFilters;
    if (reflectionAccessFilterClass != null) {
      reflectionFilters = Collections.emptyList();
    } else {
      reflectionFilters = Collections.emptyList();
    }

    // Use reflection to invoke the constructor with all parameters
    // If toNumberStrategyClass is null, pass Object.class as fallback
    Class<?> toNumberStrategyParamClass = toNumberStrategyClass != null ? toNumberStrategyClass : Object.class;

    gson = (Gson) gsonClass.getDeclaredConstructor(
        Excluder.class,
        FieldNamingStrategy.class,
        Map.class,
        boolean.class,
        boolean.class,
        boolean.class,
        boolean.class,
        boolean.class,
        boolean.class,
        boolean.class,
        boolean.class,
        LongSerializationPolicy.class,
        String.class,
        int.class,
        int.class,
        List.class,
        List.class,
        List.class,
        toNumberStrategyParamClass,
        toNumberStrategyParamClass,
        List.class
    ).newInstance(
        excluder,
        fieldNamingStrategy,
        instanceCreators,
        serializeNulls,
        complexMapKeySerialization,
        generateNonExecutableGson,
        htmlSafe,
        prettyPrinting,
        lenient,
        serializeSpecialFloatingPointValues,
        useJdkUnsafe,
        longSerializationPolicy,
        datePattern,
        dateStyle,
        timeStyle,
        builderFactories,
        builderHierarchyFactories,
        factoriesToBeAdded,
        objectToNumberStrategy,
        numberToNumberStrategy,
        reflectionFilters
    );

    // Set the private final fields factories and constructorConstructor via reflection
    Field factoriesField = gsonClass.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    factoriesField.set(gson, factoriesToBeAdded);

    Field constructorConstructorField = gsonClass.getDeclaredField("constructorConstructor");
    constructorConstructorField.setAccessible(true);
    constructorConstructorField.set(gson, new ConstructorConstructor(instanceCreators, false, reflectionFilters));
  }

  @Test
    @Timeout(8000)
  public void testToString() throws Exception {
    // Access constructorConstructor field value for expected string
    Field constructorConstructorField = Gson.class.getDeclaredField("constructorConstructor");
    constructorConstructorField.setAccessible(true);
    Object constructorConstructorValue = constructorConstructorField.get(gson);

    String expected = "{serializeNulls:true,factories:[],instanceCreators:" + constructorConstructorValue + "}";
    String actual = gson.toString();
    assertEquals(expected, actual);
  }
}