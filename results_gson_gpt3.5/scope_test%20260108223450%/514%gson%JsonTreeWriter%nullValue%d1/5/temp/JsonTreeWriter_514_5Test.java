package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

class JsonTreeWriter_514_5Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() throws Exception {
    jsonTreeWriter = new JsonTreeWriter();

    // Initialize the stack with a JsonObject to avoid IllegalStateException
    Method beginObjectMethod = JsonTreeWriter.class.getDeclaredMethod("beginObject");
    beginObjectMethod.setAccessible(true);
    beginObjectMethod.invoke(jsonTreeWriter);
  }

  @Test
    @Timeout(8000)
  void nullValue_shouldPutJsonNullInstanceAndReturnThis() throws Exception {
    // Remove any pendingName before calling nullValue to avoid IllegalStateException
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, null);

    // Clear the stack and push a JsonObject to prevent IllegalStateException in put()
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(jsonTreeWriter);
    stack.clear();

    // Push a new JsonObject onto the stack
    Class<?> jsonObjectClass = Class.forName("com.google.gson.JsonObject");
    Object jsonObjectInstance = jsonObjectClass.getDeclaredConstructor().newInstance();
    stack.add(jsonObjectInstance);

    JsonWriter returned = jsonTreeWriter.nullValue();
    assertSame(jsonTreeWriter, returned);

    // Use reflection to access private field 'product'
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object productValue = productField.get(jsonTreeWriter);

    // The product field should be JsonNull.INSTANCE after nullValue call
    assertSame(JsonNull.INSTANCE, productValue);

    // Use reflection to invoke private method peek() to verify top of stack or product
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement peeked = (JsonElement) peekMethod.invoke(jsonTreeWriter);

    // Since an object was started, peek should be that object (not JsonNull.INSTANCE)
    assertEquals("com.google.gson.JsonObject", peeked.getClass().getName());
  }

  @Test
    @Timeout(8000)
  void nullValue_whenPendingNameIsSet_shouldPutJsonNullInstanceInObject() throws Exception {
    // Use reflection to set private field 'pendingName'
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, "testName");

    JsonWriter returned = jsonTreeWriter.nullValue();
    assertSame(jsonTreeWriter, returned);

    // Access private field 'stack' (List<JsonElement>)
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(jsonTreeWriter);

    // The stack should contain a JsonObject because pendingName triggers putting into an object
    assertFalse(stack.isEmpty());
    Object top = stack.get(stack.size() - 1);
    // top element should be JsonObject
    assertEquals("com.google.gson.JsonObject", top.getClass().getName());

    // pendingName should be reset to null after put
    Object pendingNameAfter = pendingNameField.get(jsonTreeWriter);
    assertNull(pendingNameAfter);
  }
}