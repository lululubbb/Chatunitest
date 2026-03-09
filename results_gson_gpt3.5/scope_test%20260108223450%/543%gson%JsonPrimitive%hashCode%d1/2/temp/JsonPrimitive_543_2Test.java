package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.LazilyParsedNumber;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class JsonPrimitive_543_2Test {

  @Test
    @Timeout(8000)
  public void testHashCode_valueNull() throws Exception {
    JsonPrimitive instance = createInstanceWithValue(null);

    int hash = instance.hashCode();
    assertEquals(31, hash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_integralValue() throws Exception {
    JsonPrimitive instance = createInstanceWithValue(42L);

    // 42L is a Long, so isIntegral should return true.

    int expectedHash = (int) (42L ^ (42L >>> 32));
    int actualHash = instance.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_numberNonIntegral() throws Exception {
    JsonPrimitive instance = createInstanceWithValue(3.14d);

    long bits = Double.doubleToLongBits(3.14d);
    int expectedHash = (int) (bits ^ (bits >>> 32));
    int actualHash = instance.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_nonNumberValue() throws Exception {
    String stringValue = "testString";
    JsonPrimitive instance = createInstanceWithValue(stringValue);

    int expectedHash = stringValue.hashCode();
    int actualHash = instance.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  @Test
    @Timeout(8000)
  public void testHashCode_lazilyParsedNumber_integral() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123");
    JsonPrimitive instance = createInstanceWithValue(lpn);

    // The hashCode uses getAsNumber().longValue() for integral types.
    // LazilyParsedNumber's longValue() returns the parsed long.
    long expectedValue = 123L;
    int expectedHash = (int) (expectedValue ^ (expectedValue >>> 32));
    int actualHash = instance.hashCode();
    assertEquals(expectedHash, actualHash);
  }

  private JsonPrimitive createInstanceWithValue(Object value) throws Exception {
    JsonPrimitive instance = instantiateWithoutConstructor();

    Field valueField = JsonPrimitive.class.getDeclaredField("value");
    valueField.setAccessible(true);
    valueField.set(instance, value);

    // Clear cached stringValue field if exists
    try {
      Field stringValueField = JsonPrimitive.class.getDeclaredField("stringValue");
      stringValueField.setAccessible(true);
      stringValueField.set(instance, null);
    } catch (NoSuchFieldException ignored) {
      // field not present, ignore
    }

    // Clear cached booleanValue field if exists
    try {
      Field booleanValueField = JsonPrimitive.class.getDeclaredField("booleanValue");
      booleanValueField.setAccessible(true);
      booleanValueField.set(instance, null);
    } catch (NoSuchFieldException ignored) {
      // field not present, ignore
    }

    return instance;
  }

  private JsonPrimitive instantiateWithoutConstructor() throws Exception {
    Field unsafeField = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
    unsafeField.setAccessible(true);
    sun.misc.Unsafe unsafe = (sun.misc.Unsafe) unsafeField.get(null);
    return (JsonPrimitive) unsafe.allocateInstance(JsonPrimitive.class);
  }
}