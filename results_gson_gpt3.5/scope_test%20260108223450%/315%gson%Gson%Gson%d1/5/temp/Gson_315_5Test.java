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
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
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
import static org.mockito.Mockito.*;

import com.google.gson.*;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.bind.JsonAdapterAnnotationTypeAdapterFactory;
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.*;

public class Gson_315_5Test {

    private Gson gson;

    @BeforeEach
    public void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    public void testDefaultConstructor_initializesFields() throws Exception {
        // Use reflection to access private fields and verify their initialization
        Field excluderField = Gson.class.getDeclaredField("excluder");
        excluderField.setAccessible(true);
        Excluder excluder = (Excluder) excluderField.get(gson);
        assertNotNull(excluder);
        assertEquals(Excluder.DEFAULT, excluder);

        Field fieldNamingStrategyField = Gson.class.getDeclaredField("fieldNamingStrategy");
        fieldNamingStrategyField.setAccessible(true);
        FieldNamingStrategy fieldNamingStrategy = (FieldNamingStrategy) fieldNamingStrategyField.get(gson);
        assertNotNull(fieldNamingStrategy);
        assertEquals(FieldNamingPolicy.IDENTITY, fieldNamingStrategy);

        Field serializeNullsField = Gson.class.getDeclaredField("serializeNulls");
        serializeNullsField.setAccessible(true);
        boolean serializeNulls = serializeNullsField.getBoolean(gson);
        assertFalse(serializeNulls);

        Field complexMapKeySerializationField = Gson.class.getDeclaredField("complexMapKeySerialization");
        complexMapKeySerializationField.setAccessible(true);
        boolean complexMapKeySerialization = complexMapKeySerializationField.getBoolean(gson);
        assertFalse(complexMapKeySerialization);

        Field generateNonExecutableJsonField = Gson.class.getDeclaredField("generateNonExecutableJson");
        generateNonExecutableJsonField.setAccessible(true);
        boolean generateNonExecutableJson = generateNonExecutableJsonField.getBoolean(gson);
        assertFalse(generateNonExecutableJson);

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

        Field serializeSpecialFloatingPointValuesField = Gson.class.getDeclaredField("serializeSpecialFloatingPointValues");
        serializeSpecialFloatingPointValuesField.setAccessible(true);
        boolean serializeSpecialFloatingPointValues = serializeSpecialFloatingPointValuesField.getBoolean(gson);
        assertFalse(serializeSpecialFloatingPointValues);

        Field useJdkUnsafeField = Gson.class.getDeclaredField("useJdkUnsafe");
        useJdkUnsafeField.setAccessible(true);
        boolean useJdkUnsafe = useJdkUnsafeField.getBoolean(gson);
        assertTrue(useJdkUnsafe);

        Field datePatternField = Gson.class.getDeclaredField("datePattern");
        datePatternField.setAccessible(true);
        String datePattern = (String) datePatternField.get(gson);
        assertNull(datePattern);

        Field dateStyleField = Gson.class.getDeclaredField("dateStyle");
        dateStyleField.setAccessible(true);
        int dateStyle = dateStyleField.getInt(gson);
        assertEquals(DateFormat.DEFAULT, dateStyle);

        Field timeStyleField = Gson.class.getDeclaredField("timeStyle");
        timeStyleField.setAccessible(true);
        int timeStyle = timeStyleField.getInt(gson);
        assertEquals(DateFormat.DEFAULT, timeStyle);

        Field longSerializationPolicyField = Gson.class.getDeclaredField("longSerializationPolicy");
        longSerializationPolicyField.setAccessible(true);
        LongSerializationPolicy longSerializationPolicy = (LongSerializationPolicy) longSerializationPolicyField.get(gson);
        assertEquals(LongSerializationPolicy.DEFAULT, longSerializationPolicy);

        Field objectToNumberStrategyField = Gson.class.getDeclaredField("objectToNumberStrategy");
        objectToNumberStrategyField.setAccessible(true);
        ToNumberStrategy objectToNumberStrategy = (ToNumberStrategy) objectToNumberStrategyField.get(gson);
        assertEquals(ToNumberPolicy.DOUBLE, objectToNumberStrategy);

        Field numberToNumberStrategyField = Gson.class.getDeclaredField("numberToNumberStrategy");
        numberToNumberStrategyField.setAccessible(true);
        ToNumberStrategy numberToNumberStrategy = (ToNumberStrategy) numberToNumberStrategyField.get(gson);
        assertEquals(ToNumberPolicy.LAZILY_PARSED_NUMBER, numberToNumberStrategy);

        Field reflectionFiltersField = Gson.class.getDeclaredField("reflectionFilters");
        reflectionFiltersField.setAccessible(true);
        List<?> reflectionFilters = (List<?>) reflectionFiltersField.get(gson);
        assertNotNull(reflectionFilters);
        assertTrue(reflectionFilters.isEmpty());

        Field factoriesField = Gson.class.getDeclaredField("factories");
        factoriesField.setAccessible(true);
        List<?> factories = (List<?>) factoriesField.get(gson);
        assertNotNull(factories);
        assertFalse(factories.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testNewBuilder_returnsNewGsonBuilder() {
        GsonBuilder builder = gson.newBuilder();
        assertNotNull(builder);
        assertNotSame(gson, builder);
    }

    @Test
    @Timeout(8000)
    public void testToString_containsClassName() {
        String str = gson.toString();
        assertNotNull(str);
        assertTrue(str.contains("Gson"));
    }
}