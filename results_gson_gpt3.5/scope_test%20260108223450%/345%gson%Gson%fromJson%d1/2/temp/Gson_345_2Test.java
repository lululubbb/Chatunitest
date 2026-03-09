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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.Reader;
import java.lang.reflect.Method;

class GsonFromJsonStringTypeTokenTest {

    private Gson gson;

    @BeforeEach
    public void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    public void testFromJson_NullString_ReturnsNull() throws Exception {
        TypeToken<String> typeToken = TypeToken.get(String.class);
        String json = null;

        String result = gson.fromJson(json, typeToken);

        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_ValidJsonString_CallsFromJsonReader() throws Exception {
        String json = "{\"key\":\"value\"}";
        TypeToken<MyClass> typeToken = TypeToken.get(MyClass.class);

        // Spy on Gson to verify internal calls
        Gson spyGson = Mockito.spy(gson);

        // Use reflection to get the fromJson(Reader, TypeToken) method
        Method fromJsonReaderMethod = Gson.class.getDeclaredMethod("fromJson", Reader.class, TypeToken.class);
        fromJsonReaderMethod.setAccessible(true);

        // Stub the fromJson(Reader, TypeToken) call to return null to avoid real deserialization
        doReturn(null).when(spyGson).fromJson(any(Reader.class), eq(typeToken));

        // Invoke the focal method
        Object result = spyGson.fromJson(json, typeToken);

        // Verify that fromJson(Reader, TypeToken) was called internally with any Reader and correct typeToken
        verify(spyGson).fromJson(any(Reader.class), eq(typeToken));

        // Result can be null or an instance depending on MyClass definition, here just check no exception thrown
        assertNull(result);
    }

    // Helper class to use as TypeToken generic parameter
    static class MyClass {
        String key;
    }
}