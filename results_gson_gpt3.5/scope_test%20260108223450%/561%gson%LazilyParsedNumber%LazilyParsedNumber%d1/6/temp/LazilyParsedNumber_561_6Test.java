package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LazilyParsedNumber_561_6Test {

  private LazilyParsedNumber lazilyParsedNumber;

  @BeforeEach
  void setUp() {
    lazilyParsedNumber = new LazilyParsedNumber("12345");
  }

  @Test
    @Timeout(8000)
  void testIntValue() {
    assertEquals(12345, lazilyParsedNumber.intValue());
  }

  @Test
    @Timeout(8000)
  void testLongValue() {
    assertEquals(12345L, lazilyParsedNumber.longValue());
  }

  @Test
    @Timeout(8000)
  void testFloatValue() {
    assertEquals(12345f, lazilyParsedNumber.floatValue());
  }

  @Test
    @Timeout(8000)
  void testDoubleValue() {
    assertEquals(12345d, lazilyParsedNumber.doubleValue());
  }

  @Test
    @Timeout(8000)
  void testToString() {
    assertEquals("12345", lazilyParsedNumber.toString());
  }

  @Test
    @Timeout(8000)
  void testHashCodeAndEquals() {
    LazilyParsedNumber sameValue = new LazilyParsedNumber("12345");
    LazilyParsedNumber differentValue = new LazilyParsedNumber("54321");
    assertEquals(lazilyParsedNumber.hashCode(), sameValue.hashCode());
    assertTrue(lazilyParsedNumber.equals(sameValue));
    assertFalse(lazilyParsedNumber.equals(differentValue));
    assertFalse(lazilyParsedNumber.equals(null));
    assertFalse(lazilyParsedNumber.equals("12345"));
  }

  @Test
    @Timeout(8000)
  void testWriteReplace() throws Exception {
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(lazilyParsedNumber);
    assertEquals(new LazilyParsedNumber("12345").toString(), result.toString());
  }

  @Test
    @Timeout(8000)
  void testReadObject_withValidStream() throws Exception {
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject("12345");
    oos.flush();
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);

    LazilyParsedNumber instance = new LazilyParsedNumber("dummy");
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      readObject.invoke(instance, ois);
    });
    assertTrue(thrown.getCause() instanceof InvalidObjectException);
    assertEquals("Deserialization is unsupported", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void testReadObject_withInvalidObjectException() throws Exception {
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    ObjectInputStream ois = mock(ObjectInputStream.class);
    when(ois.readObject()).thenReturn(Integer.valueOf(123));

    LazilyParsedNumber instance = new LazilyParsedNumber("dummy");
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      readObject.invoke(instance, ois);
    });
    assertTrue(thrown.getCause() instanceof InvalidObjectException);
    assertEquals("Deserialization is unsupported", thrown.getCause().getMessage());
  }
}