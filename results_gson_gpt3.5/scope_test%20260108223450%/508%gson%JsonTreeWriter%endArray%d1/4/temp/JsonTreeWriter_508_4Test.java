package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_508_4Test {
  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void endArray_whenStackIsEmpty_throwsIllegalStateException() {
    // stack is empty initially
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      writer.endArray();
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  public void endArray_whenPendingNameIsNotNull_throwsIllegalStateException() throws Exception {
    setPendingName("name");
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      writer.endArray();
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  public void endArray_whenTopOfStackIsJsonArray_removesTopAndReturnsThis() throws Exception {
    JsonArray jsonArray = new JsonArray();
    pushStackElement(jsonArray);

    JsonWriter returned = writer.endArray();

    assertSame(writer, returned);
    List<JsonElement> stack = getStack();
    assertFalse(stack.contains(jsonArray));
  }

  @Test
    @Timeout(8000)
  public void endArray_whenTopOfStackIsNotJsonArray_throwsIllegalStateException() throws Exception {
    // Push a JsonObject instead of JsonArray
    JsonElement jsonObject = createJsonObjectMock();
    pushStackElement(jsonObject);

    Exception exception = assertThrows(IllegalStateException.class, () -> {
      writer.endArray();
    });
    assertNotNull(exception);
  }

  // Helper to set private field pendingName
  private void setPendingName(String name) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, name);
  }

  // Helper to get private field stack
  @SuppressWarnings("unchecked")
  private List<JsonElement> getStack() throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(writer);
  }

  // Helper to push element to stack
  private void pushStackElement(JsonElement element) throws Exception {
    List<JsonElement> stack = getStack();
    stack.add(element);
  }

  // Helper to create a JsonObject mock (JsonElement subclass)
  private JsonElement createJsonObjectMock() {
    return new JsonElement() {
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };
  }
}