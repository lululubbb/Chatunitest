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

class LazilyParsedNumber_567_5Test {

  private LazilyParsedNumber lazilyParsedNumber;
  private String value = "123.456";

  @BeforeEach
  void setUp() {
    lazilyParsedNumber = new LazilyParsedNumber(value);
  }

  @Test
    @Timeout(8000)
  void testWriteReplace_returnsBigDecimalWithValue() throws Throwable {
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);

    Object result;
    try {
      result = writeReplace.invoke(lazilyParsedNumber);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }

    assertNotNull(result);
    assertTrue(result instanceof BigDecimal);
    assertEquals(new BigDecimal(value), result);
  }
}