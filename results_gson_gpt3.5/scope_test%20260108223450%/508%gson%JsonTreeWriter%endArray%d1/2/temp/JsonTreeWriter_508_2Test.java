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
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_508_2Test {
  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void endArray_stackEmpty_throwsIllegalStateException() throws Exception {
    // Clear stack via reflection
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(writer);
    stack.clear();

    // pendingName is null by default

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      writer.endArray();
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void endArray_pendingNameNotNull_throwsIllegalStateException() throws Exception {
    // Add element to stack to avoid empty stack
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<Object> stack = (List<Object>) stackField.get(writer);
    stack.clear();
    stack.add(new JsonArray());

    // Set pendingName to non-null via reflection
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, "someName");

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      writer.endArray();
    });
    assertNotNull(thrown);
  }

  @Test
    @Timeout(8000)
  public void endArray_topIsJsonArray_removesFromStackAndReturnsThis() throws Exception {
    // Add JsonArray element to stack
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<Object> stack = (List<Object>) stackField.get(writer);
    stack.clear();
    JsonArray arr = new JsonArray();
    stack.add(arr);

    // pendingName is null

    JsonWriter result = writer.endArray();

    assertSame(writer, result);
    // Confirm stack size decreased by 1 (empty now)
    assertEquals(0, stack.size());
  }

  @Test
    @Timeout(8000)
  public void endArray_topNotJsonArray_throwsIllegalStateException() throws Exception {
    // Add JsonPrimitive element to stack
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<Object> stack = (List<Object>) stackField.get(writer);
    stack.clear();
    JsonPrimitive prim = new JsonPrimitive("not array");
    stack.add(prim);

    // pendingName is null

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      writer.endArray();
    });
    assertNotNull(thrown);
  }
}