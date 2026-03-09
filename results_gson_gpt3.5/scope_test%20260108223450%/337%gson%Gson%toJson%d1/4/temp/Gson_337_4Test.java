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
import com.google.gson.stream.MalformedJsonException;
import java.io.EOFException;
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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.concurrent.ConcurrentMap;

class Gson_toJson_Test {

    private Gson gson;
    private JsonWriter writer;
    private TypeAdapter<Object> adapterMock;

    @BeforeEach
    public void setUp() throws Exception {
        gson = new Gson();
        writer = mock(JsonWriter.class);
        adapterMock = mock(TypeAdapter.class);

        // Use reflection to inject adapterMock into gson's typeTokenCache to avoid spying on final method
        Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
        typeTokenCacheField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ConcurrentMap<TypeToken<?>, TypeAdapter<?>> cache =
                (ConcurrentMap<TypeToken<?>, TypeAdapter<?>>) typeTokenCacheField.get(gson);
        cache.put(TypeToken.get(Object.class), adapterMock);
    }

    @Test
    @Timeout(8000)
    public void toJson_shouldWriteJsonSuccessfully() throws IOException {
        Object src = new Object();
        Type typeOfSrc = Object.class;

        when(writer.isLenient()).thenReturn(false);
        when(writer.isHtmlSafe()).thenReturn(false);
        when(writer.getSerializeNulls()).thenReturn(false);

        gson.toJson(src, typeOfSrc, writer);

        InOrder inOrder = inOrder(writer, adapterMock);
        inOrder.verify(writer).isLenient();
        inOrder.verify(writer).setLenient(true);
        inOrder.verify(writer).isHtmlSafe();
        inOrder.verify(writer).setHtmlSafe(gson.htmlSafe());
        inOrder.verify(writer).getSerializeNulls();
        inOrder.verify(writer).setSerializeNulls(gson.serializeNulls());
        inOrder.verify(adapterMock).write(writer, src);
        inOrder.verify(writer).setLenient(false);
        inOrder.verify(writer).setHtmlSafe(false);
        inOrder.verify(writer).setSerializeNulls(false);
    }

    @Test
    @Timeout(8000)
    public void toJson_shouldThrowJsonIOExceptionWhenIOException() throws IOException {
        Object src = new Object();
        Type typeOfSrc = Object.class;

        when(writer.isLenient()).thenReturn(false);
        when(writer.isHtmlSafe()).thenReturn(false);
        when(writer.getSerializeNulls()).thenReturn(false);

        doThrow(new IOException("IO error")).when(adapterMock).write(writer, src);

        JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
            gson.toJson(src, typeOfSrc, writer);
        });
        assertEquals("java.io.IOException: IO error", thrown.getCause().toString());
    }

    @Test
    @Timeout(8000)
    public void toJson_shouldThrowAssertionErrorWithVersionOnAssertionError() throws IOException {
        Object src = new Object();
        Type typeOfSrc = Object.class;

        when(writer.isLenient()).thenReturn(false);
        when(writer.isHtmlSafe()).thenReturn(false);
        when(writer.getSerializeNulls()).thenReturn(false);

        AssertionError ae = new AssertionError("assertion failed");
        doThrow(ae).when(adapterMock).write(writer, src);

        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            gson.toJson(src, typeOfSrc, writer);
        });
        assertTrue(thrown.getMessage().contains("AssertionError (GSON "));
        assertTrue(thrown.getMessage().contains("assertion failed"));
        assertSame(ae, thrown.getCause());
    }

    @Test
    @Timeout(8000)
    public void toJson_shouldRestoreWriterStateIfException() throws IOException {
        Object src = new Object();
        Type typeOfSrc = Object.class;

        // Setup initial writer state
        when(writer.isLenient()).thenReturn(true);
        when(writer.isHtmlSafe()).thenReturn(true);
        when(writer.getSerializeNulls()).thenReturn(true);

        doThrow(new IOException("IO error")).when(adapterMock).write(writer, src);

        try {
            gson.toJson(src, typeOfSrc, writer);
            fail("Expected JsonIOException");
        } catch (JsonIOException e) {
            // expected
        }

        InOrder inOrder = inOrder(writer);
        inOrder.verify(writer).isLenient();
        inOrder.verify(writer).setLenient(true);
        inOrder.verify(writer).isHtmlSafe();
        inOrder.verify(writer).setHtmlSafe(true);
        inOrder.verify(writer).getSerializeNulls();
        inOrder.verify(writer).setSerializeNulls(true);
        inOrder.verify(writer).setLenient(true); // reset to oldLenient in finally
        inOrder.verify(writer).setHtmlSafe(true); // reset to oldHtmlSafe in finally
        inOrder.verify(writer).setSerializeNulls(true); // reset to oldSerializeNulls in finally
    }
}