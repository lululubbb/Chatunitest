package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class JsonIOException_62_4Test {

  @Test
    @Timeout(8000)
  public void testConstructor_MessageAndCause() {
    Throwable cause = new RuntimeException("cause");
    JsonIOException ex = new JsonIOException("error message", cause);
    assertEquals("error message", ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_MessageOnly() {
    JsonIOException ex = new JsonIOException("error message");
    assertEquals("error message", ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_CauseOnly() {
    Throwable cause = new RuntimeException("cause");
    JsonIOException ex = new JsonIOException(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testSerialVersionUIDField() throws NoSuchFieldException, IllegalAccessException {
    var field = JsonIOException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long serialVersionUID = field.getLong(null);
    assertEquals(1L, serialVersionUID);
  }
}