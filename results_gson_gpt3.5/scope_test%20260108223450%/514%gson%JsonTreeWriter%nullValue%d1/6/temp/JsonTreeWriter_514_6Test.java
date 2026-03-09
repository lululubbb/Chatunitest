package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Objects;

import com.google.gson.JsonNull;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class JsonTreeWriter_514_6Test {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void nullValue_shouldPutJsonNullInstanceAndReturnThis() throws IOException, Exception {
    JsonWriter returned = jsonTreeWriter.nullValue();
    assertSame(jsonTreeWriter, returned);

    // Access private field 'product' to verify it is JsonNull.INSTANCE
    Field productField = JsonTreeWriter.class.getDeclaredField("product");
    productField.setAccessible(true);
    Object productValue = productField.get(jsonTreeWriter);
    assertSame(JsonNull.INSTANCE, productValue);

    // Access private field 'stack' to verify it is empty (no stack elements)
    Field stackField = JsonTreeWriter.class.getDeclaredField("stack");
    stackField.setAccessible(true);
    @SuppressWarnings("unchecked")
    List<JsonElement> stackValue = (List<JsonElement>) stackField.get(jsonTreeWriter);
    assertTrue(stackValue.isEmpty());

    // Access private method peek() via reflection and verify it returns product (JsonNull.INSTANCE)
    Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
    peekMethod.setAccessible(true);

    // Since peek() accesses stack list which is empty, we need to set the stack to contain a JsonArray to avoid IllegalStateException in put()
    // Put a new JsonArray on stack before invoking put()
    stackValue.add(new JsonArray());

    Object peekValue = peekMethod.invoke(jsonTreeWriter);
    // peek() returns the top of stack, so it should be the JsonArray we just added
    assertTrue(peekValue instanceof JsonArray);

    // Access private method put(JsonElement) via reflection and call with JsonNull.INSTANCE to ensure no exception
    Method putMethod = JsonTreeWriter.class.getDeclaredMethod("put", JsonElement.class);
    putMethod.setAccessible(true);
    putMethod.invoke(jsonTreeWriter, JsonNull.INSTANCE);
  }
}