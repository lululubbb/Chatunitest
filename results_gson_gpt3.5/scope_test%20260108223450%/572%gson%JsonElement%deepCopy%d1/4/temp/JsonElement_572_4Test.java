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

class JsonElement_572_4Test {

    // Since JsonElement is abstract, create a simple concrete subclass for testing
    static class JsonElementImpl extends JsonElement {
        private final JsonElement copy;

        JsonElementImpl(JsonElement copy) {
            this.copy = copy;
        }

        @Override
        public JsonElement deepCopy() {
            return this.copy != null ? this.copy : this;
        }
    }

    @Test
    @Timeout(8000)
    void testDeepCopyReturnsCorrectInstance() {
        JsonElement copy = new JsonElementImpl(null);
        JsonElement original = new JsonElementImpl(copy);

        // The deepCopy should return the copy instance passed in constructor
        assertSame(copy, original.deepCopy());
    }

    @Test
    @Timeout(8000)
    void testDeepCopyNotNull() {
        JsonElementImpl element = new JsonElementImpl(new JsonElementImpl(null));
        JsonElement copy = element.deepCopy();
        assertNotNull(copy);
    }
}