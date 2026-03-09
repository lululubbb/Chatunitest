package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.MalformedJsonException;
import java.io.IOException;
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

import java.lang.reflect.Field;

public class JsonTreeReader_253_6Test {

    private JsonTreeReader jsonTreeReader;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize with a JsonNull to avoid automatic stack initialization with an object
        jsonTreeReader = new JsonTreeReader(JsonNull.INSTANCE);
        // Clear and reset stack, stackSize, pathNames, pathIndices to empty state for controlled testing
        setField(jsonTreeReader, "stack", new Object[32]);
        setField(jsonTreeReader, "stackSize", 0);
        setField(jsonTreeReader, "pathNames", new String[32]);
        setField(jsonTreeReader, "pathIndices", new int[32]);
    }

    @Test
    @Timeout(8000)
    public void testGetPreviousPath_emptyStack() throws Exception {
        // stackSize = 0, so getPath(true) should return "$"
        setField(jsonTreeReader, "stackSize", 0);
        String path = jsonTreeReader.getPreviousPath();
        assertEquals("$", path);
    }

    @Test
    @Timeout(8000)
    public void testGetPreviousPath_withObjectAndName() throws Exception {
        // Prepare stack with one JsonObject and stackSize=1
        JsonObject jsonObject = new JsonObject();
        Object[] stack = new Object[32];
        stack[0] = jsonObject;
        setField(jsonTreeReader, "stack", stack);
        setField(jsonTreeReader, "stackSize", 1);

        // pathNames[0] = "fieldName"
        String[] pathNames = new String[32];
        pathNames[0] = "fieldName";
        setField(jsonTreeReader, "pathNames", pathNames);
        // pathIndices[0] = 0
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(jsonTreeReader, "pathIndices", pathIndices);

        // Decrement pathIndices[0] by 1 for previous path logic
        pathIndices[0] = pathIndices[0] - 1;
        setField(jsonTreeReader, "pathIndices", pathIndices);

        String path = jsonTreeReader.getPreviousPath();
        // Expected "$.fieldName"
        assertEquals("$.fieldName", path);
    }

    @Test
    @Timeout(8000)
    public void testGetPreviousPath_withArrayAndIndex() throws Exception {
        // Prepare stack with one JsonArray and stackSize=1
        JsonArray jsonArray = new JsonArray();
        Object[] stack = new Object[32];
        stack[0] = jsonArray;
        setField(jsonTreeReader, "stack", stack);
        setField(jsonTreeReader, "stackSize", 1);

        // pathNames[0] = null (arrays don't have names)
        String[] pathNames = new String[32];
        pathNames[0] = null;
        setField(jsonTreeReader, "pathNames", pathNames);
        // pathIndices[0] = 3 (current index)
        int[] pathIndices = new int[32];
        pathIndices[0] = 3;
        // Decrement pathIndices[0] by 1 for previous path logic
        pathIndices[0] = pathIndices[0] - 1;
        setField(jsonTreeReader, "pathIndices", pathIndices);

        String path = jsonTreeReader.getPreviousPath();
        // Expected "$[3]"
        assertEquals("$[3]", path);
    }

    @Test
    @Timeout(8000)
    public void testGetPreviousPath_withMultipleStackElements() throws Exception {
        // stack with JsonObject, JsonArray, JsonObject
        JsonObject obj1 = new JsonObject();
        JsonArray arr = new JsonArray();
        JsonObject obj2 = new JsonObject();

        Object[] stack = new Object[32];
        stack[0] = obj1;
        stack[1] = arr;
        stack[2] = obj2;

        setField(jsonTreeReader, "stack", stack);
        setField(jsonTreeReader, "stackSize", 3);

        String[] pathNames = new String[32];
        pathNames[0] = "obj1name";
        pathNames[1] = null;
        pathNames[2] = "obj2name";

        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        pathIndices[1] = 5;
        pathIndices[2] = 0;

        // Decrement pathIndices for previous path logic where appropriate
        pathIndices[0] = pathIndices[0] - 1;
        pathIndices[1] = pathIndices[1] - 1;
        pathIndices[2] = pathIndices[2] - 1;

        setField(jsonTreeReader, "pathNames", pathNames);
        setField(jsonTreeReader, "pathIndices", pathIndices);

        String path = jsonTreeReader.getPreviousPath();
        // Expected "$.obj1name[5].obj2name"
        assertEquals("$.obj1name[5].obj2name", path);
    }

    @Test
    @Timeout(8000)
    public void testGetPreviousPath_withNullPathNameAndArrayIndexZero() throws Exception {
        // stack with one JsonArray and stackSize=1
        JsonArray jsonArray = new JsonArray();
        Object[] stack = new Object[32];
        stack[0] = jsonArray;
        setField(jsonTreeReader, "stack", stack);
        setField(jsonTreeReader, "stackSize", 1);

        // pathNames[0] = null
        String[] pathNames = new String[32];
        pathNames[0] = null;
        setField(jsonTreeReader, "pathNames", pathNames);
        // pathIndices[0] = 0
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        // Decrement pathIndices[0] by 1 for previous path logic
        pathIndices[0] = pathIndices[0] - 1;
        setField(jsonTreeReader, "pathIndices", pathIndices);

        String path = jsonTreeReader.getPreviousPath();
        // Expected "$[0]"
        assertEquals("$[0]", path);
    }

    @Test
    @Timeout(8000)
    public void testGetPreviousPath_withNullStackElement() throws Exception {
        // stack with null element at index 0 and stackSize=1
        Object[] stack = new Object[32];
        stack[0] = null;
        setField(jsonTreeReader, "stack", stack);
        setField(jsonTreeReader, "stackSize", 1);

        String[] pathNames = new String[32];
        pathNames[0] = null;
        setField(jsonTreeReader, "pathNames", pathNames);
        int[] pathIndices = new int[32];
        pathIndices[0] = 0;
        setField(jsonTreeReader, "pathIndices", pathIndices);

        String path = jsonTreeReader.getPreviousPath();
        // Expected "$"
        assertEquals("$", path);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = JsonTreeReader.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}