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
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonTreeWriter_507_1Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void beginArray_shouldReturnThisAndModifyStackAndCallPut() throws Exception {
    // Call beginArray
    JsonWriter returned = jsonTreeWriter.beginArray();

    // returned should be the same instance
    assertSame(jsonTreeWriter, returned);

    // Access private field 'stack' via reflection
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // stack should have exactly one element which is a JsonArray
    assertEquals(1, stack.size());
    assertTrue(stack.get(0) instanceof JsonArray);

    // Access private field 'product' to check if put() updated it
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);

    // product should be the same JsonArray instance as in stack
    assertSame(stack.get(0), product);
  }

  @Test
    @Timeout(8000)
  void put_privateMethod_shouldSetProductAndModifyStackCorrectly() throws Exception {
    // Use reflection to get private method put(JsonElement)
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // Prepare a JsonPrimitive to put
    JsonPrimitive primitive = new JsonPrimitive("test");

    // invoke put
    putMethod.invoke(jsonTreeWriter, primitive);

    // Check that product is set to primitive
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
    assertSame(primitive, product);
  }

  @Test
    @Timeout(8000)
  void peek_privateMethod_shouldReturnTopOfStackOrProduct() throws Exception {
    // Use reflection to get private method peek()
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    // Access private field 'stack' via reflection
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // Clear the stack to ensure it is empty before test
    stack.clear();

    // Reset product to JsonNull.INSTANCE to avoid IndexOutOfBoundsException in peek()
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    productField.set(jsonTreeWriter, JsonNull.INSTANCE);

    // Initially stack is empty, peek() should return product (JsonNull.INSTANCE)
    JsonElement peeked = (JsonElement) peekMethod.invoke(jsonTreeWriter);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
    assertSame(product, peeked);

    // Add element to stack and test peek returns top element
    JsonArray array = new JsonArray();
    stack.add(array);

    JsonElement peekedAfterAdd = (JsonElement) peekMethod.invoke(jsonTreeWriter);
    assertSame(array, peekedAfterAdd);
  }
}