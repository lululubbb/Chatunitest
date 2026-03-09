package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
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
import java.util.List;

class JsonTreeWriter_value_Test {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void value_NullValue_CallsNullValue() throws IOException {
        JsonTreeWriter spyWriter = spy(writer);
        doReturn(true).when(spyWriter).isLenient();
        doReturn(spyWriter).when(spyWriter).nullValue();

        JsonWriter result = spyWriter.value((Number) null);

        verify(spyWriter).nullValue();
        assertSame(spyWriter, result);
    }

    @Test
    @Timeout(8000)
    public void value_NonLenientWithNaN_ThrowsIllegalArgumentException() {
        Number nan = Double.NaN;
        JsonTreeWriter spyWriter = spy(writer);
        doReturn(false).when(spyWriter).isLenient();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            spyWriter.value(nan);
        });
        assertTrue(exception.getMessage().contains("NaN"));
    }

    @Test
    @Timeout(8000)
    public void value_NonLenientWithInfinite_ThrowsIllegalArgumentException() {
        Number infinite = Double.POSITIVE_INFINITY;
        JsonTreeWriter spyWriter = spy(writer);
        doReturn(false).when(spyWriter).isLenient();

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            spyWriter.value(infinite);
        });
        assertTrue(exception.getMessage().contains("infinities"));
    }

    @Test
    @Timeout(8000)
    public void value_ValidNumber_PutsJsonPrimitiveAndReturnsThis() throws Exception {
        Number number = 42;
        JsonTreeWriter spyWriter = spy(writer);
        doReturn(true).when(spyWriter).isLenient();

        JsonWriter result = spyWriter.value(number);

        assertSame(spyWriter, result);

        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<?> stack = (List<?>) stackField.get(spyWriter);

        assertFalse(stack.isEmpty());
        assertTrue(stack.get(stack.size() - 1) instanceof JsonPrimitive);
        JsonPrimitive primitive = (JsonPrimitive) stack.get(stack.size() - 1);
        assertEquals(number.intValue(), primitive.getAsInt());
    }
}