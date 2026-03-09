package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_510_5Test {
  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void endObject_stackEmpty_throwsIllegalStateException() {
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      writer.endObject();
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  public void endObject_pendingNameNotNull_throwsIllegalStateException() throws Exception {
    setPendingName("someName");
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      writer.endObject();
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  public void endObject_topOfStackIsJsonObject_removesTopAndReturnsThis() throws Exception {
    JsonObject jsonObject = new JsonObject();
    addToStack(jsonObject);
    setPendingName(null);

    JsonWriter returned = writer.endObject();

    assertSame(writer, returned);
    List<JsonElement> stack = getStack();
    assertFalse(stack.contains(jsonObject));
  }

  @Test
    @Timeout(8000)
  public void endObject_topOfStackIsNotJsonObject_throwsIllegalStateException() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("primitive");
    addToStack(jsonPrimitive);
    setPendingName(null);

    Exception exception = assertThrows(IllegalStateException.class, () -> {
      writer.endObject();
    });
    assertNotNull(exception);
  }

  // Helper to set private field pendingName
  private void setPendingName(String name) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, name);
  }

  // Helper to add element to private stack field
  @SuppressWarnings("unchecked")
  private void addToStack(JsonElement element) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.add(element);
  }

  // Helper to get private stack field
  @SuppressWarnings("unchecked")
  private List<JsonElement> getStack() throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(writer);
  }
}