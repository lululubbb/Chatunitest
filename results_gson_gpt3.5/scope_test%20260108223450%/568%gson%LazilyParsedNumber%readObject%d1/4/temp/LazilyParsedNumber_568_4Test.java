package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.IOException;
import java.io.ObjectStreamException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class LazilyParsedNumber_568_4Test {

  @Test
    @Timeout(8000)
  void readObject_shouldThrowInvalidObjectException() throws Exception {
    LazilyParsedNumber lpn = new LazilyParsedNumber("123");

    ObjectInputStream mockIn = Mockito.mock(ObjectInputStream.class);

    Method readObjectMethod = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObjectMethod.setAccessible(true);

    InvalidObjectException exception = assertThrows(InvalidObjectException.class, () -> {
      try {
        readObjectMethod.invoke(lpn, mockIn);
      } catch (InvocationTargetException e) {
        // unwrap the cause and rethrow if it's the expected exception
        Throwable cause = e.getCause();
        if (cause instanceof InvalidObjectException) {
          throw (InvalidObjectException) cause;
        }
        throw e;
      }
    });
  }
}