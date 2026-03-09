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
import com.google.gson.FieldNamingPolicy;
import com.google.gson.reflect.TypeToken;
import com.google.gson.LongSerializationPolicy;
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
    // Prepare constructorConstructor instance with required parameters
    ConstructorConstructor constructorConstructor = new ConstructorConstructor(
        Collections.emptyMap()
    );

    // Prepare jsonAdapterFactory instance
    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory = new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    // Prepare other dependencies
    Excluder excluder = Excluder.DEFAULT;
    FieldNamingPolicy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
    Map<Type, InstanceCreator<?>> instanceCreators = Collections.emptyMap();
    boolean serializeNulls = true;
    boolean complexMapKeySerialization = false;
    boolean generateNonExecutableJson = false;
    boolean htmlSafe = true;
    boolean prettyPrinting = false;
    boolean lenient = false;
    boolean serializeSpecialFloatingPointValues = false;
    boolean useJdkUnsafe = true;
    String datePattern = null;
    int dateStyle = 0;
    int timeStyle = 0;
    List<TypeAdapterFactory> builderFactories = Collections.emptyList();
    List<TypeAdapterFactory> builderHierarchyFactories = Collections.emptyList();
    List<TypeAdapterFactory> factoriesToBeAdded = Collections.emptyList();
    ToNumberStrategy objectToNumberStrategy = ToNumberPolicy.DOUBLE;
    ToNumberStrategy numberToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
    List<ReflectionAccessFilter> reflectionFilters = Collections.emptyList();

    gson = new Gson(
        excluder,
        fieldNamingStrategy,
        instanceCreators,
        serializeNulls,
        complexMapKeySerialization,
        generateNonExecutableJson,
        htmlSafe,
        prettyPrinting,
        lenient,
        serializeSpecialFloatingPointValues,
        useJdkUnsafe,
        LongSerializationPolicy.DEFAULT,
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

    // Using reflection to set private final fields that are not set by constructor
    setField(gson, "constructorConstructor", constructorConstructor);
    setField(gson, "jsonAdapterFactory", jsonAdapterFactory);
    // The field 'factories' is final and set in constructor as 'factoriesToBeAdded' + others internally,
    // but for test we set it to a non-empty list to ensure toString contains it
    setField(gson, "factories", Collections.singletonList(jsonAdapterFactory));
  }

  private void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    field.set(target, value);
  }

  @Test
    @Timeout(8000)
  public void testToString_containsSerializeNulls() {
    String result = gson.toString();
    assertTrue(result.contains("serializeNulls:true"));
  }

  @Test
    @Timeout(8000)
  public void testToString_containsFactories() {
    String result = gson.toString();
    assertTrue(result.contains("factories="));
  }

  @Test
    @Timeout(8000)
  public void testToString_containsConstructorConstructor() {
    String result = gson.toString();
    assertTrue(result.contains("instanceCreators=") || result.contains("constructorConstructor=") || result.contains("constructorConstructor"));
  }
}