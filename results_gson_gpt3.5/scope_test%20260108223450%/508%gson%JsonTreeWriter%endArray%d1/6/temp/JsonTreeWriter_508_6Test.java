package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import java.io.Writer;
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
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonTreeWriter_508_6Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void endArray_shouldThrowIllegalStateException_whenStackIsEmpty() {
    // stack is empty initially
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
    @Timeout(8000)
  void endArray_shouldThrowIllegalStateException_whenPendingNameIsNotNull() throws Exception {
    setField(jsonTreeWriter, "pendingName", "name");
    // Add one element to stack to avoid empty stack exception
    List<JsonElement> stack = new ArrayList<>();
    stack.add(new JsonArray());
    setField(jsonTreeWriter, "stack", stack);

    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
    @Timeout(8000)
  void endArray_shouldRemoveTopStackElement_whenTopIsJsonArray() throws Exception {
    JsonArray jsonArray = new JsonArray();
    List<JsonElement> stack = new ArrayList<>();
    stack.add(jsonArray);
    setField(jsonTreeWriter, "stack", stack);

    JsonWriter result = jsonTreeWriter.endArray();

    // The stack should be empty after removing the top element
    List<JsonElement> updatedStack = getField(jsonTreeWriter, "stack");
    assertTrue(updatedStack.isEmpty());
    // The returned object should be the same instance
    assertSame(jsonTreeWriter, result);
  }

  @Test
    @Timeout(8000)
  void endArray_shouldThrowIllegalStateException_whenTopIsNotJsonArray() throws Exception {
    List<JsonElement> stack = new ArrayList<>();
    stack.add(JsonNull.INSTANCE);
    setField(jsonTreeWriter, "stack", stack);

    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  // Utility methods to access private fields via reflection

  @SuppressWarnings("unchecked")
  private <T> T getField(Object instance, String fieldName) throws Exception {
    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    return (T) field.get(instance);
  }

  private void setField(Object instance, String fieldName, Object value) throws Exception {
    Field field = instance.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(instance, value);
  }
}