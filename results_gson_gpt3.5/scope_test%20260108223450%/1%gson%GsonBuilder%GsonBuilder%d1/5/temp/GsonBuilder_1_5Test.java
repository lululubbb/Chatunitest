package com.google.gson;
import org.junit.jupiter.api.Timeout;
import static com.google.gson.Gson.DEFAULT_COMPLEX_MAP_KEYS;
import static com.google.gson.Gson.DEFAULT_DATE_PATTERN;
import static com.google.gson.Gson.DEFAULT_ESCAPE_HTML;
import static com.google.gson.Gson.DEFAULT_JSON_NON_EXECUTABLE;
import static com.google.gson.Gson.DEFAULT_LENIENT;
import static com.google.gson.Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY;
import static com.google.gson.Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY;
import static com.google.gson.Gson.DEFAULT_PRETTY_PRINT;
import static com.google.gson.Gson.DEFAULT_SERIALIZE_NULLS;
import static com.google.gson.Gson.DEFAULT_SPECIALIZE_FLOAT_VALUES;
import static com.google.gson.Gson.DEFAULT_USE_JDK_UNSAFE;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.DefaultDateTypeAdapter;
import com.google.gson.internal.bind.TreeTypeAdapter;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.internal.sql.SqlTypesSupport;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.text.DateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Excluder;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.LongSerializationPolicy;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.ReflectionAccessFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;

class GsonBuilder_1_5Test {

    private GsonBuilder gsonBuilder;

    @BeforeEach
    void setUp() {
        gsonBuilder = new GsonBuilder();
    }

    @Test
    @Timeout(8000)
    void testDefaultConstructor_initialState() throws Exception {
        // Verify default fields set as expected by reflection

        Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
        excluderField.setAccessible(true);
        Excluder excluder = (Excluder) excluderField.get(gsonBuilder);
        assertEquals(Excluder.DEFAULT, excluder);

        Field longSerializationPolicyField = GsonBuilder.class.getDeclaredField("longSerializationPolicy");
        longSerializationPolicyField.setAccessible(true);
        LongSerializationPolicy longSerializationPolicy = (LongSerializationPolicy) longSerializationPolicyField.get(gsonBuilder);
        assertEquals(LongSerializationPolicy.DEFAULT, longSerializationPolicy);

        Field fieldNamingPolicyField = GsonBuilder.class.getDeclaredField("fieldNamingPolicy");
        fieldNamingPolicyField.setAccessible(true);
        FieldNamingStrategy fieldNamingStrategy = (FieldNamingStrategy) fieldNamingPolicyField.get(gsonBuilder);
        assertEquals(FieldNamingPolicy.IDENTITY, fieldNamingStrategy);

        Field instanceCreatorsField = GsonBuilder.class.getDeclaredField("instanceCreators");
        instanceCreatorsField.setAccessible(true);
        Map<Type, InstanceCreator<?>> instanceCreators = (Map<Type, InstanceCreator<?>>) instanceCreatorsField.get(gsonBuilder);
        assertNotNull(instanceCreators);
        assertTrue(instanceCreators.isEmpty());

        Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
        factoriesField.setAccessible(true);
        List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(gsonBuilder);
        assertNotNull(factories);
        assertTrue(factories.isEmpty());

        Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
        hierarchyFactoriesField.setAccessible(true);
        List<TypeAdapterFactory> hierarchyFactories = (List<TypeAdapterFactory>) hierarchyFactoriesField.get(gsonBuilder);
        assertNotNull(hierarchyFactories);
        assertTrue(hierarchyFactories.isEmpty());

        Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
        serializeNullsField.setAccessible(true);
        boolean serializeNulls = serializeNullsField.getBoolean(gsonBuilder);
        assertEquals(Gson.DEFAULT_SERIALIZE_NULLS, serializeNulls);

        Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
        datePatternField.setAccessible(true);
        String datePattern = (String) datePatternField.get(gsonBuilder);
        assertEquals(Gson.DEFAULT_DATE_PATTERN, datePattern);

        Field dateStyleField = GsonBuilder.class.getDeclaredField("dateStyle");
        dateStyleField.setAccessible(true);
        int dateStyle = dateStyleField.getInt(gsonBuilder);
        assertEquals(java.text.DateFormat.DEFAULT, dateStyle);

        Field timeStyleField = GsonBuilder.class.getDeclaredField("timeStyle");
        timeStyleField.setAccessible(true);
        int timeStyle = timeStyleField.getInt(gsonBuilder);
        assertEquals(java.text.DateFormat.DEFAULT, timeStyle);

        Field complexMapKeySerializationField = GsonBuilder.class.getDeclaredField("complexMapKeySerialization");
        complexMapKeySerializationField.setAccessible(true);
        boolean complexMapKeySerialization = complexMapKeySerializationField.getBoolean(gsonBuilder);
        assertEquals(Gson.DEFAULT_COMPLEX_MAP_KEYS, complexMapKeySerialization);

        Field serializeSpecialFloatingPointValuesField = GsonBuilder.class.getDeclaredField("serializeSpecialFloatingPointValues");
        serializeSpecialFloatingPointValuesField.setAccessible(true);
        boolean serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValuesField.getBoolean(gsonBuilder);
        assertEquals(Gson.DEFAULT_SPECIALIZE_FLOAT_VALUES, serializeSpecialFloatingPointValues);

        Field escapeHtmlCharsField = GsonBuilder.class.getDeclaredField("escapeHtmlChars");
        escapeHtmlCharsField.setAccessible(true);
        boolean escapeHtmlChars = escapeHtmlCharsField.getBoolean(gsonBuilder);
        assertEquals(Gson.DEFAULT_ESCAPE_HTML, escapeHtmlChars);

        Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
        prettyPrintingField.setAccessible(true);
        boolean prettyPrinting = prettyPrintingField.getBoolean(gsonBuilder);
        assertEquals(Gson.DEFAULT_PRETTY_PRINT, prettyPrinting);

        Field generateNonExecutableJsonField = GsonBuilder.class.getDeclaredField("generateNonExecutableJson");
        generateNonExecutableJsonField.setAccessible(true);
        boolean generateNonExecutableJson = generateNonExecutableJsonField.getBoolean(gsonBuilder);
        assertEquals(Gson.DEFAULT_JSON_NON_EXECUTABLE, generateNonExecutableJson);

        Field lenientField = GsonBuilder.class.getDeclaredField("lenient");
        lenientField.setAccessible(true);
        boolean lenient = lenientField.getBoolean(gsonBuilder);
        assertEquals(Gson.DEFAULT_LENIENT, lenient);

        Field useJdkUnsafeField = GsonBuilder.class.getDeclaredField("useJdkUnsafe");
        useJdkUnsafeField.setAccessible(true);
        boolean useJdkUnsafe = useJdkUnsafeField.getBoolean(gsonBuilder);
        assertEquals(Gson.DEFAULT_USE_JDK_UNSAFE, useJdkUnsafe);

        Field objectToNumberStrategyField = GsonBuilder.class.getDeclaredField("objectToNumberStrategy");
        objectToNumberStrategyField.setAccessible(true);
        ToNumberStrategy objectToNumberStrategy = (ToNumberStrategy) objectToNumberStrategyField.get(gsonBuilder);
        assertEquals(Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY, objectToNumberStrategy);

        Field numberToNumberStrategyField = GsonBuilder.class.getDeclaredField("numberToNumberStrategy");
        numberToNumberStrategyField.setAccessible(true);
        ToNumberStrategy numberToNumberStrategy = (ToNumberStrategy) numberToNumberStrategyField.get(gsonBuilder);
        assertEquals(Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY, numberToNumberStrategy);

        Field reflectionFiltersField = GsonBuilder.class.getDeclaredField("reflectionFilters");
        reflectionFiltersField.setAccessible(true);
        LinkedList<ReflectionAccessFilter> reflectionFilters = (LinkedList<ReflectionAccessFilter>) reflectionFiltersField.get(gsonBuilder);
        assertNotNull(reflectionFilters);
        assertTrue(reflectionFilters.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testPrivateAddTypeAdaptersForDate_invocation() throws Exception {
        Method addTypeAdaptersForDate = GsonBuilder.class.getDeclaredMethod(
                "addTypeAdaptersForDate", String.class, int.class, int.class, List.class);
        addTypeAdaptersForDate.setAccessible(true);

        List<TypeAdapterFactory> factoriesList = new ArrayList<>();
        // Call with null pattern and default styles
        addTypeAdaptersForDate.invoke(gsonBuilder, null, java.text.DateFormat.DEFAULT, java.text.DateFormat.DEFAULT, factoriesList);
        assertNotNull(factoriesList);
        // Should add some factories for date types
        assertFalse(factoriesList.isEmpty());

        factoriesList.clear();
        // Call with non-null pattern and styles
        addTypeAdaptersForDate.invoke(gsonBuilder, "yyyy-MM-dd'T'HH:mm:ss", java.text.DateFormat.SHORT, java.text.DateFormat.LONG, factoriesList);
        assertFalse(factoriesList.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testConstructor_withGson() throws Exception {
        Gson gsonMock = mock(Gson.class);
        GsonBuilder builder = new GsonBuilder(gsonMock);
        assertNotNull(builder);

        // Because constructor copies fields from gson, verify some fields copied correctly using reflection
        Field excluderField = GsonBuilder.class.getDeclaredField("excluder");
        excluderField.setAccessible(true);
        Excluder excluder = (Excluder) excluderField.get(builder);
        assertNotNull(excluder);
    }

    @Test
    @Timeout(8000)
    void testSetters_chainingAndEffect() throws Exception {
        GsonBuilder builder = new GsonBuilder();

        builder.setVersion(1.5);
        builder.excludeFieldsWithModifiers(java.lang.reflect.Modifier.TRANSIENT);
        builder.generateNonExecutableJson();
        builder.excludeFieldsWithoutExposeAnnotation();
        builder.serializeNulls();
        builder.enableComplexMapKeySerialization();
        builder.disableInnerClassSerialization();
        builder.setLongSerializationPolicy(LongSerializationPolicy.DEFAULT);
        builder.setFieldNamingPolicy(FieldNamingPolicy.IDENTITY);
        builder.setFieldNamingStrategy(FieldNamingPolicy.IDENTITY);
        builder.setObjectToNumberStrategy(Gson.DEFAULT_OBJECT_TO_NUMBER_STRATEGY);
        builder.setNumberToNumberStrategy(Gson.DEFAULT_NUMBER_TO_NUMBER_STRATEGY);
        builder.setPrettyPrinting();
        builder.setLenient();
        builder.disableHtmlEscaping();
        builder.setDateFormat("yyyy-MM-dd");
        builder.setDateFormat(java.text.DateFormat.SHORT);
        builder.setDateFormat(java.text.DateFormat.SHORT, java.text.DateFormat.LONG);
        builder.serializeSpecialFloatingPointValues();
        builder.disableJdkUnsafe();

        // Verify some fields set correctly by reflection
        Field serializeNullsField = GsonBuilder.class.getDeclaredField("serializeNulls");
        serializeNullsField.setAccessible(true);
        assertTrue(serializeNullsField.getBoolean(builder));

        Field complexMapKeySerializationField = GsonBuilder.class.getDeclaredField("complexMapKeySerialization");
        complexMapKeySerializationField.setAccessible(true);
        assertTrue(complexMapKeySerializationField.getBoolean(builder));

        Field prettyPrintingField = GsonBuilder.class.getDeclaredField("prettyPrinting");
        prettyPrintingField.setAccessible(true);
        assertTrue(prettyPrintingField.getBoolean(builder));

        Field lenientField = GsonBuilder.class.getDeclaredField("lenient");
        lenientField.setAccessible(true);
        assertTrue(lenientField.getBoolean(builder));

        Field escapeHtmlCharsField = GsonBuilder.class.getDeclaredField("escapeHtmlChars");
        escapeHtmlCharsField.setAccessible(true);
        assertFalse(escapeHtmlCharsField.getBoolean(builder));

        Field datePatternField = GsonBuilder.class.getDeclaredField("datePattern");
        datePatternField.setAccessible(true);
        assertEquals("yyyy-MM-dd", datePatternField.get(builder));

        Field useJdkUnsafeField = GsonBuilder.class.getDeclaredField("useJdkUnsafe");
        useJdkUnsafeField.setAccessible(true);
        assertFalse(useJdkUnsafeField.getBoolean(builder));
    }

    @Test
    @Timeout(8000)
    void testRegisterTypeAdapter_andFactories() throws Exception {
        GsonBuilder builder = new GsonBuilder();
        Object typeAdapter = new Object();
        builder.registerTypeAdapter(String.class, typeAdapter);

        Field factoriesField = GsonBuilder.class.getDeclaredField("factories");
        factoriesField.setAccessible(true);
        List<TypeAdapterFactory> factories = (List<TypeAdapterFactory>) factoriesField.get(builder);
        assertFalse(factories.isEmpty());

        TypeAdapterFactory factoryMock = mock(TypeAdapterFactory.class);
        builder.registerTypeAdapterFactory(factoryMock);
        factories = (List<TypeAdapterFactory>) factoriesField.get(builder);
        assertTrue(factories.contains(factoryMock));

        builder.registerTypeHierarchyAdapter(Number.class, typeAdapter);
        Field hierarchyFactoriesField = GsonBuilder.class.getDeclaredField("hierarchyFactories");
        hierarchyFactoriesField.setAccessible(true);
        List<TypeAdapterFactory> hierarchyFactories = (List<TypeAdapterFactory>) hierarchyFactoriesField.get(builder);
        assertFalse(hierarchyFactories.isEmpty());
    }

    @Test
    @Timeout(8000)
    void testAddReflectionAccessFilter() throws Exception {
        GsonBuilder builder = new GsonBuilder();
        ReflectionAccessFilter filterMock = mock(ReflectionAccessFilter.class);
        builder.addReflectionAccessFilter(filterMock);

        Field reflectionFiltersField = GsonBuilder.class.getDeclaredField("reflectionFilters");
        reflectionFiltersField.setAccessible(true);
        LinkedList<ReflectionAccessFilter> filters = (LinkedList<ReflectionAccessFilter>) reflectionFiltersField.get(builder);
        assertTrue(filters.contains(filterMock));
    }

    @Test
    @Timeout(8000)
    void testCreate_returnsGson() {
        Gson gson = gsonBuilder.create();
        assertNotNull(gson);
        // The created Gson should not be the same instance as the builder
        assertNotSame(gsonBuilder, gson);
    }
}