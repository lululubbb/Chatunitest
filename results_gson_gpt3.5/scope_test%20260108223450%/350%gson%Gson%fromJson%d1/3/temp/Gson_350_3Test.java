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
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.EOFException;
import java.io.Reader;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

class GsonFromJsonTest {

    private Gson gson;

    @BeforeEach
    public void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    public void testFromJson_ValidJson_PrimitiveType() throws Exception {
        String json = "123";
        JsonReader reader = new JsonReader(new StringReader(json));
        Type type = Integer.class;

        Integer result = gson.fromJson(reader, type);

        assertEquals(123, result);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_ValidJson_CustomType() throws Exception {
        String json = "{\"name\":\"test\",\"age\":10}";
        JsonReader reader = new JsonReader(new StringReader(json));
        Type type = Person.class;

        Person result = gson.fromJson(reader, type);

        assertNotNull(result);
        assertEquals("test", result.name);
        assertEquals(10, result.age);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_EmptyJson_ThrowsJsonSyntaxException() throws Exception {
        String json = "";
        JsonReader reader = new JsonReader(new StringReader(json));
        Type type = Object.class;

        assertThrows(JsonSyntaxException.class, () -> {
            gson.fromJson(reader, type);
        });
    }

    @Test
    @Timeout(8000)
    public void testFromJson_JsonIOException_Propagated() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        Type type = Object.class;

        when(reader.peek()).thenThrow(new IOException("io error"));

        assertThrows(JsonIOException.class, () -> {
            gson.fromJson(reader, type);
        });
    }

    @Test
    @Timeout(8000)
    public void testFromJson_JsonSyntaxException_Propagated() throws Exception {
        JsonReader reader = mock(JsonReader.class);
        Type type = Object.class;

        when(reader.peek()).thenThrow(new MalformedJsonException("malformed"));

        assertThrows(JsonSyntaxException.class, () -> {
            gson.fromJson(reader, type);
        });
    }

    @Test
    @Timeout(8000)
    public void testFromJson_NullReader_ThrowsNullPointerException() {
        Type type = Object.class;
        assertThrows(NullPointerException.class, () -> {
            gson.fromJson((JsonReader) null, type);
        });
    }

    @Test
    @Timeout(8000)
    public void testFromJson_UsesThreadLocalCache() throws Exception {
        // Use reflection to get threadLocalAdapterResults field and ensure it is used
        Field threadLocalAdapterResultsField = Gson.class.getDeclaredField("threadLocalAdapterResults");
        threadLocalAdapterResultsField.setAccessible(true);

        ThreadLocal<?> threadLocalAdapterResults = (ThreadLocal<?>) threadLocalAdapterResultsField.get(gson);
        assertNotNull(threadLocalAdapterResults);

        String json = "true";
        JsonReader reader = new JsonReader(new StringReader(json));
        Type type = Boolean.class;

        Boolean result = gson.fromJson(reader, type);
        assertTrue(result);
    }

    // Helper class for testing custom object deserialization
    static class Person {
        String name;
        int age;
    }
}