package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class TreeTypeAdapterDelegateTest {

    @Mock
    private Gson gsonMock;

    @Mock
    private TypeAdapterFactory skipPastMock;

    @Mock
    private TypeAdapter<Object> delegateAdapterMock;

    private TreeTypeAdapter<Object> treeTypeAdapter;

    private TypeToken<Object> typeToken = TypeToken.get(Object.class);

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        when(gsonMock.getDelegateAdapter(skipPastMock, typeToken)).thenReturn(delegateAdapterMock);

        treeTypeAdapter = new TreeTypeAdapter<>(null, null, gsonMock, typeToken, skipPastMock, false);
    }

    @Test
    @Timeout(8000)
    public void testDelegateReturnsExistingDelegate() throws Exception {
        // Set delegate field to a mock instance
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(treeTypeAdapter, delegateAdapterMock);

        Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
        delegateMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        TypeAdapter<Object> result = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);

        assertSame(delegateAdapterMock, result);
        verify(gsonMock, never()).getDelegateAdapter(any(), any());
    }

    @Test
    @Timeout(8000)
    public void testDelegateAssignsAndReturnsDelegate() throws Exception {
        // Ensure delegate field is null initially
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(treeTypeAdapter, null);

        Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
        delegateMethod.setAccessible(true);

        @SuppressWarnings("unchecked")
        TypeAdapter<Object> result = (TypeAdapter<Object>) delegateMethod.invoke(treeTypeAdapter);

        assertSame(delegateAdapterMock, result);

        // Confirm that delegate field is assigned
        @SuppressWarnings("unchecked")
        TypeAdapter<Object> assignedDelegate = (TypeAdapter<Object>) delegateField.get(treeTypeAdapter);
        assertSame(delegateAdapterMock, assignedDelegate);

        verify(gsonMock, times(1)).getDelegateAdapter(skipPastMock, typeToken);
    }
}