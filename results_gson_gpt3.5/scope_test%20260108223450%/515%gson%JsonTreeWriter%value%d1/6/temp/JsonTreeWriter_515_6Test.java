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

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_515_6Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValueBooleanAddsJsonPrimitiveToStack() throws Exception {
    // Put an empty JsonArray on stack to receive the value
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();
    stack.add(new JsonArray());

    // Call value(true)
    JsonWriter returned = jsonTreeWriter.value(true);
    assertSame(jsonTreeWriter, returned);

    // The array inside stack should contain one JsonPrimitive with value true
    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top.isJsonArray());
    JsonArray array = top.getAsJsonArray();
    assertEquals(1, array.size());
    JsonElement element = array.get(0);
    assertTrue(element instanceof JsonPrimitive);
    assertEquals(true, ((JsonPrimitive) element).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testValueBooleanWithPendingNameAddsToObject() throws Exception {
    // Set pendingName to simulate name-value pair insertion
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, "key");

    // Put an empty JsonObject on stack to receive the name-value pair
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();
    stack.add(new JsonObject());

    // Call value(false)
    JsonWriter returned = jsonTreeWriter.value(false);
    assertSame(jsonTreeWriter, returned);

    // The stack top should be JsonObject with the key "key" and value JsonPrimitive false
    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top.isJsonObject());
    JsonObject obj = top.getAsJsonObject();
    assertTrue(obj.has("key"));
    assertEquals(false, obj.get("key").getAsBoolean());

    // pendingName should be reset to null
    assertNull(pendingNameField.get(jsonTreeWriter));
  }

  @Test
    @Timeout(8000)
  public void testValueBooleanWhenStackEmptySetsProduct() throws Exception {
    // Ensure stack is empty
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    // Call value(true)
    jsonTreeWriter.value(true);

    // product should now be JsonPrimitive true
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
    assertTrue(product instanceof JsonPrimitive);
    assertEquals(true, ((JsonPrimitive) product).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testValueBooleanWithClosedSentinelThrows() throws Exception {
    // Set product to SENTINEL_CLOSED to simulate closed writer
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);

    Field sentinelClosedField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    JsonElement sentinelClosed = (JsonElement) sentinelClosedField.get(null);

    productField.set(jsonTreeWriter, sentinelClosed);

    // Also add a JsonArray on the stack to avoid IndexOutOfBoundsException in put()
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();
    stack.add(new JsonArray());

    // Calling value should throw IllegalStateException because put will throw
    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      jsonTreeWriter.value(true);
    });
  }

  @Test
    @Timeout(8000)
  public void testPutMethodAddsCorrectly() throws Exception {
    // Put an empty JsonArray on stack to receive the value
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();
    stack.add(new JsonArray());

    // Access private method put(JsonElement)
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // Call put with JsonPrimitive true
    putMethod.invoke(jsonTreeWriter, new JsonPrimitive(true));

    // Check stack top is JsonArray and it has one element JsonPrimitive true
    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top.isJsonArray());
    JsonArray array = top.getAsJsonArray();
    assertEquals(1, array.size());
    assertTrue(array.get(0).isJsonPrimitive());
    assertEquals(true, array.get(0).getAsBoolean());
  }
}