package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.lang.reflect.Field;

public class JsonPrimitive_532_2Test {

  @Test
    @Timeout(8000)
  public void testIsString_withStringValue() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive("testString");
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_Integer() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(123);
    assertFalse(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_Boolean() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive(true);
    assertFalse(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withNonStringValue_Character() throws Exception {
    JsonPrimitive jsonPrimitive = new JsonPrimitive('c');
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withPrivateValueSetToStringUsingReflection() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.mock(JsonPrimitive.class, Mockito.CALLS_REAL_METHODS);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, "reflectedString");
    assertTrue(jsonPrimitive.isString());
  }

  @Test
    @Timeout(8000)
  public void testIsString_withPrivateValueSetToNonStringUsingReflection() throws Exception {
    JsonPrimitive jsonPrimitive = Mockito.mock(JsonPrimitive.class, Mockito.CALLS_REAL_METHODS);
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(jsonPrimitive, 456);
    assertFalse(jsonPrimitive.isString());
  }
}