package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_508_5Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void endArray_whenStackIsEmpty_throwsIllegalStateException() {
    // stack is empty initially
    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
    @Timeout(8000)
  public void endArray_whenPendingNameIsNotNull_throwsIllegalStateException() throws Exception {
    setPendingName("name");
    // Add a JsonArray to stack so stack is not empty
    pushToStack(new JsonArray());

    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  @Test
    @Timeout(8000)
  public void endArray_whenTopOfStackIsJsonArray_removesTopAndReturnsThis() throws Exception {
    JsonArray jsonArray = new JsonArray();
    pushToStack(jsonArray);

    JsonWriter result = jsonTreeWriter.endArray();

    assertSame(jsonTreeWriter, result);
    List<JsonElement> stack = getStack();
    assertFalse(stack.contains(jsonArray));
  }

  @Test
    @Timeout(8000)
  public void endArray_whenTopOfStackIsNotJsonArray_throwsIllegalStateException() throws Exception {
    // Push a JsonObject (not JsonArray)
    pushToStack(new com.google.gson.JsonObject());

    assertThrows(IllegalStateException.class, () -> jsonTreeWriter.endArray());
  }

  // --- Helper methods using reflection ---

  private void setPendingName(String name) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, name);
  }

  @SuppressWarnings("unchecked")
  private List<JsonElement> getStack() throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(jsonTreeWriter);
  }

  private void pushToStack(JsonElement element) throws Exception {
    List<JsonElement> stack = getStack();
    stack.add(element);
  }
}