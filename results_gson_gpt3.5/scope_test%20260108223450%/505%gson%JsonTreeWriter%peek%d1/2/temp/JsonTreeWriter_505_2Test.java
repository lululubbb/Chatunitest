package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_505_2Test {
    private JsonTreeWriter jsonTreeWriter;
    private Method peekMethod;

    @BeforeEach
    public void setUp() throws Exception {
        jsonTreeWriter = new JsonTreeWriter();
        peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void testPeek_withSingleElementOnStack() throws Exception {
        // Use reflection to access private field 'stack'
        var stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

        JsonPrimitive element = new JsonPrimitive("test");
        stack.add(element);

        JsonElement result = (JsonElement) peekMethod.invoke(jsonTreeWriter);
        assertSame(element, result);
    }

    @Test
    @Timeout(8000)
    public void testPeek_withMultipleElementsOnStack() throws Exception {
        var stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

        JsonPrimitive first = new JsonPrimitive("first");
        JsonPrimitive second = new JsonPrimitive("second");
        JsonPrimitive third = new JsonPrimitive("third");

        stack.add(first);
        stack.add(second);
        stack.add(third);

        JsonElement result = (JsonElement) peekMethod.invoke(jsonTreeWriter);
        assertSame(third, result);
    }

    @Test
    @Timeout(8000)
    public void testPeek_emptyStack_throwsIndexOutOfBoundsException() throws Exception {
        var stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

        stack.clear();

        IndexOutOfBoundsException thrown = assertThrows(IndexOutOfBoundsException.class, () -> {
            try {
                peekMethod.invoke(jsonTreeWriter);
            } catch (java.lang.reflect.InvocationTargetException e) {
                // Rethrow the cause if it is IndexOutOfBoundsException
                Throwable cause = e.getCause();
                if (cause instanceof IndexOutOfBoundsException) {
                    throw (IndexOutOfBoundsException) cause;
                }
                throw e;
            }
        });
        assertNotNull(thrown);
    }
}