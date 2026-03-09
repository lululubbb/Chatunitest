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
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class GsonFromJsonStringTypeTokenTest {

    private Gson gson;

    @BeforeEach
    public void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    public void testFromJson_NullJson_ReturnsNull() {
        TypeToken<String> typeToken = TypeToken.get(String.class);
        String result = gson.fromJson((String) null, typeToken);
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_ValidJson_ReturnsObject() {
        String json = "\"hello\"";
        TypeToken<String> typeToken = TypeToken.get(String.class);
        String result = gson.fromJson(json, typeToken);
        assertEquals("hello", result);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_EmptyJson_ReturnsNullOrThrows() {
        String emptyJson = "";
        TypeToken<String> typeToken = TypeToken.get(String.class);
        assertThrows(JsonSyntaxException.class, () -> gson.fromJson(emptyJson, typeToken));
    }

    @Test
    @Timeout(8000)
    public void testFromJson_MalformedJson_ThrowsJsonSyntaxException() {
        String malformedJson = "{unclosed";
        TypeToken<Object> typeToken = TypeToken.get(Object.class);
        assertThrows(JsonSyntaxException.class, () -> gson.fromJson(malformedJson, typeToken));
    }

    @Test
    @Timeout(8000)
    public void testFromJson_InvokesPrivateFromJsonReader() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String json = "\"test\"";
        TypeToken<String> typeToken = TypeToken.get(String.class);

        Method fromJsonReaderMethod = Gson.class.getDeclaredMethod("fromJson", com.google.gson.stream.JsonReader.class, TypeToken.class);
        fromJsonReaderMethod.setAccessible(true);

        com.google.gson.stream.JsonReader jsonReader = new com.google.gson.stream.JsonReader(new StringReader(json));
        @SuppressWarnings("unchecked")
        String result = (String) fromJsonReaderMethod.invoke(gson, jsonReader, typeToken);

        assertEquals("test", result);
    }
}