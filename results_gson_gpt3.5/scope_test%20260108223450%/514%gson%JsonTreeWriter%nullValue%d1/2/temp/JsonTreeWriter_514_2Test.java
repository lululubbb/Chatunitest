package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonTreeWriter_514_2Test {
  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  public void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  public void nullValue_shouldPutJsonNullInstanceAndReturnThis() throws IOException, Exception {
    JsonWriter returned = jsonTreeWriter.nullValue();
    assertSame(jsonTreeWriter, returned);

    // Use reflection to access private field 'product' which holds the current JsonElement
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object product = productField.get(jsonTreeWriter);
    assertTrue(product instanceof JsonNull);
    assertSame(JsonNull.INSTANCE, product);
  }

  @Test
    @Timeout(8000)
  public void put_privateMethod_shouldPutValueCorrectly() throws Exception {
    // Use reflection to invoke private method put(JsonElement)
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);

    // Initially product is JsonNull.INSTANCE
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);

    // Put a JsonPrimitive value
    JsonPrimitive primitive = new JsonPrimitive("test");
    putMethod.invoke(jsonTreeWriter, primitive);

    Object product = productField.get(jsonTreeWriter);
    assertEquals(primitive, product);
  }

  @Test
    @Timeout(8000)
  public void peek_privateMethod_shouldReturnCorrectElement() throws Exception {
    // Use reflection to invoke private method peek()
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    // Initially stack is empty, so peek should throw IndexOutOfBoundsException or similar
    Exception exception = assertThrows(Exception.class, () -> {
      peekMethod.invoke(jsonTreeWriter);
    });

    // Add element to stack via reflection
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stack = (List<JsonElement>) stackField.get(jsonTreeWriter);
    JsonPrimitive primitive = new JsonPrimitive("peekTest");
    stack.add(primitive);

    Object peeked = peekMethod.invoke(jsonTreeWriter);
    assertEquals(primitive, peeked);
  }
}