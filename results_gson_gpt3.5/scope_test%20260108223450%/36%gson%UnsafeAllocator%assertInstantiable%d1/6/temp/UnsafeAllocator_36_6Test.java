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

class UnsafeAllocator_36_6Test {

  @Test
    @Timeout(8000)
  void testAssertInstantiable_withInstantiableClass() throws Exception {
    Method method = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    method.setAccessible(true);

    // Using a known instantiable class (e.g., String)
    try {
      method.invoke(null, String.class);
    } catch (InvocationTargetException e) {
      // If the method throws, unwrap and fail
      fail("assertInstantiable threw an exception for instantiable class: " + e.getCause());
    }
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_withNonInstantiableClass() throws Exception {
    Method method = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    method.setAccessible(true);

    // Use an interface, which is non-instantiable and should cause checkInstantiable() to return a message.
    Class<?> nonInstantiable = Runnable.class;

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
        () -> method.invoke(null, nonInstantiable),
        "Expected assertInstantiable to throw InvocationTargetException wrapping AssertionError for non-instantiable class");

    Throwable cause = thrown.getCause();
    if (!(cause instanceof AssertionError)) {
      fail("Expected AssertionError but got " + cause);
    }
  }
}