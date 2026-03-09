package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.JsonNull;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_515_4Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void testValueBooleanAddsPrimitiveAndReturnsThis() throws IOException, Exception {
    JsonWriter returned = jsonTreeWriter.value(true);
    assertSame(jsonTreeWriter, returned);

    // Access private field 'product' via reflection
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);

    // Since stack is empty, product should be set to the JsonPrimitive true
    assertEquals(new JsonPrimitive(true), product);

    // Access private field 'stack' via reflection
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // The stack should be empty because the value was set as product, not pushed onto stack
    assertEquals(0, stack.size());
  }

  @Test
    @Timeout(8000)
  void testValueBooleanWhenPendingNameAddsToObject() throws Exception {
    // Set pendingName to simulate writing to JsonObject
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, "key");

    // Push a JsonObject onto the stack
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.add(new JsonObject());

    jsonTreeWriter.value(false);

    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top.isJsonObject());
    assertEquals(new JsonPrimitive(false), top.getAsJsonObject().get("key"));

    // pendingName should be cleared after put
    assertNull(pendingNameField.get(jsonTreeWriter));
  }

  @Test
    @Timeout(8000)
  void testValueBooleanWhenStackEmptySetsProduct() throws Exception {
    // Ensure stack is empty
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    jsonTreeWriter.value(true);

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);

    assertEquals(new JsonPrimitive(true), product);
  }

  @Test
    @Timeout(8000)
  void testPutPrivateMethodAddsCorrectlyToStack() throws Exception {
    // Access private put method using reflection
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // Initially empty stack
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    // Call put with a JsonPrimitive
    JsonPrimitive primitive = new JsonPrimitive(true);
    putMethod.invoke(jsonTreeWriter, primitive);

    // product should be set to primitive since stack is empty
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);

    assertEquals(primitive, product);

    // Now test put when stack has a JsonArray
    JsonArray array = new JsonArray();
    stack.clear();
    stack.add(array);

    JsonPrimitive prim2 = new JsonPrimitive(false);
    putMethod.invoke(jsonTreeWriter, prim2);

    assertEquals(1, stack.size());
    assertTrue(array.contains(prim2));
  }
}