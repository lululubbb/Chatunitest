package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class ArrayTypeAdapter_269_3Test {

    private Gson mockGson;
    private TypeAdapter<String> mockComponentAdapter;
    private ArrayTypeAdapter<String> arrayTypeAdapter;

    @BeforeEach
    public void setUp() {
        mockGson = mock(Gson.class);
        mockComponentAdapter = mock(TypeAdapter.class);
        arrayTypeAdapter = new ArrayTypeAdapter<>(mockGson, mockComponentAdapter, String.class);
    }

    @Test
    @Timeout(8000)
    public void testRead_null() throws IOException {
        JsonReader mockReader = mock(JsonReader.class);
        when(mockReader.peek()).thenReturn(JsonToken.NULL);
        doNothing().when(mockReader).nextNull();

        Object result = arrayTypeAdapter.read(mockReader);

        verify(mockReader).nextNull();
        assertNull(result);
    }

    @Test
    @Timeout(8000)
    public void testRead_emptyArray() throws IOException {
        JsonReader mockReader = mock(JsonReader.class);
        when(mockReader.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.END_ARRAY);
        doNothing().when(mockReader).beginArray();
        doNothing().when(mockReader).endArray();

        Object result = arrayTypeAdapter.read(mockReader);

        verify(mockReader).beginArray();
        verify(mockReader).endArray();
        assertEquals(0, Array.getLength(result));
        assertEquals(String[].class, result.getClass());
    }

    @Test
    @Timeout(8000)
    public void testRead_arrayWithElements() throws IOException {
        JsonReader mockReader = mock(JsonReader.class);
        when(mockReader.peek()).thenReturn(JsonToken.BEGIN_ARRAY, JsonToken.STRING, JsonToken.STRING, JsonToken.END_ARRAY);
        doNothing().when(mockReader).beginArray();
        doNothing().when(mockReader).endArray();
        when(mockReader.hasNext()).thenReturn(true, true, false);

        when(mockComponentAdapter.read(mockReader)).thenReturn("one", "two");

        Object result = arrayTypeAdapter.read(mockReader);

        verify(mockReader).beginArray();
        verify(mockReader).endArray();
        assertEquals(2, Array.getLength(result));
        assertEquals("one", Array.get(result, 0));
        assertEquals("two", Array.get(result, 1));
        assertEquals(String[].class, result.getClass());
    }

    @Test
    @Timeout(8000)
    public void testWrite_null() throws IOException {
        JsonWriter mockWriter = mock(JsonWriter.class);

        arrayTypeAdapter.write(mockWriter, null);

        verify(mockWriter).nullValue();
    }

    @Test
    @Timeout(8000)
    public void testWrite_arrayWithElements() throws IOException {
        JsonWriter mockWriter = mock(JsonWriter.class);
        String[] array = new String[]{"one", "two"};

        // Removed doNothing() stubbings because JsonWriter methods are final and cannot be stubbed this way.
        // Mockito will by default do nothing on void methods unless they throw.

        arrayTypeAdapter.write(mockWriter, array);

        verify(mockWriter).beginArray();
        verify(mockWriter).endArray();
        verify(mockComponentAdapter, times(2)).write(eq(mockWriter), any());
    }

    @Test
    @Timeout(8000)
    public void testFactoryCreate_withArrayType() {
        Gson gson = mock(Gson.class);
        TypeAdapter<String> componentAdapter = mock(TypeAdapter.class);
        when(gson.getAdapter(TypeToken.get(String.class))).thenReturn(componentAdapter);

        TypeToken<String[]> typeToken = TypeToken.get(String[].class);

        @SuppressWarnings("unchecked")
        TypeAdapter<String[]> adapter = (TypeAdapter<String[]>) ArrayTypeAdapter.FACTORY.create(gson, typeToken);

        assertNotNull(adapter);
        assertTrue(adapter instanceof ArrayTypeAdapter);
    }

    @Test
    @Timeout(8000)
    public void testFactoryCreate_withNonArrayType() {
        Gson gson = mock(Gson.class);
        TypeToken<String> typeToken = TypeToken.get(String.class);

        TypeAdapter<String> adapter = ArrayTypeAdapter.FACTORY.create(gson, typeToken);

        assertNull(adapter);
    }
}