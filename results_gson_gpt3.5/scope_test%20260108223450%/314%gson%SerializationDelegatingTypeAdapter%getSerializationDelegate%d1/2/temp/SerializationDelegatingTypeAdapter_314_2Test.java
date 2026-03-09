package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

class SerializationDelegatingTypeAdapter_314_2Test {

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
            // Delegate writing to the delegate adapter
            delegate.write(out, value);
        }

        @Override
        public T read(JsonReader in) throws IOException {
            // Delegate reading to the delegate adapter
            return delegate.read(in);
        }
    }

    @Test
    @Timeout(8000)
    void testGetSerializationDelegate_returnsDelegate() {
        @SuppressWarnings("unchecked")
        TypeAdapter<String> mockedDelegate = Mockito.mock(TypeAdapter.class);
        SerializationDelegatingTypeAdapter<String> adapter = new SerializationDelegatingTypeAdapterImpl<>(mockedDelegate);

        TypeAdapter<String> result = adapter.getSerializationDelegate();

        assertNotNull(result);
        assertSame(mockedDelegate, result);
    }
}