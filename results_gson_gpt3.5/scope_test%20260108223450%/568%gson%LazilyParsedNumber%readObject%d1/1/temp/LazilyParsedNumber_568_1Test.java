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

public class LazilyParsedNumber_568_1Test {

  @Test
    @Timeout(8000)
  void readObject_throwsInvalidObjectException() throws Exception {
    LazilyParsedNumber instance = new LazilyParsedNumber("123");

    ObjectInputStream mockStream = Mockito.mock(ObjectInputStream.class);

    Method readObjectMethod = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObjectMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      readObjectMethod.invoke(instance, mockStream);
    });
    // Check cause is InvalidObjectException
    Throwable cause = thrown.getCause();
    if (!(cause instanceof InvalidObjectException)) {
      throw thrown;
    }
  }
}