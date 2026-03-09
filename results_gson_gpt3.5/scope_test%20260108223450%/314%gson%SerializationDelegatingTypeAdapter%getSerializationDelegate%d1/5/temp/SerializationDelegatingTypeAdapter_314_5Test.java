package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.mock;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SerializationDelegatingTypeAdapter_314_5Test {

    private SerializationDelegatingTypeAdapter<Object> adapter;

    @BeforeEach
    void setUp() {
        adapter = new SerializationDelegatingTypeAdapter<Object>() {
            private final TypeAdapter<Object> delegate = mock(TypeAdapter.class);

            @Override
            public TypeAdapter<Object> getSerializationDelegate() {
                return delegate;
            }

            @Override
            public Object read(JsonReader in) throws IOException {
                // implement abstract method with dummy return
                return null;
            }

            @Override
            public void write(JsonWriter out, Object value) throws IOException {
                // implement abstract method with empty body
            }
        };
    }

    @Test
    @Timeout(8000)
    void testGetSerializationDelegate() {
        TypeAdapter<Object> delegate = adapter.getSerializationDelegate();
        assertSame(delegate, adapter.getSerializationDelegate());
    }
}