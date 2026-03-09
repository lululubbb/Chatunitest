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
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.Reader;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLongArray;

class Gson_atomicLongArrayAdapter_Test {

    private TypeAdapter<Number> mockLongAdapter;
    private TypeAdapter<AtomicLongArray> atomicLongArrayAdapter;

    @BeforeEach
    public void setUp() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // Create a mock TypeAdapter<Number>
        mockLongAdapter = mock(TypeAdapter.class);

        // Use reflection to invoke private static method atomicLongArrayAdapter(TypeAdapter<Number>)
        Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
        method.setAccessible(true);
        atomicLongArrayAdapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, mockLongAdapter);
    }

    @Test
    @Timeout(8000)
    public void testWrite_atomicLongArray() throws IOException {
        AtomicLongArray array = new AtomicLongArray(new long[]{10L, 20L, 30L});

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Mock longAdapter.write to write the long value as JSON number
        doAnswer(invocation -> {
            JsonWriter out = invocation.getArgument(0);
            Number value = invocation.getArgument(1);
            out.value(value.longValue());
            return null;
        }).when(mockLongAdapter).write(any(JsonWriter.class), any(Number.class));

        atomicLongArrayAdapter.write(jsonWriter, array);
        jsonWriter.close();

        assertEquals("[10,20,30]", stringWriter.toString());

        // Verify that longAdapter.write called for each element
        verify(mockLongAdapter, times(3)).write(any(JsonWriter.class), any(Number.class));
    }

    @Test
    @Timeout(8000)
    public void testWrite_null() throws IOException {
        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        atomicLongArrayAdapter.write(jsonWriter, null);
        jsonWriter.close();

        assertEquals("null", stringWriter.toString());

        // longAdapter.write should never be called
        verify(mockLongAdapter, never()).write(any(JsonWriter.class), any(Number.class));
    }

    @Test
    @Timeout(8000)
    public void testRead_atomicLongArray() throws IOException {
        String json = "[100,200,300]";

        JsonReader jsonReader = new JsonReader(new StringReader(json));

        // Mock longAdapter.read to read the next long value from JsonReader
        doAnswer(invocation -> {
            JsonReader in = invocation.getArgument(0);
            return in.nextLong();
        }).when(mockLongAdapter).read(any(JsonReader.class));

        AtomicLongArray result = atomicLongArrayAdapter.read(jsonReader);

        assertNotNull(result);
        assertEquals(3, result.length());
        assertEquals(100L, result.get(0));
        assertEquals(200L, result.get(1));
        assertEquals(300L, result.get(2));

        verify(mockLongAdapter, times(3)).read(any(JsonReader.class));
    }

    @Test
    @Timeout(8000)
    public void testRead_emptyArray() throws IOException {
        String json = "[]";

        JsonReader jsonReader = new JsonReader(new StringReader(json));

        // Mock longAdapter.read to avoid unexpected calls
        doAnswer(invocation -> {
            JsonReader in = invocation.getArgument(0);
            return in.nextLong();
        }).when(mockLongAdapter).read(any(JsonReader.class));

        AtomicLongArray result = atomicLongArrayAdapter.read(jsonReader);

        assertNotNull(result);
        assertEquals(0, result.length());

        verify(mockLongAdapter, never()).read(any(JsonReader.class));
    }

    @Test
    @Timeout(8000)
    public void testRead_nullReturnsNull() throws IOException {
        String json = "null";

        JsonReader jsonReader = new JsonReader(new StringReader(json));

        // Mock longAdapter.read to avoid unexpected calls
        doAnswer(invocation -> {
            JsonReader in = invocation.getArgument(0);
            return in.nextLong();
        }).when(mockLongAdapter).read(any(JsonReader.class));

        AtomicLongArray result = atomicLongArrayAdapter.read(jsonReader);

        assertNull(result);

        verify(mockLongAdapter, never()).read(any(JsonReader.class));
    }
}