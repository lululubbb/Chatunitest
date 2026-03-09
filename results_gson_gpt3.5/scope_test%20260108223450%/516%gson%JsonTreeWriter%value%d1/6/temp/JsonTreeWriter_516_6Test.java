package com.google.gson.internal.bind;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class JsonTreeWriterValueTest {

  private JsonTreeWriter jsonTreeWriter;

  @BeforeEach
  void setUp() {
    jsonTreeWriter = new JsonTreeWriter();
  }

  @Test
    @Timeout(8000)
  void value_withNonNullBoolean_returnsThis() throws IOException {
    JsonWriter returned = jsonTreeWriter.value(Boolean.TRUE);
    assertSame(jsonTreeWriter, returned);

    // Use reflection to verify that peek() returns a JsonPrimitive with correct value
    try {
      Method peekMethod = JsonTreeWriter.class.getDeclaredMethod("peek");
      peekMethod.setAccessible(true);
      Object top = peekMethod.invoke(jsonTreeWriter);
      assertNotNull(top);
      assertTrue(top instanceof JsonPrimitive);
      JsonPrimitive primitive = (JsonPrimitive) top;
      assertEquals(Boolean.TRUE, primitive.getAsBoolean());
    } catch (ReflectiveOperationException e) {
      fail("Reflection failure: " + e.getMessage());
    }
  }

  @Test
    @Timeout(8000)
  void value_withNullBoolean_callsNullValueAndReturnsItsResult() throws IOException {
    JsonWriter returned = jsonTreeWriter.value((Boolean) null);
    // nullValue returns JsonWriter, for JsonTreeWriter it returns this, so returned should be jsonTreeWriter
    assertSame(jsonTreeWriter, returned);

    // Use reflection to check that product is JsonNull.INSTANCE after nullValue call
    try {
      Field productField = JsonTreeWriter.class.getDeclaredField("product");
      productField.setAccessible(true);
      Object product = productField.get(jsonTreeWriter);
      assertNotNull(product);
      assertSame(JsonNull.INSTANCE, product);
    } catch (ReflectiveOperationException e) {
      fail("Reflection failure: " + e.getMessage());
    }
  }
}