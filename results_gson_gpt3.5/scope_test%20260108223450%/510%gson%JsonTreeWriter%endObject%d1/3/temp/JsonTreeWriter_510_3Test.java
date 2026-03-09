package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.io.Writer;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
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

public class JsonTreeWriter_510_3Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void endObject_whenStackIsEmpty_throwsIllegalStateException() throws Exception {
    // Ensure stack is empty
    setStack(jsonTreeWriter, new ArrayList<>());

    // pendingName is null by default
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.endObject();
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void endObject_whenPendingNameIsNotNull_throwsIllegalStateException() throws Exception {
    // Prepare stack with one JsonObject element
    List<JsonElement> stack = new ArrayList<>();
    stack.add(new JsonObject());
    setStack(jsonTreeWriter, stack);

    // Set pendingName to non-null
    setPendingName(jsonTreeWriter, "name");

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.endObject();
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void endObject_whenTopOfStackIsJsonObject_removesTopAndReturnsThis() throws Exception {
    List<JsonElement> stack = new ArrayList<>();
    JsonObject obj1 = new JsonObject();
    JsonObject obj2 = new JsonObject();
    stack.add(obj1);
    stack.add(obj2);
    setStack(jsonTreeWriter, stack);

    setPendingName(jsonTreeWriter, null);

    JsonWriter returned = jsonTreeWriter.endObject();

    assertSame(jsonTreeWriter, returned);
    List<JsonElement> modifiedStack = getStack(jsonTreeWriter);
    assertEquals(1, modifiedStack.size());
    assertSame(obj1, modifiedStack.get(0));
  }

  @Test
    @Timeout(8000)
  public void endObject_whenTopOfStackIsNotJsonObject_throwsIllegalStateException() throws Exception {
    List<JsonElement> stack = new ArrayList<>();
    stack.add(new JsonArray());
    setStack(jsonTreeWriter, stack);

    setPendingName(jsonTreeWriter, null);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.endObject();
    });
    assertNotNull(thrown);
  }

  // Helper methods to access private fields with reflection

  @SuppressWarnings("unchecked")
  private List<JsonElement> getStack(JsonTreeWriter writer) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(writer);
  }

  private void setStack(JsonTreeWriter writer, List<JsonElement> stack) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    stackField.set(writer, stack);
  }

  private void setPendingName(JsonTreeWriter writer, String name) throws Exception {
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, name);
  }
}