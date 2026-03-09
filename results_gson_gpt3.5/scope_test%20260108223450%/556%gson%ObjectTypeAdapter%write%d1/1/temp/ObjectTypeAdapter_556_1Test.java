package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.Gson;
import com.google.gson.ToNumberStrategy;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;

public class ObjectTypeAdapter_556_1Test {

    private Gson gsonMock;
    private ToNumberStrategy toNumberStrategyMock;
    private ObjectTypeAdapter objectTypeAdapter;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() throws Exception {
        gsonMock = mock(Gson.class);
        toNumberStrategyMock = mock(ToNumberStrategy.class);
        // Using reflection to access private constructor
        Constructor<ObjectTypeAdapter> constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
        constructor.setAccessible(true);
        objectTypeAdapter = constructor.newInstance(gsonMock, toNumberStrategyMock);

        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);
    }

    @Test
    @Timeout(8000)
    public void testWrite_NullValue_CallsNullValue() throws IOException {
        objectTypeAdapter.write(jsonWriter, null);
        assertEquals("null", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testWrite_TypeAdapterIsObjectTypeAdapter_WritesEmptyObject() throws IOException {
        Object value = new Object();

        // Return the ObjectTypeAdapter instance when getAdapter is called with value's class TypeToken
        when(gsonMock.getAdapter(value.getClass())).thenReturn((TypeAdapter) objectTypeAdapter);

        objectTypeAdapter.write(jsonWriter, value);

        String jsonOutput = stringWriter.toString();
        assertEquals("{}", jsonOutput);
    }

    @Test
    @Timeout(8000)
    public void testWrite_TypeAdapterIsDifferent_DelegatesWrite() throws IOException {
        Object value = new Object();

        @SuppressWarnings("unchecked")
        TypeAdapter<Object> typeAdapterMock = mock(TypeAdapter.class);
        // Return the mocked TypeAdapter when getAdapter is called with value's class
        when(gsonMock.getAdapter(value.getClass())).thenReturn((TypeAdapter) typeAdapterMock);

        objectTypeAdapter.write(jsonWriter, value);

        verify(typeAdapterMock).write(jsonWriter, value);
    }
}