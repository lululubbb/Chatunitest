package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class MalformedJsonException_183_1Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String message = "Test message";
    MalformedJsonException ex = new MalformedJsonException(message);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndThrowable() {
    String message = "Test message";
    Throwable cause = new RuntimeException("Cause");
    MalformedJsonException ex = new MalformedJsonException(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withThrowable() {
    Throwable cause = new RuntimeException("Cause");
    MalformedJsonException ex = new MalformedJsonException(cause);
    assertNotNull(ex.getCause());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testSerialVersionUIDField() throws NoSuchFieldException, IllegalAccessException {
    var field = MalformedJsonException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long value = field.getLong(null);
    assertEquals(1L, value);
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructorsViaReflection() throws Exception {
    Constructor<MalformedJsonException> ctor1 = MalformedJsonException.class.getDeclaredConstructor(String.class);
    ctor1.setAccessible(true);
    MalformedJsonException ex1 = ctor1.newInstance("msg");
    assertEquals("msg", ex1.getMessage());

    Constructor<MalformedJsonException> ctor2 = MalformedJsonException.class.getDeclaredConstructor(String.class, Throwable.class);
    ctor2.setAccessible(true);
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex2 = ctor2.newInstance("msg", cause);
    assertEquals("msg", ex2.getMessage());
    assertEquals(cause, ex2.getCause());

    Constructor<MalformedJsonException> ctor3 = MalformedJsonException.class.getDeclaredConstructor(Throwable.class);
    ctor3.setAccessible(true);
    MalformedJsonException ex3 = ctor3.newInstance(cause);
    assertEquals(cause, ex3.getCause());
  }

  @Test
    @Timeout(8000)
  public void testThrowingMalformedJsonException() {
    assertThrows(MalformedJsonException.class, () -> {
      throw new MalformedJsonException("error");
    });
  }
}