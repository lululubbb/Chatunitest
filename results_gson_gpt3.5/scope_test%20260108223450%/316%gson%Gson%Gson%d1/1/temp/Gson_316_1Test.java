package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.SerializationDelegatingTypeAdapter;
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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

class Gson_316_1Test {

    private Excluder excluder;
    private FieldNamingStrategy fieldNamingStrategy;
    private Map<Type, InstanceCreator<?>> instanceCreators;
    private List<TypeAdapterFactory> builderFactories;
    private List<TypeAdapterFactory> builderHierarchyFactories;
    private List<TypeAdapterFactory> factoriesToBeAdded;
    private ToNumberStrategy objectToNumberStrategy;
    private ToNumberStrategy numberToNumberStrategy;
    private List<ReflectionAccessFilter> reflectionFilters;

    @BeforeEach
    void setUp() {
        excluder = Excluder.DEFAULT;
        fieldNamingStrategy = FieldNamingPolicy.IDENTITY;
        instanceCreators = Collections.emptyMap();
        builderFactories = Collections.emptyList();
        builderHierarchyFactories = Collections.emptyList();
        factoriesToBeAdded = Collections.emptyList();
        objectToNumberStrategy = ToNumberPolicy.DOUBLE;
        numberToNumberStrategy = ToNumberPolicy.LAZILY_PARSED_NUMBER;
        reflectionFilters = Collections.emptyList();
    }

    @Test
    @Timeout(8000)
    void testConstructor_Defaults() throws Exception {
        // Use reflection to invoke the package-private constructor with default values
        Gson gson = new Gson(
                excluder,
                fieldNamingStrategy,
                instanceCreators,
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
                builderFactories,
                builderHierarchyFactories,
                factoriesToBeAdded,
                objectToNumberStrategy,
                numberToNumberStrategy,
                reflectionFilters);

        // Validate fields set correctly via reflection
        Field excluderField = Gson.class.getDeclaredField("excluder");
        excluderField.setAccessible(true);
        assertSame(excluder, excluderField.get(gson));

        Field fieldNamingStrategyField = Gson.class.getDeclaredField("fieldNamingStrategy");
        fieldNamingStrategyField.setAccessible(true);
        assertSame(fieldNamingStrategy, fieldNamingStrategyField.get(gson));

        Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
        serializeNullsField.setAccessible(true);
        assertFalse(serializeNullsField.getBoolean(gson));

        Field complexMapKeySerializationField = Gson.class.getDeclaredField("complexMapKeySerialization");
        complexMapKeySerializationField.setAccessible(true);
        assertFalse(complexMapKeySerializationField.getBoolean(gson));

        Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
        generateNonExecutableJsonField.setAccessible(true);
        assertFalse(generateNonExecutableJsonField.getBoolean(gson));

        Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
        htmlSafeField.setAccessible(true);
        assertTrue(htmlSafeField.getBoolean(gson));

        Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
        prettyPrintingField.setAccessible(true);
        assertFalse(prettyPrintingField.getBoolean(gson));

        Field lenientField = Gson.class.getDeclaredField("lenient");
        lenientField.setAccessible(true);
        assertFalse(lenientField.getBoolean(gson));

        Field serializeSpecialFloatingPointValuesField = Gson.class.getDeclaredField("serializeSpecialFloatingPointValues");
        serializeSpecialFloatingPointValuesField.setAccessible(true);
        assertFalse(serializeSpecialFloatingPointValuesField.getBoolean(gson));

        Field useJdkUnsafeField = Gson.class.getDeclaredField("useJdkUnsafe");
        useJdkUnsafeField.setAccessible(true);
        assertTrue(useJdkUnsafeField.getBoolean(gson));

        Field longSerializationPolicyField = Gson.class.getDeclaredField("longSerializationPolicy");
        longSerializationPolicyField.setAccessible(true);
        assertEquals(LongSerializationPolicy.DEFAULT, longSerializationPolicyField.get(gson));

        Field datePatternField = Gson.class.getDeclaredField("datePattern");
        datePatternField.setAccessible(true);
        assertNull(datePatternField.get(gson));

        Field dateStyleField = Gson.class.getDeclaredField("dateStyle");
        dateStyleField.setAccessible(true);
        assertEquals(DateFormat.DEFAULT, dateStyleField.getInt(gson));

        Field timeStyleField = Gson.class.getDeclaredField("timeStyle");
        timeStyleField.setAccessible(true);
        assertEquals(DateFormat.DEFAULT, timeStyleField.getInt(gson));

        Field factoriesField = Gson.class.getDeclaredField("factories");
        factoriesField.setAccessible(true);
        List<?> factories = (List<?>) factoriesField.get(gson);

        // Verify factories list contains expected factories in order
        assertNotNull(factories);
        assertTrue(factories.size() > 10); // Should contain many factories

        // Check that first factory is TypeAdapters.JSON_ELEMENT_FACTORY
        assertSame(TypeAdapters.JSON_ELEMENT_FACTORY, factories.get(0));
        // Check that excluder is in factories
        assertTrue(factories.contains(excluder));
    }

    @Test
    @Timeout(8000)
    void testConstructor_WithFactoriesToBeAdded() throws Exception {
        TypeAdapterFactory mockFactory = mock(TypeAdapterFactory.class);
        List<TypeAdapterFactory> addedFactories = Collections.singletonList(mockFactory);

        Gson gson = new Gson(
                excluder,
                fieldNamingStrategy,
                instanceCreators,
                true, // serializeNulls
                true, // complexMapKeySerialization
                true, // generateNonExecutableGson
                false, // htmlSafe
                true,  // prettyPrinting
                true,  // lenient
                true,  // serializeSpecialFloatingPointValues
                false, // useJdkUnsafe
                LongSerializationPolicy.STRING,
                "yyyy-MM-dd",
                DateFormat.SHORT,
                DateFormat.MEDIUM,
                builderFactories,
                builderHierarchyFactories,
                addedFactories,
                objectToNumberStrategy,
                numberToNumberStrategy,
                reflectionFilters);

        Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
        serializeNullsField.setAccessible(true);
        assertTrue(serializeNullsField.getBoolean(gson));

        Field complexMapKeySerializationField = Gson.class.getDeclaredField("complexMapKeySerialization");
        complexMapKeySerializationField.setAccessible(true);
        assertTrue(complexMapKeySerializationField.getBoolean(gson));

        Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
        generateNonExecutableJsonField.setAccessible(true);
        assertTrue(generateNonExecutableJsonField.getBoolean(gson));

        Field htmlSafeField = Gson.class.getDeclaredField("htmlSafe");
        htmlSafeField.setAccessible(true);
        assertFalse(htmlSafeField.getBoolean(gson));

        Field prettyPrintingField = Gson.class.getDeclaredField("prettyPrinting");
        prettyPrintingField.setAccessible(true);
        assertTrue(prettyPrintingField.getBoolean(gson));

        Field lenientField = Gson.class.getDeclaredField("lenient");
        lenientField.setAccessible(true);
        assertTrue(lenientField.getBoolean(gson));

        Field serializeSpecialFloatingPointValuesField = Gson.class.getDeclaredField("serializeSpecialFloatingPointValues");
        serializeSpecialFloatingPointValuesField.setAccessible(true);
        assertTrue(serializeSpecialFloatingPointValuesField.getBoolean(gson));

        Field useJdkUnsafeField = Gson.class.getDeclaredField("useJdkUnsafe");
        useJdkUnsafeField.setAccessible(true);
        assertFalse(useJdkUnsafeField.getBoolean(gson));

        Field longSerializationPolicyField = Gson.class.getDeclaredField("longSerializationPolicy");
        longSerializationPolicyField.setAccessible(true);
        assertEquals(LongSerializationPolicy.STRING, longSerializationPolicyField.get(gson));

        Field datePatternField = Gson.class.getDeclaredField("datePattern");
        datePatternField.setAccessible(true);
        assertEquals("yyyy-MM-dd", datePatternField.get(gson));

        Field dateStyleField = Gson.class.getDeclaredField("dateStyle");
        dateStyleField.setAccessible(true);
        assertEquals(DateFormat.SHORT, dateStyleField.getInt(gson));

        Field timeStyleField = Gson.class.getDeclaredField("timeStyle");
        timeStyleField.setAccessible(true);
        assertEquals(DateFormat.MEDIUM, timeStyleField.getInt(gson));

        Field factoriesField = Gson.class.getDeclaredField("factories");
        factoriesField.setAccessible(true);
        List<?> factories = (List<?>) factoriesField.get(gson);

        // Verify that our added factory is included
        assertTrue(factories.contains(mockFactory));
    }

    @Test
    @Timeout(8000)
    void testFactoriesContainExpectedFactories() throws Exception {
        Gson gson = new Gson(
                excluder,
                fieldNamingStrategy,
                instanceCreators,
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
                DateFormat.DEFAULT,
                DateFormat.DEFAULT,
                builderFactories,
                builderHierarchyFactories,
                factoriesToBeAdded,
                objectToNumberStrategy,
                numberToNumberStrategy,
                reflectionFilters);

        Field factoriesField = Gson.class.getDeclaredField("factories");
        factoriesField.setAccessible(true);
        List<?> factories = (List<?>) factoriesField.get(gson);

        // Check presence of key known factories by class or instance
        boolean hasJsonElementFactory = factories.stream()
            .anyMatch(f -> f == TypeAdapters.JSON_ELEMENT_FACTORY);
        assertTrue(hasJsonElementFactory);

        boolean hasObjectTypeAdapterFactory = factories.stream()
            .anyMatch(f -> f.getClass().getSimpleName().equals("ObjectTypeAdapter$1"));
        assertTrue(hasObjectTypeAdapterFactory);

        boolean hasExcluder = factories.contains(excluder);
        assertTrue(hasExcluder);

        boolean hasCollectionTypeAdapterFactory = factories.stream()
            .anyMatch(f -> f instanceof CollectionTypeAdapterFactory);
        assertTrue(hasCollectionTypeAdapterFactory);

        boolean hasMapTypeAdapterFactory = factories.stream()
            .anyMatch(f -> f instanceof MapTypeAdapterFactory);
        assertTrue(hasMapTypeAdapterFactory);

        boolean hasReflectiveTypeAdapterFactory = factories.stream()
            .anyMatch(f -> f instanceof ReflectiveTypeAdapterFactory);
        assertTrue(hasReflectiveTypeAdapterFactory);

        boolean hasJsonAdapterAnnotationTypeAdapterFactory = factories.stream()
            .anyMatch(f -> f instanceof JsonAdapterAnnotationTypeAdapterFactory);
        assertTrue(hasJsonAdapterAnnotationTypeAdapterFactory);
    }
}