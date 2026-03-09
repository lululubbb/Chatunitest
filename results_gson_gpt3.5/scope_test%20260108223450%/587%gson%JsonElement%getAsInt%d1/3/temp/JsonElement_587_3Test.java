package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class JsonElement_587_3Test {

    static class JsonElementStub extends JsonElement {
        @Override
        public JsonElement deepCopy() {
            return this;
        }
    }

    @Test
    @Timeout(8000)
    void testGetAsInt_throwsUnsupportedOperationException() throws Exception {
        JsonElement jsonElement = new JsonElementStub();

        // Using reflection to invoke getAsInt (though it is public, per instruction)
        Method method = JsonElement.class.getDeclaredMethod("getAsInt");
        method.setAccessible(true);

        InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
            method.invoke(jsonElement);
        });

        Throwable cause = thrown.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof UnsupportedOperationException);
        assertEquals("JsonElementStub", cause.getMessage());
    }

    @Test
    @Timeout(8000)
    void testGetAsInt_throwsUnsupportedOperationException_unwrapped() {
        JsonElement jsonElement = new JsonElementStub();

        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, jsonElement::getAsInt);
        assertEquals("JsonElementStub", exception.getMessage());
    }
}