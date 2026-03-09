package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.*;

class JsonReaderInternalAccess_182_5Test {

    private JsonReaderInternalAccess jsonReaderInternalAccess;
    private JsonReader mockReader;

    @BeforeEach
    void setUp() {
        mockReader = mock(JsonReader.class);
        jsonReaderInternalAccess = new JsonReaderInternalAccess() {
            @Override
            public void promoteNameToValue(JsonReader reader) throws IOException {
                // No implementation, abstract method must be overridden for testing
            }
        };
        JsonReaderInternalAccess.INSTANCE = jsonReaderInternalAccess;
    }

    @Test
    @Timeout(8000)
    void testPromoteNameToValue_invocation() {
        assertDoesNotThrow(() -> jsonReaderInternalAccess.promoteNameToValue(mockReader));
        verifyNoInteractions(mockReader);
    }

    @Test
    @Timeout(8000)
    void testInvokePromoteNameToValueByReflection() throws Exception {
        Method method = JsonReaderInternalAccess.class.getDeclaredMethod("promoteNameToValue", JsonReader.class);
        method.setAccessible(true);
        assertDoesNotThrow(() -> method.invoke(jsonReaderInternalAccess, mockReader));
    }
}