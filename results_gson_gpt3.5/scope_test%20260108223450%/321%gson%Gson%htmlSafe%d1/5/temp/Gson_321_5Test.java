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

class GsonHtmlSafeTest {

  @Test
    @Timeout(8000)
  public void testHtmlSafeTrue() throws Exception {
    Gson gson = createGsonWithHtmlSafe(true);
    assertTrue(gson.htmlSafe());
  }

  @Test
    @Timeout(8000)
  public void testHtmlSafeFalse() throws Exception {
    Gson gson = createGsonWithHtmlSafe(false);
    assertFalse(gson.htmlSafe());
  }

  private Gson createGsonWithHtmlSafe(boolean htmlSafeValue) throws Exception {
    Class<Gson> gsonClass = Gson.class;

    for (Constructor<?> constructor : gsonClass.getDeclaredConstructors()) {
      if (constructor.getParameterCount() == 21) {
        constructor.setAccessible(true);
        Object[] params = new Object[21];

        // Parameters according to constructor signature:
        // 0 Excluder excluder
        // 1 FieldNamingStrategy fieldNamingStrategy
        // 2 Map<Type, InstanceCreator<?>>
        // 3 boolean serializeNulls
        // 4 boolean complexMapKeySerialization
        // 5 boolean generateNonExecutableGson
        // 6 boolean htmlSafe
        // 7 boolean prettyPrinting
        // 8 boolean lenient
        // 9 boolean serializeSpecialFloatingPointValues
        // 10 boolean useJdkUnsafe
        // 11 LongSerializationPolicy longSerializationPolicy
        // 12 String datePattern
        // 13 int dateStyle
        // 14 int timeStyle
        // 15 List<TypeAdapterFactory> builderFactories
        // 16 List<TypeAdapterFactory> builderHierarchyFactories
        // 17 List<TypeAdapterFactory> factoriesToBeAdded
        // 18 ToNumberStrategy objectToNumberStrategy
        // 19 ToNumberStrategy numberToNumberStrategy
        // 20 List<ReflectionAccessFilter> reflectionFilters

        params[0] = Excluder.DEFAULT; // Excluder excluder
        params[1] = FieldNamingPolicy.IDENTITY; // FieldNamingStrategy fieldNamingStrategy
        params[2] = Collections.emptyMap(); // instanceCreators
        params[3] = false; // serializeNulls
        params[4] = false; // complexMapKeySerialization
        params[5] = false; // generateNonExecutableGson
        params[6] = htmlSafeValue; // htmlSafe
        params[7] = false; // prettyPrinting
        params[8] = false; // lenient
        params[9] = false; // serializeSpecialFloatingPointValues
        params[10] = true; // useJdkUnsafe
        params[11] = LongSerializationPolicy.DEFAULT; // longSerializationPolicy
        params[12] = null; // datePattern
        params[13] = 0; // dateStyle
        params[14] = 0; // timeStyle
        params[15] = Collections.emptyList(); // builderFactories
        params[16] = Collections.emptyList(); // builderHierarchyFactories
        params[17] = Collections.emptyList(); // factoriesToBeAdded
        params[18] = null; // objectToNumberStrategy
        params[19] = null; // numberToNumberStrategy
        params[20] = Collections.emptyList(); // reflectionFilters

        return (Gson) constructor.newInstance(params);
      }
    }
    throw new IllegalStateException("Suitable Gson constructor not found");
  }
}