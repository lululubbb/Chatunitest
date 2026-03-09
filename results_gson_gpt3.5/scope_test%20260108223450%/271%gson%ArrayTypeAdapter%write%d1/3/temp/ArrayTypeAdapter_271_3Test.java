package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Array;

public class ArrayTypeAdapter_271_3Test {

    private JsonWriter jsonWriterMock;
    private TypeAdapter<Object> componentTypeAdapterMock;
    private ArrayTypeAdapter<Object> arrayTypeAdapter;

    @BeforeEach
    public void setUp() {
        jsonWriterMock = mock(JsonWriter.class);
        componentTypeAdapterMock = mock(TypeAdapter.class);
        arrayTypeAdapter = new ArrayTypeAdapter<>(mock(com.google.gson.Gson.class), componentTypeAdapterMock, Object.class);
    }

    @Test
    @Timeout(8000)
    public void testWrite_NullArray() throws IOException {
        arrayTypeAdapter.write(jsonWriterMock, null);

        verify(jsonWriterMock).nullValue();
        verifyNoMoreInteractions(jsonWriterMock);
        verifyNoInteractions(componentTypeAdapterMock);
    }

    @Test
    @Timeout(8000)
    public void testWrite_EmptyArray() throws IOException {
        Object[] emptyArray = new Object[0];

        arrayTypeAdapter.write(jsonWriterMock, emptyArray);

        InOrder inOrder = inOrder(jsonWriterMock, componentTypeAdapterMock);
        inOrder.verify(jsonWriterMock).beginArray();
        inOrder.verify(jsonWriterMock).endArray();
        verifyNoMoreInteractions(jsonWriterMock);
        verifyNoInteractions(componentTypeAdapterMock);
    }

    @Test
    @Timeout(8000)
    public void testWrite_ArrayWithElements() throws IOException {
        Object[] array = new Object[]{"one", "two", null};

        // Mock componentTypeAdapter.write for each element to avoid NPE in TypeAdapterRuntimeTypeWrapper
        doNothing().when(componentTypeAdapterMock).write(eq(jsonWriterMock), eq("one"));
        doNothing().when(componentTypeAdapterMock).write(eq(jsonWriterMock), eq("two"));
        doNothing().when(componentTypeAdapterMock).write(eq(jsonWriterMock), isNull());

        // Use reflection to set the componentTypeAdapter field to the mock to avoid NPE in TypeAdapterRuntimeTypeWrapper
        try {
            java.lang.reflect.Field field = ArrayTypeAdapter.class.getDeclaredField("componentTypeAdapter");
            field.setAccessible(true);
            field.set(arrayTypeAdapter, componentTypeAdapterMock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        arrayTypeAdapter.write(jsonWriterMock, array);

        InOrder inOrder = inOrder(jsonWriterMock, componentTypeAdapterMock);
        inOrder.verify(jsonWriterMock).beginArray();
        inOrder.verify(componentTypeAdapterMock).write(jsonWriterMock, "one");
        inOrder.verify(componentTypeAdapterMock).write(jsonWriterMock, "two");
        inOrder.verify(componentTypeAdapterMock).write(jsonWriterMock, null);
        inOrder.verify(jsonWriterMock).endArray();

        verifyNoMoreInteractions(jsonWriterMock);
        verifyNoMoreInteractions(componentTypeAdapterMock);
    }

    @Test
    @Timeout(8000)
    public void testWrite_PrimitiveArray() throws IOException {
        int[] intArray = new int[]{1, 2, 3};

        // Use a TypeAdapter<Integer> mock for primitive wrapper type
        TypeAdapter<Integer> intAdapterMock = mock(TypeAdapter.class);
        ArrayTypeAdapter<Integer> intArrayTypeAdapter = new ArrayTypeAdapter<>(mock(com.google.gson.Gson.class), intAdapterMock, Integer.class);

        // Mock intAdapterMock.write to avoid NPE
        doNothing().when(intAdapterMock).write(eq(jsonWriterMock), eq(1));
        doNothing().when(intAdapterMock).write(eq(jsonWriterMock), eq(2));
        doNothing().when(intAdapterMock).write(eq(jsonWriterMock), eq(3));

        intArrayTypeAdapter.write(jsonWriterMock, intArray);

        InOrder inOrder = inOrder(jsonWriterMock, intAdapterMock);
        inOrder.verify(jsonWriterMock).beginArray();
        inOrder.verify(intAdapterMock).write(jsonWriterMock, 1);
        inOrder.verify(intAdapterMock).write(jsonWriterMock, 2);
        inOrder.verify(intAdapterMock).write(jsonWriterMock, 3);
        inOrder.verify(jsonWriterMock).endArray();

        verifyNoMoreInteractions(jsonWriterMock);
        verifyNoMoreInteractions(intAdapterMock);
    }
}