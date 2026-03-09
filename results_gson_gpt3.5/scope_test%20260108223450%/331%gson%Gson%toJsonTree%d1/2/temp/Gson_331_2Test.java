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
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

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
    public void testToJsonTree_NonNullInput_UsesOverloadedMethod() throws Exception {
        String testString = "test";

        // Use reflection to access private toJsonTree(Object, Type) method
        Method toJsonTreeWithType = Gson.class.getDeclaredMethod("toJsonTree", Object.class, java.lang.reflect.Type.class);
        toJsonTreeWithType.setAccessible(true);

        // Spy on gson to verify call to private method
        Gson spyGson = spy(gson);

        // Call the public toJsonTree(Object) method
        JsonElement result = spyGson.toJsonTree(testString);

        // Verify that the private toJsonTree(Object, Type) method was called with correct args
        verify(spyGson).toJsonTree(testString, testString.getClass());

        // Also invoke the private method directly and compare results
        JsonElement directResult = (JsonElement) toJsonTreeWithType.invoke(gson, testString, testString.getClass());
        assertEquals(directResult, result);
    }

}