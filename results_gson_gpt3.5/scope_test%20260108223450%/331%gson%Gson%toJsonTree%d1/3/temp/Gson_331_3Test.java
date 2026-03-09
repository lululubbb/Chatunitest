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
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void toJsonTree_nonNullInput_invokesToJsonTreeWithClass() throws Exception {
        String src = "testString";

        // Spy on gson to verify the call to toJsonTree(Object, Type)
        Gson spyGson = spy(gson);

        // Call toJsonTree(Object)
        JsonElement result = spyGson.toJsonTree(src);

        // Verify that toJsonTree(Object, Type) is called with src and src.getClass()
        verify(spyGson).toJsonTree(src, src.getClass());

        // Use reflection to get the toJsonTree(Object, Type) method (should be public)
        Method toJsonTreeWithType = Gson.class.getDeclaredMethod("toJsonTree", Object.class, Type.class);
        toJsonTreeWithType.setAccessible(true);

        // Also verify that the result is same as the one returned by toJsonTree(Object, Type)
        JsonElement expected = (JsonElement) toJsonTreeWithType.invoke(gson, src, src.getClass());
        assertEquals(expected, result);
    }
}