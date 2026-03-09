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

public class LazilyParsedNumber_564_4Test {

  @Test
    @Timeout(8000)
  public void testFloatValue_ValidFloatString() {
    LazilyParsedNumber number = new LazilyParsedNumber("123.45");
    float result = number.floatValue();
    assertEquals(123.45f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_ScientificNotation() {
    LazilyParsedNumber number = new LazilyParsedNumber("1.23e2");
    float result = number.floatValue();
    assertEquals(123f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_NegativeFloat() {
    LazilyParsedNumber number = new LazilyParsedNumber("-987.65");
    float result = number.floatValue();
    assertEquals(-987.65f, result, 0.0001f);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_InvalidFloat_ThrowsNumberFormatException() {
    LazilyParsedNumber number = new LazilyParsedNumber("not_a_number");
    assertThrows(NumberFormatException.class, number::floatValue);
  }

  @Test
    @Timeout(8000)
  public void testFloatValue_EmptyString_ThrowsNumberFormatException() {
    LazilyParsedNumber number = new LazilyParsedNumber("");
    assertThrows(NumberFormatException.class, number::floatValue);
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_IsPrivateAndReturnsObject() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("1.23");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(number);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadObject_IsPrivateVoidMethod() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("1.23");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", java.io.ObjectInputStream.class);
    readObject.setAccessible(true);

    // We expect InvalidObjectException to be thrown when invoking readObject
    java.io.ObjectInputStream dummyStream = null;
    Executable exec = () -> readObject.invoke(number, dummyStream);

    Throwable thrown = assertThrows(InvocationTargetException.class, exec);
    // InvocationTargetException wraps the actual exception
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof InvalidObjectException);
  }
}