package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class JsonTreeWriter_520_1Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void testValue_NullValue_CallsNullValue() throws IOException {
        JsonTreeWriter spyWriter = spy(jsonTreeWriter);
        doReturn(true).when(spyWriter).isLenient();
        doReturn(spyWriter).when(spyWriter).nullValue();

        JsonWriter result = spyWriter.value((Number) null);

        verify(spyWriter).nullValue();
        assertSame(spyWriter, result);
    }

    @Test
    @Timeout(8000)
    public void testValue_NonLenient_NaN_Throws() {
        JsonTreeWriter writer = jsonTreeWriter;
        setLenient(writer, false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            writer.value(Double.NaN);
        });
        assertTrue(thrown.getMessage().contains("NaN"));
    }

    @Test
    @Timeout(8000)
    public void testValue_NonLenient_Infinite_Throws() {
        JsonTreeWriter writer = jsonTreeWriter;
        setLenient(writer, false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            writer.value(Double.POSITIVE_INFINITY);
        });
        assertTrue(thrown.getMessage().contains("infinities"));
    }

    @Test
    @Timeout(8000)
    public void testValue_Lenient_AllowsNaNAndInfinite() throws IOException {
        JsonTreeWriter writer = jsonTreeWriter;
        setLenient(writer, true);

        // Should not throw
        JsonWriter resultNaN = writer.value(Double.NaN);
        assertSame(writer, resultNaN);

        JsonWriter resultInfinite = writer.value(Double.POSITIVE_INFINITY);
        assertSame(writer, resultInfinite);
    }

    @Test
    @Timeout(8000)
    public void testValue_ValidNumber_CallsPutAndReturnsThis() throws Exception {
        JsonTreeWriter writer = jsonTreeWriter;
        setLenient(writer, true);

        JsonTreeWriter spyWriter = spy(writer);

        // Use reflection to verify private put method call
        Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", com.google.gson.JsonElement.class);
        putMethod.setAccessible(true);

        Number number = 123;
        JsonWriter result = spyWriter.value(number);

        // Verify that put was called via reflection on spyWriter
        verify(spyWriter, times(1)).value(number); // value called once

        // Since put is private, verify it was called by checking internal state change:
        // The product field should now be a JsonPrimitive with value 123

        Field productField = JsonTreeWriter.class.getDeclaredField("product");
        productField.setAccessible(true);
        Object product = productField.get(spyWriter);
        assertTrue(product instanceof JsonPrimitive);
        assertEquals(number.intValue(), ((JsonPrimitive) product).getAsInt());

        assertSame(spyWriter, result);
    }

    // Helper method to set lenient field via reflection
    private void setLenient(JsonTreeWriter writer, boolean lenient) {
        try {
            Field lenientField = JsonWriter.class.getDeclaredField("lenient");
            lenientField.setAccessible(true);
            lenientField.setBoolean(writer, lenient);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}