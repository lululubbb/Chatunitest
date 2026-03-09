package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;

public class ArrayTypeAdapter_270_4Test {

    private ArrayTypeAdapter<Integer> adapter;
    private TypeAdapter<Integer> componentTypeAdapter;
    private JsonReader jsonReader;

    @BeforeEach
    public void setUp() {
        componentTypeAdapter = mock(TypeAdapter.class);
        adapter = new ArrayTypeAdapter<>(null, componentTypeAdapter, int.class);
        jsonReader = mock(JsonReader.class);
    }

    @Test
    @Timeout(8000)
    public void testRead_NullJsonToken_ReturnsNull() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.NULL);

        Object result = adapter.read(jsonReader);

        verify(jsonReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_PrimitiveArray_ReadsCorrectly() throws IOException {
        when(jsonReader.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.NUMBER, JsonToken.NUMBER, JsonToken.END_ARRAY);
        when(jsonReader.hasNext()).thenReturn(true, true, false);
        doNothing().when(jsonReader).beginArray();
        doNothing().when(jsonReader).endArray();

        when(componentTypeAdapter.read(jsonReader)).thenReturn(1, 2);

        Object result = adapter.read(jsonReader);

        InOrder inOrder = inOrder(jsonReader, componentTypeAdapter);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).beginArray();
        inOrder.verify(jsonReader, times(2)).hasNext();
        inOrder.verify(componentTypeAdapter, times(2)).read(jsonReader);
        inOrder.verify(jsonReader).endArray();

        assertTrue(result.getClass().isArray());
        assertEquals(int.class, result.getClass().getComponentType());
        assertEquals(2, Array.getLength(result));
        assertEquals(1, Array.get(result, 0));
        assertEquals(2, Array.get(result, 1));
    }

    @Test
    @Timeout(8000)
    public void testRead_ObjectArray_ReadsCorrectly() throws IOException {
        // Use Integer class (wrapper) to test non-primitive path
        ArrayTypeAdapter<Integer> adapterObject = new ArrayTypeAdapter<>(null, componentTypeAdapter, Integer.class);

        when(jsonReader.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.NUMBER, JsonToken.NUMBER, JsonToken.END_ARRAY);
        when(jsonReader.hasNext()).thenReturn(true, true, false);
        doNothing().when(jsonReader).beginArray();
        doNothing().when(jsonReader).endArray();

        when(componentTypeAdapter.read(jsonReader)).thenReturn(10, 20);

        Object result = adapterObject.read(jsonReader);

        InOrder inOrder = inOrder(jsonReader, componentTypeAdapter);
        inOrder.verify(jsonReader).peek();
        inOrder.verify(jsonReader).beginArray();
        inOrder.verify(jsonReader, times(2)).hasNext();
        inOrder.verify(componentTypeAdapter, times(2)).read(jsonReader);
        inOrder.verify(jsonReader).endArray();

        assertTrue(result.getClass().isArray());
        assertEquals(Integer.class, result.getClass().getComponentType());
        assertEquals(2, Array.getLength(result));
        assertEquals(10, Array.get(result, 0));
        assertEquals(20, Array.get(result, 1));
    }
}