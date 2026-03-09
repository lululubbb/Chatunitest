package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InvalidObjectException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_563_1Test {

  private LazilyParsedNumber longNumber;
  private LazilyParsedNumber bigDecimalNumber;

  @BeforeEach
  public void setUp() {
    longNumber = new LazilyParsedNumber("123456789");
    bigDecimalNumber = new LazilyParsedNumber("123456789123456789123456789");
  }

  @Test
    @Timeout(8000)
  public void testLongValue_parsesLongSuccessfully() {
    long expected = 123456789L;
    assertEquals(expected, longNumber.longValue());
  }

  @Test
    @Timeout(8000)
  public void testLongValue_parsesBigDecimalFallback() {
    BigDecimal bigDecimal = new BigDecimal("123456789123456789123456789");
    assertEquals(bigDecimal.longValue(), bigDecimalNumber.longValue());
  }

  @Test
    @Timeout(8000)
  public void testLongValue_invalidNumberFormat() {
    LazilyParsedNumber invalidNumber = new LazilyParsedNumber("notANumber");
    // The BigDecimal constructor throws NumberFormatException on invalid string,
    // so longValue() will throw NumberFormatException as well.
    assertThrows(NumberFormatException.class, invalidNumber::longValue);
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_invocation() throws Exception {
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(longNumber);
    // The method is private and returns Object, typically for serialization proxy,
    // no exception means success; result should not be null
    assertEquals(result != null, true);
  }

  @Test
    @Timeout(8000)
  public void testReadObject_invocation() throws Exception {
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> readObject.invoke(longNumber, (Object) null));
    // The cause of InvocationTargetException should be InvalidObjectException
    assertEquals(InvalidObjectException.class, thrown.getCause().getClass());
  }
}