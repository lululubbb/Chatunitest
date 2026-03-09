package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class MalformedJsonException_184_6Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithMessageAndThrowable() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex = new MalformedJsonException(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithMessage() {
    String message = "only message";
    MalformedJsonException ex = new MalformedJsonException(message);
    assertEquals(message, ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithThrowable() {
    Throwable cause = new RuntimeException("only cause");
    MalformedJsonException ex = new MalformedJsonException(cause);
    assertEquals(cause.toString(), ex.getMessage());
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
    Constructor<MalformedJsonException> constructor = MalformedJsonException.class.getDeclaredConstructor(String.class, Throwable.class);
    constructor.setAccessible(true);
    String message = "reflection message";
    Throwable cause = new IllegalArgumentException("reflection cause");
    MalformedJsonException ex = constructor.newInstance(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}