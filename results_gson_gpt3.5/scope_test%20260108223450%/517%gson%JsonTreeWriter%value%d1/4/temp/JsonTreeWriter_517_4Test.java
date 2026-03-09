package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.List;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_517_4Test {
  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() throws Exception {
    writer = new JsonTreeWriter();

    // Initialize the stack and product properly to avoid IllegalStateException
    // The JsonTreeWriter expects the stack to be non-empty and product to be JsonNull.INSTANCE initially
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    java.util.List<JsonElement> stack = (java.util.List<JsonElement>) stackField.get(writer);
    stack.clear();
    stack.add(JsonNull.INSTANCE);

    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    productField.set(writer, JsonNull.INSTANCE);

    // Also clear pendingName to null
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(writer, null);
  }

  @Test
    @Timeout(8000)
  public void testValue_NormalFloat() throws Exception {
    // value within normal float range, lenient = true
    setLenient(writer, true);

    // Add a JsonNull to stack to avoid IllegalStateException
    addJsonNullToStack(writer);

    JsonWriter returned = writer.value(1.5f);
    assertSame(writer, returned);

    // verify that the stack contains the JsonPrimitive with value 1.5f
    JsonElement top = getStackTop(writer);
    assertTrue(top.isJsonPrimitive());
    JsonPrimitive primitive = top.getAsJsonPrimitive();
    assertEquals(1.5f, primitive.getAsFloat());
  }

  @Test
    @Timeout(8000)
  public void testValue_LenientFalse_NormalFloat() throws Exception {
    setLenient(writer, false);

    // Add a JsonNull to stack to avoid IllegalStateException
    addJsonNullToStack(writer);

    JsonWriter returned = writer.value(2.5f);
    assertSame(writer, returned);

    JsonElement top = getStackTop(writer);
    assertTrue(top.isJsonPrimitive());
    JsonPrimitive primitive = top.getAsJsonPrimitive();
    assertEquals(2.5f, primitive.getAsFloat());
  }

  @Test
    @Timeout(8000)
  public void testValue_LenientFalse_NaN_Throws() throws Exception {
    setLenient(writer, false);

    // Add a JsonNull to stack to avoid IllegalStateException
    addJsonNullToStack(writer);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Float.NaN);
    });
    assertTrue(thrown.getMessage().contains("NaN"));
  }

  @Test
    @Timeout(8000)
  public void testValue_LenientFalse_PositiveInfinity_Throws() throws Exception {
    setLenient(writer, false);

    // Add a JsonNull to stack to avoid IllegalStateException
    addJsonNullToStack(writer);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Float.POSITIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  @Test
    @Timeout(8000)
  public void testValue_LenientFalse_NegativeInfinity_Throws() throws Exception {
    setLenient(writer, false);

    // Add a JsonNull to stack to avoid IllegalStateException
    addJsonNullToStack(writer);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      writer.value(Float.NEGATIVE_INFINITY);
    });
    assertTrue(thrown.getMessage().contains("infinities"));
  }

  private void setLenient(JsonTreeWriter writer, boolean lenient) throws Exception {
    // JsonWriter has a protected boolean lenient field; set it via reflection
    Field lenientField = JsonWriter.class.getDeclaredField("lenient");
    lenientField.setAccessible(true);
    lenientField.setBoolean(writer, lenient);
  }

  private JsonElement getStackTop(JsonTreeWriter writer) throws Exception {
    // The put method pushes the JsonPrimitive onto the stack field
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    java.util.List<JsonElement> stack = (java.util.List<JsonElement>) stackField.get(writer);
    assertFalse(stack.isEmpty(), "Stack should not be empty after put");
    return stack.get(stack.size() - 1);
  }

  private void addJsonNullToStack(JsonTreeWriter writer) throws Exception {
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    java.util.List<JsonElement> stack = (java.util.List<JsonElement>) stackField.get(writer);
    // Only add if stack is empty or top is not JsonNull.INSTANCE
    if (stack.isEmpty() || !stack.get(stack.size() - 1).equals(JsonNull.INSTANCE)) {
      stack.add(JsonNull.INSTANCE);
    }
  }
}