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

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

class Gson_toJsonTree_Test {

    private Gson gson;

    @BeforeEach
    public void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    public void testToJsonTree_NullInput_ReturnsJsonNullInstance() {
        JsonElement result = gson.toJsonTree(null);
        assertSame(JsonNull.INSTANCE, result);
    }

    @Test
    @Timeout(8000)
    public void testToJsonTree_WithPrimitiveInteger() {
        Integer src = 123;
        JsonElement result = gson.toJsonTree(src);
        assertNotNull(result);
        // The JsonElement should be a JsonPrimitive representing the integer 123
        assertEquals("123", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testToJsonTree_WithString() {
        String src = "testString";
        JsonElement result = gson.toJsonTree(src);
        assertNotNull(result);
        assertEquals("\"testString\"", result.toString());
    }

    @Test
    @Timeout(8000)
    public void testToJsonTree_WithCustomObject() {
        TestClass src = new TestClass(10, "hello");
        JsonElement result = gson.toJsonTree(src);
        assertNotNull(result);
        String json = result.toString();
        assertTrue(json.contains("\"number\":10"));
        assertTrue(json.contains("\"text\":\"hello\""));
    }

    @Test
    @Timeout(8000)
    public void testToJsonTree_InvokesPrivateToJsonTreeWithType() throws Exception {
        String src = "reflectionTest";
        Method toJsonTreeMethod = Gson.class.getDeclaredMethod("toJsonTree", Object.class, Type.class);
        toJsonTreeMethod.setAccessible(true);
        // Pass String.class as the Type argument
        Object result = toJsonTreeMethod.invoke(gson, src, (Type) String.class);
        assertNotNull(result);
        assertTrue(result instanceof JsonElement);
        assertEquals("\"reflectionTest\"", result.toString());
    }

    static class TestClass {
        int number;
        String text;

        TestClass(int number, String text) {
            this.number = number;
            this.text = text;
        }
    }
}