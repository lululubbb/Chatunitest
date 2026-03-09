package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectStreamException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_561_3Test {

  @Test
    @Timeout(8000)
  public void testConstructorAndToString() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.45");
    assertEquals("123.45", lpn.toString());
  }

  @Test
    @Timeout(8000)
  public void testIntValue() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123");
    assertEquals(123, lpn.intValue());

    lpn = new LazilyParsedNumber("123.99");
    assertEquals(123, lpn.intValue());

    lpn = new LazilyParsedNumber("999999999999999999999999999999");
    assertEquals(new BigDecimal("999999999999999999999999999999").intValue(), lpn.intValue());
  }

  @Test
    @Timeout(8000)
  public void testLongValue() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123");
    assertEquals(123L, lpn.longValue());

    lpn = new LazilyParsedNumber("123.99");
    assertEquals(123L, lpn.longValue());

    lpn = new LazilyParsedNumber("999999999999999999999999999999");
    assertEquals(new BigDecimal("999999999999999999999999999999").longValue(), lpn.longValue());
  }

  @Test
    @Timeout(8000)
  public void testFloatValue() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.45");
    assertEquals(123.45f, lpn.floatValue(), 0.00001);

    lpn = new LazilyParsedNumber("1.4E+39");
    assertEquals(Float.parseFloat("1.4E+39"), lpn.floatValue(), 0.00001);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.45");
    assertEquals(123.45d, lpn.doubleValue(), 0.00001);

    lpn = new LazilyParsedNumber("1.4E+308");
    assertEquals(Double.parseDouble("1.4E+308"), lpn.doubleValue(), 0.00001);
  }

  @Test
    @Timeout(8000)
  public void testHashCodeAndEquals() {
    LazilyParsedNumber lpn1 = new LazilyParsedNumber("123.45");
    LazilyParsedNumber lpn2 = new LazilyParsedNumber("123.45");
    LazilyParsedNumber lpn3 = new LazilyParsedNumber("543.21");

    assertEquals(lpn1.hashCode(), lpn2.hashCode());
    assertTrue(lpn1.equals(lpn2));
    assertFalse(lpn1.equals(lpn3));
    assertFalse(lpn1.equals(null));
    assertFalse(lpn1.equals("123.45"));
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.45");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replaced = writeReplace.invoke(lpn);
    assertTrue(replaced instanceof BigDecimal);
    assertEquals(new BigDecimal("123.45"), replaced);
  }

  @Test
    @Timeout(8000)
  public void testReadObjectThrowsInvalidObjectException() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.45");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    ObjectInputStream mockIn = mock(ObjectInputStream.class);
    InvocationTargetException ite = assertThrows(InvocationTargetException.class, () -> {
      readObject.invoke(lpn, mockIn);
    });
    Throwable cause = ite.getCause();
    assertTrue(cause instanceof InvalidObjectException);
    assertEquals("Deserialization is unsupported", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testSerializationRoundTrip() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.45");

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(lpn);
    oos.close();

    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);

    Object deserialized = ois.readObject();
    ois.close();

    // Because writeReplace returns BigDecimal, deserialized object is BigDecimal
    assertTrue(deserialized instanceof BigDecimal);
    assertEquals(new BigDecimal("123.45"), deserialized);
  }
}