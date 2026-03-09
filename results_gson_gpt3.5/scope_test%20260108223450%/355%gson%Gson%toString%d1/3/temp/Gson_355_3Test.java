package com.google.gson;
import org.junit.jupiter.api.Timeout;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;
import com.google.gson.ToNumberStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.TypeAdapterFactory;
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
    Map<Type, com.google.gson.InstanceCreator<?>> instanceCreators = Collections.emptyMap();
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
    int dateStyle = 2;
    int timeStyle = 2;
    List<TypeAdapterFactory> builderFactories = Collections.emptyList();
    List<TypeAdapterFactory> builderHierarchyFactories = Collections.emptyList();
    List<TypeAdapterFactory> factoriesToBeAdded = Collections.emptyList();

    ToNumberStrategy objectToNumberStrategy = com.google.gson.ToNumberPolicy.DOUBLE;
    ToNumberStrategy numberToNumberStrategy = com.google.gson.ToNumberPolicy.LAZILY_PARSED_NUMBER;

    List<ReflectionAccessFilter> reflectionFilters = Collections.emptyList();

    var constructor = Gson.class.getDeclaredConstructor(
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
        ToNumberStrategy.class,
        ToNumberStrategy.class,
        List.class
    );
    constructor.setAccessible(true);
    gson = (Gson) constructor.newInstance(
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

    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    factoriesField.set(gson, Collections.emptyList());

    Field constructorConstructorField = Gson.class.getDeclaredField("constructorConstructor");
    constructorConstructorField.setAccessible(true);
    constructorConstructorField.set(gson, new ConstructorConstructor(instanceCreators, false, reflectionFilters));
  }

  @Test
    @Timeout(8000)
  public void testToString_containsExpectedValues() throws Exception {
    String toStringResult = gson.toString();

    String expectedStart = "{serializeNulls:true,factories:[],instanceCreators:";
    assertTrue(toStringResult.startsWith(expectedStart));
    assertTrue(toStringResult.endsWith("}"));
  }

  @Test
    @Timeout(8000)
  public void testToString_withSerializeNullsFalse() throws Exception {
    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    serializeNullsField.setBoolean(gson, false);

    String toStringResult = gson.toString();

    String expectedStart = "{serializeNulls:false,factories:[],instanceCreators:";
    assertTrue(toStringResult.startsWith(expectedStart));
    assertTrue(toStringResult.endsWith("}"));
  }

  @Test
    @Timeout(8000)
  public void testToString_withNonEmptyFactories() throws Exception {
    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factoriesList = Collections.singletonList(new TypeAdapterFactory() {
      @Override
      public <T> com.google.gson.TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
        return null;
      }
      @Override
      public String toString() {
        return "customFactory";
      }
    });
    factoriesField.set(gson, factoriesList);

    String toStringResult = gson.toString();

    assertTrue(toStringResult.contains("customFactory"));
  }

  @Test
    @Timeout(8000)
  public void testToString_withNullConstructorConstructor() throws Exception {
    Field constructorConstructorField = Gson.class.getDeclaredField("constructorConstructor");
    constructorConstructorField.setAccessible(true);
    constructorConstructorField.set(gson, null);

    String toStringResult = gson.toString();

    assertTrue(toStringResult.contains("instanceCreators:null"));
  }
}