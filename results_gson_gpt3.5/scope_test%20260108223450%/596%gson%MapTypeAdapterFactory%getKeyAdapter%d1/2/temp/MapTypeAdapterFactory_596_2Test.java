package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.$Gson$Types;
import com.google.gson.internal.ConstructorConstructor;
import com.google.gson.internal.JsonReaderInternalAccess;
import com.google.gson.internal.ObjectConstructor;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.internal.bind.TypeAdapters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class MapTypeAdapterFactory_596_2Test {

    private MapTypeAdapterFactory mapTypeAdapterFactory;
    private Gson mockGson;

    @BeforeEach
    public void setUp() {
        // ConstructorConstructor is required but not used in getKeyAdapter, so can be mocked or null
        mapTypeAdapterFactory = new MapTypeAdapterFactory(null, false);
        mockGson = mock(Gson.class);
    }

    @Test
    @Timeout(8000)
    public void testGetKeyAdapter_withBooleanPrimitive_returnsBooleanAsString() throws Exception {
        Type keyType = boolean.class;

        TypeAdapter<?> adapter = invokeGetKeyAdapter(mockGson, keyType);

        assertSame(TypeAdapters.BOOLEAN_AS_STRING, adapter);
        verifyNoInteractions(mockGson);
    }

    @Test
    @Timeout(8000)
    public void testGetKeyAdapter_withBooleanWrapper_returnsBooleanAsString() throws Exception {
        Type keyType = Boolean.class;

        TypeAdapter<?> adapter = invokeGetKeyAdapter(mockGson, keyType);

        assertSame(TypeAdapters.BOOLEAN_AS_STRING, adapter);
        verifyNoInteractions(mockGson);
    }

    @Test
    @Timeout(8000)
    public void testGetKeyAdapter_withOtherType_callsGsonGetAdapter() throws Exception {
        Type keyType = String.class;
        TypeAdapter<?> expectedAdapter = mock(TypeAdapter.class);
        TypeToken<?> typeToken = TypeToken.get(keyType);

        // Cast to (TypeToken) to avoid generic capture issues in Mockito stubbing
        when(mockGson.getAdapter((TypeToken) typeToken)).thenReturn(expectedAdapter);

        TypeAdapter<?> adapter = invokeGetKeyAdapter(mockGson, keyType);

        assertSame(expectedAdapter, adapter);
        verify(mockGson).getAdapter((TypeToken) typeToken);
    }

    private TypeAdapter<?> invokeGetKeyAdapter(Gson context, Type keyType) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
        method.setAccessible(true);
        return (TypeAdapter<?>) method.invoke(mapTypeAdapterFactory, context, keyType);
    }
}