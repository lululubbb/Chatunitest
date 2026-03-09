package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_508_1Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void endArray_whenStackEmpty_throwsIllegalStateException() {
    // stack is empty initially
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
    @Timeout(8000)
  public void endArray_whenPendingNameNotNull_throwsIllegalStateException() throws Exception {
    // add a JsonArray to stack to avoid first condition
    List<JsonElement> stack = getStack(jsonTreeWriter);
    stack.add(new JsonArray());

    // set pendingName to non-null
    setPendingName(jsonTreeWriter, "name");

    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
    @Timeout(8000)
  public void endArray_whenTopOfStackIsJsonArray_removesItAndReturnsThis() throws Exception {
    List<JsonElement> stack = getStack(jsonTreeWriter);
    JsonArray jsonArray = new JsonArray();
    stack.add(jsonArray);
    setPendingName(jsonTreeWriter, null);

    JsonWriter result = jsonTreeWriter.endArray();

    // The top JsonArray should be removed
    List<JsonElement> updatedStack = getStack(jsonTreeWriter);
    assertFalse(updatedStack.contains(jsonArray));
    assertEquals(jsonTreeWriter, result);
  }

  @Test
    @Timeout(8000)
  public void endArray_whenTopOfStackIsNotJsonArray_throwsIllegalStateException() throws Exception {
    List<JsonElement> stack = getStack(jsonTreeWriter);
    stack.add(JsonNull.INSTANCE); // Not a JsonArray
    setPendingName(jsonTreeWriter, null);

    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  // Utility to get private field 'stack'
  @SuppressWarnings("unchecked")
  private List<JsonElement> getStack(JsonTreeWriter writer) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(writer);
  }

  // Utility to set private field 'pendingName'
  private void setPendingName(JsonTreeWriter writer, String name) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, name);
  }

  // Utility to invoke private method peek()
  private JsonElement invokePeek(JsonTreeWriter writer) throws Exception {
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    return (JsonElement) peekMethod.invoke(writer);
  }
}