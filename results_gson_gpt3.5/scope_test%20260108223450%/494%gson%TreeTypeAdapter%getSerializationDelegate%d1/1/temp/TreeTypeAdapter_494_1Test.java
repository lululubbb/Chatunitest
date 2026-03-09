package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonDeserializationContext;
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
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class TreeTypeAdapter_494_1Test {

    private Gson gsonMock;
    private TypeToken<String> typeToken;
    private TypeAdapterFactory skipPastMock;
    private JsonSerializer<String> serializerMock;
    private JsonDeserializer<String> deserializerMock;
    private TreeTypeAdapter<String> treeTypeAdapter;

    @BeforeEach
    public void setUp() {
        gsonMock = mock(Gson.class);
        typeToken = TypeToken.get(String.class);
        skipPastMock = mock(TypeAdapterFactory.class);
        serializerMock = mock(JsonSerializer.class);
        deserializerMock = mock(JsonDeserializer.class);
    }

    @Test
    @Timeout(8000)
    public void getSerializationDelegate_returnsThis_whenSerializerNotNull() throws Exception {
        treeTypeAdapter = new TreeTypeAdapter<>(serializerMock, null, gsonMock, typeToken, skipPastMock, true);

        Method method = TreeTypeAdapter.class.getDeclaredMethod("getSerializationDelegate");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        TypeAdapter<String> result = (TypeAdapter<String>) method.invoke(treeTypeAdapter);

        assertSame(treeTypeAdapter, result);
    }

    @Test
    @Timeout(8000)
    public void getSerializationDelegate_returnsDelegate_whenSerializerNull() throws Exception {
        treeTypeAdapter = new TreeTypeAdapter<>(null, deserializerMock, gsonMock, typeToken, skipPastMock, true);

        // Setup delegate field via reflection to simulate delegate() return
        TypeAdapter<String> delegateMock = mock(TypeAdapter.class);

        // Use reflection to set the private delegate field directly
        java.lang.reflect.Field delegateField = TreeTypeAdapter.class.getDeclaredField("delegate");
        delegateField.setAccessible(true);
        delegateField.set(treeTypeAdapter, delegateMock);

        Method method = TreeTypeAdapter.class.getDeclaredMethod("getSerializationDelegate");
        method.setAccessible(true);

        @SuppressWarnings("unchecked")
        TypeAdapter<String> result = (TypeAdapter<String>) method.invoke(treeTypeAdapter);

        assertSame(delegateMock, result);
    }
}