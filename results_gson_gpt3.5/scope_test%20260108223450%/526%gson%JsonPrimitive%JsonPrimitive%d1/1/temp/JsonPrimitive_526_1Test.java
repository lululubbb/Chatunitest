package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import static org.junit.jupiter.api.Assertions.*;

class JsonPrimitive_526_1Test {

  @Test
    @Timeout(8000)
  void testConstructorWithCharacter() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getConstructor(Character.class);
    Character input = 'a';
    JsonPrimitive jsonPrimitive = constructor.newInstance(input);

    // Access private field 'value' to verify it is the string representation of the character
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertEquals("a", value);
  }

  @Test
    @Timeout(8000)
  void testConstructorWithNullCharacterThrowsNPE() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getConstructor(Character.class);
    Throwable thrown = assertThrows(Exception.class, () -> constructor.newInstance((Character) null));
    // unwrap InvocationTargetException to check cause
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  void testIsIntegralStaticMethod() throws Exception {
    // Prepare JsonPrimitive instances with integral and non-integral values
    Constructor<JsonPrimitive> constructorString = JsonPrimitive.class.getConstructor(String.class);
    Constructor<JsonPrimitive> constructorNumber = JsonPrimitive.class.getConstructor(Number.class);

    JsonPrimitive integralPrimitive = constructorNumber.newInstance(42);
    JsonPrimitive nonIntegralPrimitive = constructorNumber.newInstance(3.14);
    JsonPrimitive stringPrimitive = constructorString.newInstance("string");

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    boolean integralResult = (boolean) isIntegralMethod.invoke(null, integralPrimitive);
    boolean nonIntegralResult = (boolean) isIntegralMethod.invoke(null, nonIntegralPrimitive);
    boolean stringResult = (boolean) isIntegralMethod.invoke(null, stringPrimitive);

    assertTrue(integralResult);
    assertFalse(nonIntegralResult);
    assertFalse(stringResult);
  }
}