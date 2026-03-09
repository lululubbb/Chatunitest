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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class LazilyParsedNumber_561_4Test {

  private LazilyParsedNumber number;
  private final String value = "123.456";

  @BeforeEach
  public void setUp() {
    number = new LazilyParsedNumber(value);
  }

  @Test
    @Timeout(8000)
  public void testIntValue() {
    int expected = new BigDecimal(value).intValue();
    assertEquals(expected, number.intValue());
  }

  @Test
    @Timeout(8000)
  public void testLongValue() {
    long expected = new BigDecimal(value).longValue();
    assertEquals(expected, number.longValue());
  }

  @Test
    @Timeout(8000)
  public void testFloatValue() {
    float expected = new BigDecimal(value).floatValue();
    assertEquals(expected, number.floatValue(), 0.00001f);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue() {
    double expected = new BigDecimal(value).doubleValue();
    assertEquals(expected, number.doubleValue(), 0.0000001);
  }

  @Test
    @Timeout(8000)
  public void testToString() {
    assertEquals(value, number.toString());
  }

  @Test
    @Timeout(8000)
  public void testHashCodeEquals() {
    LazilyParsedNumber sameValue = new LazilyParsedNumber(value);
    LazilyParsedNumber differentValue = new LazilyParsedNumber("789.01");

    assertEquals(number.hashCode(), sameValue.hashCode());
    assertTrue(number.equals(sameValue));
    assertFalse(number.equals(differentValue));
    assertFalse(number.equals(null));
    assertFalse(number.equals("some string"));
  }

  @Test
    @Timeout(8000)
  public void testWriteReplace() throws Exception {
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object replacement = writeReplace.invoke(number);
    assertEquals(value, replacement.toString());
  }

  @Test
    @Timeout(8000)
  public void testReadObjectValid() throws Exception {
    // Serialize the LazilyParsedNumber object
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(number);
    oos.close();

    // Deserialize the object back
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);

    Object obj = ois.readObject();
    assertTrue(obj instanceof LazilyParsedNumber);
    assertEquals(value, obj.toString());
  }

  @Test
    @Timeout(8000)
  public void testReadObjectInvalid() throws Exception {
    // We will mock ObjectInputStream to return an invalid value to trigger exception
    ObjectInputStream mockIn = mock(ObjectInputStream.class);
    when(mockIn.readObject()).thenReturn(123); // not a String

    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    LazilyParsedNumber instance = new LazilyParsedNumber(value);

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      readObject.invoke(instance, mockIn);
    });
    assertTrue(ex.getCause() instanceof InvalidObjectException);
  }
}