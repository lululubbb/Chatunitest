package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class LazilyParsedNumber_568_5Test {

  private LazilyParsedNumber lazilyParsedNumber;

  @BeforeEach
  public void setUp() {
    lazilyParsedNumber = new LazilyParsedNumber("123.45");
  }

  @Test
    @Timeout(8000)
  public void testReadObject_throwsInvalidObjectException() throws Exception {
    ObjectInputStream mockInputStream = Mockito.mock(ObjectInputStream.class);

    Method readObjectMethod = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObjectMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      readObjectMethod.invoke(lazilyParsedNumber, mockInputStream);
    });

    Throwable cause = thrown.getCause();
    // Check that the cause is InvalidObjectException with the expected message
    assertEquals(InvalidObjectException.class, cause.getClass());
    assertEquals("Deserialization is unsupported", cause.getMessage());
  }
}