package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class $Gson$Preconditions_119_5Test {

  @Test
    @Timeout(8000)
  public void testCheckArgument_withTrueCondition_doesNotThrow() {
    // Should not throw any exception
    $Gson$Preconditions.checkArgument(true);
  }

  @Test
    @Timeout(8000)
  public void testCheckArgument_withFalseCondition_throwsIllegalArgumentException() {
    assertThrows(IllegalArgumentException.class, () -> {
      $Gson$Preconditions.checkArgument(false);
    });
  }

  @Test
    @Timeout(8000)
  public void testCheckArgument_viaReflection_withTrueCondition() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = $Gson$Preconditions.class.getDeclaredMethod("checkArgument", boolean.class);
    method.invoke(null, true);
  }

  @Test
    @Timeout(8000)
  public void testCheckArgument_viaReflection_withFalseCondition() throws NoSuchMethodException {
    Method method = null;
    try {
      method = $Gson$Preconditions.class.getDeclaredMethod("checkArgument", boolean.class);
      method.invoke(null, false);
    } catch (InvocationTargetException e) {
      if (!(e.getCause() instanceof IllegalArgumentException)) {
        throw new RuntimeException("Unexpected exception", e);
      }
    } catch (IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}