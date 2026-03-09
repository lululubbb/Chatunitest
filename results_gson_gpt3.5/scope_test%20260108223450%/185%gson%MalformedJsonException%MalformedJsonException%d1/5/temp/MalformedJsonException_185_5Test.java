package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class MalformedJsonException_185_5Test {

  @Test
    @Timeout(8000)
  void testConstructor_withThrowable() {
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex = new MalformedJsonException(cause);
    assertEquals(cause, ex.getCause());
    assertNotNull(ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessage() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<MalformedJsonException> ctor = MalformedJsonException.class.getDeclaredConstructor(String.class);
    ctor.setAccessible(true);
    MalformedJsonException ex = ctor.newInstance("error message");
    assertEquals("error message", ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructor_withMessageAndThrowable() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<MalformedJsonException> ctor = MalformedJsonException.class.getDeclaredConstructor(String.class, Throwable.class);
    ctor.setAccessible(true);
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex = ctor.newInstance("error message", cause);
    assertEquals("error message", ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}