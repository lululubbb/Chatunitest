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
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentMap;

class GsonFromJsonTest {

    private Gson gson;
    private JsonReader jsonReader;
    private TypeToken<String> typeToken;
    private TypeAdapter<String> typeAdapter;

    @BeforeEach
    void setUp() throws Exception {
        gson = spy(new Gson());
        jsonReader = mock(JsonReader.class);
        typeToken = TypeToken.get(String.class);
        typeAdapter = mock(TypeAdapter.class);

        // Inject the mocked typeAdapter into gson's typeTokenCache to avoid calling getAdapter real method
        Field typeTokenCacheField = Gson.class.getDeclaredField("typeTokenCache");
        typeTokenCacheField.setAccessible(true);
        @SuppressWarnings("unchecked")
        ConcurrentMap<TypeToken<?>, TypeAdapter<?>> cache =
                (ConcurrentMap<TypeToken<?>, TypeAdapter<?>>) typeTokenCacheField.get(gson);
        cache.put(typeToken, typeAdapter);

        // Stub getAdapter(TypeToken) to call real method except for typeToken
        doAnswer(invocation -> {
            TypeToken<?> arg = invocation.getArgument(0);
            if (arg.equals(typeToken)) {
                return typeAdapter;
            } else {
                return invocation.callRealMethod();
            }
        }).when(gson).getAdapter(any(TypeToken.class));
    }

    @Test
    @Timeout(8000)
    void fromJson_returnsObject_whenNormalFlow() throws IOException {
        when(jsonReader.isLenient()).thenReturn(false);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(typeAdapter.read(jsonReader)).thenReturn("result");

        String result = gson.fromJson(jsonReader, typeToken);

        assertEquals("result", result);

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).isLenient();
        inOrder.verify(jsonReader).setLenient(true);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).setLenient(false);
    }

    @Test
    @Timeout(8000)
    void fromJson_returnsNull_whenEmptyDocument() throws IOException {
        when(jsonReader.isLenient()).thenReturn(false);
        when(jsonReader.peek()).thenThrow(new EOFException());

        String result = gson.fromJson(jsonReader, typeToken);

        assertNull(result);

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).isLenient();
        inOrder.verify(jsonReader).setLenient(true);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).setLenient(false);
    }

    @Test
    @Timeout(8000)
    void fromJson_throwsJsonSyntaxException_whenEOFExceptionNotEmpty() throws IOException {
        when(jsonReader.isLenient()).thenReturn(false);
        when(jsonReader.peek()).thenReturn(JsonToken.STRING);
        when(typeAdapter.read(jsonReader)).thenThrow(new EOFException());

        JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> gson.fromJson(jsonReader, typeToken));
        assertTrue(ex.getCause() instanceof EOFException);

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).isLenient();
        inOrder.verify(jsonReader).setLenient(true);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).setLenient(false);
    }

    @Test
    @Timeout(8000)
    void fromJson_throwsJsonSyntaxException_whenIllegalStateException() throws IOException {
        when(jsonReader.isLenient()).thenReturn(false);
        when(jsonReader.peek()).thenThrow(new IllegalStateException());

        JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> gson.fromJson(jsonReader, typeToken));
        assertTrue(ex.getCause() instanceof IllegalStateException);

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).isLenient();
        inOrder.verify(jsonReader).setLenient(true);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).setLenient(false);
    }

    @Test
    @Timeout(8000)
    void fromJson_throwsJsonSyntaxException_whenIOException() throws IOException {
        when(jsonReader.isLenient()).thenReturn(false);
        when(jsonReader.peek()).thenThrow(new IOException());

        JsonSyntaxException ex = assertThrows(JsonSyntaxException.class, () -> gson.fromJson(jsonReader, typeToken));
        assertTrue(ex.getCause() instanceof IOException);

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).isLenient();
        inOrder.verify(jsonReader).setLenient(true);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).setLenient(false);
    }

    @Test
    @Timeout(8000)
    void fromJson_throwsAssertionError_withMessage() throws IOException, NoSuchFieldException, IllegalAccessException {
        when(jsonReader.isLenient()).thenReturn(false);
        when(jsonReader.peek()).thenThrow(new AssertionError("assertion failed"));

        // Inject GsonBuildConfig.VERSION via reflection to avoid NPE in error message
        try {
            Class<?> buildConfigClass = Class.forName("com.google.gson.GsonBuildConfig");
            Field versionField = buildConfigClass.getDeclaredField("VERSION");
            versionField.setAccessible(true);
            versionField.set(null, "2.10");
        } catch (ClassNotFoundException ignored) {
            // If GsonBuildConfig not found, skip setting version, test still runs but message may differ
        }

        AssertionError error = assertThrows(AssertionError.class, () -> gson.fromJson(jsonReader, typeToken));
        assertTrue(error.getMessage().contains("AssertionError (GSON"));
        assertTrue(error.getMessage().contains("assertion failed"));
        assertNotNull(error.getCause());
        assertEquals("assertion failed", error.getCause().getMessage());

        InOrder inOrder = inOrder(jsonReader);
        inOrder.verify(jsonReader).isLenient();
        inOrder.verify(jsonReader).setLenient(true);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).setLenient(false);
    }
}