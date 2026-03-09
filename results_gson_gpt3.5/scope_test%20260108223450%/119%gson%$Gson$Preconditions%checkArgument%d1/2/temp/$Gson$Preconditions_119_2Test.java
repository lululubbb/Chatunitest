package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class $Gson$Preconditions_119_2Test {

  @Test
    @Timeout(8000)
  public void testCheckArgument_conditionTrue_noException() {
    // Should not throw any exception
    $Gson$Preconditions.checkArgument(true);
  }

  @Test
    @Timeout(8000)
  public void testCheckArgument_conditionFalse_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Preconditions.checkArgument(false);
    });
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructor_invocation() throws Exception {
    Constructor<$Gson$Preconditions> constructor = $Gson$Preconditions.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    // The constructor throws UnsupportedOperationException wrapped in InvocationTargetException
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      constructor.newInstance();
    });
    // Assert the cause is UnsupportedOperationException
    assertThrows(UnsupportedOperationException.class, () -> {
      throw thrown.getCause();
    });
  }
}