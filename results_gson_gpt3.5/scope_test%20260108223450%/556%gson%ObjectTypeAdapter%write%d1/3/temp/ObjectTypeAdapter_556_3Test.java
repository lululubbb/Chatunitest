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
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.ToNumberStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Constructor;

public class ObjectTypeAdapter_556_3Test {

    private Gson mockGson;
    private JsonWriter jsonWriter;
    private StringWriter stringWriter;
    private ObjectTypeAdapter objectTypeAdapter;

    @BeforeEach
    public void setUp() throws Exception {
        mockGson = mock(Gson.class);
        stringWriter = new StringWriter();
        jsonWriter = new JsonWriter(stringWriter);

        Constructor<ObjectTypeAdapter> constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
        constructor.setAccessible(true);
        objectTypeAdapter = constructor.newInstance(mockGson, null);
    }

    @Test
    @Timeout(8000)
    public void testWrite_nullValue_callsNullValue() throws IOException {
        objectTypeAdapter.write(jsonWriter, null);
        assertEquals("null", stringWriter.toString());
    }

    @Test
    @Timeout(8000)
    public void testWrite_typeAdapterIsObjectTypeAdapter_writesEmptyObject() throws IOException {
        Object value = new Object();

        @SuppressWarnings("unchecked")
        TypeAdapter<Object> mockTypeAdapter = (TypeAdapter<Object>) (TypeAdapter<?>) objectTypeAdapter;
        // Use TypeToken to capture the class type for mocking
        when(mockGson.<Object>getAdapter((Class<Object>) value.getClass())).thenReturn(mockTypeAdapter);

        objectTypeAdapter.write(jsonWriter, value);

        String json = stringWriter.toString();
        assertEquals("{}", json);
    }

    @Test
    @Timeout(8000)
    public void testWrite_typeAdapterIsNotObjectTypeAdapter_delegatesWrite() throws IOException {
        Object value = new Object();

        @SuppressWarnings("unchecked")
        TypeAdapter<Object> mockTypeAdapter = mock(TypeAdapter.class);
        when(mockGson.<Object>getAdapter((Class<Object>) value.getClass())).thenReturn(mockTypeAdapter);

        objectTypeAdapter.write(jsonWriter, value);

        verify(mockTypeAdapter).write(jsonWriter, value);
    }
}