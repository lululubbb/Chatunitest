package com.google.gson.stream;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MalformedJsonException_184_4Test {

  @Test
    @Timeout(8000)
  public void testConstructorWithMessageAndThrowable() {
    String message = "Error message";
    Throwable cause = new RuntimeException("Cause exception");
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
  public void testSerialVersionUIDField() throws NoSuchFieldException, IllegalAccessException {
    var field = MalformedJsonException.class.getDeclaredField("serialVersionUID");
    field.setAccessible(true);
    long serialVersionUID = field.getLong(null);
    assertEquals(1L, serialVersionUID);
  }

  @Test
    @Timeout(8000)
  public void testAllConstructorsViaReflection() throws Exception {
    Constructor<MalformedJsonException> ctorMsg = MalformedJsonException.class.getConstructor(String.class);
    Constructor<MalformedJsonException> ctorCause = MalformedJsonException.class.getConstructor(Throwable.class);
    Constructor<MalformedJsonException> ctorMsgCause = MalformedJsonException.class.getConstructor(String.class, Throwable.class);

    String msg = "msg";
    Throwable cause = new RuntimeException("cause");

    MalformedJsonException ex1 = ctorMsg.newInstance(msg);
    MalformedJsonException ex2 = ctorCause.newInstance(cause);
    MalformedJsonException ex3 = ctorMsgCause.newInstance(msg, cause);

    assertEquals(msg, ex1.getMessage());
    assertNull(ex1.getCause());

    assertEquals(cause.toString(), ex2.getMessage());
    assertEquals(cause, ex2.getCause());

    assertEquals(msg, ex3.getMessage());
    assertEquals(cause, ex3.getCause());
  }
}