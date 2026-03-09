package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_515_2Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValue_boolean_shouldPutJsonPrimitiveAndReturnThis() throws Exception {
    JsonWriter returned = jsonTreeWriter.value(true);
    assertSame(jsonTreeWriter, returned);

    // Use reflection to access private 'stack' field
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(jsonTreeWriter);
    assertTrue(stack.isEmpty(), "Stack should be empty after value() call because top-level values are stored in 'product' field");

    // Check the 'product' field instead
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(jsonTreeWriter);
    assertTrue(product instanceof JsonPrimitive);
    assertEquals(new JsonPrimitive(true), product);
  }

  @Test
    @Timeout(8000)
  public void testPut_methodAddsElementToStack() throws Exception {
    // Use reflection to invoke private method 'put'
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    JsonPrimitive element = new JsonPrimitive(false);
    putMethod.invoke(jsonTreeWriter, element);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(jsonTreeWriter);

    // The stack contains elements by identity or equals, but JsonElement equals is overridden.
    // Use stream anyMatch to check equivalence
    boolean contains = stack.stream().anyMatch(e -> e.equals(element));
    assertTrue(contains, "Stack should contain the element after put()");
  }

  @Test
    @Timeout(8000)
  public void testValue_boolean_multipleValues() throws IOException, Exception {
    jsonTreeWriter.value(true);

    // Use reflection to invoke private method 'put' to add second value
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);
    JsonPrimitive secondValue = new JsonPrimitive(false);
    putMethod.invoke(jsonTreeWriter, secondValue);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<?> stack = (List<?>) stackField.get(jsonTreeWriter);

    // The first value is stored in 'product' field, the second in stack
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(jsonTreeWriter);

    assertEquals(new JsonPrimitive(true), product);
    assertEquals(1, stack.size());
    assertEquals(secondValue, stack.get(0));
  }
}