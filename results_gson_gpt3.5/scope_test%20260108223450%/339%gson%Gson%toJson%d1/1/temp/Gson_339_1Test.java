package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.internal.LazilyParsedNumber;
import com.google.gson.internal.Primitives;
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
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
import java.io.Reader;
import java.io.StringReader;
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

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;

import com.google.gson.JsonIOException;

import com.google.gson.internal.Streams;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Method;

class Gson_toJson_Test {

    private Gson gson;

    @BeforeEach
    void setUp() {
        gson = new Gson();
    }

    @Test
    @Timeout(8000)
    void toJson_withValidJsonElementAndAppendable_writesJson() throws Exception {
        // Prepare JsonElement mock
        JsonElement jsonElement = mock(JsonElement.class);

        // Prepare Appendable mock (using StringWriter to verify output)
        StringWriter writer = new StringWriter();

        // Spy on Gson to mock newJsonWriter and toJson(JsonElement, JsonWriter)
        Gson spyGson = spy(gson);

        // Mock JsonWriter returned by newJsonWriter
        JsonWriter jsonWriter = mock(JsonWriter.class);
        doReturn(jsonWriter).when(spyGson).newJsonWriter(any());

        // Do nothing when toJson(JsonElement, JsonWriter) is called
        doNothing().when(spyGson).toJson(eq(jsonElement), eq(jsonWriter));

        // Call the method under test
        spyGson.toJson(jsonElement, writer);

        // Verify newJsonWriter called with writer wrapped by Streams.writerForAppendable
        verify(spyGson).newJsonWriter(any());

        // Verify toJson(JsonElement, JsonWriter) called with correct arguments
        verify(spyGson).toJson(jsonElement, jsonWriter);
    }

    @Test
    @Timeout(8000)
    void toJson_whenIOExceptionThrown_throwsJsonIOException() throws Exception {
        JsonElement jsonElement = mock(JsonElement.class);
        Appendable writer = mock(Appendable.class);

        // Spy on Gson to mock newJsonWriter to throw IOException
        Gson spyGson = spy(gson);

        doThrow(new IOException("io error")).when(spyGson).newJsonWriter(any());

        JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
            spyGson.toJson(jsonElement, writer);
        });

        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof IOException);
        assertEquals("io error", thrown.getCause().getMessage());
    }

    @Test
    @Timeout(8000)
    void toJson_privateMethod_invocation_viaReflection() throws Exception {
        // Use reflection to invoke private method newJsonWriter(Writer)
        StringWriter stringWriter = new StringWriter();

        Method newJsonWriterMethod = Gson.class.getDeclaredMethod("newJsonWriter", java.io.Writer.class);
        newJsonWriterMethod.setAccessible(true);

        JsonWriter jsonWriter = (JsonWriter) newJsonWriterMethod.invoke(gson, stringWriter);

        assertNotNull(jsonWriter);
    }
}