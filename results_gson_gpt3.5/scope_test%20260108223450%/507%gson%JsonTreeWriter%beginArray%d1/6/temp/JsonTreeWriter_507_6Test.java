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
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_507_6Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void beginArray_shouldAddJsonArrayToStackAndReturnThis() throws Exception {
    JsonWriter result = jsonTreeWriter.beginArray();
    assertSame(jsonTreeWriter, result);

    // Access private field 'stack' using reflection
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertFalse(stack.isEmpty());
    JsonElement topElement = stack.get(stack.size() - 1);
    assertTrue(topElement instanceof JsonArray);
  }

  @Test
    @Timeout(8000)
  public void put_shouldAddElementToStackOrSetProduct() throws Exception {
    // Access private method put(JsonElement)
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // Initially stack is empty, so product should be set
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);

    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    assertTrue(stack.isEmpty());

    // Put a JsonPrimitive and verify product is set
    com.google.gson.JsonPrimitive primitive = new com.google.gson.JsonPrimitive("test");
    putMethod.invoke(jsonTreeWriter, primitive);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
    assertSame(primitive, product);

    // Add array to stack and then put an element to verify it adds to top container
    JsonArray array = new JsonArray();
    stack.add(array);

    com.google.gson.JsonPrimitive primitive2 = new com.google.gson.JsonPrimitive("test2");
    putMethod.invoke(jsonTreeWriter, primitive2);

    // The top container should now have primitive2 as element
    assertEquals(1, array.size());
    assertEquals(primitive2, array.get(0));
  }

  @Test
    @Timeout(8000)
  public void peek_shouldReturnTopOfStackOrProduct() throws Exception {
    // Access private method peek()
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);

    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // Clear stack to avoid IndexOutOfBoundsException
    stack.clear();

    // Set product to a known value instead of JsonNull.INSTANCE to avoid confusion
    com.google.gson.JsonPrimitive productValue = new com.google.gson.JsonPrimitive("product");
    productField.set(jsonTreeWriter, productValue);

    // When stack empty, peek returns product
    assertSame(productValue, peekMethod.invoke(jsonTreeWriter));

    // Add element to stack and peek returns top element
    JsonArray array = new JsonArray();
    stack.add(array);
    // Set product to JsonNull.INSTANCE (null in logic) to avoid peek() returning product when stack is not empty
    productField.set(jsonTreeWriter, JsonNull.INSTANCE);

    assertSame(array, peekMethod.invoke(jsonTreeWriter));
  }
}