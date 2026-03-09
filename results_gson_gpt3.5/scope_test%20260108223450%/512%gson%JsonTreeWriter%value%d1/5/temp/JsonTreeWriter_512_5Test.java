package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_512_5Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() throws Exception {
        jsonTreeWriter = new JsonTreeWriter();

        // Initialize the stack with a root element to avoid empty stack errors
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();
        stack.add(new JsonObject());

        // Clear pendingName field to avoid IllegalStateException in put()
        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);
        pendingNameField.set(jsonTreeWriter, null);
    }

    private void prepareStackAndPendingName(JsonTreeWriter writer) throws Exception {
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
        stack.clear();
        stack.add(new JsonObject());

        Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
        pendingNameField.setAccessible(true);
        pendingNameField.set(writer, null);
    }

    @Test
    @Timeout(8000)
    public void testValue_WithNonNullString_AddsJsonPrimitiveAndReturnsThis() throws Exception {
        prepareStackAndPendingName(jsonTreeWriter);

        String testValue = "testString";

        JsonWriter returned = jsonTreeWriter.value(testValue);

        assertSame(jsonTreeWriter, returned);

        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

        assertFalse(stack.isEmpty());
        JsonElement element = stack.get(stack.size() - 1);
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(testValue, ((JsonPrimitive) element).getAsString());
    }

    @Test
    @Timeout(8000)
    public void testValue_WithNullString_CallsNullValueAndReturnsItsResult() throws IOException {
        JsonTreeWriter spyWriter = spy(jsonTreeWriter);
        // Prepare stack and pendingName for spy as well
        try {
            prepareStackAndPendingName(spyWriter);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        doReturn(spyWriter).when(spyWriter).nullValue();

        JsonWriter returned = spyWriter.value((String) null);

        verify(spyWriter).nullValue();
        assertSame(spyWriter, returned);
    }

    @Test
    @Timeout(8000)
    public void testPut_AddsElementToStack() throws Exception {
        prepareStackAndPendingName(jsonTreeWriter);

        Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
        putMethod.setAccessible(true);

        JsonPrimitive primitive = new JsonPrimitive("hello");
        putMethod.invoke(jsonTreeWriter, primitive);

        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

        assertFalse(stack.isEmpty());
        assertEquals(primitive, stack.get(stack.size() - 1));
    }

    @Test
    @Timeout(8000)
    public void testValue_WithEmptyString_AddsJsonPrimitiveAndReturnsThis() throws Exception {
        prepareStackAndPendingName(jsonTreeWriter);

        String testValue = "";

        JsonWriter returned = jsonTreeWriter.value(testValue);

        assertSame(jsonTreeWriter, returned);

        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

        assertFalse(stack.isEmpty());
        JsonElement element = stack.get(stack.size() - 1);
        assertTrue(element instanceof JsonPrimitive);
        assertEquals(testValue, ((JsonPrimitive) element).getAsString());
    }
}