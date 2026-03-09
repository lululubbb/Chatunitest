package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_509_6Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void beginObject_shouldAddJsonObjectToStack_andReturnThis() throws Exception {
    JsonWriter returned = writer.beginObject();
    assertSame(writer, returned, "beginObject should return this");

    // Access private field 'stack' via reflection
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    assertFalse(stack.isEmpty(), "Stack should not be empty after beginObject");
    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top instanceof JsonObject, "Top of stack should be a JsonObject");

    // Access private method peek() via reflection and verify it returns the same JsonObject
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement peeked = (JsonElement) peekMethod.invoke(writer);
    assertSame(top, peeked, "peek() should return the top of the stack");

    // Access private field 'pendingName' to verify it remains null
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    Object pendingName = pendingNameField.get(writer);
    assertNull(pendingName, "pendingName should be null after beginObject");

    // Access private field 'product' to verify it is now the JsonObject added
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    assertSame(top, product, "product should be the JsonObject after beginObject");
  }
}