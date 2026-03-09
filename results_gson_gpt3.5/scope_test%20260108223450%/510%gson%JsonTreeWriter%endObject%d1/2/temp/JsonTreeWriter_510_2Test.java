package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_510_2Test {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void endObject_whenStackEmpty_throwsIllegalStateException() {
        // stack is empty by default
        assertThrows(IllegalStateException.class, () -> writer.endObject());
    }

    @Test
    @Timeout(8000)
    public void endObject_whenPendingNameNotNull_throwsIllegalStateException() throws Exception {
        setPendingName("name");
        // stack must not be empty, so add element to stack
        addJsonElementToStack(new JsonObject());

        assertThrows(IllegalStateException.class, () -> writer.endObject());
    }

    @Test
    @Timeout(8000)
    public void endObject_whenTopOfStackIsJsonObject_removesItAndReturnsThis() throws Exception {
        JsonObject obj = new JsonObject();
        addJsonElementToStack(obj);

        JsonWriter result = writer.endObject();

        assertSame(writer, result);

        List<?> stack = getStack();
        assertFalse(stack.contains(obj));
    }

    @Test
    @Timeout(8000)
    public void endObject_whenTopOfStackIsNotJsonObject_throwsIllegalStateException() throws Exception {
        addJsonElementToStack(new JsonPrimitive("primitive"));

        assertThrows(IllegalStateException.class, () -> writer.endObject());
    }

    // Helper methods to manipulate private fields and invoke private methods

    @SuppressWarnings("unchecked")
    private List<JsonElement> getStack() throws Exception {
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        return (List<JsonElement>) stackField.get(writer);
    }

    private void addJsonElementToStack(JsonElement element) throws Exception {
        List<JsonElement> stack = getStack();
        stack.add(element);
    }

    private void setPendingName(String name) throws Exception {
        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);
        pendingNameField.set(writer, name);
    }
}