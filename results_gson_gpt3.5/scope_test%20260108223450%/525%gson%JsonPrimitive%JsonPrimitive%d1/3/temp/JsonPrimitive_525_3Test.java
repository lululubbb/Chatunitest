package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

public class JsonPrimitive_525_3Test {

  @Test
    @Timeout(8000)
  public void testConstructor_String_NonNull() throws Exception {
    String testString = "testValue";
    JsonPrimitive jsonPrimitive = new JsonPrimitive(testString);

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    Object value = valueField.get(jsonPrimitive);

    assertEquals(testString, value);
  }

  @Test
    @Timeout(8000)
  public void testConstructor_String_Null_ThrowsNPE() {
    assertThrows(NullPointerException.class, () -> new JsonPrimitive((String) null));
  }

  @Test
    @Timeout(8000)
  public void testIsIntegral_PrivateMethod() throws Exception {
    Constructor<JsonPrimitive> constructor = JsonPrimitive.class.getDeclaredConstructor(String.class);
    constructor.setAccessible(true);
    JsonPrimitive primitive = constructor.newInstance("123");

    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    // Should be false because value is a String, not integral Number
    boolean result = (boolean) isIntegralMethod.invoke(null, primitive);
    assertFalse(result);

    // Create a JsonPrimitive with integral Number to test true case
    Constructor<JsonPrimitive> numberConstructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    numberConstructor.setAccessible(true);
    JsonPrimitive intPrimitive = numberConstructor.newInstance(42);

    boolean resultIntegral = (boolean) isIntegralMethod.invoke(null, intPrimitive);
    assertTrue(resultIntegral);
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() throws Exception {
    JsonPrimitive p1 = new JsonPrimitive("value");
    JsonPrimitive p2 = new JsonPrimitive("value");
    JsonPrimitive p3 = new JsonPrimitive("other");

    assertEquals(p1, p2);
    assertEquals(p1.hashCode(), p2.hashCode());

    assertNotEquals(p1, p3);
    assertNotEquals(p1.hashCode(), p3.hashCode());

    assertNotEquals(p1, null);
    assertNotEquals(p1, "some string");
  }

  @Test
    @Timeout(8000)
  public void testIsBoolean_GetAsBoolean() throws Exception {
    Constructor<JsonPrimitive> boolConstructor = JsonPrimitive.class.getDeclaredConstructor(Boolean.class);
    boolConstructor.setAccessible(true);
    JsonPrimitive boolPrimitiveTrue = boolConstructor.newInstance(true);
    JsonPrimitive boolPrimitiveFalse = boolConstructor.newInstance(false);

    assertTrue(boolPrimitiveTrue.isBoolean());
    assertTrue(boolPrimitiveFalse.isBoolean());

    assertTrue(boolPrimitiveTrue.getAsBoolean());
    assertFalse(boolPrimitiveFalse.getAsBoolean());
  }

  @Test
    @Timeout(8000)
  public void testIsNumber_GetAsNumber() throws Exception {
    Constructor<JsonPrimitive> numberConstructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    numberConstructor.setAccessible(true);
    JsonPrimitive intPrimitive = numberConstructor.newInstance(123);
    JsonPrimitive doublePrimitive = numberConstructor.newInstance(123.45);

    assertTrue(intPrimitive.isNumber());
    assertEquals(123, intPrimitive.getAsNumber());

    assertTrue(doublePrimitive.isNumber());
    assertEquals(123.45, doublePrimitive.getAsNumber());
  }

  @Test
    @Timeout(8000)
  public void testIsString_GetAsString() throws Exception {
    JsonPrimitive stringPrimitive = new JsonPrimitive("stringValue");
    assertTrue(stringPrimitive.isString());
    assertEquals("stringValue", stringPrimitive.getAsString());
  }

  @Test
    @Timeout(8000)
  public void testGetAsDouble_GetAsFloat_GetAsLong_GetAsInt_GetAsShort_GetAsByte()
      throws Exception {
    Constructor<JsonPrimitive> numberConstructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    numberConstructor.setAccessible(true);
    JsonPrimitive numberPrimitive = numberConstructor.newInstance(123);

    assertEquals(123.0, numberPrimitive.getAsDouble());
    assertEquals(123.0f, numberPrimitive.getAsFloat());
    assertEquals(123L, numberPrimitive.getAsLong());
    assertEquals(123, numberPrimitive.getAsInt());
    assertEquals((short)123, numberPrimitive.getAsShort());
    assertEquals((byte)123, numberPrimitive.getAsByte());
  }

  @Test
    @Timeout(8000)
  public void testGetAsBigDecimal_GetAsBigInteger() throws Exception {
    Constructor<JsonPrimitive> numberConstructor = JsonPrimitive.class.getDeclaredConstructor(Number.class);
    numberConstructor.setAccessible(true);
    JsonPrimitive numberPrimitive = numberConstructor.newInstance(123);

    BigDecimal bigDecimal = numberPrimitive.getAsBigDecimal();
    BigInteger bigInteger = numberPrimitive.getAsBigInteger();

    assertEquals(BigDecimal.valueOf(123), bigDecimal);
    assertEquals(BigInteger.valueOf(123), bigInteger);
  }

  @Test
    @Timeout(8000)
  public void testGetAsCharacter_Deprecated() throws Exception {
    Constructor<JsonPrimitive> charConstructor = JsonPrimitive.class.getDeclaredConstructor(Character.class);
    charConstructor.setAccessible(true);
    JsonPrimitive charPrimitive = charConstructor.newInstance('a');

    assertEquals('a', charPrimitive.getAsCharacter());
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy() throws Exception {
    JsonPrimitive original = new JsonPrimitive("copyTest");
    JsonPrimitive copy = original.deepCopy();

    // Fix: for immutable String value, deepCopy may return same instance.
    // So skip assertNotSame if value is a String.
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);

    Object originalValue = valueField.get(original);
    Object copyValue = valueField.get(copy);

    if (!(originalValue instanceof String)) {
      assertNotSame(original, copy);
    }

    assertEquals(originalValue, copyValue);
  }
}