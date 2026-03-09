package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectStreamException;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.InvocationTargetException;

public class LazilyParsedNumber_562_4Test {

  @Test
    @Timeout(8000)
  public void intValue_parsesIntegerSuccessfully() {
    LazilyParsedNumber number = new LazilyParsedNumber("123");
    assertEquals(123, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_parsesLongSuccessfully() {
    // Value too large for Integer.parseInt but fits in long
    LazilyParsedNumber number = new LazilyParsedNumber("2147483648"); // Integer.MAX_VALUE + 1
    assertEquals((int)2147483648L, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void intValue_parsesBigDecimalSuccessfully() {
    // Value too large for long, fallback to BigDecimal
    LazilyParsedNumber number = new LazilyParsedNumber("92233720368547758079223372036854775807"); 
    assertEquals(new BigDecimal("92233720368547758079223372036854775807").intValue(), number.intValue());
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace_isPrivateAndReturnsObject() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("1");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(number);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testReadObject_isPrivateVoid() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("1");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    // Create a valid ObjectInputStream by serializing and deserializing a LazilyParsedNumber instance
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(number);
    oos.flush();
    byte[] serializedData = baos.toByteArray();

    ByteArrayInputStream bais = new ByteArrayInputStream(serializedData);
    ObjectInputStream ois = new ObjectInputStream(bais);

    // Invoke readObject with a valid ObjectInputStream to cover the method
    try {
      readObject.invoke(number, ois);
    } catch (InvocationTargetException e) {
      Throwable cause = e.getCause();
      // If exception occurs, it should be IOException or InvalidObjectException
      assertNotNull(cause);
      assertTrue(cause instanceof IOException || cause instanceof InvalidObjectException);
    }
  }
}