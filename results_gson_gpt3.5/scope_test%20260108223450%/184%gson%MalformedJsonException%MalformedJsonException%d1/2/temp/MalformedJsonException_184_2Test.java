package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class MalformedJsonException_184_2Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithMessageAndThrowable() {
    String message = "Error message";
    Throwable cause = new RuntimeException("Cause");
    MalformedJsonException ex = new MalformedJsonException(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithMessage() {
    String message = "Only message";
    MalformedJsonException ex = new MalformedJsonException(message);
    assertEquals(message, ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructorWithThrowable() {
    Throwable cause = new RuntimeException("Only cause");
    MalformedJsonException ex = new MalformedJsonException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testPrivateSerialVersionUIDField() throws NoSuchFieldException, IllegalAccessException {
    var field = MalformedJsonException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long serialVersionUID = field.getLong(null);
    assertEquals(1L, serialVersionUID);
  }

}