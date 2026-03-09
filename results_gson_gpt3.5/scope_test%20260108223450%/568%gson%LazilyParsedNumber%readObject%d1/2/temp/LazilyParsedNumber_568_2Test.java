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

public class LazilyParsedNumber_568_2Test {

  @Test
    @Timeout(8000)
  void readObject_throwsInvalidObjectException() throws Exception {
    LazilyParsedNumber number = new LazilyParsedNumber("123");

    ObjectInputStream mockIn = Mockito.mock(ObjectInputStream.class);

    Method readObjectMethod = LazilyParsedNumber.class.getDeclaredMethod("readObject", ObjectInputStream.class);
    readObjectMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
        () -> readObjectMethod.invoke(number, mockIn));

    // Check that the cause is the expected InvalidObjectException
    assertThrows(InvalidObjectException.class, () -> { throw thrown.getCause(); });
  }
}