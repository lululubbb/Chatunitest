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

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.InstanceCreator;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ToNumberPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapterFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonToStringTest {

  private Gson gson;

  @BeforeEach
  void setUp() throws Exception {
    // Use the public static method GsonBuilder.DEFAULT_EXCLUDER to get Excluder instance
    Excluder excluder = Excluder.DEFAULT;

    FieldNamingStrategy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
    Map<Type, InstanceCreator<?>> instanceCreators = Collections.emptyMap();
    boolean serializeNulls = true;
    boolean complexMapKeySerialization = true;
    boolean generateNonExecutableGson = true;
    boolean htmlSafe = true;
    boolean prettyPrinting = true;
    boolean lenient = true;
    boolean serializeSpecialFloatingPointValues = true;
    boolean useJdkUnsafe = true;
    LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;
    String datePattern = "yyyy-MM-dd";
    int dateStyle = 1;
    int timeStyle = 2;
    List<TypeAdapterFactory> builderFactories = Collections.emptyList();
    List<TypeAdapterFactory> builderHierarchyFactories = Collections.emptyList();
    List<TypeAdapterFactory> factoriesToBeAdded = Collections.emptyList();
    ToNumberStrategy objectToNumberStrategy = ToNumberPolicy.DOUBLE;
    ToNumberStrategy numberToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    List<ReflectionAccessFilter> reflectionFilters = Collections.emptyList();

    ConstructorConstructor constructorConstructor = new ConstructorConstructor(instanceCreators, true, reflectionFilters);

    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    gson = new Gson(
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
        reflectionFilters);

    setField(gson, "constructorConstructor", constructorConstructor);
    setField(gson, "jsonAdapterFactory", jsonAdapterFactory);
    setField(gson, "factories", Collections.singletonList(jsonAdapterFactory));
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }

  @Test
    @Timeout(8000)
  void testToString_containsSerializeNullsFactoriesAndConstructorConstructor() {
    String result = gson.toString();
    assertTrue(result.contains("serializeNulls:true"), "toString should contain serializeNulls:true");
    assertTrue(result.contains("factories=["), "toString should contain factories list");
    assertTrue(result.contains("instanceCreators="), "toString should contain instanceCreators (constructorConstructor)");
  }
}