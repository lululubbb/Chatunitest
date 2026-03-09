package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_510_6Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void endObject_whenStackEmpty_throwsIllegalStateException() {
        // stack is empty by default
        assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endObject());
    }

    @Test
    @Timeout(8000)
    public void endObject_whenPendingNameNotNull_throwsIllegalStateException() throws Exception {
        setPendingName("name");
        // stack must not be empty to reach pendingName check, so add a dummy element
        addToStack(new JsonObject());

        assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endObject());
    }

    @Test
    @Timeout(8000)
    public void endObject_whenTopOfStackIsJsonObject_removesTopAndReturnsThis() throws Exception {
        JsonObject jsonObject = new JsonObject();
        addToStack(jsonObject);
        setPendingName(null);

        JsonWriter returned = jsonTreeWriter.endObject();

        assertSame(jsonTreeWriter, returned);
        List<JsonElement> stack = getStack();
        assertTrue(stack.isEmpty());
    }

    @Test
    @Timeout(8000)
    public void endObject_whenTopOfStackIsNotJsonObject_throwsIllegalStateException() throws Exception {
        // Add an element that is NOT a JsonObject, e.g. JsonNull
        addToStack(JsonNull.INSTANCE);
        setPendingName(null);

        assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endObject());
    }

    // Helpers to access private fields via reflection

    private void setPendingName(String name) throws Exception {
        Field field = JsonTreeWriter.class.getDeclaredField("pendingName");
        field.setAccessible(true);
        field.set(jsonTreeWriter, name);
    }

    @SuppressWarnings("unchecked")
    private List<JsonElement> getStack() throws Exception {
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        return (List<JsonElement>) stackField.get(jsonTreeWriter);
    }

    private void addToStack(JsonElement element) throws Exception {
        List<JsonElement> stack = getStack();
        stack.add(element);
    }
}