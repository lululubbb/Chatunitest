package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import java.io.Writer;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_510_1Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void endObject_withEmptyStack_throwsIllegalStateException() throws Exception {
    // stack is empty by default on new instance
    setPendingName(jsonTreeWriter, null);
    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.endObject();
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  public void endObject_withPendingNameNotNull_throwsIllegalStateException() throws Exception {
    // Use reflection to add an element to stack so it is not empty
    List<JsonElement> stack = getStack(jsonTreeWriter);
    stack.add(new JsonObject());

    // Set pendingName to non-null
    setPendingName(jsonTreeWriter, "name");

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.endObject();
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  public void endObject_withTopElementNotJsonObject_throwsIllegalStateException() throws Exception {
    List<JsonElement> stack = getStack(jsonTreeWriter);
    // Add JsonPrimitive on stack top, not JsonObject
    stack.add(new JsonPrimitive("primitive"));
    setPendingName(jsonTreeWriter, null);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.endObject();
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  public void endObject_withJsonObjectOnTop_removesTopAndReturnsThis() throws Exception {
    List<JsonElement> stack = getStack(jsonTreeWriter);
    JsonObject jsonObject = new JsonObject();
    stack.add(jsonObject);
    setPendingName(jsonTreeWriter, null);

    int initialSize = stack.size();
    JsonWriter returned = jsonTreeWriter.endObject();
    int finalSize = stack.size();

    assertSame(jsonTreeWriter, returned);
    assertEquals(initialSize - 1, finalSize);
    assertFalse(stack.contains(jsonObject));
  }

  // Helper to get private field stack
  @SuppressWarnings("unchecked")
  private List<JsonElement> getStack(JsonTreeWriter writer) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(writer);
  }

  // Helper to set private field pendingName
  private void setPendingName(JsonTreeWriter writer, String name) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, name);
  }
}