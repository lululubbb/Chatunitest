package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class MalformedJsonException_184_3Test {

  @Test
    @Timeout(8000)
  public void testConstructor_MessageAndThrowable() {
    String message = "error message";
    Throwable cause = new RuntimeException("cause");
    MalformedJsonException ex = new MalformedJsonException(message, cause);

    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Message() {
    String message = "only message";
    MalformedJsonException ex = new MalformedJsonException(message);

    assertEquals(message, ex.getMessage());
    assertEquals(null, ex.getCause());
  }

  @Test
    @Timeout(8000)
  public void testConstructor_Throwable() {
    Throwable cause = new RuntimeException("only cause");
    MalformedJsonException ex = new MalformedJsonException(cause);

    assertEquals(cause.toString(), ex.getMessage());
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
  public void testPrivateConstructorUsingReflection() throws Exception {
    Constructor<MalformedJsonException> constructor = MalformedJsonException.class.getDeclaredConstructor(String.class, Throwable.class);
    constructor.setAccessible(true);
    String message = "reflect message";
    Throwable cause = new IllegalArgumentException("reflect cause");
    MalformedJsonException ex = constructor.newInstance(message, cause);
    assertEquals(message, ex.getMessage());
    assertEquals(cause, ex.getCause());
  }
}