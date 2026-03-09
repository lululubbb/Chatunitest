package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ObjectStreamException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LazilyParsedNumber_567_6Test {

  private LazilyParsedNumber lazilyParsedNumber;
  private Method writeReplaceMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    lazilyParsedNumber = new LazilyParsedNumber("123.456");
    writeReplaceMethod = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplaceMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void writeReplace_returnsBigDecimalWithValue() throws Throwable {
    Object result;
    try {
      result = writeReplaceMethod.invoke(lazilyParsedNumber);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
    assertNotNull(result);
    assertTrue(result instanceof BigDecimal);
    assertEquals(new BigDecimal("123.456"), result);
  }

  @Test
    @Timeout(8000)
  void writeReplace_handlesDifferentValue() throws Throwable {
    LazilyParsedNumber number = new LazilyParsedNumber("-98765.4321");
    Object result;
    try {
      result = writeReplaceMethod.invoke(number);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
    assertNotNull(result);
    assertTrue(result instanceof BigDecimal);
    assertEquals(new BigDecimal("-98765.4321"), result);
  }

  @Test
    @Timeout(8000)
  void writeReplace_handlesZeroValue() throws Throwable {
    LazilyParsedNumber number = new LazilyParsedNumber("0");
    Object result;
    try {
      result = writeReplaceMethod.invoke(number);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
    assertNotNull(result);
    assertTrue(result instanceof BigDecimal);
    assertEquals(BigDecimal.ZERO, result);
  }

}