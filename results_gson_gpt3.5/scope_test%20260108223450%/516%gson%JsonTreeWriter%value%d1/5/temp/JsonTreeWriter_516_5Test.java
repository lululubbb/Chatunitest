package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeWriter_516_5Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() throws IOException {
        jsonTreeWriter = new JsonTreeWriter();
        // Initialize the writer state by beginning an array or object so stack is not empty
        jsonTreeWriter.beginArray();
    }

    @Test
    @Timeout(8000)
    public void testValue_WithNonNullBoolean() throws IOException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        JsonWriter returned = jsonTreeWriter.value(Boolean.TRUE);
        assertSame(jsonTreeWriter, returned);

        // Use reflection to access private method 'peek' to verify internal state
        Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
        JsonElement topElement = (JsonElement) peekMethod.invoke(jsonTreeWriter);

        // The top element should be a JsonPrimitive with value true
        assertTrue(topElement.isJsonPrimitive());
        assertEquals(new JsonPrimitive(Boolean.TRUE), topElement);
    }

    @Test
    @Timeout(8000)
    public void testValue_WithNullBoolean() throws IOException {
        // Remove the initial beginArray call from setUp by creating a fresh writer here
        jsonTreeWriter = new JsonTreeWriter();

        JsonWriter returned = jsonTreeWriter.value((Boolean) null);
        assertSame(jsonTreeWriter, returned);

        // get() should return JsonNull.INSTANCE after nullValue() call
        assertEquals(JsonNull.INSTANCE, jsonTreeWriter.get());
    }
}