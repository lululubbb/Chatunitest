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

class LazilyParsedNumber_567_1Test {

  private LazilyParsedNumber lazilyParsedNumber;
  private Method writeReplaceMethod;

  @BeforeEach
  void setUp() throws NoSuchMethodException {
    lazilyParsedNumber = new LazilyParsedNumber("123.45");
    writeReplaceMethod = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplaceMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testWriteReplaceReturnsBigDecimalWithCorrectValue() throws Throwable {
    Object result;
    try {
      result = writeReplaceMethod.invoke(lazilyParsedNumber);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
    assertNotNull(result);
    assertTrue(result instanceof BigDecimal);
    assertEquals(new BigDecimal("123.45"), result);
  }

  @Test
    @Timeout(8000)
  void testWriteReplaceThrowsObjectStreamException() throws Throwable {
    // The method signature declares throws ObjectStreamException, but no exception is thrown in code.
    // We test that no exception is thrown in normal case.
    assertDoesNotThrow(() -> {
      try {
        writeReplaceMethod.invoke(lazilyParsedNumber);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }
}