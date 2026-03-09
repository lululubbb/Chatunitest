package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ObjectStreamException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LazilyParsedNumber_567_3Test {

  private LazilyParsedNumber lazilyParsedNumber;

  @BeforeEach
  void setUp() {
    lazilyParsedNumber = new LazilyParsedNumber("123.456");
  }

  @Test
    @Timeout(8000)
  void testWriteReplaceReturnsBigDecimalWithCorrectValue()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ObjectStreamException {
    Method writeReplaceMethod = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplaceMethod.setAccessible(true);

    Object result = writeReplaceMethod.invoke(lazilyParsedNumber);

    assertNotNull(result);
    assertTrue(result instanceof BigDecimal);
    assertEquals(new BigDecimal("123.456"), result);
  }

  @Test
    @Timeout(8000)
  void testWriteReplaceWithNegativeValue()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ObjectStreamException {
    LazilyParsedNumber negativeNumber = new LazilyParsedNumber("-987.654");
    Method writeReplaceMethod = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplaceMethod.setAccessible(true);

    Object result = writeReplaceMethod.invoke(negativeNumber);

    assertNotNull(result);
    assertTrue(result instanceof BigDecimal);
    assertEquals(new BigDecimal("-987.654"), result);
  }

  @Test
    @Timeout(8000)
  void testWriteReplaceWithZeroValue()
      throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ObjectStreamException {
    LazilyParsedNumber zeroNumber = new LazilyParsedNumber("0");
    Method writeReplaceMethod = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplaceMethod.setAccessible(true);

    Object result = writeReplaceMethod.invoke(zeroNumber);

    assertNotNull(result);
    assertTrue(result instanceof BigDecimal);
    assertEquals(BigDecimal.ZERO, result);
  }
}