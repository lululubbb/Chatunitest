package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class UnsafeAllocator_36_5Test {

  @Test
    @Timeout(8000)
  void assertInstantiable_whenCheckInstantiableReturnsNull_doesNotThrow() throws Exception {
    // Arrange
    Class<?> clazz = String.class;

    // Use reflection to get UnsafeAllocator.assertInstantiable
    Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiable.setAccessible(true);

    // Act & Assert: no exception thrown
    assertDoesNotThrow(() -> assertInstantiable.invoke(null, clazz));
  }

  @Test
    @Timeout(8000)
  void assertInstantiable_whenCheckInstantiableReturnsMessage_throwsAssertionError() throws Exception {
    // Arrange
    Class<?> nonInstantiableClass = Runnable.class;

    // Use reflection to get UnsafeAllocator.assertInstantiable
    Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiable.setAccessible(true);

    // Act & Assert: AssertionError is thrown with expected message
    InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
        () -> assertInstantiable.invoke(null, nonInstantiableClass));

    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof AssertionError);

    String expectedMessageStart = "UnsafeAllocator is used for non-instantiable type: ";
    assertTrue(cause.getMessage().startsWith(expectedMessageStart));
  }
}