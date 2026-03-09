package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
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
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.google.gson.Gson;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.ToNumberPolicy;
import com.google.gson.internal.Excluder;

import java.lang.reflect.Constructor;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

class GsonSerializeNullsTest {

  @Test
    @Timeout(8000)
  void testSerializeNulls_DefaultConstructor() {
    Gson gson = new Gson();
    // Default Gson serializeNulls should be false as per DEFAULT_SERIALIZE_NULLS
    assertFalse(gson.serializeNulls());
  }

  @Test
    @Timeout(8000)
  void testSerializeNulls_CustomTrue() throws Exception {
    // Using reflection to create Gson instance with serializeNulls = true
    Gson gson = createGsonWithSerializeNulls(true);
    assertTrue(gson.serializeNulls());
  }

  @Test
    @Timeout(8000)
  void testSerializeNulls_CustomFalse() throws Exception {
    // Using reflection to create Gson instance with serializeNulls = false
    Gson gson = createGsonWithSerializeNulls(false);
    assertFalse(gson.serializeNulls());
  }

  @SuppressWarnings("unchecked")
  private Gson createGsonWithSerializeNulls(boolean serializeNulls) throws Exception {
    Class<Gson> gsonClass = Gson.class;

    // Find the constructor with parameters count 21 and matching types
    Constructor<?>[] constructors = gsonClass.getDeclaredConstructors();
    Constructor<?> targetConstructor = null;
    for (Constructor<?> c : constructors) {
      if (c.getParameterCount() == 21) {
        Class<?>[] paramTypes = c.getParameterTypes();
        // Check first few parameter types to match expected signature
        if (paramTypes[0] == Excluder.class
            && paramTypes[1] == FieldNamingStrategy.class
            && Map.class.isAssignableFrom(paramTypes[2])
            && paramTypes[3] == boolean.class) {
          targetConstructor = c;
          break;
        }
      }
    }
    if (targetConstructor == null) {
      throw new NoSuchMethodException("Gson constructor with expected signature not found");
    }
    targetConstructor.setAccessible(true);

    // Prepare arguments matching constructor parameter types
    Object[] args = new Object[21];
    args[0] = Excluder.DEFAULT;                       // Excluder
    args[1] = FieldNamingPolicy.IDENTITY;             // FieldNamingStrategy
    args[2] = Map.of();                                // Map<Type, InstanceCreator<?>>
    args[3] = serializeNulls;                          // serializeNulls
    args[4] = false;                                   // complexMapKeySerialization
    args[5] = false;                                   // generateNonExecutableGson
    args[6] = true;                                    // htmlSafe
    args[7] = false;                                   // prettyPrinting
    args[8] = false;                                   // lenient
    args[9] = false;                                   // serializeSpecialFloatingPointValues
    args[10] = true;                                   // useJdkUnsafe
    args[11] = LongSerializationPolicy.DEFAULT;       // LongSerializationPolicy
    args[12] = null;                                   // datePattern
    args[13] = 0;                                      // dateStyle
    args[14] = 0;                                      // timeStyle
    args[15] = List.of();                              // builderFactories
    args[16] = List.of();                              // builderHierarchyFactories
    args[17] = List.of();                              // factoriesToBeAdded
    args[18] = ToNumberPolicy.DOUBLE;                  // objectToNumberStrategy
    args[19] = ToNumberPolicy.LAZILY_PARSED_NUMBER;   // numberToNumberStrategy
    args[20] = List.of();                              // reflectionFilters

    return (Gson) targetConstructor.newInstance(args);
  }
}