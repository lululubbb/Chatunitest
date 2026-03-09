package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.internal.bind.JsonTreeWriter;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;

public class JsonTreeWriter_507_5Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void testBeginArray_shouldAddJsonArrayToStackAndReturnThis() throws IOException, NoSuchFieldException, IllegalAccessException {
    JsonWriter returned = jsonTreeWriter.beginArray();
    assertSame(jsonTreeWriter, returned);

    // Use reflection to get the private field 'stack'
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);

    assertFalse(stack.isEmpty());
    JsonElement lastElement = stack.get(stack.size() - 1);
    assertTrue(lastElement instanceof JsonArray);

    // Also verify that the product field was set to the JsonArray by put method
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    JsonElement product = (JsonElement) productField.get(jsonTreeWriter);
    assertSame(lastElement, product);
  }
}