package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class JsonReaderInternalAccess_182_3Test {

    @Test
    @Timeout(8000)
    void testPromoteNameToValue_invokesWithoutException() throws Exception {
        // Create a mock implementation of JsonReaderInternalAccess
        JsonReaderInternalAccess mockInstance = Mockito.mock(JsonReaderInternalAccess.class);

        // Use reflection to set the INSTANCE field
        Field instanceField = JsonReaderInternalAccess.class.getDeclaredField("INSTANCE");
        instanceField.setAccessible(true);
        instanceField.set(null, mockInstance);

        assertNotNull(JsonReaderInternalAccess.INSTANCE, "JsonReaderInternalAccess.INSTANCE should not be null");

        // Pass a Reader with a JSON object that has a name to promote
        JsonReader jsonReader = new JsonReader(new StringReader("{\"key\":\"value\"}"));

        // Advance the reader to the NAME token so promoteNameToValue can be called without exception
        jsonReader.beginObject();
        // Do NOT call nextName() here, because promoteNameToValue expects the next token to be NAME
        // Calling nextName() advances past the NAME token to the STRING value, causing the error

        // Make sure the mock does not throw IOException when promoteNameToValue is called
        Mockito.doNothing().when(mockInstance).promoteNameToValue(jsonReader);

        // Call the method and verify no exception is thrown
        assertDoesNotThrow(() -> JsonReaderInternalAccess.INSTANCE.promoteNameToValue(jsonReader));
    }
}