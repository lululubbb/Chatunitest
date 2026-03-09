package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonToken;
import java.io.EOFException;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapter;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.Streams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class GsonConstructorTest {

    private Excluder excluderMock;
    private FieldNamingStrategy fieldNamingStrategyMock;
    private Map<Type, InstanceCreator<?>> instanceCreatorsMock;
    private List<TypeAdapterFactory> builderFactoriesMock;
    private List<TypeAdapterFactory> builderHierarchyFactoriesMock;
    private List<TypeAdapterFactory> factoriesToBeAddedMock;
    private List<ReflectionAccessFilter> reflectionFiltersMock;

    @BeforeEach
    void setUp() {
        excluderMock = mock(Excluder.class);
        fieldNamingStrategyMock = mock(FieldNamingStrategy.class);
        instanceCreatorsMock = new HashMap<>();
        builderFactoriesMock = new ArrayList<>();
        builderHierarchyFactoriesMock = new ArrayList<>();
        factoriesToBeAddedMock = new ArrayList<>();
        reflectionFiltersMock = new ArrayList<>();
    }

    @Test
    @Timeout(8000)
    void testGsonConstructor_AllParameters() throws Exception {
        // Setup parameters
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
        int dateStyle = 1;
        int timeStyle = 2;
        ToNumberStrategy objectToNumberStrategy = ToNumberPolicy.DOUBLE;
        ToNumberStrategy numberToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;

        // Invoke constructor via reflection
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
                List.class);

        constructor.setAccessible(true);

        Gson gson = constructor.newInstance(
                excluderMock,
                fieldNamingStrategyMock,
                instanceCreatorsMock,
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
                builderFactoriesMock,
                builderHierarchyFactoriesMock,
                factoriesToBeAddedMock,
                objectToNumberStrategy,
                numberToNumberStrategy,
                reflectionFiltersMock);

        assertNotNull(gson);

        // Validate fields set correctly via reflection
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
        assertEquals(longSerializationPolicy, longSerializationPolicyField.get(gson));

        Field datePatternField = Gson.class.getDeclaredField("datePattern");
        datePatternField.setAccessible(true);
        assertEquals(datePattern, datePatternField.get(gson));

        Field dateStyleField = Gson.class.getDeclaredField("dateStyle");
        dateStyleField.setAccessible(true);
        assertEquals(dateStyle, dateStyleField.getInt(gson));

        Field timeStyleField = Gson.class.getDeclaredField("timeStyle");
        timeStyleField.setAccessible(true);
        assertEquals(timeStyle, timeStyleField.getInt(gson));

        Field factoriesField = Gson.class.getDeclaredField("factories");
        factoriesField.setAccessible(true);
        List<?> factories = (List<?>) factoriesField.get(gson);
        // Should contain at least the built-in factories added in constructor
        assertTrue(factories.size() > 0);

        Field objectToNumberStrategyField = Gson.class.getDeclaredField("objectToNumberStrategy");
        objectToNumberStrategyField.setAccessible(true);
        assertEquals(objectToNumberStrategy, objectToNumberStrategyField.get(gson));

        Field numberToNumberStrategyField = Gson.class.getDeclaredField("numberToNumberStrategy");
        numberToNumberStrategyField.setAccessible(true);
        assertEquals(numberToNumberStrategy, numberToNumberStrategyField.get(gson));

        Field reflectionFiltersField = Gson.class.getDeclaredField("reflectionFilters");
        reflectionFiltersField.setAccessible(true);
        assertEquals(reflectionFiltersMock, reflectionFiltersField.get(gson));
    }

    @Test
    @Timeout(8000)
    void testGsonConstructor_EmptyFactories() throws Exception {
        // Use empty lists for factoriesToBeAdded to cover that branch
        List<TypeAdapterFactory> emptyFactories = Collections.emptyList();

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
                List.class);

        constructor.setAccessible(true);

        Gson gson = constructor.newInstance(
                excluderMock,
                fieldNamingStrategyMock,
                instanceCreatorsMock,
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                true,
                LongSerializationPolicy.DEFAULT,
                null,
                0,
                0,
                builderFactoriesMock,
                builderHierarchyFactoriesMock,
                emptyFactories,
                ToNumberPolicy.DOUBLE,
                ToNumberPolicy.LAZILY_PARSED_NUMBER,
                reflectionFiltersMock);

        assertNotNull(gson);

        Field factoriesField = Gson.class.getDeclaredField("factories");
        factoriesField.setAccessible(true);
        List<?> factories = (List<?>) factoriesField.get(gson);
        // Should contain built-in factories and no added factories
        assertTrue(factories.contains(TypeAdapters.JSON_ELEMENT_FACTORY));
        assertTrue(factories.contains(excluderMock));
    }
}