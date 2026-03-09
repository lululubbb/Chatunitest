package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeWriter_509_1Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void beginObject_shouldAddJsonObjectToStackAndReturnThis() throws IOException, ReflectiveOperationException {
    // Call beginObject
    JsonWriter returned = jsonTreeWriter.beginObject();

    // returned should be same instance
    assertSame(jsonTreeWriter, returned);

    // Using reflection to get private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // stack should not be empty
    assertFalse(stack.isEmpty());

    // Last element in stack should be a JsonObject
    JsonElement lastElement = stack.get(stack.size() - 1);
    assertTrue(lastElement instanceof JsonObject);

    // Using reflection to get private method 'peek'
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement peeked = (JsonElement) peekMethod.invoke(jsonTreeWriter);
    assertSame(lastElement, peeked);

    // Using reflection to get private method 'get'
    Method getMethod = JsonTreeWriter.class.getDeclaredMethod("get");
    getMethod.setAccessible(true);
    JsonElement product = (JsonElement) getMethod.invoke(jsonTreeWriter);
    // Fix: product should be the JsonObject just added because beginObject sets the root element
    assertSame(lastElement, product);

    // Using reflection to get private field 'pendingName'
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    Object pendingName = pendingNameField.get(jsonTreeWriter);
    assertNull(pendingName);
  }

  @Test
    @Timeout(8000)
  void beginObject_multipleCalls_shouldStackMultipleObjects() throws IOException, ReflectiveOperationException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.endObject();
    jsonTreeWriter.beginObject();

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertEquals(1, stack.size());
    assertTrue(stack.get(0) instanceof JsonObject);
  }

  @Test
    @Timeout(8000)
  void beginObject_afterClose_shouldThrow() throws Exception {
    // Use reflection to set product field to SENTINEL_CLOSED to simulate closed writer
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Field sentinelClosedField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    Object sentinelClosed = sentinelClosedField.get(null);
    productField.set(jsonTreeWriter, sentinelClosed);

    // Also clear stack and pendingName to simulate closed state properly
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, null);

    // Call beginObject and expect IllegalStateException
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> jsonTreeWriter.beginObject());
    // The actual implementation throws IllegalStateException when closed
  }
}