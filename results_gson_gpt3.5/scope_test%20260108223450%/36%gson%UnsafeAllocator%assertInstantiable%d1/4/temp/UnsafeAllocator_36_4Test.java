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

import com.google.gson.internal.UnsafeAllocator;

class UnsafeAllocator_36_4Test {

  @Test
    @Timeout(8000)
  void assertInstantiable_noException() throws Exception {
    // Setup a mock for ConstructorConstructor to return null (instantiable)
    Class<?> clazz = String.class;

    // Use reflection to access private method
    Method method = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    method.setAccessible(true);

    // Should not throw any exception for instantiable class
    assertDoesNotThrow(() -> method.invoke(null, clazz));
  }

  @Test
    @Timeout(8000)
  void assertInstantiable_throwsAssertionError() throws Exception {
    // We need to mock ConstructorConstructor.checkInstantiable to return a non-null message
    // Since ConstructorConstructor is not accessible here, we create a dummy class to simulate

    // Create a dummy class that triggers exception message
    class DummyNonInstantiable {}

    // Use reflection to access private method
    Method method = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    method.setAccessible(true);

    // Use a spy or mock on ConstructorConstructor.checkInstantiable would be ideal,
    // but it's static and private in another class, so we simulate by using a custom class
    // that triggers a non-null message by overriding the method or by using a proxy.
    // Since not possible here, we rely on a known non-instantiable class:
    // For Gson, an interface or abstract class might cause non-instantiable message.
    Class<?> nonInstantiableClass = Runnable.class; // interface, should trigger message

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
        () -> method.invoke(null, nonInstantiableClass));
    Throwable cause = thrown.getCause();
    assertTrue(cause instanceof AssertionError);
    assertTrue(cause.getMessage().startsWith("UnsafeAllocator is used for non-instantiable type:"));
  }
}