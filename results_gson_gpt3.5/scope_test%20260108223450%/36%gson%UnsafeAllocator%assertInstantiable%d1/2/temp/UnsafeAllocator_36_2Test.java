package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class UnsafeAllocator_36_2Test {

  @Test
    @Timeout(8000)
  void assertInstantiable_noException() throws Exception {
    // Arrange
    Class<?> clazz = String.class;

    // Act & Assert
    // Should not throw any exception
    Method assertInstantiableMethod = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiableMethod.setAccessible(true);
    assertDoesNotThrow(() -> assertInstantiableMethod.invoke(null, clazz));
  }

  @Test
    @Timeout(8000)
  void assertInstantiable_throwsAssertionError() throws Exception {
    // Arrange
    Class<?> clazz = AbstractListSubclass.class;

    Method assertInstantiableMethod = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiableMethod.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      assertInstantiableMethod.invoke(null, clazz);
    });

    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof AssertionError);
    assertTrue(cause.getMessage().startsWith("UnsafeAllocator is used for non-instantiable type:"));
  }

  // A dummy abstract class to simulate non-instantiable class for the test
  abstract static class AbstractListSubclass extends java.util.AbstractList<Object> {
    @Override
    public Object get(int index) { return null; }
    @Override
    public int size() { return 0; }
  }
}