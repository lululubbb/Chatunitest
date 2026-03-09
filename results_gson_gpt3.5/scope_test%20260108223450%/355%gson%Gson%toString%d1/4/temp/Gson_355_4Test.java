package com.google.gson;
import org.junit.jupiter.api.Timeout;
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

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.Excluder;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.ToNumberStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ToNumberPolicy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class GsonToStringTest {

  private Gson gson;

  @BeforeEach
  public void setUp() throws Exception {
    // Prepare constructorConstructor with required parameters
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        Collections.emptyMap(),
        false,
        Collections.emptyList()
    );

    // Prepare jsonAdapterFactory (not used in constructor, can be null)
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory = null;

    // Prepare excluder
    Excluder excluder = Excluder.DEFAULT;

    // Prepare fieldNamingStrategy
    FieldNamingStrategy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;

    // Prepare instanceCreators empty map
    Map<Type, Object> instanceCreators = Collections.emptyMap();

    // Prepare builderFactories and builderHierarchyFactories
    List<TypeAdapterFactory> builderFactories = Collections.emptyList();
    List<TypeAdapterFactory> builderHierarchyFactories = Collections.emptyList();
    List<TypeAdapterFactory> factoriesToBeAdded = Collections.emptyList();

    // Prepare factories list (empty)
    List<TypeAdapterFactory> factories = Collections.emptyList();

    // Prepare reflectionFilters
    List<ReflectionAccessFilter> reflectionFilters = Collections.emptyList();

    // Create Gson instance with all fields
    gson = new Gson(
        excluder,
        fieldNamingStrategy,
        Collections.emptyMap(),
        false, // serializeNulls
        false, // complexMapKeySerialization
        false, // generateNonExecutableGson
        true,  // htmlSafe
        false, // prettyPrinting
        false, // lenient
        false, // serializeSpecialFloatingPointValues
        true,  // useJdkUnsafe
        LongSerializationPolicy.DEFAULT,
        null, // datePattern
        2, // dateStyle
        2, // timeStyle
        builderFactories,
        builderHierarchyFactories,
        factoriesToBeAdded,
        ToNumberPolicy.DOUBLE,
        ToNumberPolicy.LAZILY_PARSED_NUMBER,
        reflectionFilters
    );

    // Use reflection to set private final fields constructorConstructor and factories
    setFinalField(gson, "constructorConstructor", constructorConstructor);
    setFinalField(gson, "factories", factories);
    setFinalField(gson, "serializeNulls", false);
  }

  @Test
    @Timeout(8000)
  public void testToString_containsExpectedFields() {
    String str = gson.toString();
    assertTrue(str.contains("serializeNulls:false"));
    assertTrue(str.contains("factories:[]"));
    assertTrue(str.contains("instanceCreators=") || str.contains("instanceCreators@"));
  }

  private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
    Field field = null;
    Class<?> clazz = target.getClass();
    while (clazz != null) {
      try {
        field = clazz.getDeclaredField(fieldName);
        break;
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    if (field == null) {
      throw new NoSuchFieldException(fieldName);
    }
    field.setAccessible(true);
    field.set(target, value);
  }
}