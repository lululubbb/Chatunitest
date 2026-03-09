package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import org.junit.jupiter.api.Test;

public class JsonIOException_63_5Test {

  @Test
    @Timeout(8000)
  public void testConstructor_withThrowable() {
    Throwable cause = new Throwable("cause");
    JsonIOException ex = new JsonIOException(cause);
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessage() {
    String msg = "error message";
    JsonIOException ex = new JsonIOException(msg);
    assertEquals(msg, ex.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_withMessageAndThrowable() {
    String msg = "error message";
    Throwable cause = new Throwable("cause");
    JsonIOException ex = new JsonIOException(msg, cause);
    assertEquals(msg, ex.getMessage());
    assertSame(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testSerialVersionUID() throws NoSuchFieldException, IllegalAccessException {
    Field field = JsonIOException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long serialVersionUID = field.getLong(null);
    assertEquals(1L, serialVersionUID);
  }
}