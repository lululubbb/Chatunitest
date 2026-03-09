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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ArrayTypeAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;

public class ArrayTypeAdapter_271_1Test {

    private JsonWriter jsonWriter;
    private StringWriter stringWriter;
    private TypeAdapter<Object> componentTypeAdapter;
    private ArrayTypeAdapter<Object> arrayTypeAdapter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = spy(new JsonWriter(stringWriter));
        componentTypeAdapter = mock(TypeAdapter.class);
        arrayTypeAdapter = new ArrayTypeAdapter<>(new Gson(), componentTypeAdapter, Object.class);
    }

    @Test
    @Timeout(8000)
    public void write_NullArray_WritesNullValue() throws IOException {
        arrayTypeAdapter.write(jsonWriter, null);
        verify(jsonWriter).nullValue();
        verifyNoMoreInteractions(jsonWriter);
        verifyNoInteractions(componentTypeAdapter);
    }

    @Test
    @Timeout(8000)
    public void write_EmptyArray_WritesBeginAndEndArrayOnly() throws IOException {
        Object emptyArray = Array.newInstance(Object.class, 0);
        arrayTypeAdapter.write(jsonWriter, emptyArray);
        InOrder inOrder = inOrder(jsonWriter, componentTypeAdapter);
        inOrder.verify(jsonWriter).beginArray();
        inOrder.verify(jsonWriter).endArray();
        verifyNoMoreInteractions(jsonWriter, componentTypeAdapter);
    }

    @Test
    @Timeout(8000)
    public void write_ArrayWithElements_WritesAllElements() throws IOException {
        Object[] array = (Object[]) Array.newInstance(Object.class, 3);
        array[0] = "one";
        array[1] = "two";
        array[2] = "three";

        // Mock componentTypeAdapter.write to call real method on spy jsonWriter
        doAnswer(invocation -> {
            // simulate writing string value, do nothing
            return null;
        }).when(componentTypeAdapter).write(any(JsonWriter.class), any());

        arrayTypeAdapter.write(jsonWriter, array);

        InOrder inOrder = inOrder(jsonWriter, componentTypeAdapter);
        inOrder.verify(jsonWriter).beginArray();
        inOrder.verify(componentTypeAdapter).write(eq(jsonWriter), eq("one"));
        inOrder.verify(componentTypeAdapter).write(eq(jsonWriter), eq("two"));
        inOrder.verify(componentTypeAdapter).write(eq(jsonWriter), eq("three"));
        inOrder.verify(jsonWriter).endArray();

        verifyNoMoreInteractions(jsonWriter, componentTypeAdapter);
    }

    @Test
    @Timeout(8000)
    public void write_ArrayWithNullElements_WritesNullElements() throws IOException {
        Object[] array = (Object[]) Array.newInstance(Object.class, 3);
        array[0] = "one";
        array[1] = null;
        array[2] = "three";

        doAnswer(invocation -> {
            JsonWriter writer = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            if(value == null) {
                writer.nullValue();
            } else {
                // simulate writing string value, do nothing
            }
            return null;
        }).when(componentTypeAdapter).write(any(JsonWriter.class), any());

        arrayTypeAdapter.write(jsonWriter, array);

        InOrder inOrder = inOrder(jsonWriter, componentTypeAdapter);
        inOrder.verify(jsonWriter).beginArray();
        inOrder.verify(componentTypeAdapter).write(eq(jsonWriter), eq("one"));
        inOrder.verify(componentTypeAdapter).write(eq(jsonWriter), isNull());
        inOrder.verify(componentTypeAdapter).write(eq(jsonWriter), eq("three"));
        inOrder.verify(jsonWriter).endArray();

        verifyNoMoreInteractions(jsonWriter, componentTypeAdapter);
    }
}