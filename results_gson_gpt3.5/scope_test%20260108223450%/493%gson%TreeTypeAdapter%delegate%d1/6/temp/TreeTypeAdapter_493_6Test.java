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

public class TreeTypeAdapter_493_6Test {

    @Mock
    private Gson mockGson;

    @Mock
    private TypeAdapterFactory mockSkipPast;

    @Mock
    private TypeAdapter<Object> mockDelegateAdapter;

    private TreeTypeAdapter<Object> treeTypeAdapter;

    private TypeToken<Object> typeToken = TypeToken.get(Object.class);

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        // Create TreeTypeAdapter with null serializer and deserializer for simplicity
        treeTypeAdapter = new TreeTypeAdapter<>(null, null, mockGson, typeToken, mockSkipPast, false);
    }

    @Test
    @Timeout(8000)
    public void testDelegateReturnsExistingDelegate() throws Exception {
        // Use reflection to set the private volatile delegate field to a mock delegate adapter
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(treeTypeAdapter, mockDelegateAdapter);

        // Access private method delegate()
        Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
        delegateMethod.setAccessible(true);

        // Call the delegate method
        Object result = delegateMethod.invoke(treeTypeAdapter);

        // Verify the returned delegate is the existing one (mockDelegateAdapter)
        assertSame(mockDelegateAdapter, result);

        // Verify gson.getDelegateAdapter is NOT called because delegate is already set
        verify(mockGson, never()).getDelegateAdapter(any(), any());
    }

    @Test
    @Timeout(8000)
    public void testDelegateReturnsNewDelegateWhenNull() throws Exception {
        // Ensure delegate field is null
        Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(treeTypeAdapter, null);

        // Mock behavior of gson.getDelegateAdapter to return mockDelegateAdapter
        when(mockGson.getDelegateAdapter(mockSkipPast, typeToken)).thenReturn(mockDelegateAdapter);

        // Access private method delegate()
        Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
        delegateMethod.setAccessible(true);

        // Call the delegate method
        Object result = delegateMethod.invoke(treeTypeAdapter);

        // Verify the returned delegate is the mockDelegateAdapter returned by gson.getDelegateAdapter
        assertSame(mockDelegateAdapter, result);

        // Verify gson.getDelegateAdapter is called once with correct arguments
        verify(mockGson, times(1)).getDelegateAdapter(mockSkipPast, typeToken);

        // Verify that the delegate field is set to mockDelegateAdapter
        Object delegateValue = delegateField.get(treeTypeAdapter);
        assertSame(mockDelegateAdapter, delegateValue);
    }
}