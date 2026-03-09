package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

class JsonTreeWriterPeekTest {

    private JsonTreeWriter writer;

    @BeforeEach
    public void setUp() {
        writer = new JsonTreeWriter();
    }

    @Test
    @Timeout(8000)
    public void testPeek_withSingleElementInStack_returnsThatElement() throws Exception {
        // Prepare stack with one JsonElement
        JsonPrimitive element = new JsonPrimitive("test");
        setStack(new ArrayList<>(List.of(element)));

        JsonElement result = invokePeek();

        assertSame(element, result);
    }

    @Test
    @Timeout(8000)
    public void testPeek_withMultipleElementsInStack_returnsLastElement() throws Exception {
        JsonPrimitive element1 = new JsonPrimitive("one");
        JsonPrimitive element2 = new JsonPrimitive("two");
        JsonPrimitive element3 = new JsonPrimitive("three");
        List<JsonElement> list = new ArrayList<>();
        list.add(element1);
        list.add(element2);
        list.add(element3);
        setStack(list);

        JsonElement result = invokePeek();

        assertSame(element3, result);
    }

    @Test
    @Timeout(8000)
    public void testPeek_withEmptyStack_throwsIndexOutOfBoundsException() throws Exception {
        setStack(new ArrayList<>());

        Exception exception = assertThrows(IndexOutOfBoundsException.class, this::invokePeek);
        assertNotNull(exception);
    }

    // Helper method to set the private stack field via reflection
    private void setStack(List<JsonElement> elements) throws Exception {
        Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
        stackField.setAccessible(true);
        // Clear the existing stack and add all elements to avoid replacing the list instance
        List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
        stack.clear();
        stack.addAll(elements);
    }

    // Helper method to invoke private peek method via reflection
    private JsonElement invokePeek() throws Exception {
        Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
        peekMethod.setAccessible(true);
        return (JsonElement) peekMethod.invoke(writer);
    }
}