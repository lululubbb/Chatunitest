package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
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
import com.google.gson.reflect.TypeToken;
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
import java.lang.reflect.Type;
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

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class GsonFieldNamingStrategyTest {

  @Test
    @Timeout(8000)
  void testFieldNamingStrategy_Default() {
    Gson gson = new Gson();
    FieldNamingStrategy strategy = gson.fieldNamingStrategy();
    assertNotNull(strategy);
    assertEquals(FieldNamingPolicy.IDENTITY, strategy);
  }

  @Test
    @Timeout(8000)
  void testFieldNamingStrategy_CustomStrategy() throws Exception {
    FieldNamingStrategy customStrategy = field -> "customName";
    Gson gson = createGsonWithFieldNamingStrategy(customStrategy);
    FieldNamingStrategy strategy = gson.fieldNamingStrategy();
    assertNotNull(strategy);
    assertSame(customStrategy, strategy);
  }

  private Gson createGsonWithFieldNamingStrategy(FieldNamingStrategy strategy) throws Exception {
    Class<Gson> gsonClass = Gson.class;

    Constructor<Gson> constructor = gsonClass.getDeclaredConstructor(
        com.google.gson.internal.Excluder.class,
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
        com.google.gson.ToNumberStrategy.class,
        com.google.gson.ToNumberStrategy.class,
        List.class
    );
    constructor.setAccessible(true);
    return constructor.newInstance(
        com.google.gson.internal.Excluder.DEFAULT,
        strategy,
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
        null,  // datePattern
        DateFormat.DEFAULT,
        DateFormat.DEFAULT,
        Collections.emptyList(),
        Collections.emptyList(),
        Collections.emptyList(),
        null,  // objectToNumberStrategy
        null,  // numberToNumberStrategy
        Collections.emptyList()
    );
  }
}