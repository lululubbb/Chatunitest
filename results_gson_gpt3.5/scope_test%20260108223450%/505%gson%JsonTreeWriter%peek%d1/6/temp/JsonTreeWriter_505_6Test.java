package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class JsonTreeWriter_505_6Test {

    private JsonTreeWriter jsonTreeWriter;

    @BeforeEach
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void peek_ShouldReturnTopElement_WhenStackHasElements() throws Exception {
        // Prepare a JsonElement and set it on the stack field using reflection
        JsonArray jsonArray = new JsonArray();

        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.add(jsonArray);

        // Access private peek method
        Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        JsonElement result = (JsonElement) peekMethod.invoke(jsonTreeWriter);

        assertSame(jsonArray, result);
    }

    @Test
    @Timeout(8000)
    public void peek_ShouldThrowIndexOutOfBoundsException_WhenStackIsEmpty() throws Exception {
        // Ensure stack is empty
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();

        Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        IndexOutOfBoundsException thrown = assertThrows(IndexOutOfBoundsException.class, () -> {
            try {
                peekMethod.invoke(jsonTreeWriter);
            } catch (InvocationTargetException e) {
                // Rethrow the cause of InvocationTargetException if it is IndexOutOfBoundsException
                Throwable cause = e.getCause();
                if (cause instanceof IndexOutOfBoundsException) {
                    throw (IndexOutOfBoundsException) cause;
                }
                // Otherwise rethrow the InvocationTargetException
                throw e;
            }
        });
        assertNotNull(thrown);
    }

    @Test
    @Timeout(8000)
    public void peek_ShouldReturnLastElement_WhenMultipleElementsInStack() throws Exception {
        JsonObject jsonObject = new JsonObject();
        JsonPrimitive jsonPrimitive = new JsonPrimitive("test");

        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();
        stack.add(jsonObject);
        stack.add(jsonPrimitive);

        Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);

        JsonElement result = (JsonElement) peekMethod.invoke(jsonTreeWriter);

        assertSame(jsonPrimitive, result);
    }
}