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

class JsonElement_572_2Test {

    static class JsonElementImpl extends JsonElement {
        private final String value;

        JsonElementImpl(String value) {
            this.value = value;
        }

        @Override
        public JsonElement deepCopy() {
            // Simple deep copy simulation for testing
            return new JsonElementImpl(this.value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof JsonElementImpl)) return false;
            JsonElementImpl other = (JsonElementImpl) obj;
            return value.equals(other.value);
        }

        @Override
        public int hashCode() {
            return value.hashCode();
        }
    }

    @Test
    @Timeout(8000)
    void deepCopy_shouldReturnEqualButDistinctInstance() {
        JsonElementImpl original = new JsonElementImpl("testValue");
        JsonElement copy = original.deepCopy();

        assertNotNull(copy);
        assertEquals(original, copy);
        assertNotSame(original, copy);
    }

    @Test
    @Timeout(8000)
    void deepCopy_onMockedJsonElement_returnsMockedCopy() {
        JsonElement mockElement = Mockito.mock(JsonElement.class);
        JsonElement mockCopy = Mockito.mock(JsonElement.class);

        Mockito.when(mockElement.deepCopy()).thenReturn(mockCopy);

        JsonElement result = mockElement.deepCopy();

        assertSame(mockCopy, result);
        Mockito.verify(mockElement).deepCopy();
    }
}