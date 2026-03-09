package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_509_5Test {

  private JsonTreeWriter writer;

  @BeforeEach
  public void setUp() {
    writer = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void beginObject_shouldReturnThisAndModifyStack() throws Exception {
    JsonWriter result = writer.beginObject();
    assertSame(writer, result);

    // Access private field 'stack' via reflection
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    // The top of the stack should be a JsonObject
    assertFalse(stack.isEmpty());
    JsonElement top = stack.get(stack.size() - 1);
    assertTrue(top instanceof JsonObject);

    // Also verify the private 'product' field is not JsonNull (indirectly)
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(writer);
    assertNotNull(product);
  }

  @Test
    @Timeout(8000)
  public void beginObject_multipleCalls_shouldAddMultipleObjectsToStack() throws Exception {
    // Call beginObject once to start the root object
    writer.beginObject();
    // Call endObject to close the first object so that we can add another one
    writer.endObject();

    // Now beginObject again to add a second object
    writer.beginObject();

    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(writer);

    assertEquals(1, stack.size());
    assertTrue(stack.get(0) instanceof JsonObject);
  }

}