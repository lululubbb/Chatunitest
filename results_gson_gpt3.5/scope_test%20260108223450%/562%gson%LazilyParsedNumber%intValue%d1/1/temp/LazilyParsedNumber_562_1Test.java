package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LazilyParsedNumber_562_1Test {

  @Test
    @Timeout(8000)
  public void testIntValue_parsesInteger() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertEquals(123, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void testIntValue_parsesLongWhenIntegerFails() {
    // value too large for int but fits in long
    LazilyParsedNumber number = new LazilyParsedNumber("2147483648"); // Integer.MAX_VALUE + 1
    assertEquals(2147483648L, number.longValue());
    // intValue should cast long to int (overflow behavior)
    assertEquals((int)2147483648L, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void testIntValue_parsesBigDecimalWhenLongFails() {
    // value too large for long, must parse as BigDecimal
    String bigValue = "92233720368547758079223372036854775807"; // larger than Long.MAX_VALUE
    LazilyParsedNumber number = new LazilyParsedNumber(bigValue);

    // intValue uses BigDecimal.intValue(), which returns low 32 bits
    int expected = new java.math.BigDecimal(bigValue).intValue();
    assertEquals(expected, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_invokesSuccessfully() throws Throwable {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(number);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadObject_invokesSuccessfully() throws Throwable {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);
    // Passing null should throw NullPointerException or IOException, catch to verify method exists
    try {
      readObject.invoke(number, new Object[] { null });
      fail("Expected exception");
    } catch (InvocationTargetException e) {
      // Expected, ignore
      assertTrue(e.getCause() instanceof IOException || e.getCause() instanceof NullPointerException);
    }
  }

  @Test
    @Timeout(8000)
  public void testEqualsAndHashCode() {
    LazilyParsedNumber n1 = new LazilyParsedNumber("123");
    LazilyParsedNumber n2 = new LazilyParsedNumber("123");
    LazilyParsedNumber n3 = new LazilyParsedNumber("456");

    assertEquals(n1, n2);
    assertEquals(n1.hashCode(), n2.hashCode());

    assertNotEquals(n1, n3);
    assertNotEquals(n1.hashCode(), n3.hashCode());

    assertNotEquals(n1, null);
    assertNotEquals(n1, "123");
  }

  @Test
    @Timeout(8000)
  public void testLongFloatDoubleToString() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertEquals(123L, number.longValue());
    assertEquals(123f, number.floatValue());
    assertEquals(123d, number.doubleValue());
    assertEquals("123", number.toString());
  }
}