package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class JsonPrimitive_523_5Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withBoolean() throws Exception {
    Boolean boolValue = Boolean.TRUE;
    JsonPrimitive jsonPrimitive = new JsonPrimitive(boolValue);

    // Use reflection to access the private field 'value'
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertSame(boolValue, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withNullBoolean_throwsNPE() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((Boolean) null));
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_andGetAsBoolean() {
    JsonPrimitive jsonPrimitiveTrue = new JsonPrimitive(Boolean.TRUE);
    assertTrue(jsonPrimitiveTrue.isBoolean());
    assertTrue(jsonPrimitiveTrue.getAsBoolean());

    JsonPrimitive jsonPrimitiveFalse = new JsonPrimitive(Boolean.FALSE);
    assertTrue(jsonPrimitiveFalse.isBoolean());
    assertFalse(jsonPrimitiveFalse.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode_withBoolean() {
    JsonPrimitive jp1 = new JsonPrimitive(Boolean.TRUE);
    JsonPrimitive jp2 = new JsonPrimitive(Boolean.TRUE);
    JsonPrimitive jp3 = new JsonPrimitive(Boolean.FALSE);

    assertEquals(jp1, jp2);
    assertEquals(jp1.hashCode(), jp2.hashCode());
    assertNotEquals(jp1, jp3);
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy_withBoolean() {
    JsonPrimitive original = new JsonPrimitive(Boolean.TRUE);
    JsonPrimitive copy = original.deepCopy();

    // For Boolean, deepCopy returns the same instance (since Boolean is immutable),
    // so we check equality but not identity
    assertEquals(original, copy);
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_staticMethod() throws Exception {
    // Create JsonPrimitive with Boolean TRUE (not integral)
    JsonPrimitive booleanPrimitive = new JsonPrimitive(Boolean.TRUE);

    // Use reflection to access private static method isIntegral
    var method = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    method.setAccessible(true);

    boolean resultBoolean = (boolean) method.invoke(null, booleanPrimitive);
    assertFalse(resultBoolean);

    // Create JsonPrimitive with a Number integral value via reflection constructor
    Constructor<JsonPrimitive> numberCtor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    numberCtor.setAccessible(true);
    JsonPrimitive numberPrimitive = numberCtor.newInstance(123);

    boolean resultNumber = (boolean) method.invoke(null, numberPrimitive);
    assertTrue(resultNumber);

    // Create JsonPrimitive with a Number non-integral value
    JsonPrimitive nonIntegral = numberCtor.newInstance(123.45d);
    boolean resultNonIntegral = (boolean) method.invoke(null, nonIntegral);
    assertFalse(resultNonIntegral);
  }
}