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
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class Gson_toJsonTree_Test {

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    void toJsonTree_nullInput_returnsJsonNullInstance() {
        JsonElement result = gson.toJsonTree(null);
        assertSame(JsonNull.INSTANCE, result);
    }

    @Test
    @Timeout(8000)
    void toJsonTree_nonNullInput_callsToJsonTreeWithType() {
        Object src = "string";
        JsonElement expected = new JsonPrimitive("string");

        // Spy on gson to verify toJsonTree(Object, Type) call
        Gson spyGson = Mockito.spy(gson);

        // Use TypeToken to get the exact Type for String.class
        Type srcType = src.getClass();

        // Mock toJsonTree(Object, Type) to return expected only when called with exact parameters
        doReturn(expected).when(spyGson).toJsonTree(eq(src), eq(srcType));

        JsonElement actual = spyGson.toJsonTree(src);

        assertSame(expected, actual);
        verify(spyGson).toJsonTree(src, srcType);
    }

    @Test
    @Timeout(8000)
    void toJsonTree_privateToJsonTreeMethod_invokedViaReflection() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String src = "reflectionTest";

        Method toJsonTreeMethod = Gson.class.getDeclaredMethod("toJsonTree", Object.class, Type.class);
        toJsonTreeMethod.setAccessible(true);

        JsonElement result = (JsonElement) toJsonTreeMethod.invoke(gson, src, (Type) src.getClass());

        assertNotNull(result);
        assertTrue(result.isJsonPrimitive());
        assertEquals(src, result.getAsString());
    }

    @Test
    @Timeout(8000)
    void toJsonTree_withCustomObject_returnsJsonObject() throws Exception {
        class Custom {
            String field = "value";
        }
        Custom custom = new Custom();

        Method toJsonTreeMethod = Gson.class.getDeclaredMethod("toJsonTree", Object.class, Type.class);
        toJsonTreeMethod.setAccessible(true);

        JsonElement jsonElement = (JsonElement) toJsonTreeMethod.invoke(gson, custom, (Type) custom.getClass());

        assertNotNull(jsonElement);
        assertTrue(jsonElement.isJsonObject());
        assertEquals("value", jsonElement.getAsJsonObject().get("field").getAsString());
    }

    @Test
    @Timeout(8000)
    void toJsonTree_withPrimitiveInteger_returnsJsonPrimitive() throws Exception {
        Integer value = 123;

        Method toJsonTreeMethod = Gson.class.getDeclaredMethod("toJsonTree", Object.class, Type.class);
        toJsonTreeMethod.setAccessible(true);

        JsonElement jsonElement = (JsonElement) toJsonTreeMethod.invoke(gson, value, (Type) Integer.class);

        assertNotNull(jsonElement);
        assertTrue(jsonElement.isJsonPrimitive());
        assertEquals(123, jsonElement.getAsInt());
    }

}