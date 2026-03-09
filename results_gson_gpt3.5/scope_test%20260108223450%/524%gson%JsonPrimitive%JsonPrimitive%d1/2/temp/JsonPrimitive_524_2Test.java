package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.lang.reflect.Field;
import java.util.Objects;

class JsonPrimitive_524_2Test {

  @Test
    @Timeout(8000)
  void testConstructorWithNumber_nonNullValue() throws Exception {
    Number number = 123;
    JsonPrimitive primitive = new JsonPrimitive(number);

    // Access private final field 'value' using reflection
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(primitive);

    assertSame(number, value);
  }

  @Test
    @Timeout(8000)
  void testConstructorWithNumber_nullValue_throwsNPE() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((Number) null));
  }

  @Test
    @Timeout(8000)
  void testEqualsAndHashCode() {
    Number number1 = 123;
    Number number2 = 123;
    JsonPrimitive primitive1 = new JsonPrimitive(number1);
    JsonPrimitive primitive2 = new JsonPrimitive(number2);

    assertEquals(primitive1, primitive2);
    assertEquals(primitive1.hashCode(), primitive2.hashCode());
  }

  @Test
    @Timeout(8000)
  void testIsIntegralPrivateMethod() throws Exception {
    Number integralNumber = 10;
    Number decimalNumber = 10.5;

    JsonPrimitive integralPrimitive = new JsonPrimitive(integralNumber);
    JsonPrimitive decimalPrimitive = new JsonPrimitive(decimalNumber);

    // Access private static method isIntegral using reflection
    var method = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    method.setAccessible(true);

    boolean integralResult = (boolean) method.invoke(null, integralPrimitive);
    boolean decimalResult = (boolean) method.invoke(null, decimalPrimitive);

    assertTrue(integralResult);
    assertFalse(decimalResult);
  }
}