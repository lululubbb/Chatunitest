package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class SerializationDelegatingTypeAdapter_314_1Test {

    static class ConcreteSerializationDelegatingTypeAdapter extends SerializationDelegatingTypeAdapter<String> {
        private final TypeAdapter<String> delegate;

        ConcreteSerializationDelegatingTypeAdapter(TypeAdapter<String> delegate) {
            this.delegate = delegate;
        }

        @Override
        public TypeAdapter<String> getSerializationDelegate() {
            return delegate;
        }

        @Override
        public String read(JsonReader in) throws IOException {
            // Provide a dummy implementation to satisfy abstract method
            return null;
        }

        @Override
        public void write(JsonWriter out, String value) throws IOException {
            // Provide a dummy implementation to satisfy abstract method
        }
    }

    @Test
    @Timeout(8000)
    void testGetSerializationDelegate_returnsDelegate() {
        @SuppressWarnings("unchecked")
        TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
        ConcreteSerializationDelegatingTypeAdapter adapter = new ConcreteSerializationDelegatingTypeAdapter(mockDelegate);

        TypeAdapter<String> result = adapter.getSerializationDelegate();

        assertSame(mockDelegate, result);
    }

    @Test
    @Timeout(8000)
    void testGetSerializationDelegate_reflectionInvocation() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        @SuppressWarnings("unchecked")
        TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
        ConcreteSerializationDelegatingTypeAdapter adapter = new ConcreteSerializationDelegatingTypeAdapter(mockDelegate);

        Method method = SerializationDelegatingTypeAdapter.class.getDeclaredMethod("getSerializationDelegate");
        method.setAccessible(true);

        Object result = method.invoke(adapter);

        assertSame(mockDelegate, result);
    }
}