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
import org.junit.jupiter.api.Test;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class GsonFieldNamingStrategyTest {

  @Test
    @Timeout(8000)
  void testFieldNamingStrategy_Default() {
    Gson gson = new Gson();
    FieldNamingStrategy strategy = gson.fieldNamingStrategy();
    assertNotNull(strategy);
    assertSame(FieldNamingPolicy.IDENTITY, strategy);
  }

  @Test
    @Timeout(8000)
  void testFieldNamingStrategy_Custom() throws Exception {
    FieldNamingStrategy customStrategy = field -> "customName";

    // Create Gson instance with default constructor
    Gson gson = new Gson();

    // Use reflection to set the private final field 'fieldNamingStrategy' in Gson instance
    Field field = Gson.class.getDeclaredField("fieldNamingStrategy");
    field.setAccessible(true);

    // Remove final modifier to allow setting the field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(gson, customStrategy);

    FieldNamingStrategy strategy = gson.fieldNamingStrategy();
    assertNotNull(strategy);

    // Since translateName requires a Field, create a dummy Field instance for testing
    java.lang.reflect.Field dummyField = Dummy.class.getDeclaredField("anyField");
    assertEquals("customName", strategy.translateName(dummyField));
    assertSame(customStrategy, strategy);
  }

  private static class Dummy {
    public int anyField;
  }
}