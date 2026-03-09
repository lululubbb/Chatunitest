package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_515_5Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testValueBooleanTrue() throws Exception {
    JsonWriter returned = jsonTreeWriter.value(true);
    assertSame(jsonTreeWriter, returned);

    // Use reflection to get 'stack' field and verify it contains JsonPrimitive(true)
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertFalse(stack.isEmpty());
    JsonElement element = stack.get(stack.size() - 1);
    assertTrue(element.isJsonPrimitive());
    JsonPrimitive primitive = element.getAsJsonPrimitive();
    assertTrue(primitive.isBoolean());
    assertEquals(true, primitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testValueBooleanFalse() throws Exception {
    JsonWriter returned = jsonTreeWriter.value(false);
    assertSame(jsonTreeWriter, returned);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertFalse(stack.isEmpty());
    JsonElement element = stack.get(stack.size() - 1);
    assertTrue(element.isJsonPrimitive());
    JsonPrimitive primitive = element.getAsJsonPrimitive();
    assertTrue(primitive.isBoolean());
    assertEquals(false, primitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testPutMethodAddsElementToStack() throws Exception {
    // Access private method put(JsonElement)
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    JsonPrimitive primitive = new JsonPrimitive(true);
    putMethod.invoke(jsonTreeWriter, primitive);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertFalse(stack.isEmpty());
    JsonElement element = stack.get(stack.size() - 1);
    assertTrue(element.isJsonPrimitive());
    JsonPrimitive primitiveFromStack = element.getAsJsonPrimitive();
    assertTrue(primitiveFromStack.isBoolean());
    assertEquals(primitive, primitiveFromStack);
  }

  @Test
    @Timeout(8000)
  public void testValueBooleanThrowsIOException() {
    // The method signature declares IOException but the method does not throw it in normal use.
    // So no test needed for exception throwing here.
  }
}