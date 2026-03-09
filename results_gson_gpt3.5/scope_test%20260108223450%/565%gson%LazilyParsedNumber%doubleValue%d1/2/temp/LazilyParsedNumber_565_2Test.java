package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_565_2Test {

  @Test
    @Timeout(8000)
  public void testDoubleValue_validNumber() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.456");
    double result = lpn.doubleValue();
    assertEquals(123.456, result, 0.0000001);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue_negativeNumber() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("-789.01");
    double result = lpn.doubleValue();
    assertEquals(-789.01, result, 0.0000001);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue_integerString() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("42");
    double result = lpn.doubleValue();
    assertEquals(42.0, result, 0.0000001);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue_invalidNumber_throwsNumberFormatException() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("notANumber");
    assertThrows(NumberFormatException.class, lpn::doubleValue);
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_returnsObject() throws Throwable {
    LazilyParsedNumber lpn = new LazilyParsedNumber("10.5");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(lpn);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadObject_withValidStream() throws Throwable {
    LazilyParsedNumber lpn = new LazilyParsedNumber("10.5");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    // Use a valid ObjectInputStream stream header to avoid EOFException on construction
    byte[] validStreamHeader = new byte[] {
        (byte) 0xAC, (byte) 0xED, // STREAM_MAGIC
        0x00, 0x05 // STREAM_VERSION
    };
    ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(validStreamHeader));
    try {
      readObject.invoke(lpn, ois);
      fail("Expected IOException or InvalidObjectException");
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      assertTrue(cause instanceof IOException || cause instanceof InvalidObjectException);
    }
  }

  @Test
    @Timeout(8000)
  public void testHashCode_equals_consistency() {
    LazilyParsedNumber lpn1 = new LazilyParsedNumber("123.45");
    LazilyParsedNumber lpn2 = new LazilyParsedNumber("123.45");
    LazilyParsedNumber lpn3 = new LazilyParsedNumber("543.21");

    assertEquals(lpn1.hashCode(), lpn2.hashCode());
    assertNotEquals(lpn1.hashCode(), lpn3.hashCode());

    assertTrue(lpn1.equals(lpn2));
    assertFalse(lpn1.equals(lpn3));
    assertFalse(lpn1.equals(null));
    assertFalse(lpn1.equals("some string"));
  }

  @Test
    @Timeout(8000)
  public void testIntValue_longValue_floatValue_toString() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.45");
    int intValue = lpn.intValue();
    long longValue = lpn.longValue();
    float floatValue = lpn.floatValue();
    String str = lpn.toString();

    assertEquals((int)Double.parseDouble("123.45"), intValue);
    assertEquals((long)Double.parseDouble("123.45"), longValue);
    assertEquals((float)Double.parseDouble("123.45"), floatValue, 0.00001f);
    assertEquals("123.45", str);
  }
}