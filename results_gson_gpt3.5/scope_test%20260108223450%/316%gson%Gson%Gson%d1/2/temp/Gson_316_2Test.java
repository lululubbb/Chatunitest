package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonToken;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

class Gson_316_2Test {

    private Excluder excluderMock;
    private FieldNamingStrategy fieldNamingStrategyMock;
    private Map<Type, InstanceCreator<?>> instanceCreators;
    private List<TypeAdapterFactory> builderFactories;
    private List<TypeAdapterFactory> builderHierarchyFactories;
    private List<TypeAdapterFactory> factoriesToBeAdded;
    private List<ReflectionAccessFilter> reflectionFilters;

    @BeforeEach
    void setUp() {
        excluderMock = mock(Excluder.class);
        when(excluderMock.equals(any())).thenCallRealMethod(); // dummy behavior
        fieldNamingStrategyMock = mock(FieldNamingStrategy.class);
        instanceCreators = new HashMap<>();
        builderFactories = new ArrayList<>();
        builderHierarchyFactories = new ArrayList<>();
        factoriesToBeAdded = new ArrayList<>();
        reflectionFilters = new ArrayList<>();
    }

    @Test
    @Timeout(8000)
    void testConstructor_DefaultsAndFactories() throws Exception {
        // Using default values for parameters as per fields in Gson
        boolean serializeNulls = false;
        boolean complexMapKeySerialization = false;
        boolean generateNonExecutableGson = false;
        boolean htmlSafe = true;
        boolean prettyPrinting = false;
        boolean lenient = false;
        boolean serializeSpecialFloatingPointValues = false;
        boolean useJdkUnsafe = true;
        LongSerializationPolicy longSerializationPolicy = LongSerializationPolicy.DEFAULT;
        String datePattern = null;
        int dateStyle = 2; // DateFormat.MEDIUM
        int timeStyle = 2; // DateFormat.MEDIUM
        ToNumberStrategy objectToNumberStrategy = ToNumberPolicy.DOUBLE;
        ToNumberStrategy numberToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;

        Gson gson = new Gson(
                excluderMock,
                fieldNamingStrategyMock,
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

        // Check fields set correctly
        Field excluderField = Gson.class.getDeclaredField("excluder");
        excluderField.setAccessible(true);
        assertSame(excluderMock, excluderField.get(gson));

        Field fieldNamingStrategyField = Gson.class.getDeclaredField("fieldNamingStrategy");
        fieldNamingStrategyField.setAccessible(true);
        assertSame(fieldNamingStrategyMock, fieldNamingStrategyField.get(gson));

        Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
        serializeNullsField.setAccessible(true);
        assertEquals(serializeNulls, serializeNullsField.getBoolean(gson));

        Field complexMapKeySerializationField = Gson.class.getDeclaredField("complexMapKeySerialization");
        complexMapKeySerializationField.setAccessible(true);
        assertEquals(complexMapKeySerialization, complexMapKeySerializationField.getBoolean(gson));

        Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
        generateNonExecutableJsonField.setAccessible(true);
        assertEquals(generateNonExecutableGson, generateNonExecutableJsonField.getBoolean(gson));

        Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
        htmlSafeField.setAccessible(true);
        assertEquals(htmlSafe, htmlSafeField.getBoolean(gson));

        Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
        prettyPrintingField.setAccessible(true);
        assertEquals(prettyPrinting, prettyPrintingField.getBoolean(gson));

        Field lenientField = Gson.class.getDeclaredField("lenient");
        lenientField.setAccessible(true);
        assertEquals(lenient, lenientField.getBoolean(gson));

        Field serializeSpecialFloatingPointValuesField = Gson.class.getDeclaredField("serializeSpecialFloatingPointValues");
        serializeSpecialFloatingPointValuesField.setAccessible(true);
        assertEquals(serializeSpecialFloatingPointValues, serializeSpecialFloatingPointValuesField.getBoolean(gson));

        Field useJdkUnsafeField = Gson.class.getDeclaredField("useJdkUnsafe");
        useJdkUnsafeField.setAccessible(true);
        assertEquals(useJdkUnsafe, useJdkUnsafeField.getBoolean(gson));

        Field longSerializationPolicyField = Gson.class.getDeclaredField("longSerializationPolicy");
        longSerializationPolicyField.setAccessible(true);
        assertSame(longSerializationPolicy, longSerializationPolicyField.get(gson));

        Field datePatternField = Gson.class.getDeclaredField("datePattern");
        datePatternField.setAccessible(true);
        assertEquals(datePattern, datePatternField.get(gson));

        Field dateStyleField = Gson.class.getDeclaredField("dateStyle");
        dateStyleField.setAccessible(true);
        assertEquals(dateStyle, dateStyleField.getInt(gson));

        Field timeStyleField = Gson.class.getDeclaredField("timeStyle");
        timeStyleField.setAccessible(true);
        assertEquals(timeStyle, timeStyleField.getInt(gson));

        Field objectToNumberStrategyField = Gson.class.getDeclaredField("objectToNumberStrategy");
        objectToNumberStrategyField.setAccessible(true);
        assertSame(objectToNumberStrategy, objectToNumberStrategyField.get(gson));

        Field numberToNumberStrategyField = Gson.class.getDeclaredField("numberToNumberStrategy");
        numberToNumberStrategyField.setAccessible(true);
        assertSame(numberToNumberStrategy, numberToNumberStrategyField.get(gson));

        Field reflectionFiltersField = Gson.class.getDeclaredField("reflectionFilters");
        reflectionFiltersField.setAccessible(true);
        assertSame(reflectionFilters, reflectionFiltersField.get(gson));

        // Check that factories list is unmodifiable and contains expected built-in factories
        Field factoriesField = Gson.class.getDeclaredField("factories");
        factoriesField.setAccessible(true);
        List<?> factories = (List<?>) factoriesField.get(gson);

        assertNotNull(factories);
        assertTrue(factories.contains(TypeAdapters.JSON_ELEMENT_FACTORY));
        assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("Excluder")));
        assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("CollectionTypeAdapterFactory")));
        assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("MapTypeAdapterFactory")));
        assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("JsonAdapterAnnotationTypeAdapterFactory")));
        assertTrue(factories.stream().anyMatch(f -> f.getClass().getSimpleName().equals("ReflectiveTypeAdapterFactory")));

        // Check that jsonAdapterFactory field is set and is instance of JsonAdapterAnnotationTypeAdapterFactory
        Field jsonAdapterFactoryField = Gson.class.getDeclaredField("jsonAdapterFactory");
        jsonAdapterFactoryField.setAccessible(true);
        Object jsonAdapterFactory = jsonAdapterFactoryField.get(gson);
        assertNotNull(jsonAdapterFactory);
        assertTrue(jsonAdapterFactory instanceof JsonAdapterAnnotationTypeAdapterFactory);
    }
}