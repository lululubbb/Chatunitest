package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class LazilyParsedNumber_565_5Test {

  @Test
    @Timeout(8000)
  void doubleValue_validNumber_returnsDouble() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.456");
    double result = lpn.doubleValue();
    assertEquals(123.456, result, 0.0000001);
  }

  @Test
    @Timeout(8000)
  void doubleValue_scientificNotation_returnsDouble() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("1.23e2");
    double result = lpn.doubleValue();
    assertEquals(123.0, result, 0.0000001);
  }

  @Test
    @Timeout(8000)
  void doubleValue_negativeNumber_returnsDouble() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("-987.654");
    double result = lpn.doubleValue();
    assertEquals(-987.654, result, 0.0000001);
  }

  @Test
    @Timeout(8000)
  void doubleValue_invalidNumber_throwsNumberFormatException() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("notANumber");
    assertThrows(NumberFormatException.class, lpn::doubleValue);
  }

  @Test
    @Timeout(8000)
  void privateWriteReplace_invocation_notNull() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.456");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(lpn);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void privateReadObject_invocation_throwsInvalidObjectException() throws NoSuchMethodException {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.456");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);
    Executable executable = () -> {
      try {
        readObject.invoke(lpn, (Object) null);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    };
    assertThrows(InvalidObjectException.class, executable);
  }
}