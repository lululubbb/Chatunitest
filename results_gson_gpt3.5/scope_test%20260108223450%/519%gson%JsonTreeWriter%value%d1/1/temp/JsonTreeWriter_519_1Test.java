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
import java.util.List;

public class JsonTreeWriter_519_1Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValue_long_simple() throws Exception {
    JsonWriter returned = writer.value(123L);
    assertSame(writer, returned);

    // Access private field 'product' to verify it holds the JsonPrimitive with the long value
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    assertTrue(product instanceof JsonPrimitive);
    assertEquals(123L, ((JsonPrimitive) product).getAsLong());
  }

  @Test
    @Timeout(8000)
  public void testValue_long_withArrayInStack() throws Exception {
    // Use reflection to invoke beginArray() to push a JsonArray on stack
    JsonWriter bw = writer.beginArray();
    assertSame(writer, bw);

    JsonWriter returned = writer.value(456L);
    assertSame(writer, returned);

    // Access private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    // The top of stack should be a JsonArray containing the new JsonPrimitive
    assertFalse(stack.isEmpty());
    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top instanceof JsonArray);
    JsonArray array = (JsonArray) top;
    assertEquals(1, array.size());
    assertEquals(456L, array.get(0).getAsLong());
  }

  @Test
    @Timeout(8000)
  public void testValue_long_withObjectAndPendingName() throws Exception {
    // Begin object and set a pending name
    writer.beginObject();
    writer.name("fieldName");

    // Call value(long)
    JsonWriter returned = writer.value(789L);
    assertSame(writer, returned);

    // Access private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    // The top of stack should be a JsonObject with the field set
    assertFalse(stack.isEmpty());
    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top instanceof JsonObject);
    JsonObject obj = (JsonObject) top;
    assertTrue(obj.has("fieldName"));
    assertEquals(789L, obj.get("fieldName").getAsLong());

    // pendingName should be reset to null
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    String pendingName = (String) pendingNameField.get(writer);
    assertNull(pendingName);
  }

  @Test
    @Timeout(8000)
  public void testValue_long_afterClose_throws() throws Exception {
    // Call close() to set the writer to closed state
    writer.close();

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> writer.value(1L));
    // The message is null, so just assert that exception was thrown (message may be null)
    assertNull(thrown.getMessage());
  }
}