package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonTreeWriter_507_3Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void beginArray_shouldAddJsonArrayToStackAndReturnThis() throws IOException, Exception {
    JsonWriter returned = jsonTreeWriter.beginArray();
    assertSame(jsonTreeWriter, returned);

    // Access private field 'stack' via reflection
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertFalse(stack.isEmpty());
    JsonElement element = stack.get(stack.size() - 1);
    assertTrue(element instanceof JsonArray);

    // Access private method 'peek' to assert top of stack is JsonArray
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement peeked = (JsonElement) peekMethod.invoke(jsonTreeWriter);
    assertTrue(peeked instanceof JsonArray);
  }

  @Test
    @Timeout(8000)
  void beginArray_multipleCalls_shouldStackMultipleJsonArrays() throws IOException, Exception {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginArray();

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertEquals(2, stack.size());
    assertTrue(stack.get(0) instanceof JsonArray);
    assertTrue(stack.get(1) instanceof JsonArray);
  }

  @Test
    @Timeout(8000)
  void beginArray_afterPutPendingName_shouldAddArrayWithName() throws Exception, IOException {
    // Set private field pendingName via reflection
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, "testName");

    // Put a JsonObject on the stack to receive the named array
    JsonArray rootArray = new JsonArray();
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.add(new com.google.gson.JsonObject());

    jsonTreeWriter.beginArray();

    // The top of the stack should be the new JsonArray
    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top instanceof JsonArray);

    // The JsonObject below should contain the named array
    JsonElement parent = stack.get(stack.size() - 2);
    assertTrue(parent instanceof com.google.gson.JsonObject);
    assertTrue(((com.google.gson.JsonObject) parent).has("testName"));

    // pendingName should be reset to null after put
    assertNull(pendingNameField.get(jsonTreeWriter));
  }
}