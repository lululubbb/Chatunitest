package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_562_5Test {

  @Test
    @Timeout(8000)
  public void intValue_parsesIntegerSuccessfully() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertEquals(123, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_parsesLongSuccessfully() {
    // Use a value that is not an int but fits in a long
    LazilyParsedNumber number = new LazilyParsedNumber("2147483648"); // Integer.MAX_VALUE + 1
    assertEquals((int) 2147483648L, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_parsesBigDecimalSuccessfully() {
    // Use a value that is not a valid long but valid BigDecimal
    LazilyParsedNumber number = new LazilyParsedNumber("1.5");
    assertEquals(new BigDecimal("1.5").intValue(), number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_invalidNumber_throwsNumberFormatException() {
    LazilyParsedNumber number = new LazilyParsedNumber("notANumber");
    assertThrows(NumberFormatException.class, number::intValue);
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_invokedViaReflection() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(number);
    assertNotNull(result);
    assertTrue(result instanceof Number);
    assertEquals(number.toString(), result.toString());
  }

  @Test
    @Timeout(8000)
  public void testReadObject_invokedViaReflection_throwsInvalidObjectException() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);
    java.io.ObjectInputStream mockIn = mock(java.io.ObjectInputStream.class);
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> readObject.invoke(number, mockIn));
    assertTrue(thrown.getCause() instanceof java.io.InvalidObjectException);
  }
}