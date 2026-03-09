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
import com.google.gson.internal.bind.TypeAdapters;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class MapTypeAdapterFactory_596_4Test {

    private MapTypeAdapterFactory mapTypeAdapterFactory;
    private Gson mockGson;

    @BeforeEach
    public void setUp() {
        // We do not need ConstructorConstructor instance for testing getKeyAdapter as it is not used in the method
        mapTypeAdapterFactory = new MapTypeAdapterFactory(null, false);
        mockGson = mock(Gson.class);
    }

    @Test
    @Timeout(8000)
    public void testGetKeyAdapter_booleanPrimitive_returnsBooleanAsStringAdapter() throws Exception {
        Type keyType = boolean.class;

        TypeAdapter<?> adapter = invokeGetKeyAdapter(mapTypeAdapterFactory, mockGson, keyType);

        assertSame(TypeAdapters.BOOLEAN_AS_STRING, adapter);
        verifyNoInteractions(mockGson);
    }

    @Test
    @Timeout(8000)
    public void testGetKeyAdapter_BooleanClass_returnsBooleanAsStringAdapter() throws Exception {
        Type keyType = Boolean.class;

        TypeAdapter<?> adapter = invokeGetKeyAdapter(mapTypeAdapterFactory, mockGson, keyType);

        assertSame(TypeAdapters.BOOLEAN_AS_STRING, adapter);
        verifyNoInteractions(mockGson);
    }

    @Test
    @Timeout(8000)
    public void testGetKeyAdapter_otherType_invokesGsonGetAdapter() throws Exception {
        Type keyType = String.class;
        @SuppressWarnings({"unchecked", "rawtypes"})
        TypeAdapter<?> expectedAdapter = mock(TypeAdapter.class);
        @SuppressWarnings({"unchecked", "rawtypes"})
        TypeToken<?> keyTypeToken = TypeToken.get(keyType);
        when(mockGson.getAdapter((TypeToken<?>) any())).thenAnswer(invocation -> {
            TypeToken<?> arg = invocation.getArgument(0);
            if (arg.equals(keyTypeToken)) {
                return expectedAdapter;
            }
            return null;
        });

        TypeAdapter<?> adapter = invokeGetKeyAdapter(mapTypeAdapterFactory, mockGson, keyType);

        assertSame(expectedAdapter, adapter);
        verify(mockGson).getAdapter(keyTypeToken);
        verifyNoMoreInteractions(mockGson);
    }

    private TypeAdapter<?> invokeGetKeyAdapter(MapTypeAdapterFactory factory, Gson context, Type keyType)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method method = MapTypeAdapterFactory.class.getDeclaredMethod("getKeyAdapter", Gson.class, Type.class);
        method.setAccessible(true);
        return (TypeAdapter<?>) method.invoke(factory, context, keyType);
    }
}