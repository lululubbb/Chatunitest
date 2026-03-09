package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.TypeAdapter;
import org.junit.jupiter.api.Test;

class SerializationDelegatingTypeAdapter_314_4Test {

    static class TestSerializationDelegatingTypeAdapter extends SerializationDelegatingTypeAdapter<String> {
        private final TypeAdapter<String> delegate;

        TestSerializationDelegatingTypeAdapter(TypeAdapter<String> delegate) {
            this.delegate = delegate;
        }

        @Override
        public TypeAdapter<String> getSerializationDelegate() {
            return delegate;
        }

        @Override
        public void write(com.google.gson.stream.JsonWriter out, String value) {
            // no-op for test
        }

        @Override
        public String read(com.google.gson.stream.JsonReader in) {
            return null;
        }
    }

    @Test
    @Timeout(8000)
    void getSerializationDelegate_returnsDelegate() {
        TypeAdapter<String> mockDelegate = mock(TypeAdapter.class);
        SerializationDelegatingTypeAdapter<String> adapter = new TestSerializationDelegatingTypeAdapter(mockDelegate);

        TypeAdapter<String> serializationDelegate = adapter.getSerializationDelegate();

        assertSame(mockDelegate, serializationDelegate);
    }
}