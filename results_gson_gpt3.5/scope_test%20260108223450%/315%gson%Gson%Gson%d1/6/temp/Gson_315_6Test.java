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
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

public class Gson_315_6Test {

  private Gson gson;

  @BeforeEach
  public void setUp() {
    gson = new Gson();
  }

  @Test
    @Timeout(8000)
  public void testDefaultConstructorCreatesNonNullGson() {
    assertNotNull(gson);
    // Check default values via reflection
    try {
      Field excluderField = Gson.class.getDeclaredField("excluder");
      excluderField.setAccessible(true);
      Excluder excluder = (Excluder) excluderField.get(gson);
      assertNotNull(excluder);

      Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
      serializeNullsField.setAccessible(true);
      boolean serializeNulls = serializeNullsField.getBoolean(gson);
      assertFalse(serializeNulls);

      Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
      htmlSafeField.setAccessible(true);
      boolean htmlSafe = htmlSafeField.getBoolean(gson);
      assertTrue(htmlSafe);

      Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
      prettyPrintingField.setAccessible(true);
      boolean prettyPrinting = prettyPrintingField.getBoolean(gson);
      assertFalse(prettyPrinting);

      Field lenientField = Gson.class.getDeclaredField("lenient");
      lenientField.setAccessible(true);
      boolean lenient = lenientField.getBoolean(gson);
      assertFalse(lenient);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failure: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  public void testGsonConstructorWithAllParameters() throws Exception {
    Excluder excluder = Excluder.DEFAULT;
    FieldNamingStrategy fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
    var instanceCreators = Collections.<Type, InstanceCreator<?>>emptyMap();
    boolean serializeNulls = true;
    boolean complexMapKeySerialization = true;
    boolean generateNonExecutableGson = true;
    boolean htmlSafe = false;
    boolean prettyPrinting = true;
    boolean lenient = true;
    boolean serializeSpecialFloatingPointValues = true;
    boolean useJdkUnsafe = false;
    LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;
    String datePattern = "yyyy-MM-dd";
    int dateStyle = DateFormat.SHORT;
    int timeStyle = DateFormat.LONG;
    var builderFactories = Collections.<TypeAdapterFactory>emptyList();
    var builderHierarchyFactories = Collections.<TypeAdapterFactory>emptyList();
    var factoriesToBeAdded = Collections.<TypeAdapterFactory>emptyList();
    ToNumberStrategy objectToNumberStrategy = ToNumberPolicy.DOUBLE;
    ToNumberStrategy numberToNumberStrategy = ToNumberPolicy.LONG_OR_DOUBLE;
    var reflectionFilters = Collections.<ReflectionAccessFilter>emptyList();

    Constructor<Gson> constructor = Gson.class.getDeclaredConstructor(
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
    Gson customGson = constructor.newInstance(
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

    assertNotNull(customGson);

    // Validate some fields via reflection
    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    assertTrue(serializeNullsField.getBoolean(customGson));

    Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    assertFalse(htmlSafeField.getBoolean(customGson));

    Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    assertTrue(prettyPrintingField.getBoolean(customGson));

    Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    assertTrue(lenientField.getBoolean(customGson));

    Field datePatternField = Gson.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    assertEquals(datePattern, datePatternField.get(customGson));
  }

  @Test
    @Timeout(8000)
  public void testToStringContainsClassName() {
    String str = gson.toString();
    assertNotNull(str);
    assertTrue(str.contains("Gson"));
  }

  @Test
    @Timeout(8000)
  public void testGetAdapterReturnsTypeAdapter() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = gson.getAdapter(stringTypeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetAdapterWithClassReturnsTypeAdapter() {
    TypeAdapter<String> adapter = gson.getAdapter(String.class);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testGetDelegateAdapterReturnsTypeAdapter() {
    TypeAdapterFactory mockFactory = mock(TypeAdapterFactory.class);
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeAdapter<String> adapter = gson.getDelegateAdapter(mockFactory, stringTypeToken);
    assertNotNull(adapter);
  }

  @Test
    @Timeout(8000)
  public void testNewBuilderReturnsGsonBuilder() {
    GsonBuilder builder = gson.newBuilder();
    assertNotNull(builder);
  }

  @Test
    @Timeout(8000)
  public void testExcluderAndFieldNamingStrategyAccess() {
    Excluder excluder = gson.excluder();
    assertNotNull(excluder);
    FieldNamingStrategy strategy = gson.fieldNamingStrategy();
    assertNotNull(strategy);
  }

  @Test
    @Timeout(8000)
  public void testSerializeNullsAndHtmlSafe() {
    assertFalse(gson.serializeNulls());
    assertTrue(gson.htmlSafe());
  }
}