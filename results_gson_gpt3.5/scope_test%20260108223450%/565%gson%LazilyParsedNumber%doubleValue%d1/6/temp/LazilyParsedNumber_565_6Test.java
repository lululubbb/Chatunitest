package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class LazilyParsedNumber_565_6Test {

  @Test
    @Timeout(8000)
  void doubleValue_validNumber_returnsParsedDouble() {
    LazilyParsedNumber number = new LazilyParsedNumber("123.456");
    double result = number.doubleValue();
    assertEquals(123.456, result, 0.000001);
  }

  @Test
    @Timeout(8000)
  void doubleValue_negativeNumber_returnsParsedDouble() {
    LazilyParsedNumber number = new LazilyParsedNumber("-987.654");
    double result = number.doubleValue();
    assertEquals(-987.654, result, 0.000001);
  }

  @Test
    @Timeout(8000)
  void doubleValue_invalidNumber_throwsNumberFormatException() {
    LazilyParsedNumber number = new LazilyParsedNumber("notANumber");
    assertThrows(NumberFormatException.class, number::doubleValue);
  }

  @Test
    @Timeout(8000)
  void toString_returnsOriginalValue() {
    LazilyParsedNumber number = new LazilyParsedNumber("42.0");
    assertEquals("42.0", number.toString());
  }

  @Test
    @Timeout(8000)
  void equals_sameValueObjects_areEqual() {
    LazilyParsedNumber n1 = new LazilyParsedNumber("100");
    LazilyParsedNumber n2 = new LazilyParsedNumber("100");
    assertEquals(n1, n2);
    assertEquals(n1.hashCode(), n2.hashCode());
  }

  @Test
    @Timeout(8000)
  void equals_differentValueObjects_notEqual() {
    LazilyParsedNumber n1 = new LazilyParsedNumber("100");
    LazilyParsedNumber n2 = new LazilyParsedNumber("200");
    assertNotEquals(n1, n2);
  }

  @Test
    @Timeout(8000)
  void equals_nullAndOtherType_notEqual() {
    LazilyParsedNumber n = new LazilyParsedNumber("1");
    assertNotEquals(n, null);
    assertNotEquals(n, "1");
  }

  @Test
    @Timeout(8000)
  void intValue_returnsIntValue() {
    LazilyParsedNumber n = new LazilyParsedNumber("123");
    assertEquals(123, n.intValue());
  }

  @Test
    @Timeout(8000)
  void longValue_returnsLongValue() {
    LazilyParsedNumber n = new LazilyParsedNumber("1234567890123");
    assertEquals(1234567890123L, n.longValue());
  }

  @Test
    @Timeout(8000)
  void floatValue_returnsFloatValue() {
    LazilyParsedNumber n = new LazilyParsedNumber("3.14");
    assertEquals(3.14f, n.floatValue(), 0.0001f);
  }

  @Test
    @Timeout(8000)
  void writeReplace_reflection_returnsString() throws Exception {
    LazilyParsedNumber n = new LazilyParsedNumber("1");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replacement = writeReplace.invoke(n);
    assertNotNull(replacement);
    // The writeReplace returns the original String value, not LazilyParsedNumber instance
    assertTrue(replacement instanceof CharSequence);
    assertEquals("1", replacement.toString());
  }

  @Test
    @Timeout(8000)
  void readObject_reflection_throwsInvalidObjectException() throws Exception {
    LazilyParsedNumber n = new LazilyParsedNumber("1");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);
    ObjectInputStream mockStream = Mockito.mock(ObjectInputStream.class);
    // readObject should throw InvalidObjectException wrapped in InvocationTargetException
    InvocationTargetException exception = assertThrows(InvocationTargetException.class, () -> readObject.invoke(n, mockStream));
    // The cause should be InvalidObjectException
    assertTrue(exception.getCause() instanceof InvalidObjectException);
    assertEquals("Deserialization is unsupported", exception.getCause().getMessage());
  }
}