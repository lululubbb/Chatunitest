package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class MalformedJsonException_185_2Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withThrowable() {
    Throwable cause = new Throwable("cause");
    MalformedJsonException ex = new MalformedJsonException(cause);
    assertNotNull(ex);
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String message = "error message";
    MalformedJsonException ex = new MalformedJsonException(message);
    assertNotNull(ex);
    assertEquals(message, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndThrowable() {
    String message = "error message";
    Throwable cause = new Throwable("cause");
    MalformedJsonException ex = new MalformedJsonException(message, cause);
    assertNotNull(ex);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testPrivateSerialVersionUIDField() throws NoSuchFieldException, IllegalAccessException {
    var field = MalformedJsonException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long value = field.getLong(null);
    assertEquals(1L, value);
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructorReflection() throws Exception {
    Constructor<MalformedJsonException> constructor = MalformedJsonException.class.getDeclaredConstructor(Throwable.class);
    constructor.setAccessible(true);
    Throwable cause = new Throwable("cause");
    MalformedJsonException ex = constructor.newInstance(cause);
    assertNotNull(ex);
    assertEquals(cause, ex.getCause());
  }
}