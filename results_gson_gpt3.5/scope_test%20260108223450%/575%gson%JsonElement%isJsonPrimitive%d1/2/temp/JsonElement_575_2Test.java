package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;

public class JsonElement_575_2Test {

    @Test
    @Timeout(8000)
    public void testIsJsonPrimitive_whenInstanceIsJsonPrimitive_shouldReturnTrue() {
        JsonElement element = new JsonPrimitive("test");
        assertTrue(element.isJsonPrimitive());
    }

    @Test
    @Timeout(8000)
    public void testIsJsonPrimitive_whenInstanceIsNotJsonPrimitive_shouldReturnFalse() {
        JsonElement element = new JsonElement() {
            @Override
            public JsonElement deepCopy() {
                return this;
            }
        };
        assertFalse(element.isJsonPrimitive());
    }
}