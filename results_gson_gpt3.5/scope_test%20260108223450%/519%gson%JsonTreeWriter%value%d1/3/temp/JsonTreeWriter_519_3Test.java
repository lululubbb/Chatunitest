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

public class JsonTreeWriter_519_3Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() throws Exception {
    writer = new JsonTreeWriter();

    // Clear internal state before each test to avoid interference
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.clear();

    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, null);

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    productField.set(writer, JsonNull.INSTANCE);
  }

  @Test
    @Timeout(8000)
  public void testValue_long_pushesJsonPrimitiveAndReturnsThis() throws IOException, Exception {
    JsonWriter returned = writer.value(123L);
    assertSame(writer, returned);

    // Use reflection to access private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    assertTrue(stack.isEmpty(), "Stack should remain empty after value(long) call");

    // Instead check the 'product' field for the JsonPrimitive
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);

    assertTrue(product instanceof JsonPrimitive);
    assertEquals(123L, ((JsonPrimitive) product).getAsLong());
  }

  @Test
    @Timeout(8000)
  public void testValue_long_whenPendingNameIsSet_putsInObject() throws Exception {
    // Use reflection to set private field 'pendingName'
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, "testName");

    // Use reflection to access private field 'stack' and push a JsonObject to simulate object context
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.add(new JsonObject());

    writer.value(456L);

    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top.isJsonObject());
    assertTrue(top.getAsJsonObject().has("testName"));
    assertEquals(456L, top.getAsJsonObject().get("testName").getAsLong());

    // pendingName should be cleared
    assertNull(pendingNameField.get(writer));
  }

  @Test
    @Timeout(8000)
  public void testValue_long_whenStackEmpty_setsProduct() throws Exception {
    // Use reflection to access private field 'stack' and clear it to ensure empty
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.clear();

    writer.value(789L);

    // Use reflection to access private field 'product'
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);

    assertTrue(product instanceof JsonPrimitive);
    assertEquals(789L, ((JsonPrimitive) product).getAsLong());
  }

  @Test
    @Timeout(8000)
  public void testPutPrivateMethod_addsElementToStack() throws Exception {
    // Use reflection to invoke private put method
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    JsonPrimitive primitive = new JsonPrimitive(100L);

    // Initially empty stack
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.clear();

    // Also clear pendingName to avoid interference
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, null);

    putMethod.invoke(writer, primitive);

    // After put, stack should NOT be empty
    assertFalse(stack.isEmpty());
    JsonElement element = stack.get(stack.size() - 1);
    assertEquals(primitive, element);
  }

  @Test
    @Timeout(8000)
  public void testValue_long_withClosedWriter_throwsException() throws Exception {
    // Set product to SENTINEL_CLOSED to simulate closed writer
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Field sentinelClosedField = JsonTreeWriter.class.getDeclaredField("SENTINEL_CLOSED");
    sentinelClosedField.setAccessible(true);
    JsonElement sentinelClosed = (JsonElement) sentinelClosedField.get(null);
    productField.set(writer, sentinelClosed);

    // Use reflection to access private field 'stack' and add a JsonObject to simulate context
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.clear();
    stack.add(new JsonObject()); // Add an object to avoid stack empty condition

    // Set pendingName to null to avoid interference
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, null);

    IOException thrown = assertThrows(IOException.class, () -> writer.value(1L));
    assertEquals("JsonWriter is closed.", thrown.getMessage());
  }
}