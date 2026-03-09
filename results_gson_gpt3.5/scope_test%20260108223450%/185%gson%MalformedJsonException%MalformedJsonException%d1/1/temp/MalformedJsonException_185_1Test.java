package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class MalformedJsonException_185_1Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withThrowable() {
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex = new MalformedJsonException(cause);
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String msg = "error message";
    MalformedJsonException ex = new MalformedJsonException(msg);
    assertEquals(msg, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndThrowable() {
    String msg = "error message";
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex = new MalformedJsonException(msg, cause);
    assertEquals(msg, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testSerialVersionUID_field() throws NoSuchFieldException, IllegalAccessException {
    var field = MalformedJsonException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long value = field.getLong(null);
    assertEquals(1L, value);
  }
}