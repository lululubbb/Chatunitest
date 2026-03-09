package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_509_3Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void beginObject_shouldAddJsonObjectToStack_andReturnThis() throws IOException, Exception {
    // Call beginObject
    JsonWriter returned = jsonTreeWriter.beginObject();

    // returned should be the same instance
    assertSame(jsonTreeWriter, returned);

    // Using reflection to get private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // The stack should not be empty
    assertFalse(stack.isEmpty());

    // The last element in stack should be a JsonObject
    JsonElement lastElement = stack.get(stack.size() - 1);
    assertTrue(lastElement instanceof JsonObject);

    // Using reflection to get private method 'peek' and verify it returns the same object on top of stack
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement peeked = (JsonElement) peekMethod.invoke(jsonTreeWriter);
    assertSame(lastElement, peeked);

    // Using reflection to get private method 'get' and verify it returns the current product
    Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
    getMethod.setAccessible(true);

    // We must call endObject() to complete the object and set product properly
    jsonTreeWriter.endObject();

    JsonElement product = (JsonElement) getMethod.invoke(jsonTreeWriter);

    // Since beginObject calls put(object) which sets product to object,
    // and endObject completes it, product should be the same as lastElement
    assertSame(lastElement, product);
  }

  @Test
    @Timeout(8000)
  public void beginObject_multipleTimes_shouldAddMultipleJsonObjectsToStack() throws IOException, Exception {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertEquals(0, stack.size());
  }

  @Test
    @Timeout(8000)
  public void beginObject_afterClose_shouldThrowException() throws Exception {
    // Using reflection to set private field 'product' to SENTINEL_CLOSED to simulate closed writer
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Field sentinelClosedField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    JsonPrimitive sentinelClosed = (JsonPrimitive) sentinelClosedField.get(null);
    productField.set(jsonTreeWriter, sentinelClosed);

    // Also clear the stack to simulate closed state properly (since beginObject checks stack size)
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    // Also set pendingName to null to avoid NullPointerException inside beginObject()
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, null);

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.beginObject();
    });
    assertTrue(thrown.getMessage().contains("closed"));
  }
}