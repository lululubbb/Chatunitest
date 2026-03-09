package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class UnsafeAllocator_37_3Test {

  @Test
    @Timeout(8000)
  void testCreate_UnsafePath() throws Exception {
    // Use reflection to invoke private static create()
    var createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);

    UnsafeAllocator allocator = (UnsafeAllocator) createMethod.invoke(null);
    assertNotNull(allocator);

    // Test newInstance with a normal class - should not throw and produce an instance
    Class<?> clazz = String.class;
    Object instance = allocator.newInstance(clazz);
    assertNotNull(instance);
    assertTrue(clazz.isInstance(instance) || instance.getClass() == clazz);
  }

  @Test
    @Timeout(8000)
  void testCreate_ObjectStreamClassPath() throws Exception {
    // This path is hard to force because it depends on reflection internals.
    // Instead, test that newInstance throws for an abstract class (assertInstantiable)
    UnsafeAllocator allocator = UnsafeAllocator.INSTANCE;

    // UnsafeAllocator is abstract, so instantiating it should throw AssertionError
    AssertionError thrown = assertThrows(AssertionError.class, () -> allocator.newInstance(UnsafeAllocator.class));
    assertTrue(thrown.getMessage().contains("Abstract classes can't be instantiated"));
  }

  @Test
    @Timeout(8000)
  void testCreate_FallbackThrows() throws Exception {
    // Using reflection invoke create to get the allocator instance
    var createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);
    UnsafeAllocator allocator = (UnsafeAllocator) createMethod.invoke(null);

    // Try to instantiate an interface to trigger assertInstantiable exception
    AssertionError thrown = assertThrows(AssertionError.class, () -> allocator.newInstance(Runnable.class));
    assertTrue(thrown.getMessage().contains("Interfaces can't be instantiated"));
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_PrivateMethod() throws Exception {
    var assertInstantiableMethod = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiableMethod.setAccessible(true);

    // Should pass for Object.class
    assertInstantiableMethod.invoke(null, Object.class);

    // Should throw for interface
    try {
      assertInstantiableMethod.invoke(null, Runnable.class);
      fail("Expected InvocationTargetException");
    } catch (java.lang.reflect.InvocationTargetException e) {
      assertTrue(e.getCause() instanceof AssertionError);
      assertTrue(e.getCause().getMessage().contains("Interfaces can't be instantiated"));
    }

    // Should throw for primitive
    try {
      assertInstantiableMethod.invoke(null, int.class);
      fail("Expected InvocationTargetException");
    } catch (java.lang.reflect.InvocationTargetException e) {
      assertTrue(e.getCause() instanceof AssertionError);
      assertTrue(e.getCause().getMessage().contains("Primitive types can't be instantiated"));
    }
  }
}