package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

class JsonTreeWriter_value_Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void value_boolean_shouldPutJsonPrimitiveWithBoolean() throws Exception {
    JsonWriter returned = jsonTreeWriter.value(true);

    assertSame(jsonTreeWriter, returned);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertEquals(1, stack.size());
    JsonElement element = stack.get(0);
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertTrue(primitive.isBoolean());
    assertTrue(primitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  void value_boolean_false_shouldPutJsonPrimitiveWithFalse() throws Exception {
    jsonTreeWriter.value(false);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertEquals(1, stack.size());
    JsonElement element = stack.get(0);
    assertTrue(element instanceof JsonPrimitive);
    JsonPrimitive primitive = (JsonPrimitive) element;
    assertTrue(primitive.isBoolean());
    assertFalse(primitive.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  void put_privateMethod_shouldAddElementToStack() throws Exception {
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    JsonPrimitive primitive = new JsonPrimitive(true);
    putMethod.invoke(jsonTreeWriter, primitive);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertEquals(1, stack.size());
    assertSame(primitive, stack.get(0));
  }

  @Test
    @Timeout(8000)
  void value_boolean_afterPut_shouldStackContainCorrectElement() throws IOException, Exception {
    jsonTreeWriter.value(true);

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertEquals(1, stack.size());

    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);
    JsonElement peeked = (JsonElement) peekMethod.invoke(jsonTreeWriter);

    assertSame(stack.get(stack.size() - 1), peeked);
  }
}