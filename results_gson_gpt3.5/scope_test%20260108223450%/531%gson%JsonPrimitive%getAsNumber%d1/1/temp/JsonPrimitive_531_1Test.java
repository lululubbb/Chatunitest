package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.lang.reflect.Field;

public class JsonPrimitive_531_1Test {

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withNumberValue() {
    Number numberValue = 42;
    JsonPrimitive jsonPrimitive = new JsonPrimitive(numberValue);
    Number result = jsonPrimitive.getAsNumber();
    assertSame(numberValue, result);
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withStringValue() {
    String stringValue = "123.45";
    JsonPrimitive jsonPrimitive = new JsonPrimitive(stringValue);
    Number result = jsonPrimitive.getAsNumber();
    assertNotNull(result);
    assertEquals(stringValue, result.toString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_withUnsupportedValue() throws Exception {
    JsonPrimitive jsonPrimitive = createJsonPrimitiveWithValue(new Object());

    Executable executable = jsonPrimitive::getAsNumber;
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, executable);
    assertEquals("Primitive is neither a number nor a string", exception.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testGetAsNumber_unsupportedOperationException() throws Exception {
    JsonPrimitive jsonPrimitive = createJsonPrimitiveWithValue(new Object());
    Executable executable = jsonPrimitive::getAsNumber;
    UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, executable);
    assertEquals("Primitive is neither a number nor a string", exception.getMessage());
  }

  private JsonPrimitive createJsonPrimitiveWithValue(Object value) throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(0); // create with any valid constructor
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, value);
    return jsonPrimitive;
  }
}