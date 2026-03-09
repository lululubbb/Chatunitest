package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.google.gson.JsonElement;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonElement_586_2Test {

    @Test
    @Timeout(8000)
    public void testGetAsLong_UnsupportedOperationExceptionThrown() throws Exception {
        JsonElement jsonElement = new JsonElement() {
            @Override
            public JsonElement deepCopy() {
                return null;
            }
        };

        // Direct call to getAsLong() should throw UnsupportedOperationException
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, jsonElement::getAsLong);
        assertEquals(jsonElement.getClass().getSimpleName(), exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testGetAsLong_UsingReflection() throws Exception {
        JsonElement jsonElement = new JsonElement() {
            @Override
            public JsonElement deepCopy() {
                return null;
            }
        };

        Method getAsLongMethod = JsonElement.class.getDeclaredMethod("getAsLong");
        getAsLongMethod.setAccessible(true);

        try {
            getAsLongMethod.invoke(jsonElement);
        } catch (Exception e) {
            Throwable cause = e.getCause();
            assertEquals(UnsupportedOperationException.class, cause.getClass());
            assertEquals(jsonElement.getClass().getSimpleName(), cause.getMessage());
        }
    }
}