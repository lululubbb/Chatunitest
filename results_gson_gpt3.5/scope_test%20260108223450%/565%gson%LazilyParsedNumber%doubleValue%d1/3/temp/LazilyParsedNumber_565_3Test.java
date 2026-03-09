package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;

class LazilyParsedNumber_565_3Test {

  @Test
    @Timeout(8000)
  void testDoubleValue_validNumber() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.456");
    double expected = 123.456d;
    assertEquals(expected, lpn.doubleValue(), 0.0000001);
  }

  @Test
    @Timeout(8000)
  void testDoubleValue_negativeNumber() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("-987.654");
    double expected = -987.654d;
    assertEquals(expected, lpn.doubleValue(), 0.0000001);
  }

  @Test
    @Timeout(8000)
  void testDoubleValue_zero() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("0");
    double expected = 0d;
    assertEquals(expected, lpn.doubleValue(), 0.0000001);
  }

  @Test
    @Timeout(8000)
  void testDoubleValue_invalidNumber_throwsNumberFormatException() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("notANumber");
    assertThrows(NumberFormatException.class, lpn::doubleValue);
  }

  @Test
    @Timeout(8000)
  void testWriteReplace_privateMethod_returnsObject() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("1.23");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(lpn);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testReadObject_privateMethod() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("1.23");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);
    // Passing null to simulate ObjectInputStream, expecting IOException or InvalidObjectException might be thrown
    assertThrows(Exception.class, () -> readObject.invoke(lpn, (Object) null));
  }
}