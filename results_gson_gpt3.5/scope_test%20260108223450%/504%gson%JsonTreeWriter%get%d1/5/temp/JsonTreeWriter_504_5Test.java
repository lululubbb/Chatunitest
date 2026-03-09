package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.internal.bind.JsonTreeWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_504_5Test {
  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void get_whenStackIsEmpty_returnsProduct() throws Exception {
    // Initially product is JsonNull.INSTANCE
    JsonElement product = writer.get();
    assertNotNull(product);
    assertTrue(product instanceof JsonNull);
  }

  @Test
    @Timeout(8000)
  public void get_whenStackIsNotEmpty_throwsIllegalStateException() throws Exception {
    // Use reflection to add a dummy element to the private stack field
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.add(new JsonPrimitive("dummy"));

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> writer.get());
    assertTrue(exception.getMessage().contains("Expected one JSON element but was"));
  }

  @Test
    @Timeout(8000)
  public void get_afterPutProductIsReturned() throws Exception {
    // Use reflection to invoke private put(JsonElement) to set product and clear stack
    JsonPrimitive primitive = new JsonPrimitive("test");
    // Put method is private, so use reflection
    java.lang.reflect.Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);
    putMethod.invoke(writer, primitive);

    // Clear stack to avoid exception
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);
    stack.clear();

    JsonElement result = writer.get();
    assertEquals(primitive, result);
  }
}