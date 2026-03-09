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
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GsonToJsonTest {

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    void testToJson_NullInput_ReturnsJsonNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String json = gson.toJson(null);
        assertEquals("null", json);
    }

    @Test
    @Timeout(8000)
    void testToJson_StringInput_ReturnsJsonString() {
        String input = "testString";
        String json = gson.toJson(input);
        assertEquals("\"testString\"", json);
    }

    @Test
    @Timeout(8000)
    void testToJson_IntegerInput_ReturnsJsonNumber() {
        Integer input = 123;
        String json = gson.toJson(input);
        assertEquals("123", json);
    }

    @Test
    @Timeout(8000)
    void testToJson_ObjectInput_ReturnsJsonObject() {
        TestClass input = new TestClass("abc", 10);
        String json = gson.toJson(input);
        assertTrue(json.contains("\"field1\":\"abc\""));
        assertTrue(json.contains("\"field2\":10"));
    }

    @Test
    @Timeout(8000)
    void testToJson_PrivateMethodToJson_ObjectInput_ReflectionInvocation() throws Exception {
        Method toJsonMethod = Gson.class.getDeclaredMethod("toJson", Object.class);
        toJsonMethod.setAccessible(true);

        TestClass input = new TestClass("xyz", 5);
        Object result = toJsonMethod.invoke(gson, input);

        assertNotNull(result);
        assertTrue(result instanceof String);
        String json = (String) result;
        assertTrue(json.contains("\"field1\":\"xyz\""));
        assertTrue(json.contains("\"field2\":5"));
    }

    static class TestClass {
        String field1;
        int field2;

        TestClass(String field1, int field2) {
            this.field1 = field1;
            this.field2 = field2;
        }
    }
}