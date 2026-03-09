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
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GsonToStringTest {

  private Gson gson;

  @BeforeEach
  void setUp() throws Exception {
    Map<Type, InstanceCreator<?>> instanceCreators = new HashMap<>();

    ConstructorConstructor constructorConstructor =
        new ConstructorConstructor(instanceCreators, false, Collections.emptyList());

    List<TypeAdapterFactory> factories = Collections.emptyList();

    JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory =
        new JsonAdapterAnnotationTypeAdapterFactory(constructorConstructor);

    gson = new Gson(
        new com.google.gson.internal.Excluder(),
        com.google.gson.FieldNamingPolicy.IDENTITY,
        instanceCreators,
        true, // serializeNulls
        false, // complexMapKeySerialization
        false, // generateNonExecutableGson
        true, // htmlSafe
        false, // prettyPrinting
        false, // lenient
        false, // serializeSpecialFloatingPointValues
        true, // useJdkUnsafe
        LongSerializationPolicy.DEFAULT,
        null, // datePattern
        2, // dateStyle
        2, // timeStyle
        Collections.emptyList(), // builderFactories
        Collections.emptyList(), // builderHierarchyFactories
        factories, // factoriesToBeAdded
        com.google.gson.ToNumberPolicy.DOUBLE,
        com.google.gson.ToNumberPolicy.LAZILY_PARSED_NUMBER,
        Collections.emptyList() // reflectionFilters
    );

    setField(gson, "serializeNulls", true);
    setField(gson, "factories", factories);
    setField(gson, "constructorConstructor", constructorConstructor);
    setField(gson, "instanceCreators", instanceCreators);
  }

  @Test
    @Timeout(8000)
  void testToString_containsSerializeNullsFactoriesAndConstructorConstructor() {
    String result = gson.toString();

    String expectedStart = "{serializeNulls:true,factories:";

    assertTrue(result.startsWith(expectedStart));
    assertTrue(result.contains("factories:[]"));
    assertTrue(result.contains("instanceCreators:com.google.gson.internal.ConstructorConstructor"));
  }

  private static void setField(Object target, String fieldName, Object value) throws Exception {
    Field field = Gson.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(target, value);
  }
}