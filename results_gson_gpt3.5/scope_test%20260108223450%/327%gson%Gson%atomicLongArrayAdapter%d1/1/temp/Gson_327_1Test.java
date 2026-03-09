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
import java.io.IOException;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicLongArray;

class Gson_atomicLongArrayAdapter_Test {

    private Gson gson;

    @BeforeEach
    public void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    public void testWrite_and_Read_atomicLongArrayAdapter() throws Exception {
        // Arrange
        @SuppressWarnings("unchecked")
        TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);

        doAnswer(invocation -> {
            JsonWriter writer = invocation.getArgument(0);
            Number number = invocation.getArgument(1);
            writer.value(number.longValue());
            return null;
        }).when(longAdapter).write(any(JsonWriter.class), any(Number.class));

        doAnswer(invocation -> {
            JsonReader reader = invocation.getArgument(0);
            return reader.nextLong();
        }).when(longAdapter).read(any(JsonReader.class));

        Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

        AtomicLongArray arrayToWrite = new AtomicLongArray(new long[]{10L, 20L, 30L});

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act - write AtomicLongArray
        adapter.write(jsonWriter, arrayToWrite);
        jsonWriter.close();

        // Assert JSON output
        String jsonOutput = stringWriter.toString();
        assertEquals("[10,20,30]", jsonOutput);

        // Act - read AtomicLongArray from JSON
        StringReader stringReader = new StringReader(jsonOutput);
        JsonReader jsonReader = new JsonReader(stringReader);
        AtomicLongArray arrayRead = adapter.read(jsonReader);

        // Assert the read AtomicLongArray matches the original values
        assertNotNull(arrayRead);
        assertEquals(arrayToWrite.length(), arrayRead.length());
        for (int i = 0; i < arrayToWrite.length(); i++) {
            assertEquals(arrayToWrite.get(i), arrayRead.get(i));
        }
    }

    @Test
    @Timeout(8000)
    public void testWrite_nullAtomicLongArray() throws Exception {
        @SuppressWarnings("unchecked")
        TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);
        Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

        StringWriter stringWriter = new StringWriter();
        JsonWriter jsonWriter = new JsonWriter(stringWriter);

        // Act
        adapter.write(jsonWriter, null);
        jsonWriter.close();

        // Assert output is "null"
        assertEquals("null", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testRead_emptyArray() throws Exception {
        @SuppressWarnings("unchecked")
        TypeAdapter<Number> longAdapter = mock(TypeAdapter.class);

        doAnswer(invocation -> {
            JsonReader reader = invocation.getArgument(0);
            return reader.nextLong();
        }).when(longAdapter).read(any(JsonReader.class));

        Method method = Gson.class.getDeclaredMethod("atomicLongArrayAdapter", TypeAdapter.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        TypeAdapter<AtomicLongArray> adapter = (TypeAdapter<AtomicLongArray>) method.invoke(null, longAdapter);

        StringReader stringReader = new StringReader("[]");
        JsonReader jsonReader = new JsonReader(stringReader);

        // Act
        AtomicLongArray arrayRead = adapter.read(jsonReader);

        // Assert
        assertNotNull(arrayRead);
        assertEquals(0, arrayRead.length());
    }
}