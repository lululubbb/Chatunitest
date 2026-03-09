package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JsonReaderInternalAccess_182_6Test {

    private JsonReaderInternalAccess jsonReaderInternalAccess;
    private JsonReader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(JsonReader.class);
        jsonReaderInternalAccess = new JsonReaderInternalAccess() {
            @Override
            public void promoteNameToValue(JsonReader reader) throws IOException {
                // Provide a simple concrete implementation for testing
                if (reader == null) {
                    throw new IOException("Reader is null");
                }
                // simulate some logic
                reader.peek();
            }
        };
        JsonReaderInternalAccess.INSTANCE = jsonReaderInternalAccess;
    }

    @Test
    @Timeout(8000)
    void testPromoteNameToValue_withValidReader() throws IOException {
        when(mockReader.peek()).thenReturn(null);

        jsonReaderInternalAccess.promoteNameToValue(mockReader);

        verify(mockReader, times(1)).peek();
    }

    @Test
    @Timeout(8000)
    void testPromoteNameToValue_withNullReader_throwsIOException() {
        assertThrows(IOException.class, () -> jsonReaderInternalAccess.promoteNameToValue(null));
    }
}