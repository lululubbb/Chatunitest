package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.internal.$Gson$Preconditions;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TreeTypeAdapter_494_5Test {

    private TreeTypeAdapter<String> treeTypeAdapterWithSerializer;
    private TreeTypeAdapter<String> treeTypeAdapterWithoutSerializer;
    private JsonSerializer<String> mockSerializer;
    private Gson mockGson;
    private TypeToken<String> typeToken;
    private TypeAdapterFactory mockFactory;

    @BeforeEach
    void setUp() {
        mockSerializer = mock(JsonSerializer.class);
        mockGson = mock(Gson.class);
        typeToken = TypeToken.get(String.class);
        mockFactory = mock(TypeAdapterFactory.class);

        // Instance with serializer != null
        treeTypeAdapterWithSerializer = new TreeTypeAdapter<>(mockSerializer, null, mockGson, typeToken, mockFactory, false);

        // Instance with serializer == null
        treeTypeAdapterWithoutSerializer = new TreeTypeAdapter<>(null, null, mockGson, typeToken, mockFactory, false);
    }

    @Test
    @Timeout(8000)
    void getSerializationDelegate_whenSerializerNotNull_returnsThis() throws Exception {
        TypeAdapter<String> result = invokeGetSerializationDelegate(treeTypeAdapterWithSerializer);
        assertSame(treeTypeAdapterWithSerializer, result);
    }

    @Test
    @Timeout(8000)
    void getSerializationDelegate_whenSerializerNull_returnsDelegate() throws Exception {
        // Prepare a mock delegate to be returned by delegate() method
        TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);

        // Use reflection to set the private delegate field to null initially
        setPrivateField(treeTypeAdapterWithoutSerializer, "delegate", null);

        // Use reflection to override the private delegate() method to return mockDelegate
        TreeTypeAdapter<String> adapter = treeTypeAdapterWithoutSerializer;
        Method delegateMethod = TreeTypeAdapter.class.getDeclaredMethod("delegate");
        delegateMethod.setAccessible(true);

        // Create a proxy instance to override delegate() method
        // Since TreeTypeAdapter is final, we cannot subclass it, so we use a dynamic proxy on an interface or reflection hack.
        // Instead, we replace the delegate field to mockDelegate, so delegate() returns it naturally.
        setPrivateField(adapter, "delegate", mockDelegate);

        TypeAdapter<String> result = invokeGetSerializationDelegate(adapter);
        assertSame(mockDelegate, result);
    }

    // Helper method to invoke private getSerializationDelegate() via reflection
    private TypeAdapter<String> invokeGetSerializationDelegate(TreeTypeAdapter<String> adapter) throws Exception {
        Method method = TreeTypeAdapter.class.getDeclaredMethod("getSerializationDelegate");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        TypeAdapter<String> result = (TypeAdapter<String>) method.invoke(adapter);
        return result;
    }

    // Helper method to set private fields via reflection
    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        Field field = TreeTypeAdapter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}