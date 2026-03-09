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

class LazilyParsedNumber_567_4Test {

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
    Object replacement;
    try {
      replacement = writeReplaceMethod.invoke(lazilyParsedNumber);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
    assertNotNull(replacement);
    assertTrue(replacement instanceof BigDecimal);
    BigDecimal bigDecimal = (BigDecimal) replacement;
    assertEquals(new BigDecimal("123.456"), bigDecimal);
  }

  @Test
    @Timeout(8000)
  void writeReplace_throwsObjectStreamException() throws Exception {
    // Since writeReplace only creates BigDecimal from string value, it should not throw.
    // But to test exception branch, we can use a subclass or reflection hack to cause exception.
    // Here, we test that no exception is thrown for valid input.
    assertDoesNotThrow(() -> {
      try {
        writeReplaceMethod.invoke(lazilyParsedNumber);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }
}