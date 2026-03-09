package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class MalformedJsonException_184_1Test {

  @Test
    @Timeout(8000)
  public void testConstructor_MessageAndThrowable() {
    String message = "Error message";
    Throwable cause = new RuntimeException("Cause exception");
    MalformedJsonException ex = new MalformedJsonException(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_MessageOnly() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<MalformedJsonException> constructor = MalformedJsonException.class.getDeclaredConstructor(String.class);
    constructor.setAccessible(true);
    String message = "Only message";
    MalformedJsonException ex = constructor.newInstance(message);
    assertEquals(message, ex.getMessage());
    assertNull(ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_ThrowableOnly() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<MalformedJsonException> constructor = MalformedJsonException.class.getDeclaredConstructor(Throwable.class);
    constructor.setAccessible(true);
    Throwable cause = new RuntimeException("Only cause");
    MalformedJsonException ex = constructor.newInstance(cause);
    assertEquals(cause.toString(), ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}