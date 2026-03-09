package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.Reader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class JsonTreeReader_237_3Test {

    private JsonTreeReader jsonTreeReader;
    private Method expectMethod;
    private Method peekMethod;

    @BeforeEach
    public void setUp() throws NoSuchMethodException {
        // Create a JsonElement to pass to constructor, using JsonNull since it's simplest
        JsonElement element = JsonNull.INSTANCE;
        jsonTreeReader = new JsonTreeReader(element);

        expectMethod = JsonTreeReader.class.getDeclaredMethod("expect", JsonToken.class);
        expectMethod.setAccessible(true);

        peekMethod = JsonTreeReader.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    public void expect_whenPeekMatchesExpected_doesNotThrow() throws Throwable {
        // We need to set the stack so that peek() returns expected JsonToken
        // peek() is overridden and depends on stack, so we simulate it by pushing a JsonToken
        // But stack holds JsonElement or similar, so we push a JsonArray to cause peek() to return BEGIN_ARRAY etc.

        // push a JsonArray to stack to simulate peek() returning BEGIN_ARRAY
        JsonArray jsonArray = new JsonArray();
        pushStack(jsonArray);

        // invoke expect with expected = BEGIN_ARRAY, should not throw
        try {
            expectMethod.invoke(jsonTreeReader, JsonToken.BEGIN_ARRAY);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    @Timeout(8000)
    public void expect_whenPeekDoesNotMatchExpected_throwsIllegalStateException() throws Throwable {
        // push JsonArray so peek() returns BEGIN_ARRAY
        JsonArray jsonArray = new JsonArray();
        pushStack(jsonArray);

        // invoke expect with expected = BEGIN_OBJECT, should throw IllegalStateException
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            try {
                expectMethod.invoke(jsonTreeReader, JsonToken.BEGIN_OBJECT);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });

        assertTrue(thrown.getMessage().contains("Expected BEGIN_OBJECT but was BEGIN_ARRAY"));
    }

    @Test
    @Timeout(8000)
    public void expect_whenStackEmpty_throwsIllegalStateException() throws Throwable {
        // stack is empty by default, peek() will throw or return something else
        IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
            try {
                expectMethod.invoke(jsonTreeReader, JsonToken.BEGIN_OBJECT);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });

        assertTrue(thrown.getMessage().contains("Expected BEGIN_OBJECT but was"));
    }

    // Helper method to push an element onto the private stack field
    private void pushStack(JsonElement element) {
        try {
            var stackField = JsonTreeReader.class.getDeclaredField("stack");
            stackField.setAccessible(true);
            var stackSizeField = JsonTreeReader.class.getDeclaredField("stackSize");
            stackSizeField.setAccessible(true);

            Object[] stack = (Object[]) stackField.get(jsonTreeReader);
            int stackSize = stackSizeField.getInt(jsonTreeReader);

            stack[stackSize] = element;
            stackSizeField.setInt(jsonTreeReader, stackSize + 1);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}