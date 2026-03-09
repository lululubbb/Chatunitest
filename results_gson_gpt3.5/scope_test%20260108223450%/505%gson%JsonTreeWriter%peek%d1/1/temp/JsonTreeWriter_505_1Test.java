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

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.List;

class JsonTreeWriterPeekTest {

    private JsonTreeWriter jsonTreeWriter;
    private Method peekMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        jsonTreeWriter = new JsonTreeWriter();
        peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void peek_shouldReturnTopElement_whenStackHasElements() throws Exception {
        // Arrange
        JsonPrimitive element1 = new JsonPrimitive("first");
        JsonPrimitive element2 = new JsonPrimitive("second");

        // Use reflection to access private field 'stack'
        var stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear(); // ensure empty before adding
        stack.add(element1);
        stack.add(element2);

        // Act
        JsonElement result = (JsonElement) peekMethod.invoke(jsonTreeWriter);

        // Assert
        assertSame(element2, result);
    }

    @Test
    @Timeout(8000)
    void peek_shouldThrowIndexOutOfBoundsException_whenStackIsEmpty() throws Exception {
        // Ensure stack is empty
        var stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
        stack.clear();

        // Act & Assert
        Exception exception = assertThrows(Exception.class, () -> peekMethod.invoke(jsonTreeWriter));
        // InvocationTargetException is expected, cause should be IndexOutOfBoundsException
        assertTrue(exception instanceof java.lang.reflect.InvocationTargetException);
        Throwable cause = exception.getCause();
        assertNotNull(cause);
        assertTrue(cause instanceof IndexOutOfBoundsException);
    }
}