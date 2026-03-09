package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_565_1Test {

  @Test
    @Timeout(8000)
  public void testDoubleValue_validNumber() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.456");
    assertEquals(123.456, lpn.doubleValue(), 0.0000001);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue_negativeNumber() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("-789.0123");
    assertEquals(-789.0123, lpn.doubleValue(), 0.0000001);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue_zero() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("0");
    assertEquals(0.0, lpn.doubleValue(), 0.0000001);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue_invalidNumber_throwsNumberFormatException() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("notANumber");
    assertThrows(NumberFormatException.class, lpn::doubleValue);
  }

  @Test
    @Timeout(8000)
  public void testIntValue_andLongValue_andFloatValue_andToString() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("42");
    assertEquals(42, lpn.intValue());
    assertEquals(42L, lpn.longValue());
    assertEquals(42.0f, lpn.floatValue(), 0.0001f);
    assertEquals("42", lpn.toString());
  }

  @Test
    @Timeout(8000)
  public void testHashCode_andEquals() {
    LazilyParsedNumber lpn1 = new LazilyParsedNumber("123.45");
    LazilyParsedNumber lpn2 = new LazilyParsedNumber("123.45");
    LazilyParsedNumber lpn3 = new LazilyParsedNumber("543.21");

    assertEquals(lpn1.hashCode(), lpn2.hashCode());
    assertTrue(lpn1.equals(lpn2));
    assertFalse(lpn1.equals(lpn3));
    assertFalse(lpn1.equals(null));
    assertFalse(lpn1.equals("some string"));
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_returnsBigDecimal() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("100");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(lpn);
    assertNotSame(lpn, result);
    assertEquals("100", result.toString());
    assertEquals("java.math.BigDecimal", result.getClass().getName());
  }

  @Test
    @Timeout(8000)
  public void testReadObject_validInvocation() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("200");
    ObjectInputStream mockIn = mock(ObjectInputStream.class);
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);
    // Expect InvalidObjectException wrapped in InvocationTargetException
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> readObject.invoke(lpn, mockIn));
    assertTrue(thrown.getCause() instanceof InvalidObjectException);
  }

  @Test
    @Timeout(8000)
  public void testReadObject_throwsInvalidObjectException() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("300");
    ObjectInputStream mockIn = mock(ObjectInputStream.class);
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);
    // Simulate IOException from mockIn.defaultReadObject()
    doThrow(new IOException()).when(mockIn).defaultReadObject();

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> readObject.invoke(lpn, mockIn));
    assertTrue(thrown.getCause() instanceof InvalidObjectException);
  }
}