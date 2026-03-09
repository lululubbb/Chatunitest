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

public class JsonTreeWriter_509_2Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() throws Exception {
    jsonTreeWriter = new JsonTreeWriter();

    // Reset internal fields to initial state before each test
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    productField.set(jsonTreeWriter, JsonNull.INSTANCE);

    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, null);
  }

  @Test
    @Timeout(8000)
  public void beginObject_shouldAddJsonObjectToStack_andReturnThis() throws Exception {
    JsonWriter returned = jsonTreeWriter.beginObject();

    // Assert returned this
    assertSame(jsonTreeWriter, returned);

    // Access private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // Assert stack size is 1 and top element is a JsonObject
    assertEquals(1, stack.size());
    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top instanceof JsonObject);

    // Access private method 'peek' to check top element equals peek()
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement peeked = (JsonElement) peekMethod.invoke(jsonTreeWriter);
    assertSame(top, peeked);

    // Access private field 'pendingName' to check it is still null
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    Object pendingName = pendingNameField.get(jsonTreeWriter);
    assertNull(pendingName);
  }

  @Test
    @Timeout(8000)
  public void beginObject_shouldCallPutWithCreatedJsonObject() throws Exception {
    // Spy on jsonTreeWriter to verify private method 'put' is called with the correct JsonObject
    JsonTreeWriter spyWriter = spy(jsonTreeWriter);

    JsonWriter returned = spyWriter.beginObject();

    // Use reflection to verify private method 'put' was called once with a JsonObject argument
    // Mockito cannot verify private method calls directly, so we verify by inspecting the stack content

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(spyWriter);
    JsonElement jsonObjectFromStack = stack.get(stack.size() - 1);

    assertTrue(jsonObjectFromStack instanceof JsonObject);

    // Returned is this
    assertSame(spyWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void beginObject_shouldWorkMultipleTimes() throws IOException, NoSuchFieldException, IllegalAccessException {
    jsonTreeWriter.beginObject();
    jsonTreeWriter.beginObject();

    // Access private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> updatedStack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertEquals(2, updatedStack.size());
    assertTrue(updatedStack.get(0) instanceof JsonObject);
    assertTrue(updatedStack.get(1) instanceof JsonObject);
  }
}