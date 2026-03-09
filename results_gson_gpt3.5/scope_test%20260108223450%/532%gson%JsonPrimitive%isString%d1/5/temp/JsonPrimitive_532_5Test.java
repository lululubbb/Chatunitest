package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import com.google.gson.JsonPrimitive;
import java.lang.reflect.Field;

public class JsonPrimitive_532_5Test {

  @Test
    @Timeout(8000)
  public void testIsString_withStringValue_shouldReturnTrue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("test");
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_shouldReturnFalse() throws Exception {
    JsonPrimitive jsonPrimitiveNumber = new JsonPrimitive(123);
    assertFalse(jsonPrimitiveNumber.isString());

    JsonPrimitive jsonPrimitiveBoolean = new JsonPrimitive(true);
    assertFalse(jsonPrimitiveBoolean.isString());

    JsonPrimitive jsonPrimitiveChar = new JsonPrimitive('c');
    // The isString() returns true for Character because JsonPrimitive stores Character as String internally
    // So we expect true here instead of false
    assertTrue(jsonPrimitiveChar.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withReflection_setValueToString_shouldReturnTrue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(1);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, "reflectionString");
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withReflection_setValueToNonString_shouldReturnFalse() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("initial");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, 3.14);
    assertFalse(jsonPrimitive.isString());
  }
}