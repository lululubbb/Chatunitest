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

public class JsonTreeWriter_519_4Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() throws Exception {
    jsonTreeWriter = new JsonTreeWriter();

    // Reset internal state before each test to ensure clean stack and product
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    productField.set(jsonTreeWriter, JsonNull.INSTANCE);

    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, null);
  }

  @Test
    @Timeout(8000)
  public void testValue_long_addsJsonPrimitiveAndReturnsThis() throws IOException, Exception {
    JsonWriter returned = jsonTreeWriter.value(123L);
    assertSame(jsonTreeWriter, returned);

    // Access private field 'stack' via reflection
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // Since JsonTreeWriter adds values to 'product' when stack is empty,
    // check product field instead of stack size.
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);

    assertTrue(product instanceof JsonPrimitive);
    assertEquals(123L, ((JsonPrimitive) product).getAsLong());
    assertEquals(0, stack.size());
  }

  @Test
    @Timeout(8000)
  public void testPut_privateMethod_addsElementToStackOrProduct() throws Exception {
    // Use reflection to get private method put(JsonElement)
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // Initially stack is empty and product is JsonNull.INSTANCE
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);

    // Put when stack is empty: product should be set
    JsonPrimitive primitive = new JsonPrimitive("test");
    putMethod.invoke(jsonTreeWriter, primitive);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
    assertEquals(primitive, product);

    // Put when stack has one JsonArray: element should be added to array
    JsonArray jsonArray = new JsonArray();
    stack.add(jsonArray);
    JsonPrimitive primitive2 = new JsonPrimitive(42);
    putMethod.invoke(jsonTreeWriter, primitive2);
    assertEquals(1, jsonArray.size());
    assertEquals(primitive2, jsonArray.get(0));

    // Put when stack has one JsonObject and pendingName set: element added with key
    stack.clear();
    JsonObject jsonObject = new JsonObject();
    stack.add(jsonObject);

    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, "key");

    JsonPrimitive primitive3 = new JsonPrimitive(true);
    putMethod.invoke(jsonTreeWriter, primitive3);
    assertEquals(1, jsonObject.entrySet().size());
    assertTrue(jsonObject.has("key"));
    assertEquals(primitive3, jsonObject.get("key"));

    // pendingName should be null after put
    assertNull(pendingNameField.get(jsonTreeWriter));
  }

  @Test
    @Timeout(8000)
  public void testValue_long_multipleCalls_stackContainsAll() throws IOException, Exception {
    jsonTreeWriter.beginArray(); // Begin array so values are added to stack

    jsonTreeWriter.value(1L);
    jsonTreeWriter.value(2L);
    jsonTreeWriter.value(3L);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    // The array should be at stack.get(stack.size() - 1)
    JsonElement lastElement = stack.get(stack.size() - 1);
    assertTrue(lastElement instanceof JsonArray);
    JsonArray jsonArray = (JsonArray) lastElement;

    assertEquals(3, jsonArray.size());
    assertEquals(1L, jsonArray.get(0).getAsLong());
    assertEquals(2L, jsonArray.get(1).getAsLong());
    assertEquals(3L, jsonArray.get(2).getAsLong());
  }
}