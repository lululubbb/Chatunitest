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

public class JsonTreeWriter_515_3Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValueBooleanAddsJsonPrimitiveAndReturnsThis() throws IOException, Exception {
    JsonWriter returned = jsonTreeWriter.value(true);
    assertSame(jsonTreeWriter, returned);

    // Access private field 'product' to verify JsonPrimitive was added
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);

    assertTrue(product instanceof JsonPrimitive);
    assertEquals(true, ((JsonPrimitive) product).getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testValueBooleanWithPendingNameAddsToObject() throws Exception {
    // Use reflection to set pendingName to simulate writing a property
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, "boolName");

    // Use reflection to add a JsonObject to stack so put adds property to it
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.add(new JsonObject());

    JsonWriter returned = jsonTreeWriter.value(false);
    assertSame(jsonTreeWriter, returned);

    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top instanceof JsonObject);
    JsonObject obj = (JsonObject) top;
    assertTrue(obj.has("boolName"));
    assertEquals(false, obj.get("boolName").getAsBoolean());

    // pendingName should be cleared after put
    assertNull(pendingNameField.get(jsonTreeWriter));
  }

  @Test
    @Timeout(8000)
  public void testValueBooleanWithNoStackAddsToProduct() throws Exception {
    // Clear stack to simulate empty stack scenario
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    jsonTreeWriter.value(true);

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);

    assertTrue(product instanceof JsonPrimitive);
    assertEquals(true, ((JsonPrimitive) product).getAsBoolean());
  }
}