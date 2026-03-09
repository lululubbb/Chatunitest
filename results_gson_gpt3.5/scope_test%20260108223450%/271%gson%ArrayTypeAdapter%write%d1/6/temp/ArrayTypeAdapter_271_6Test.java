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

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.bind.ArrayTypeAdapter;
import com.google.gson.stream.JsonWriter;
import com.google.gson.TypeAdapter;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Array;

public class ArrayTypeAdapter_271_6Test {

    private JsonWriter jsonWriter;
    private TypeAdapter<Object> componentTypeAdapter;
    private ArrayTypeAdapter<Object> arrayTypeAdapter;
    private StringWriter stringWriter;

    @BeforeEach
    public void setUp() {
        stringWriter = new StringWriter();
        jsonWriter = spy(new JsonWriter(stringWriter));
        componentTypeAdapter = mock(TypeAdapter.class);
        arrayTypeAdapter = new ArrayTypeAdapter<>(new Gson(), componentTypeAdapter, Object.class);
    }

    @Test
    @Timeout(8000)
    public void write_nullArray_callsNullValue() throws IOException {
        arrayTypeAdapter.write(jsonWriter, null);
        verify(jsonWriter).nullValue();
        verifyNoMoreInteractions(jsonWriter);
        verifyNoInteractions(componentTypeAdapter);
    }

    @Test
    @Timeout(8000)
    public void write_emptyArray_writesBeginAndEndArrayOnly() throws IOException {
        Object emptyArray = Array.newInstance(Object.class, 0);

        arrayTypeAdapter.write(jsonWriter, emptyArray);

        InOrder inOrder = inOrder(jsonWriter, componentTypeAdapter);
        inOrder.verify(jsonWriter).beginArray();
        inOrder.verify(jsonWriter).endArray();
        verifyNoMoreInteractions(jsonWriter);
        verifyNoInteractions(componentTypeAdapter);
    }

    @Test
    @Timeout(8000)
    public void write_arrayWithElements_writesAllElements() throws IOException {
        Object[] array = new Object[] {"first", "second", null};

        doAnswer(invocation -> {
            JsonWriter out = invocation.getArgument(0);
            Object value = invocation.getArgument(1);
            // simulate writing the value by writing its string representation to the underlying writer
            if (value == null) {
                out.nullValue();
            } else {
                out.value(value.toString());
            }
            return null;
        }).when(componentTypeAdapter).write(any(JsonWriter.class), any());

        // Use doCallRealMethod() on arrayTypeAdapter.write() to call the real method and avoid spying issues
        ArrayTypeAdapter<Object> spyAdapter = spy(arrayTypeAdapter);
        doCallRealMethod().when(spyAdapter).write(any(JsonWriter.class), any());

        spyAdapter.write(jsonWriter, array);

        InOrder inOrder = inOrder(jsonWriter, componentTypeAdapter);
        inOrder.verify(jsonWriter).beginArray();
        inOrder.verify(componentTypeAdapter).write(eq(jsonWriter), eq("first"));
        inOrder.verify(componentTypeAdapter).write(eq(jsonWriter), eq("second"));
        inOrder.verify(componentTypeAdapter).write(eq(jsonWriter), isNull());
        inOrder.verify(jsonWriter).endArray();

        verifyNoMoreInteractions(jsonWriter);
        verifyNoMoreInteractions(componentTypeAdapter);
    }
}