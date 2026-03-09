package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.Excluder;
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
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;
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
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.GsonBuildConfig;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Field;

public class GsonFromJsonTest {

    private Gson gson;
    private JsonReader jsonReaderMock;
    private TypeToken<String> typeToken;
    private TypeAdapter<String> typeAdapterMock;

    @BeforeEach
    public void setUp() throws Exception {
        gson = new Gson();
        jsonReaderMock = mock(JsonReader.class);
        typeToken = TypeToken.get(String.class);
        typeAdapterMock = mock(TypeAdapter.class);

        // Inject typeAdapterMock into gson's typeTokenCache to return our mock adapter
        Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
        typeTokenCacheField.setAccessible(true);
        @SuppressWarnings("unchecked")
        java.util.concurrent.ConcurrentMap<TypeToken<?>, TypeAdapter<?>> cache =
                (java.util.concurrent.ConcurrentMap<TypeToken<?>, TypeAdapter<?>>) typeTokenCacheField.get(gson);
        cache.put(typeToken, typeAdapterMock);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_NormalFlow() throws Exception {
        when(jsonReaderMock.isLenient()).thenReturn(false);
        when(jsonReaderMock.peek()).thenReturn(JsonToken.STRING);
        when(typeAdapterMock.read(jsonReaderMock)).thenReturn("result");

        String result = gson.fromJson(jsonReaderMock, typeToken);

        assertEquals("result", result);

        InOrder inOrder = inOrder(jsonReaderMock);
        inOrder.verify(jsonReaderMock).isLenient();
        inOrder.verify(jsonReaderMock).setLenient(true);
        inOrder.verify(jsonReaderMock).peek();
        inOrder.verify(jsonReaderMock).setLenient(false);

        verify(typeAdapterMock).read(jsonReaderMock);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_EmptyDocument_ReturnsNull() throws Exception {
        when(jsonReaderMock.isLenient()).thenReturn(false);
        when(jsonReaderMock.peek()).thenThrow(new EOFException());

        String result = gson.fromJson(jsonReaderMock, typeToken);

        assertNull(result);

        InOrder inOrder = inOrder(jsonReaderMock);
        inOrder.verify(jsonReaderMock).isLenient();
        inOrder.verify(jsonReaderMock).setLenient(true);
        inOrder.verify(jsonReaderMock).peek();
        inOrder.verify(jsonReaderMock).setLenient(false);

        verifyNoInteractions(typeAdapterMock);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_UnexpectedEOF_ThrowsJsonSyntaxException() throws Exception {
        when(jsonReaderMock.isLenient()).thenReturn(false);
        // Simulate peek throws EOFException but after peek was successful once (isEmpty false)
        // To simulate this, we do peek() throws EOFException after isEmpty=false
        // We need to simulate peek() once successful, then second call throws EOFException
        when(jsonReaderMock.peek()).thenReturn(JsonToken.STRING).thenThrow(new EOFException());

        // We need to mock typeAdapter.read to throw EOFException to simulate the second peek failing
        when(typeAdapterMock.read(jsonReaderMock)).thenThrow(new EOFException());

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            gson.fromJson(jsonReaderMock, typeToken);
        });

        assertTrue(thrown.getCause() instanceof EOFException);

        InOrder inOrder = inOrder(jsonReaderMock);
        inOrder.verify(jsonReaderMock).isLenient();
        inOrder.verify(jsonReaderMock).setLenient(true);
        inOrder.verify(jsonReaderMock).peek();
        inOrder.verify(jsonReaderMock).setLenient(false);

        verify(typeAdapterMock).read(jsonReaderMock);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_IllegalStateException_ThrowsJsonSyntaxException() throws Exception {
        when(jsonReaderMock.isLenient()).thenReturn(false);
        when(jsonReaderMock.peek()).thenReturn(JsonToken.STRING);
        when(typeAdapterMock.read(jsonReaderMock)).thenThrow(new IllegalStateException("illegal"));

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            gson.fromJson(jsonReaderMock, typeToken);
        });

        assertTrue(thrown.getCause() instanceof IllegalStateException);

        InOrder inOrder = inOrder(jsonReaderMock);
        inOrder.verify(jsonReaderMock).isLenient();
        inOrder.verify(jsonReaderMock).setLenient(true);
        inOrder.verify(jsonReaderMock).peek();
        inOrder.verify(jsonReaderMock).setLenient(false);

        verify(typeAdapterMock).read(jsonReaderMock);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_IOException_ThrowsJsonSyntaxException() throws Exception {
        when(jsonReaderMock.isLenient()).thenReturn(false);
        when(jsonReaderMock.peek()).thenReturn(JsonToken.STRING);
        when(typeAdapterMock.read(jsonReaderMock)).thenThrow(new IOException("io"));

        JsonSyntaxException thrown = assertThrows(JsonSyntaxException.class, () -> {
            gson.fromJson(jsonReaderMock, typeToken);
        });

        assertTrue(thrown.getCause() instanceof IOException);

        InOrder inOrder = inOrder(jsonReaderMock);
        inOrder.verify(jsonReaderMock).isLenient();
        inOrder.verify(jsonReaderMock).setLenient(true);
        inOrder.verify(jsonReaderMock).peek();
        inOrder.verify(jsonReaderMock).setLenient(false);

        verify(typeAdapterMock).read(jsonReaderMock);
    }

    @Test
    @Timeout(8000)
    public void testFromJson_AssertionError_ThrowsAssertionErrorWithMessage() throws Exception {
        when(jsonReaderMock.isLenient()).thenReturn(false);
        when(jsonReaderMock.peek()).thenReturn(JsonToken.STRING);
        AssertionError error = new AssertionError("assertion failed");
        when(typeAdapterMock.read(jsonReaderMock)).thenThrow(error);

        AssertionError thrown = assertThrows(AssertionError.class, () -> {
            gson.fromJson(jsonReaderMock, typeToken);
        });

        assertTrue(thrown.getMessage().contains("AssertionError (GSON " + GsonBuildConfig.VERSION + "): assertion failed"));
        assertSame(error, thrown.getCause());

        InOrder inOrder = inOrder(jsonReaderMock);
        inOrder.verify(jsonReaderMock).isLenient();
        inOrder.verify(jsonReaderMock).setLenient(true);
        inOrder.verify(jsonReaderMock).peek();
        inOrder.verify(jsonReaderMock).setLenient(false);

        verify(typeAdapterMock).read(jsonReaderMock);
    }
}