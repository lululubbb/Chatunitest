package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_511_5Test {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void name_NullName_ThrowsNullPointerException() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> writer.name(null));
        assertEquals("name == null", exception.getMessage());
    }

    @Test
    @Timeout(8000)
    public void name_EmptyStack_ThrowsIllegalStateException() {
        // stack is empty by default
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> writer.name("test"));
        assertNotNull(exception);
    }

    @Test
    @Timeout(8000)
    public void name_PendingNameNotNull_ThrowsIllegalStateException() throws Exception {
        setStackWithJsonObject();
        setPendingName("pending");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> writer.name("test"));
        assertNotNull(exception);
    }

    @Test
    @Timeout(8000)
    public void name_PeekNotJsonObject_ThrowsIllegalStateException() throws Exception {
        setStackWithJsonArray();

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> writer.name("test"));
        assertNotNull(exception);
    }

    @Test
    @Timeout(8000)
    public void name_ValidJsonObject_SetsPendingNameAndReturnsThis() throws Exception {
        setStackWithJsonObject();

        JsonWriter result = writer.name("propertyName");

        assertSame(writer, result);
        assertEquals("propertyName", getPendingName());
    }

    // Helper methods to set private fields via reflection

    private void setStackWithJsonObject() throws Exception {
        List<JsonElement> stack = new ArrayList<>();
        stack.add(new JsonObject());
        setField("stack", stack);
        setField("pendingName", null);
    }

    private void setStackWithJsonArray() throws Exception {
        List<JsonElement> stack = new ArrayList<>();
        stack.add(new JsonArray());
        setField("stack", stack);
        setField("pendingName", null);
    }

    private void setPendingName(String value) throws Exception {
        setField("pendingName", value);
    }

    private String getPendingName() throws Exception {
        return (String) getField("pendingName");
    }

    private void setField(String fieldName, Object value) throws Exception {
        Field field = JsonTreeWriter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(writer, value);
    }

    private Object getField(String fieldName) throws Exception {
        Field field = JsonTreeWriter.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(writer);
    }
}