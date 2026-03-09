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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Reader;
import java.io.StringReader;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

class GsonFromJsonTest {

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    void fromJson_withValidJsonAndType_returnsExpectedObject() {
        String json = "{\"value\":\"test\"}";
        Reader reader = new StringReader(json);
        Type type = TypeToken.get(TestClass.class).getType();

        TestClass result = gson.fromJson(reader, type);

        assertNotNull(result);
        assertEquals("test", result.value);
    }

    @Test
    @Timeout(8000)
    void fromJson_withNullReader_throwsJsonSyntaxException() {
        Reader reader = null;
        Type type = TypeToken.get(TestClass.class).getType();

        assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, type));
    }

    @Test
    @Timeout(8000)
    void fromJson_withEmptyJson_throwsJsonSyntaxException() {
        Reader reader = new StringReader("");
        Type type = TypeToken.get(TestClass.class).getType();

        assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, type));
    }

    @Test
    @Timeout(8000)
    void fromJson_withMalformedJson_throwsJsonSyntaxException() {
        String json = "{value:\"test\""; // missing closing brace
        Reader reader = new StringReader(json);
        Type type = TypeToken.get(TestClass.class).getType();

        assertThrows(JsonSyntaxException.class, () -> gson.fromJson(reader, type));
    }

    @Test
    @Timeout(8000)
    void fromJson_invokesPrivateFromJsonWithTypeToken() throws Exception {
        String json = "{\"value\":\"test\"}";
        Reader reader = new StringReader(json);
        Type type = TypeToken.get(TestClass.class).getType();

        Method method = Gson.class.getDeclaredMethod("fromJson", Reader.class, TypeToken.class);
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        TestClass result = (TestClass) method.invoke(gson, reader, TypeToken.get(type));

        assertNotNull(result);
        assertEquals("test", result.value);
    }

    private static class TestClass {
        String value;
    }
}