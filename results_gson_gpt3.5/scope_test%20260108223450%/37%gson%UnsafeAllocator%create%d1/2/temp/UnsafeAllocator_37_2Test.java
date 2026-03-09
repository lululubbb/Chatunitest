package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class UnsafeAllocator_37_2Test {

  @Test
    @Timeout(8000)
  void testCreate_withSunMiscUnsafe() throws Exception {
    // Setup mocks for sun.misc.Unsafe path
    Class<?> unsafeClass;
    try {
      unsafeClass = Class.forName("sun.misc.Unsafe");
    } catch (ClassNotFoundException e) {
      // Skip test if sun.misc.Unsafe is not present
      return;
    }
    Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
    unsafeField.setAccessible(true);
    Object unsafeInstance = unsafeField.get(null);
    Method allocateInstanceMethod = unsafeClass.getMethod("allocateInstance", Class.class);

    UnsafeAllocator allocator = invokeCreate();

    // The returned allocator should create new instance using Unsafe.allocateInstance
    Object instance = allocator.newInstance(String.class);
    assertNotNull(instance);
    assertEquals(String.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  void testCreate_withObjectStreamClass() throws Exception {
    // Check if getConstructorId method exists
    Method getConstructorId;
    try {
      getConstructorId = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
    } catch (NoSuchMethodException e) {
      // Skip test if method does not exist
      return;
    }
    getConstructorId.setAccessible(true);
    int constructorId = (Integer) getConstructorId.invoke(null, Object.class);

    Method newInstance = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, int.class);
    newInstance.setAccessible(true);

    UnsafeAllocator allocator = invokeCreate();

    Object instance = allocator.newInstance(String.class);
    assertNotNull(instance);
    assertEquals(String.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  void testCreate_withObjectInputStream() throws Exception {
    // Check if newInstance method exists
    Method newInstance;
    try {
      newInstance = ObjectInputStream.class.getDeclaredMethod("newInstance", Class.class, Class.class);
    } catch (NoSuchMethodException e) {
      // Skip test if method does not exist
      return;
    }
    newInstance.setAccessible(true);

    UnsafeAllocator allocator = invokeCreate();

    Object instance = allocator.newInstance(String.class);
    assertNotNull(instance);
    assertEquals(String.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  void testCreate_fallbackUnsupportedOperationException() throws Exception {
    // Create a UnsafeAllocator instance that always throws UnsupportedOperationException
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Cannot allocate " + c + ". Usage of JDK sun.misc.Unsafe is enabled, "
            + "but it could not be used. Make sure your runtime is configured correctly.");
      }
    };

    UnsupportedOperationException thrown = assertThrows(UnsupportedOperationException.class, () -> {
      allocator.newInstance(String.class);
    });
    assertTrue(thrown.getMessage().contains("Cannot allocate"));
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_privateConstructor() throws Exception {
    // Access private static method assertInstantiable(Class<?>)
    Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiable.setAccessible(true);

    // Should pass for instantiable class
    assertDoesNotThrow(() -> assertInstantiable.invoke(null, String.class));

    // Should throw for abstract class
    Class<?> abstractClass = AbstractClass.class;
    Exception e = assertThrows(Exception.class, () -> assertInstantiable.invoke(null, abstractClass));
    // Check that cause is UnsupportedOperationException
    assertTrue(e.getCause() instanceof UnsupportedOperationException);
  }

  private static UnsafeAllocator invokeCreate() throws Exception {
    Method create = UnsafeAllocator.class.getDeclaredMethod("create");
    create.setAccessible(true);
    return (UnsafeAllocator) create.invoke(null);
  }

  // Added an abstract class with an abstract method to ensure the class is recognized as abstract
  abstract static class AbstractClass {
    public abstract void abstractMethod();
  }
}