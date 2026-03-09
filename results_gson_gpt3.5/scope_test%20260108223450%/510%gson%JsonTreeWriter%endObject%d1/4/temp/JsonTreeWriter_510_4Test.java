package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_510_4Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void endObject_whenStackEmpty_throwsIllegalStateException() throws Exception {
    // stack is empty initially
    Exception exception = assertThrows(IllegalStateException.class, () -> {
      writer.endObject();
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  public void endObject_whenPendingNameNotNull_throwsIllegalStateException() throws Exception {
    // Set pendingName to non-null via reflection
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, "name");

    // Add a JsonObject to stack so stack is not empty
    setStackWithJsonObject();

    Exception exception = assertThrows(IllegalStateException.class, () -> {
      writer.endObject();
    });
    assertNotNull(exception);
  }

  @Test
    @Timeout(8000)
  public void endObject_whenTopOfStackIsJsonObject_removesTopAndReturnsThis() throws Exception {
    setStackWithJsonObject();

    // pendingName is null by default, so no exception

    // Change type to JsonWriter to match method signature
    // but actually the returned object is JsonTreeWriter (subclass)
    // So assign to JsonWriter and cast to JsonTreeWriter if needed
    // Or just assign to JsonWriter as method returns JsonWriter
    // The test only needs to check returned == writer (same instance)
    // So use JsonWriter type here

    // Use JsonWriter as returned type
    com.google.gson.stream.JsonWriter returned = writer.endObject();

    assertSame(writer, returned);

    // Verify that the stack size decreased by 1 (from 1 to 0)
    List<?> stack = getStack();
    assertEquals(0, stack.size());
  }

  @Test
    @Timeout(8000)
  public void endObject_whenTopOfStackIsNotJsonObject_throwsIllegalStateException() throws Exception {
    // Add something other than JsonObject to stack (e.g. JsonPrimitive)
    addNonJsonObjectToStack();

    Exception exception = assertThrows(IllegalStateException.class, () -> {
      writer.endObject();
    });
    assertNotNull(exception);
  }

  // Helper methods for reflection manipulation

  @SuppressWarnings("unchecked")
  private List<JsonElement> getStack() throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    return (List<JsonElement>) stackField.get(writer);
  }

  private void setStackWithJsonObject() throws Exception {
    List<JsonElement> stack = getStack();
    stack.clear();
    JsonObject jsonObject = new JsonObject();
    stack.add(jsonObject);
  }

  private void addNonJsonObjectToStack() throws Exception {
    List<JsonElement> stack = getStack();
    stack.clear();
    Class<?> jsonPrimitiveClass = Class.forName("com.google.gson.JsonPrimitive");
    Object jsonPrimitive = jsonPrimitiveClass.getConstructor(String.class).newInstance("primitive");
    stack.add((JsonElement) jsonPrimitive);
  }
}