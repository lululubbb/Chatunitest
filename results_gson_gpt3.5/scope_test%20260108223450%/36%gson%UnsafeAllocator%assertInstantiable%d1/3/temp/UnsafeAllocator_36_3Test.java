package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class UnsafeAllocator_36_3Test {

  @Test
    @Timeout(8000)
  void assertInstantiable_withInstantiableClass_doesNotThrow() {
    try {
      Method method = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
      method.setAccessible(true);
      // Use a class that is instantiable, e.g. String.class
      method.invoke(null, String.class);
    } catch (InvocationTargetException e) {
      // If the underlying method throws, unwrap and fail
      fail("assertInstantiable threw an exception for instantiable class: " + e.getCause());
    } catch (Exception e) {
      fail("Reflection failed: " + e);
    }
  }

  @Test
    @Timeout(8000)
  void assertInstantiable_withNonInstantiableClass_throwsAssertionError() throws Exception {
    Method method = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    method.setAccessible(true);

    // We need to simulate ConstructorConstructor.checkInstantiable returning non-null
    // Since ConstructorConstructor is not provided, use a dummy class that triggers it.
    // For example, an interface or abstract class should be non-instantiable.
    Class<?> nonInstantiableClass = Runnable.class;

    AssertionError thrown = assertThrows(AssertionError.class, () -> {
      try {
        method.invoke(null, nonInstantiableClass);
      } catch (InvocationTargetException e) {
        // unwrap and throw the cause to be caught by assertThrows
        throw e.getCause();
      }
    });

    // Check the error message contains expected text
    String message = thrown.getMessage();
    if (!message.contains("UnsafeAllocator is used for non-instantiable type:")) {
      fail("AssertionError message does not contain expected text: " + message);
    }
  }
}