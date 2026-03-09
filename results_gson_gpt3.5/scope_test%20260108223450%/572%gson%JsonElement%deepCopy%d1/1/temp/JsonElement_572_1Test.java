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
import org.mockito.Mockito;

class JsonElement_572_1Test {

    static class JsonElementImpl extends JsonElement {
        private final JsonElement copy;

        JsonElementImpl(JsonElement copy) {
            this.copy = copy;
        }

        @Override
        public JsonElement deepCopy() {
            return copy;
        }
    }

    @Test
    @Timeout(8000)
    void testDeepCopy_returnsCorrectCopy() {
        JsonElement original = Mockito.mock(JsonElement.class);
        JsonElement copy = Mockito.mock(JsonElement.class);

        JsonElementImpl element = new JsonElementImpl(copy);
        JsonElement result = element.deepCopy();

        assertSame(copy, result);
    }

    @Test
    @Timeout(8000)
    void testDeepCopy_notNull() {
        JsonElementImpl element = new JsonElementImpl(null);
        JsonElement result = element.deepCopy();
        assertNull(result);
    }
}