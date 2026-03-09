package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

public class JsonTreeWriter_514_3Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testNullValue_returnsThis() throws Exception {
    JsonWriter returned = jsonTreeWriter.nullValue();
    assertSame(jsonTreeWriter, returned);
  }

  @Test
    @Timeout(8000)
  public void testNullValue_putsJsonNullInstance() throws Exception {
    // Clear stack before test
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    // Set product to null so put() can add JsonNull.INSTANCE to stack
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    productField.set(jsonTreeWriter, null);

    // Also clear pendingName to avoid IllegalStateException inside put()
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, null);

    jsonTreeWriter.nullValue();

    assertFalse(stack.isEmpty());
    JsonElement lastElement = stack.get(stack.size() - 1);
    assertSame(JsonNull.INSTANCE, lastElement);
  }

  @Test
    @Timeout(8000)
  public void testPutMethodAddsElementToStack() throws Exception {
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // Clear stack first
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    stack.clear();

    // Reset product to null so put() can add element to stack
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    productField.set(jsonTreeWriter, null);

    // Clear pendingName to avoid IllegalStateException inside put()
    Field pendingNameField = JsonTreeWriter.class.getDeclaredField("pendingName");
    pendingNameField.setAccessible(true);
    pendingNameField.set(jsonTreeWriter, null);

    putMethod.invoke(jsonTreeWriter, JsonNull.INSTANCE);

    assertFalse(stack.isEmpty());
    assertSame(JsonNull.INSTANCE, stack.get(stack.size() - 1));
  }
}