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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class JsonTreeWriter_512_3Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    void testValue_withNonNullString_returnsThis() throws IOException {
        JsonWriter returned = jsonTreeWriter.value("test");
        assertSame(jsonTreeWriter, returned);

        // Use reflection to verify that peek was called and returned a JsonPrimitive containing "test"
        try {
            Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
            peekMethod.setAccessible(true);
            Object topElement = peekMethod.invoke(jsonTreeWriter);
            // topElement should be a JsonPrimitive with value "test"
            assertTrue(topElement instanceof JsonPrimitive);
            assertEquals("test", ((JsonPrimitive) topElement).getAsString());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            fail("Reflection failed: " + e.getCause());
        }
    }

    @Test
    @Timeout(8000)
    void testValue_withNullString_callsNullValueAndReturns() throws IOException {
        // Spy the JsonTreeWriter to verify nullValue() is called
        JsonTreeWriter spyWriter = spy(jsonTreeWriter);

        doCallRealMethod().when(spyWriter).value((String) isNull());

        JsonWriter returned = spyWriter.value((String) null);
        verify(spyWriter).nullValue();
        assertSame(spyWriter, returned);
    }
}