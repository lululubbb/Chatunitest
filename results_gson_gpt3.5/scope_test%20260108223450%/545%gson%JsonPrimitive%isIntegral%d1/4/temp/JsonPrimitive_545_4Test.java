package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigInteger;

public class JsonPrimitive_545_4Test {

  private JsonPrimitive createJsonPrimitiveWithValue(Object value) throws Exception {
    // Use the public constructor of JsonPrimitive that accepts Object (Number, String, Boolean, Character)
    // Since JsonPrimitive has no no-arg constructor and Mockito cannot mock it easily.
    // We'll create an instance with a dummy value and then use reflection to set the 'value' field.
    JsonPrimitive primitive = new JsonPrimitive("dummy");
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(primitive, value);
    return primitive;
  }

  private boolean invokeIsIntegral(JsonPrimitive primitive) throws Exception {
    Method isIntegral = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegral.setAccessible(true);
    return (boolean) isIntegral.invoke(null, primitive);
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withBigInteger() throws Exception {
    JsonPrimitive primitive = createJsonPrimitiveWithValue(BigInteger.ONE);
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withLong() throws Exception {
    JsonPrimitive primitive = createJsonPrimitiveWithValue(Long.valueOf(123L));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withInteger() throws Exception {
    JsonPrimitive primitive = createJsonPrimitiveWithValue(Integer.valueOf(10));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withShort() throws Exception {
    JsonPrimitive primitive = createJsonPrimitiveWithValue(Short.valueOf((short) 5));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withByte() throws Exception {
    JsonPrimitive primitive = createJsonPrimitiveWithValue(Byte.valueOf((byte) 1));
    assertTrue(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withNonIntegralNumber() throws Exception {
    JsonPrimitive primitive = createJsonPrimitiveWithValue(Double.valueOf(1.5));
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withNonNumberValue() throws Exception {
    JsonPrimitive primitive = createJsonPrimitiveWithValue("stringValue");
    assertFalse(invokeIsIntegral(primitive));
  }

  @Test
    @Timeout(8000)
  void testIsIntegral_withNullValue() throws Exception {
    JsonPrimitive primitive = createJsonPrimitiveWithValue(null);
    assertFalse(invokeIsIntegral(primitive));
  }
}