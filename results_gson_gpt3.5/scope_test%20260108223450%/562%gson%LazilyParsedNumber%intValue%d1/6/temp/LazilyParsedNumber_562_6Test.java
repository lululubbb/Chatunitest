package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.math.BigDecimal;

public class LazilyParsedNumber_562_6Test {

  @Test
    @Timeout(8000)
  public void intValue_parsesIntegerSuccessfully() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertEquals(123, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_parsesLongSuccessfully() {
    // A value that is not a valid int, but valid long within int range after cast
    LazilyParsedNumber number = new LazilyParsedNumber("2147483648"); // Integer.MAX_VALUE + 1
    assertEquals((int) 2147483648L, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_parsesBigDecimalSuccessfully() {
    // A value that is not valid int or long but valid BigDecimal
    LazilyParsedNumber number = new LazilyParsedNumber("1.5");
    assertEquals(new BigDecimal("1.5").intValue(), number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_parsesNegativeBigDecimal() {
    LazilyParsedNumber number = new LazilyParsedNumber("-123.9");
    assertEquals(new BigDecimal("-123.9").intValue(), number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_parsesNegativeLong() {
    LazilyParsedNumber number = new LazilyParsedNumber("-2147483649"); // Integer.MIN_VALUE - 1
    assertEquals((int) Long.parseLong("-2147483649"), number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_parsesNegativeInteger() {
    LazilyParsedNumber number = new LazilyParsedNumber("-123");
    assertEquals(-123, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_throwsNumberFormatException_onInvalidNumber() {
    LazilyParsedNumber number = new LazilyParsedNumber("abc");
    // The method intValue does not throw, it returns BigDecimal.intValue() which returns 0 for invalid input? Actually BigDecimal constructor throws NumberFormatException on invalid string.
    // But the code does not catch NumberFormatException from BigDecimal constructor, so the test must expect NumberFormatException.
    assertThrows(NumberFormatException.class, number::intValue);
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_privateMethod() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(number);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadObject_privateMethod() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);
    // We invoke with null since we do not want to actually deserialize
    // This will throw NullPointerException if method uses the stream, so we catch it
    try {
      readObject.invoke(number, (Object) null);
      fail("Expected NullPointerException or IOException");
    } catch (Exception e) {
      // expected because we passed null stream
      assertTrue(e.getCause() instanceof NullPointerException || e.getCause() instanceof IOException);
    }
  }
}