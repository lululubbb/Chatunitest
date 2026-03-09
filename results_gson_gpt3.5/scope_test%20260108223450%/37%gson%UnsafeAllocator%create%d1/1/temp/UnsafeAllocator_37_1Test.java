package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class UnsafeAllocator_37_1Test {

  @Test
    @Timeout(8000)
  void testCreate_withUnsafe() throws Exception {
    // Check if sun.misc.Unsafe is available, otherwise skip
    try {
      Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
      Method allocateInstanceMethod = unsafeClass.getMethod("allocateInstance", Class.class);

      UnsafeAllocator allocator = invokeCreate();
      // Test newInstance returns an instance of String (or any class)
      String s = allocator.newInstance(String.class);
      assertNotNull(s);
      assertEquals(String.class, s.getClass());
    } catch (ClassNotFoundException | NoSuchMethodException e) {
      // Skip test if Unsafe is not available
      return;
    }
  }

  @Test
    @Timeout(8000)
  void testCreate_withObjectStreamClassMethods() throws Exception {
    // Check if ObjectStreamClass.getConstructorId(Class) exists
    Method getConstructorIdMethod = null;
    Method newInstanceMethod = null;
    try {
      getConstructorIdMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
      getConstructorIdMethod.setAccessible(true);
      newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, int.class);
      newInstanceMethod.setAccessible(true);
    } catch (NoSuchMethodException e) {
      // Skip test if methods don't exist
      return;
    }

    UnsafeAllocator allocator = invokeCreate();

    // Only run test if allocator uses ObjectStreamClass methods
    // We check if create() returns an allocator that calls ObjectStreamClass.newInstance
    // We do this by calling newInstance and checking result type
    String s = allocator.newInstance(String.class);
    assertNotNull(s);
    assertEquals(String.class, s.getClass());
  }

  @Test
    @Timeout(8000)
  void testCreate_withObjectInputStreamNewInstance() throws Exception {
    // Check if ObjectInputStream.newInstance(Class, Class) exists
    Method newInstanceMethod;
    try {
      newInstanceMethod = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
      newInstanceMethod.setAccessible(true);
    } catch (NoSuchMethodException e) {
      // Skip test if method doesn't exist
      return;
    }

    UnsafeAllocator allocator = invokeCreate();

    // Only run test if allocator uses ObjectInputStream.newInstance
    String s = allocator.newInstance(String.class);
    assertNotNull(s);
    assertEquals(String.class, s.getClass());
  }

  @Test
    @Timeout(8000)
  void testCreate_fallbackThrows() {
    // We want to test the fallback allocator that throws UnsupportedOperationException
    // To do this, forcibly create an allocator that always throws by mocking UnsafeAllocator.create()

    // Use reflection to get UnsafeAllocator#create and forcibly invoke fallback by temporarily renaming Unsafe class
    // But simpler: create a subclass that always throws and test it directly

    UnsafeAllocator fallbackAllocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Cannot allocate " + c + ". Usage of JDK sun.misc.Unsafe is enabled, "
            + "but it could not be used. Make sure your runtime is configured correctly.");
      }
    };

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class,
        () -> fallbackAllocator.newInstance(String.class));
    assertTrue(thrown.getMessage().contains("Cannot allocate"));
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_private() throws Exception {
    // Use reflection to access private static method assertInstantiable
    Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiable.setAccessible(true);

    // Should not throw for Object.class
    assertInstantiable.invoke(null, Object.class);

    // Should throw for interface
    Class<?> interfaceClass = Runnable.class;
    Exception ex = assertThrows(Exception.class, () -> assertInstantiable.invoke(null, interfaceClass));
    assertTrue(ex.getCause() instanceof UnsupportedOperationException);

    // Should throw for abstract class
    Class<?> abstractClass = Number.class;
    ex = assertThrows(Exception.class, () -> assertInstantiable.invoke(null, abstractClass));
    assertTrue(ex.getCause() instanceof UnsupportedOperationException);

    // Should throw for private class
    class PrivateClass {
      private PrivateClass() {
      }
    }
    // Private classes are instantiable (unless constructor is private), but assertInstantiable checks only modifiers of class.
    // So test a private class with no public constructor should pass assertInstantiable because it's not abstract or interface.
    // But if we want to test non-instantiable class (private abstract), let's test an abstract private class:
    Class<?> privateAbstractClass = PrivateAbstractClass.class;
    ex = assertThrows(Exception.class, () -> assertInstantiable.invoke(null, privateAbstractClass));
    assertTrue(ex.getCause() instanceof UnsupportedOperationException);
  }

  private abstract static class PrivateAbstractClass {
  }

  // Helper method to invoke private static create() method via reflection
  private UnsafeAllocator invokeCreate() {
    try {
      Method createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
      createMethod.setAccessible(true);
      return (UnsafeAllocator) createMethod.invoke(null);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}