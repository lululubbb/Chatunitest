package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
import com.google.gson.internal.Streams;
import com.google.gson.internal.bind.JsonTreeReader;
import com.google.gson.internal.bind.JsonTreeWriter;
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
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.internal.bind.CollectionTypeAdapterFactory;
import com.google.gson.internal.bind.MapTypeAdapterFactory;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.NumberTypeAdapter;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;


public class GsonConstructorTest {

    private Excluder excluderMock;
    private FieldNamingStrategy fieldNamingStrategyMock;
    private Map<Type, InstanceCreator<?>> instanceCreatorsMock;
    private List<TypeAdapterFactory> builderFactoriesMock;
    private List<TypeAdapterFactory> builderHierarchyFactoriesMock;
    private List<TypeAdapterFactory> factoriesToBeAddedMock;
    private ToNumberStrategy objectToNumberStrategyMock;
    private ToNumberStrategy numberToNumberStrategyMock;
    private List<ReflectionAccessFilter> reflectionFiltersMock;

    @BeforeEach
    public void setup() {
        excluderMock = mock(Excluder.class);
        fieldNamingStrategyMock = mock(FieldNamingStrategy.class);
        instanceCreatorsMock = new HashMap<>();
        builderFactoriesMock = new ArrayList<>();
        builderHierarchyFactoriesMock = new ArrayList<>();
        factoriesToBeAddedMock = new ArrayList<>();
        objectToNumberStrategyMock = ToNumberStrategy.DEFAULT;
        numberToNumberStrategyMock = ToNumberStrategy.DEFAULT;
        reflectionFiltersMock = new ArrayList<>();
    }

    @Test
    @Timeout(8000)
    public void testConstructor_initializesFieldsAndFactories() throws Exception {
        Gson gson = new Gson(
                excluderMock,
                fieldNamingStrategyMock,
                instanceCreatorsMock,
                true,
                true,
                true,
                false,
                true,
                true,
                true,
                true,
                LongSerializationPolicy.DEFAULT,
                "yyyy-MM-dd",
                DateFormat.MEDIUM,
                DateFormat.SHORT,
                builderFactoriesMock,
                builderHierarchyFactoriesMock,
                factoriesToBeAddedMock,
                objectToNumberStrategyMock,
                numberToNumberStrategyMock,
                reflectionFiltersMock
        );

        // Validate fields
        assertSame(excluderMock, getField(gson, "excluder"));
        assertSame(fieldNamingStrategyMock, getField(gson, "fieldNamingStrategy"));
        assertSame(instanceCreatorsMock, getField(gson, "instanceCreators"));
        assertEquals(true, getField(gson, "serializeNulls"));
        assertEquals(true, getField(gson, "complexMapKeySerialization"));
        assertEquals(true, getField(gson, "generateNonExecutableJson"));
        assertEquals(false, getField(gson, "htmlSafe"));
        assertEquals(true, getField(gson, "prettyPrinting"));
        assertEquals(true, getField(gson, "lenient"));
        assertEquals(true, getField(gson, "serializeSpecialFloatingPointValues"));
        assertEquals(true, getField(gson, "useJdkUnsafe"));
        assertEquals(LongSerializationPolicy.DEFAULT, getField(gson, "longSerializationPolicy"));
        assertEquals("yyyy-MM-dd", getField(gson, "datePattern"));
        assertEquals(DateFormat.MEDIUM, getField(gson, "dateStyle"));
        assertEquals(DateFormat.SHORT, getField(gson, "timeStyle"));

        // Validate that constructorConstructor is non-null
        Object constructorConstructor = getField(gson, "constructorConstructor");
        assertNotNull(constructorConstructor);

        // Validate factories list is unmodifiable and includes expected factories
        List<?> factories = (List<?>) getField(gson, "factories");
        assertNotNull(factories);
        assertTrue(factories instanceof Collections.UnmodifiableRandomAccessList || factories instanceof Collections.UnmodifiableList);

        // Check presence of key known factories by class type or instance equality
        boolean hasJsonElementFactory = false;
        boolean hasObjectTypeAdapterFactory = false;
        boolean hasExcluder = false;
        boolean hasObjectTypeAdapterJsonAdapterFactory = false;
        boolean hasDateTypeAdapterFactory = false;
        for (Object f : factories) {
            // TypeAdapters.JSON_ELEMENT_FACTORY is a constant of type TypeAdapterFactory
            if (f == TypeAdapters.JSON_ELEMENT_FACTORY) hasJsonElementFactory = true;
            if (f == excluderMock) hasExcluder = true;
            if (f instanceof ObjectTypeAdapter.Factory || f == ObjectTypeAdapter.getFactory(objectToNumberStrategyMock)) hasObjectTypeAdapterFactory = true;
            if (f instanceof JsonAdapterAnnotationTypeAdapterFactory) hasObjectTypeAdapterJsonAdapterFactory = true;
            if (f == DateTypeAdapter.FACTORY) hasDateTypeAdapterFactory = true;
        }

        assertTrue(hasJsonElementFactory);
        assertTrue(hasExcluder);
        assertTrue(hasObjectTypeAdapterFactory);
        assertTrue(hasObjectTypeAdapterJsonAdapterFactory);
        assertTrue(hasDateTypeAdapterFactory);

        // Validate jsonAdapterFactory field is the factory in factories list of type JsonAdapterAnnotationTypeAdapterFactory
        JsonAdapterAnnotationTypeAdapterFactory jsonAdapterFactory = (JsonAdapterAnnotationTypeAdapterFactory) getField(gson, "jsonAdapterFactory");
        assertNotNull(jsonAdapterFactory);
        assertTrue(factories.contains(jsonAdapterFactory));
    }

    @Test
    @Timeout(8000)
    public void testConstructor_withEmptyCollections() throws Exception {
        Gson gson = new Gson(
                new Excluder(),
                FieldNamingPolicy.IDENTITY,
                Collections.emptyMap(),
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                false,
                LongSerializationPolicy.DEFAULT,
                null,
                DateFormat.DEFAULT,
                DateFormat.DEFAULT,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                ToNumberPolicy.DOUBLE,
                ToNumberPolicy.LAZILY_PARSED_NUMBER,
                Collections.emptyList()
        );

        assertNotNull(getField(gson, "constructorConstructor"));
        List<?> factories = (List<?>) getField(gson, "factories");
        assertNotNull(factories);
        assertTrue(factories.size() > 0);
        // Check that factories different than empty exist
        assertTrue(factories.contains(TypeAdapters.JSON_ELEMENT_FACTORY));
    }

    // Helper method to access private fields via reflection
    private Object getField(Object target, String fieldName) throws Exception {
        Field field = null;
        Class<?> clazz = target.getClass();
        while (clazz != null) {
            try {
                field = clazz.getDeclaredField(fieldName);
                break;
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        if (field == null) throw new NoSuchFieldException(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }
}