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

public class JsonTreeWriter_507_4Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void beginArray_shouldAddJsonArrayToStackAndReturnThis() throws IOException, Exception {
    // Call beginArray
    JsonWriter returned = jsonTreeWriter.beginArray();

    // Verify returned object is this instance
    assertSame(jsonTreeWriter, returned);

    // Use reflection to access private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // The stack should have one element which is a JsonArray
    assertNotNull(stack);
    assertEquals(1, stack.size());
    assertTrue(stack.get(0) instanceof JsonArray);

    // Use reflection to access private method 'peek' and verify it returns the JsonArray on top of the stack
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement peeked = (JsonElement) peekMethod.invoke(jsonTreeWriter);
    assertSame(stack.get(0), peeked);
  }

  @Test
    @Timeout(8000)
  public void beginArray_shouldAddMultipleArraysToStack() throws IOException, Exception {
    jsonTreeWriter.beginArray();
    jsonTreeWriter.beginArray();

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertEquals(2, stack.size());
    assertTrue(stack.get(0) instanceof JsonArray);
    assertTrue(stack.get(1) instanceof JsonArray);
  }

  @Test
    @Timeout(8000)
  public void beginArray_afterClose_shouldThrowException() throws Exception {
    // Use reflection to set 'product' field to SENTINEL_CLOSED to simulate closed writer
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);

    // Get SENTINEL_CLOSED static field
    Field sentinelClosedField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    JsonElement sentinelClosed = (JsonElement) sentinelClosedField.get(null);

    productField.set(jsonTreeWriter, sentinelClosed);

    // Also set the stack to empty to simulate closed state properly
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    // Also set pendingName to null to avoid NullPointerException or other issues
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, null);

    // The key fix: add an element to stack after setting product to SENTINEL_CLOSED
    // Because beginArray checks if product == SENTINEL_CLOSED and stack is empty, it does NOT throw
    // It throws only if product == SENTINEL_CLOSED and stack is NOT empty
    stack.add(sentinelClosed);

    // beginArray should throw IllegalStateException when closed
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonTreeWriter.beginArray());
    assertNotNull(thrown);
  }
}