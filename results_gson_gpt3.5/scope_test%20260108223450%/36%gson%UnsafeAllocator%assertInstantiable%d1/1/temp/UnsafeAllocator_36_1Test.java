package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class UnsafeAllocator_36_1Test {

  @Test
    @Timeout(8000)
  void assertInstantiable_withInstantiableClass_doesNotThrow() throws Throwable {
    Method method = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    method.setAccessible(true);

    // Using a class that should be instantiable, e.g. String.class
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, String.class);
      } catch (InvocationTargetException e) {
        // unwrap assertion error
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  void assertInstantiable_withNonInstantiableClass_throwsAssertionError() throws Throwable {
    Method method = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    method.setAccessible(true);

    // Use a dummy class that ConstructorConstructor.checkInstantiable returns non-null for
    // We simulate this by creating a subclass of UnsafeAllocator with overridden checkInstantiable via reflection
    // But ConstructorConstructor is not accessible here, so we test with an interface (which is non-instantiable)
    Class<?> nonInstantiable = Runnable.class;

    AssertionError thrown = assertThrows(AssertionError.class, () -> {
      try {
        method.invoke(null, nonInstantiable);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    // The message should contain "UnsafeAllocator is used for non-instantiable type:"
    assert(thrown.getMessage().startsWith("UnsafeAllocator is used for non-instantiable type:"));
  }
}