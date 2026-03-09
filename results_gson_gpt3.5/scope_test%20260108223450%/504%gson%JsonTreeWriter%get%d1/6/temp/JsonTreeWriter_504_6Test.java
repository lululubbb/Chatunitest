package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.lang.reflect.Field;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_504_6Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testGet_WhenStackIsEmpty_ReturnsProduct() throws Exception {
    // Initially product is JsonNull.INSTANCE
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    assertTrue(product instanceof JsonNull);

    JsonElement result = writer.get();
    assertSame(product, result);
  }

  @Test
    @Timeout(8000)
  public void testGet_WhenStackIsNotEmpty_ThrowsIllegalStateException() throws Exception {
    // Use reflection to add an element to the private stack list
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.add(new JsonPrimitive("element"));

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> writer.get());
    assertTrue(thrown.getMessage().startsWith("Expected one JSON element but was"));
    assertTrue(thrown.getMessage().contains("element"));
  }
}