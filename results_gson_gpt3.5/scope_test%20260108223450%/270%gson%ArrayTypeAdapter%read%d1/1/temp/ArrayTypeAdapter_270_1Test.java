package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class ArrayTypeAdapter_270_1Test {

    private ArrayTypeAdapter<Integer> adapter;
    private TypeAdapter<Integer> componentTypeAdapter;

    @BeforeEach
    public void setUp() {
        componentTypeAdapter = mock(TypeAdapter.class);
        adapter = new ArrayTypeAdapter<>(null, componentTypeAdapter, Integer.class);
    }

    @Test
    @Timeout(8000)
    public void testRead_NullJsonToken_ReturnsNull() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.NULL);

        Object result = adapter.read(in);

        verify(in).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_PrimitiveComponentType_ReadsArrayAndReturnsPrimitiveArray() throws IOException {
        // Use int.class (primitive) for componentType
        TypeAdapter<Integer> intComponentAdapter = mock(TypeAdapter.class);
        ArrayTypeAdapter<Integer> intAdapter = new ArrayTypeAdapter<>(null, intComponentAdapter, int.class);

        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
        when(in.hasNext()).thenReturn(true, true, false);
        when(intComponentAdapter.read(in)).thenReturn(1, 2);
        doNothing().when(in).beginArray();
        doNothing().when(in).endArray();

        Object result = intAdapter.read(in);

        InOrder inOrder = inOrder(in, intComponentAdapter);
        inOrder.verify(in).peek();
        inOrder.verify(in).beginArray();
        inOrder.verify(intComponentAdapter).read(in);
        inOrder.verify(intComponentAdapter).read(in);
        inOrder.verify(in).hasNext();
        inOrder.verify(in).hasNext();
        inOrder.verify(in).hasNext();
        inOrder.verify(in).endArray();

        assertNotNull(result);
        assertTrue(result.getClass().isArray());
        assertEquals(int.class, result.getClass().getComponentType());
        assertEquals(2, Array.getLength(result));
        assertEquals(1, Array.get(result, 0));
        assertEquals(2, Array.get(result, 1));
    }

    @Test
    @Timeout(8000)
    public void testRead_ObjectComponentType_ReadsArrayAndReturnsObjectArray() throws IOException {
        JsonReader in = mock(JsonReader.class);
        when(in.peek()).thenReturn(JsonToken.BEGIN_ARRAY);
        when(in.hasNext()).thenReturn(true, true, false);
        when(componentTypeAdapter.read(in)).thenReturn(42, 24);
        doNothing().when(in).beginArray();
        doNothing().when(in).endArray();

        Object result = adapter.read(in);

        InOrder inOrder = inOrder(in, componentTypeAdapter);
        inOrder.verify(in).peek();
        inOrder.verify(in).beginArray();
        inOrder.verify(componentTypeAdapter).read(in);
        inOrder.verify(componentTypeAdapter).read(in);
        inOrder.verify(in).hasNext();
        inOrder.verify(in).hasNext();
        inOrder.verify(in).hasNext();
        inOrder.verify(in).endArray();

        assertNotNull(result);
        assertTrue(result.getClass().isArray());
        assertEquals(Integer.class, result.getClass().getComponentType());
        assertEquals(2, Array.getLength(result));
        assertEquals(42, Array.get(result, 0));
        assertEquals(24, Array.get(result, 1));
    }

}