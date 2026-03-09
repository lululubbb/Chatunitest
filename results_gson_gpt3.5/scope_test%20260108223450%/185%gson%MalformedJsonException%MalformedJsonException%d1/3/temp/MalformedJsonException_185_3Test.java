package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class MalformedJsonException_185_3Test {

  @Test
    @Timeout(8000)
  void testConstructorWithThrowable() {
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex = new MalformedJsonException(cause);
    assertEquals(cause, ex.getCause());
    assertNotNull(ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessage() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<MalformedJsonException> constructor = MalformedJsonException.class.getDeclaredConstructor(String.class);
    constructor.setAccessible(true);
    MalformedJsonException ex = constructor.newInstance("error message");
    assertEquals("error message", ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  void testConstructorWithMessageAndThrowable() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<MalformedJsonException> constructor = MalformedJsonException.class.getDeclaredConstructor(String.class, Throwable.class);
    constructor.setAccessible(true);
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex = constructor.newInstance("error message", cause);
    assertEquals("error message", ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}