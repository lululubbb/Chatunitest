package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Array;

public class ArrayTypeAdapter_271_4Test {

    private ArrayTypeAdapter<Object> arrayTypeAdapter;
    private TypeAdapter<Object> componentTypeAdapter;

    @BeforeEach
    public void setUp() {
        componentTypeAdapter = mock(TypeAdapter.class);
        // Pass a non-null Gson instance to avoid NPE in TypeAdapterRuntimeTypeWrapper
        arrayTypeAdapter = new ArrayTypeAdapter<>(new Gson(), componentTypeAdapter, Object.class);
    }

    @Test
    @Timeout(8000)
    public void write_nullArray_callsNullValue() throws IOException {
        JsonWriter out = mock(JsonWriter.class);

        arrayTypeAdapter.write(out, null);

        verify(out).nullValue();
        verifyNoMoreInteractions(out);
        verifyNoInteractions(componentTypeAdapter);
    }

    @Test
    @Timeout(8000)
    public void write_emptyArray_writesBeginAndEndArray() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        Object emptyArray = Array.newInstance(Object.class, 0);

        arrayTypeAdapter.write(out, emptyArray);

        InOrder inOrder = inOrder(out);
        inOrder.verify(out).beginArray();
        inOrder.verify(out).endArray();

        verifyNoInteractions(componentTypeAdapter);
    }

    @Test
    @Timeout(8000)
    public void write_nonEmptyArray_writesAllElements() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        Object[] array = new Object[]{"one", "two", "three"};

        // Setup the componentTypeAdapter mock to call the real method for write
        doAnswer(invocation -> {
            JsonWriter writer = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            if (value == null) {
                writer.nullValue();
            } else {
                writer.value(value.toString());
            }
            return null;
        }).when(componentTypeAdapter).write(any(JsonWriter.class), any());

        arrayTypeAdapter.write(out, array);

        InOrder inOrder = inOrder(out, componentTypeAdapter);
        inOrder.verify(out).beginArray();
        for (Object element : array) {
            inOrder.verify(componentTypeAdapter).write(out, element);
        }
        inOrder.verify(out).endArray();
    }

    @Test
    @Timeout(8000)
    public void write_arrayWithNullElements_writesAllElementsIncludingNulls() throws IOException {
        JsonWriter out = mock(JsonWriter.class);
        Object[] array = new Object[]{"first", null, "third"};

        // Setup the componentTypeAdapter mock to call the real method for write
        doAnswer(invocation -> {
            JsonWriter writer = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            if (value == null) {
                writer.nullValue();
            } else {
                writer.value(value.toString());
            }
            return null;
        }).when(componentTypeAdapter).write(any(JsonWriter.class), any());

        arrayTypeAdapter.write(out, array);

        InOrder inOrder = inOrder(out, componentTypeAdapter);
        inOrder.verify(out).beginArray();
        inOrder.verify(componentTypeAdapter).write(out, "first");
        inOrder.verify(componentTypeAdapter).write(out, null);
        inOrder.verify(componentTypeAdapter).write(out, "third");
        inOrder.verify(out).endArray();
    }
}