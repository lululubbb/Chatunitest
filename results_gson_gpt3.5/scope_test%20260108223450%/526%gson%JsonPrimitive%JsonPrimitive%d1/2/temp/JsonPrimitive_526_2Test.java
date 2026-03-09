package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class JsonPrimitive_526_2Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithCharacter() throws Exception {
    // Use constructor with Character
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Character.class);
    JsonPrimitive jsonPrimitive = constructor.newInstance('a');

    // Access private field 'value' to verify it stores the character as String
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);
    assertEquals("a", value);

    // Verify isString returns true
    Method isStringMethod = JsonPrimitive.class.getDeclaredMethod("isString");
    isStringMethod.setAccessible(true);
    boolean isString = (boolean) isStringMethod.invoke(jsonPrimitive);
    assertTrue(isString);

    // Verify getAsString returns the character as String
    String asString = jsonPrimitive.getAsString();
    assertEquals("a", asString);

    // Verify equals and hashCode consistency
    JsonPrimitive sameChar = constructor.newInstance('a');
    JsonPrimitive differentChar = constructor.newInstance('b');
    assertEquals(jsonPrimitive, sameChar);
    assertNotEquals(jsonPrimitive, differentChar);
    assertEquals(jsonPrimitive.hashCode(), sameChar.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testIsIntegralPrivateMethod() throws Exception {
    // Create instance with a Number to test isIntegral
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    JsonPrimitive intPrimitive = constructor.newInstance(10);
    JsonPrimitive longPrimitive = constructor.newInstance(10L);
    JsonPrimitive doublePrimitive = constructor.newInstance(10.5);
    JsonPrimitive bigIntegerPrimitive = constructor.newInstance(new java.math.BigInteger("12345"));
    JsonPrimitive bigDecimalPrimitive = constructor.newInstance(new java.math.BigDecimal("123.45"));

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    assertTrue((boolean) isIntegralMethod.invoke(null, intPrimitive));
    assertTrue((boolean) isIntegralMethod.invoke(null, longPrimitive));
    assertFalse((boolean) isIntegralMethod.invoke(null, doublePrimitive));
    assertTrue((boolean) isIntegralMethod.invoke(null, bigIntegerPrimitive));
    assertFalse((boolean) isIntegralMethod.invoke(null, bigDecimalPrimitive));
  }
}