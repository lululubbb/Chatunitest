package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.io.IOException;

class SerializationDelegatingTypeAdapter_314_6Test {

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
        public void write(JsonWriter out, T value) throws IOException {
            delegate.write(out, value);
        }

        @Override
        public T read(JsonReader in) throws IOException {
            return delegate.read(in);
        }
    }

    @Test
    @Timeout(8000)
    void testGetSerializationDelegate_returnsDelegate() throws Exception {
        TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
        SerializationDelegatingTypeAdapterImpl<String> adapter = new SerializationDelegatingTypeAdapterImpl<>(mockDelegate);

        // Direct call
        TypeAdapter<String> result = adapter.getSerializationDelegate();
        assertSame(mockDelegate, result);

        // Reflection call to the public method (just to demonstrate reflection usage)
        Method method = SerializationDelegatingTypeAdapter.class.getDeclaredMethod("getSerializationDelegate");
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        TypeAdapter<String> reflectedResult = (TypeAdapter<String>) method.invoke(adapter);
        assertSame(mockDelegate, reflectedResult);
    }
}