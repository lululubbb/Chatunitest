package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ToNumberPolicy;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
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
import com.google.gson.internal.bind.ObjectTypeAdapter;
import com.google.gson.stream.JsonWriter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;

import java.io.IOException;
import java.lang.reflect.Constructor;

public class ObjectTypeAdapter_556_4Test {

    private Gson gsonMock;
    private ToNumberStrategy toNumberStrategyMock;
    private ObjectTypeAdapter objectTypeAdapter;

    @BeforeEach
    public void setUp() throws Exception {
        gsonMock = mock(Gson.class);
        toNumberStrategyMock = mock(ToNumberStrategy.class);

        Constructor<ObjectTypeAdapter> constructor = ObjectTypeAdapter.class.getDeclaredConstructor(Gson.class, ToNumberStrategy.class);
        constructor.setAccessible(true);
        objectTypeAdapter = constructor.newInstance(gsonMock, toNumberStrategyMock);
    }

    @Test
    @Timeout(8000)
    public void testWrite_nullValue_callsNullValueOnJsonWriter() throws IOException {
        JsonWriter jsonWriterMock = mock(JsonWriter.class);

        objectTypeAdapter.write(jsonWriterMock, null);

        verify(jsonWriterMock).nullValue();
        verifyNoMoreInteractions(jsonWriterMock);
    }

    @Test
    @Timeout(8000)
    public void testWrite_valueWithObjectTypeAdapter_callsBeginAndEndObject() throws Exception {
        JsonWriter jsonWriterMock = mock(JsonWriter.class);

        Object value = new Object();

        // Use a raw TypeToken to avoid generic capture issues
        @SuppressWarnings("unchecked")
        TypeAdapter<Object> spyAdapter = (TypeAdapter<Object>) spy(objectTypeAdapter);

        when(gsonMock.<Object>getAdapter((Class<Object>) value.getClass())).thenReturn(spyAdapter);

        objectTypeAdapter.write(jsonWriterMock, value);

        InOrder inOrder = inOrder(jsonWriterMock);
        inOrder.verify(jsonWriterMock).beginObject();
        inOrder.verify(jsonWriterMock).endObject();
        verifyNoMoreInteractions(jsonWriterMock);
    }

    @Test
    @Timeout(8000)
    public void testWrite_valueWithOtherTypeAdapter_delegatesWrite() throws IOException {
        JsonWriter jsonWriterMock = mock(JsonWriter.class);

        Object value = new Object();

        @SuppressWarnings("unchecked")
        TypeAdapter<Object> otherAdapterMock = (TypeAdapter<Object>) mock(TypeAdapter.class);
        when(gsonMock.<Object>getAdapter((Class<Object>) value.getClass())).thenReturn(otherAdapterMock);

        objectTypeAdapter.write(jsonWriterMock, value);

        verify(otherAdapterMock).write(jsonWriterMock, value);
        verifyNoMoreInteractions(jsonWriterMock);
    }
}