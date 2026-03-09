package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ObjectInputStream;
import java.lang.reflect.Method;

class LazilyParsedNumber_570_6Test {

  @Test
    @Timeout(8000)
  void testEquals_sameInstance() {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    assertTrue(num.equals(num));
  }

  @Test
    @Timeout(8000)
  void testEquals_null() {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    assertFalse(num.equals(null));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentClass() {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    assertFalse(num.equals("123"));
  }

  @Test
    @Timeout(8000)
  void testEquals_equalValues_sameString() {
    LazilyParsedNumber num1 = new LazilyParsedNumber("123");
    LazilyParsedNumber num2 = new LazilyParsedNumber("123");
    assertTrue(num1.equals(num2));
  }

  @Test
    @Timeout(8000)
  void testEquals_equalValues_differentStringObjects() {
    LazilyParsedNumber num1 = new LazilyParsedNumber(new String("123"));
    LazilyParsedNumber num2 = new LazilyParsedNumber(new String("123"));
    assertTrue(num1.equals(num2));
  }

  @Test
    @Timeout(8000)
  void testEquals_differentValues() {
    LazilyParsedNumber num1 = new LazilyParsedNumber("123");
    LazilyParsedNumber num2 = new LazilyParsedNumber("456");
    assertFalse(num1.equals(num2));
  }

  @Test
    @Timeout(8000)
  void testWriteReplace_reflection() throws Exception {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replacement = writeReplace.invoke(num);
    assertNotNull(replacement);
  }

  @Test
    @Timeout(8000)
  void testReadObject_reflection() throws Exception {
    LazilyParsedNumber num = new LazilyParsedNumber("123");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);
    ObjectInputStream in = Mockito.mock(ObjectInputStream.class);
    // The readObject method throws InvalidObjectException on purpose,
    // so we expect InvocationTargetException with cause InvalidObjectException.
    Exception exception = assertThrows(Exception.class, () -> readObject.invoke(num, in));
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof java.io.InvalidObjectException);
    assertEquals("Deserialization is unsupported", cause.getMessage());
  }
}