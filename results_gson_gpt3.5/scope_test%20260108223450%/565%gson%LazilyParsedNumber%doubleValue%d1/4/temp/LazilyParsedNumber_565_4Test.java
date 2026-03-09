package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Method;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;

public class LazilyParsedNumber_565_4Test {

  @Test
    @Timeout(8000)
  public void testDoubleValue_validNumber() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123.456");
    assertEquals(123.456d, lpn.doubleValue(), 0.000001);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue_negativeNumber() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("-789.01");
    assertEquals(-789.01d, lpn.doubleValue(), 0.000001);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue_zero() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("0");
    assertEquals(0d, lpn.doubleValue(), 0.000001);
  }

  @Test
    @Timeout(8000)
  public void testDoubleValue_invalidNumber_throwsNumberFormatException() {
    LazilyParsedNumber lpn = new LazilyParsedNumber("notANumber");
    assertThrows(NumberFormatException.class, lpn::doubleValue);
  }

  @Test
    @Timeout(8000)
  public void testPrivateWriteReplace_returnsObject() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("42");
    Method writeReplace = LazilyParsedNumber.class.getDeclaredMethod("writeReplace");
    writeReplace.setAccessible(true);
    Object result = writeReplace.invoke(lpn);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testPrivateReadObject_invocation_throwsInvalidObjectException() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("42");
    Method readObject = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObject.setAccessible(true);

    byte[] serializedData = {
      (byte)0xAC, (byte)0xED, // STREAM_MAGIC
      0x00, 0x05              // STREAM_VERSION
    };
    ObjectInputStream dummyStream = new ObjectInputStream(new ByteArrayInputStream(serializedData)) {
      @Override
      public void defaultReadObject() {
        // do nothing
      }
    };

    Exception exception = assertThrows(Exception.class, () -> readObject.invoke(lpn, dummyStream));
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof java.io.InvalidObjectException);
    assertEquals("Deserialization is unsupported", cause.getMessage());
  }
}