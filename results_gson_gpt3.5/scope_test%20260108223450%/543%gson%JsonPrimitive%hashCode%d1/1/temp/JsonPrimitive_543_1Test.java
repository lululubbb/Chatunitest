package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JsonPrimitive_543_1Test {

  private JsonPrimitive jsonPrimitive;

  @BeforeEach
  public void setUp() {
    // no default constructor, so will create instances with reflection or via constructors
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueNull() throws Exception {
    jsonPrimitive = createJsonPrimitiveWithValue(null);
    assertEquals(31, jsonPrimitive.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_integralNumber() throws Exception {
    // Create JsonPrimitive with integral number and mock isIntegral to true
    jsonPrimitive = createJsonPrimitiveWithValue(123L);

    // Use reflection to set value field to Long 123L
    setValueField(jsonPrimitive, 123L);

    // Spy on JsonPrimitive to mock isIntegral static method
    // Since isIntegral is private static, use reflection to override it temporarily is complex,
    // instead, we will rely on actual isIntegral logic by creating a real JsonPrimitive with integral value

    int expectedHash = (int) (123L ^ (123L >>> 32));
    assertEquals(expectedHash, jsonPrimitive.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_numberNonIntegral() throws Exception {
    // Use Double value to force second condition
    Double doubleValue = 123.456d;
    jsonPrimitive = createJsonPrimitiveWithValue(doubleValue);

    long bits = Double.doubleToLongBits(doubleValue);
    int expectedHash = (int) (bits ^ (bits >>> 32));
    assertEquals(expectedHash, jsonPrimitive.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueIsNumberButNotIntegral() throws Exception {
    // LazilyParsedNumber is a Number but not integral
    Number lazyNumber = new LazilyParsedNumber("123.456");
    jsonPrimitive = createJsonPrimitiveWithValue(lazyNumber);

    long bits = Double.doubleToLongBits(lazyNumber.doubleValue());
    int expectedHash = (int) (bits ^ (bits >>> 32));
    assertEquals(expectedHash, jsonPrimitive.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_valueIsString() throws Exception {
    String stringValue = "testString";
    jsonPrimitive = createJsonPrimitiveWithValue(stringValue);
    assertEquals(stringValue.hashCode(), jsonPrimitive.hashCode());
  }

  @Test
    @Timeout(8000)
  public void testIsIntegralStaticMethod() throws Exception {
    // Use reflection to test private static boolean isIntegral(JsonPrimitive)
    Method isIntegralMethod = JsonPrimitive.class.getDeclaredMethod("isIntegral", JsonPrimitive.class);
    isIntegralMethod.setAccessible(true);

    JsonPrimitive integralPrimitive = createJsonPrimitiveWithValue(10);
    JsonPrimitive longPrimitive = createJsonPrimitiveWithValue(10L);
    JsonPrimitive bigIntegerPrimitive = createJsonPrimitiveWithValue(BigInteger.TEN);
    JsonPrimitive doublePrimitive = createJsonPrimitiveWithValue(10.5);
    JsonPrimitive stringPrimitive = createJsonPrimitiveWithValue("string");

    assertTrue((Boolean) isIntegralMethod.invoke(null, integralPrimitive));
    assertTrue((Boolean) isIntegralMethod.invoke(null, longPrimitive));
    assertTrue((Boolean) isIntegralMethod.invoke(null, bigIntegerPrimitive));
    assertFalse((Boolean) isIntegralMethod.invoke(null, doublePrimitive));
    assertFalse((Boolean) isIntegralMethod.invoke(null, stringPrimitive));
  }

  // Helper to create JsonPrimitive instance with given value using reflection
  private JsonPrimitive createJsonPrimitiveWithValue(Object value) throws Exception {
    // Use constructor JsonPrimitive(Object) is not visible, so create instance with dummy and set value field
    JsonPrimitive instance = (JsonPrimitive) UnsafeAllocator.create().newInstance(JsonPrimitive.class);
    setValueField(instance, value);
    return instance;
  }

  // Helper to set private final field 'value' via reflection
  private void setValueField(JsonPrimitive instance, Object value) throws Exception {
    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);

    // Remove final modifier
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(valueField, valueField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    valueField.set(instance, value);
  }

  // UnsafeAllocator to instantiate class without calling constructor
  static class UnsafeAllocator {
    static UnsafeAllocator create() {
      return new UnsafeAllocator();
    }

    Object newInstance(Class<?> c) throws Exception {
      // Use sun.misc.Unsafe to instantiate without constructor
      java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      sun.misc.Unsafe unsafe = (sun.misc.Unsafe) f.get(null);
      return unsafe.allocateInstance(c);
    }
  }
}