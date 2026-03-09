package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class LazilyParsedNumber_564_6Test {

  @Test
    @Timeout(8000)
  void floatValue_ValidFloatString_ReturnsParsedFloat() {
    LazilyParsedNumber number = new LazilyParsedNumber("123.45");
    float result = number.floatValue();
    assertEquals(123.45f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  void floatValue_InvalidFloatString_ThrowsNumberFormatException() {
    LazilyParsedNumber number = new LazilyParsedNumber("abc");
    assertThrows(NumberFormatException.class, number::floatValue);
  }

  @Test
    @Timeout(8000)
  void writeReplace_IsPrivateAndReturnsObject() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("1.23");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(number);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void readObject_IsPrivateAndThrowsInvalidObjectException() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("1.23");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> readObject.invoke(number, (Object) null));
    assertTrue(exception.getCause() instanceof InvalidObjectException);
  }
}