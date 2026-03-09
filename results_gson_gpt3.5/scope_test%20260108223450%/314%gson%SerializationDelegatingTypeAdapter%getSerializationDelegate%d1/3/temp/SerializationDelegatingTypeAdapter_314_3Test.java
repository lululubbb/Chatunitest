package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class SerializationDelegatingTypeAdapter_314_3Test {

    static class SerializationDelegatingTypeAdapterImpl<T> extends SerializationDelegatingTypeAdapter<T> {
        private final TypeAdapter<T> delegate;

        SerializationDelegatingTypeAdapterImpl(TypeAdapter<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public TypeAdapter<T> getSerializationDelegate() {
            return delegate;
        }

        @Override
        public T read(com.google.gson.stream.JsonReader in) {
            // Provide a dummy implementation to satisfy abstract method requirement
            return null;
        }

        @Override
        public void write(com.google.gson.stream.JsonWriter out, T value) {
            // Provide a dummy implementation to satisfy abstract method requirement
        }

        // private method to test reflection invocation example (if any)
        @SuppressWarnings("unused")
        private String privateHelper() {
            return "private";
        }
    }

    @Test
    @Timeout(8000)
    void testGetSerializationDelegate_returnsDelegate() {
        TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
        SerializationDelegatingTypeAdapterImpl<String> adapter = new SerializationDelegatingTypeAdapterImpl<>(mockDelegate);

        TypeAdapter<String> result = adapter.getSerializationDelegate();

        assertSame(mockDelegate, result);
    }

    @Test
    @Timeout(8000)
    void testInvokePrivateMethod_privateHelper() throws Exception {
        SerializationDelegatingTypeAdapterImpl<String> adapter = new SerializationDelegatingTypeAdapterImpl<>(mock(TypeAdapter.class));

        Method privateHelperMethod = SerializationDelegatingTypeAdapterImpl.class.getDeclaredMethod("privateHelper");
        privateHelperMethod.setAccessible(true);

        String result = (String) privateHelperMethod.invoke(adapter);

        assertEquals("private", result);
    }
}