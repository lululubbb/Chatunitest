package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_511_3Test {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void testName_nullName_throwsNullPointerException() {
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> writer.name(null));
        assertEquals("name == null", thrown.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testName_emptyStack_throwsIllegalStateException() {
        // stack is empty by default in new writer
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> writer.name("test"));
        assertNull(getPendingName(writer));
        assertTrue(thrown.getMessage() == null || thrown.getMessage().isEmpty());
    }

    @Test
    @Timeout(8000)
    public void testName_pendingNameNotNull_throwsIllegalStateException() throws Exception {
        // Prepare stack with one JsonObject element
        pushToStack(writer, new JsonObject());
        setPendingName(writer, "alreadyPending");

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> writer.name("test"));
        assertEquals("alreadyPending", getPendingName(writer));
    }

    @Test
    @Timeout(8000)
    public void testName_peekIsJsonObject_setsPendingNameAndReturnsThis() throws Exception {
        JsonObject jsonObject = new JsonObject();
        pushToStack(writer, jsonObject);

        JsonWriter result = writer.name("myName");

        assertSame(writer, result);
        assertEquals("myName", getPendingName(writer));
    }

    @Test
    @Timeout(8000)
    public void testName_peekIsNotJsonObject_throwsIllegalStateException() throws Exception {
        // Push a JsonArray instead of JsonObject
        pushToStack(writer, new JsonArray());

        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> writer.name("myName"));
        assertNull(getPendingName(writer));
    }

    // Helper methods to access private fields and methods via reflection

    private void pushToStack(JsonTreeWriter writer, JsonElement element) throws Exception {
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
        stack.add(element);
    }

    private String getPendingName(JsonTreeWriter writer) {
        try {
            Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
            pendingNameField.setAccessible(true);
            return (String) pendingNameField.get(writer);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setPendingName(JsonTreeWriter writer, String name) {
        try {
            Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
            pendingNameField.setAccessible(true);
            pendingNameField.set(writer, name);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}