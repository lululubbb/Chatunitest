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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.TypeAdapters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Map;

class Gson_315_4Test {

  private Gson gsonDefault;

  @BeforeEach
  void setUp() {
    gsonDefault = new Gson();
  }

  @Test
    @Timeout(8000)
  void testDefaultConstructor_initializesFields() throws Exception {
    // Using reflection to verify private final fields are initialized as expected
    Field excluderField = Gson.class.getDeclaredField("excluder");
    excluderField.setAccessible(true);
    Excluder excluder = (Excluder) excluderField.get(gsonDefault);
    assertNotNull(excluder);
    assertEquals(Excluder.DEFAULT, excluder);

    Field fieldNamingStrategyField = Gson.class.getDeclaredField("fieldNamingStrategy");
    fieldNamingStrategyField.setAccessible(true);
    FieldNamingStrategy fieldNamingStrategy = (FieldNamingStrategy) fieldNamingStrategyField.get(gsonDefault);
    assertNotNull(fieldNamingStrategy);
    assertEquals(FieldNamingPolicy.IDENTITY, fieldNamingStrategy);

    Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
    serializeNullsField.setAccessible(true);
    boolean serializeNulls = serializeNullsField.getBoolean(gsonDefault);
    assertFalse(serializeNulls);

    Field complexMapKeySerializationField = Gson.class.getDeclaredField("complexMapKeySerialization");
    complexMapKeySerializationField.setAccessible(true);
    boolean complexMapKeySerialization = complexMapKeySerializationField.getBoolean(gsonDefault);
    assertFalse(complexMapKeySerialization);

    Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
    generateNonExecutableJsonField.setAccessible(true);
    boolean generateNonExecutableJson = generateNonExecutableJsonField.getBoolean(gsonDefault);
    assertFalse(generateNonExecutableJson);

    Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
    htmlSafeField.setAccessible(true);
    boolean htmlSafe = htmlSafeField.getBoolean(gsonDefault);
    assertTrue(htmlSafe);

    Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
    prettyPrintingField.setAccessible(true);
    boolean prettyPrinting = prettyPrintingField.getBoolean(gsonDefault);
    assertFalse(prettyPrinting);

    Field lenientField = Gson.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    boolean lenient = lenientField.getBoolean(gsonDefault);
    assertFalse(lenient);

    Field serializeSpecialFloatingPointValuesField = Gson.class.getDeclaredField("serializeSpecialFloatingPointValues");
    serializeSpecialFloatingPointValuesField.setAccessible(true);
    boolean serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValuesField.getBoolean(gsonDefault);
    assertFalse(serializeSpecialFloatingPointValues);

    Field useJdkUnsafeField = Gson.class.getDeclaredField("useJdkUnsafe");
    useJdkUnsafeField.setAccessible(true);
    boolean useJdkUnsafe = useJdkUnsafeField.getBoolean(gsonDefault);
    assertTrue(useJdkUnsafe);

    Field datePatternField = Gson.class.getDeclaredField("datePattern");
    datePatternField.setAccessible(true);
    String datePattern = (String) datePatternField.get(gsonDefault);
    assertNull(datePattern);

    Field dateStyleField = Gson.class.getDeclaredField("dateStyle");
    dateStyleField.setAccessible(true);
    int dateStyle = dateStyleField.getInt(gsonDefault);
    assertEquals(DateFormat.DEFAULT, dateStyle);

    Field timeStyleField = Gson.class.getDeclaredField("timeStyle");
    timeStyleField.setAccessible(true);
    int timeStyle = timeStyleField.getInt(gsonDefault);
    assertEquals(DateFormat.DEFAULT, timeStyle);

    Field longSerializationPolicyField = Gson.class.getDeclaredField("longSerializationPolicy");
    longSerializationPolicyField.setAccessible(true);
    LongSerializationPolicy longSerializationPolicy = (LongSerializationPolicy) longSerializationPolicyField.get(gsonDefault);
    assertEquals(LongSerializationPolicy.DEFAULT, longSerializationPolicy);

    Field instanceCreatorsField = Gson.class.getDeclaredField("instanceCreators");
    instanceCreatorsField.setAccessible(true);
    Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonDefault);
    assertNotNull(instanceCreators);
    assertTrue(instanceCreators.isEmpty());

    Field factoriesField = Gson.class.getDeclaredField("factories");
    factoriesField.setAccessible(true);
    List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonDefault);
    assertNotNull(factories);
    assertTrue(factories.isEmpty());

    Field builderFactoriesField = Gson.class.getDeclaredField("builderFactories");
    builderFactoriesField.setAccessible(true);
    List<TypeAdapterFactory> builderFactories = (List<TypeAdapterFactory>) builderFactoriesField.get(gsonDefault);
    assertNotNull(builderFactories);
    assertTrue(builderFactories.isEmpty());

    Field builderHierarchyFactoriesField = Gson.class.getDeclaredField("builderHierarchyFactories");
    builderHierarchyFactoriesField.setAccessible(true);
    List<TypeAdapterFactory> builderHierarchyFactories = (List<TypeAdapterFactory>) builderHierarchyFactoriesField.get(gsonDefault);
    assertNotNull(builderHierarchyFactories);
    assertTrue(builderHierarchyFactories.isEmpty());

    Field objectToNumberStrategyField = Gson.class.getDeclaredField("objectToNumberStrategy");
    objectToNumberStrategyField.setAccessible(true);
    ToNumberStrategy objectToNumberStrategy = (ToNumberStrategy) objectToNumberStrategyField.get(gsonDefault);
    assertEquals(ToNumberPolicy.DOUBLE, objectToNumberStrategy);

    Field numberToNumberStrategyField = Gson.class.getDeclaredField("numberToNumberStrategy");
    numberToNumberStrategyField.setAccessible(true);
    ToNumberStrategy numberToNumberStrategy = (ToNumberStrategy) numberToNumberStrategyField.get(gsonDefault);
    assertEquals(ToNumberPolicy.LAZILY_PARSED_NUMBER, numberToNumberStrategy);

    Field reflectionFiltersField = Gson.class.getDeclaredField("reflectionFilters");
    reflectionFiltersField.setAccessible(true);
    List<ReflectionAccessFilter> reflectionFilters = (List<ReflectionAccessFilter>) reflectionFiltersField.get(gsonDefault);
    assertNotNull(reflectionFilters);
    assertTrue(reflectionFilters.isEmpty());
  }

  @Test
    @Timeout(8000)
  void testNewBuilder_returnsGsonBuilder() {
    GsonBuilder builder = gsonDefault.newBuilder();
    assertNotNull(builder);
    assertNotSame(gsonDefault, builder);
  }

  @Test
    @Timeout(8000)
  void testExcluder_andFieldNamingStrategy_methods() {
    assertEquals(Excluder.DEFAULT, gsonDefault.excluder());
    assertEquals(FieldNamingPolicy.IDENTITY, gsonDefault.fieldNamingStrategy());
  }

  @Test
    @Timeout(8000)
  void testSerializeNulls_andHtmlSafe_methods() {
    assertFalse(gsonDefault.serializeNulls());
    assertTrue(gsonDefault.htmlSafe());
  }
}