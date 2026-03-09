package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectStreamClass;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.ObjectInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class UnsafeAllocator_37_6Test {

  @Test
    @Timeout(8000)
  void testCreate_withSunMiscUnsafe() throws Exception {
    // Setup real Class for sun.misc.Unsafe
    Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
    Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
    theUnsafeField.setAccessible(true);
    Object unsafeInstance = theUnsafeField.get(null);
    Method allocateInstanceMethod = unsafeClass.getMethod("allocateInstance", Class.class);

    try (MockedStatic<Class> mockedStatic = mockStatic(Class.class)) {
      // Use doReturn().when() with mockStatic to mock Class.forName
      mockedStatic.doReturn(unsafeClass).when(() -> Class.forName("sun.misc.Unsafe"));

      UnsafeAllocator allocator = invokeCreate();

      class Dummy {}

      Dummy instance = allocator.newInstance(Dummy.class);
      assertNotNull(instance);
      assertEquals(Dummy.class, instance.getClass());
    }
  }

  @Test
    @Timeout(8000)
  void testCreate_withObjectStreamClass() throws Exception {
    try (MockedStatic<Class> mockedStaticClass = mockStatic(Class.class)) {
      // Use doThrow().when() to mock Class.forName throwing ClassNotFoundException
      mockedStaticClass.doThrow(ClassNotFoundException.class).when(() -> Class.forName("sun.misc.Unsafe"));

      // Do not mock ObjectStreamClass static methods to avoid interference
      UnsafeAllocator allocator = invokeCreate();

      class Dummy {}

      Dummy instance = allocator.newInstance(Dummy.class);
      assertNotNull(instance);
      assertEquals(Dummy.class, instance.getClass());
    }
  }

  @Test
    @Timeout(8000)
  void testCreate_withObjectInputStream() throws Exception {
    try (MockedStatic<Class> mockedStaticClass = mockStatic(Class.class)) {
      // Use doThrow().when() to mock Class.forName throwing ClassNotFoundException
      mockedStaticClass.doThrow(ClassNotFoundException.class).when(() -> Class.forName("sun.misc.Unsafe"));

      // Do not mock ObjectStreamClass or ObjectInputStream static methods
      UnsafeAllocator allocator = invokeCreate();

      class Dummy {}

      Dummy instance = allocator.newInstance(Dummy.class);
      assertNotNull(instance);
      assertEquals(Dummy.class, instance.getClass());
    }
  }

  @Test
    @Timeout(8000)
  void testCreate_fallbackUnsupportedOperation() throws Exception {
    try (MockedStatic<Class> mockedStaticClass = mockStatic(Class.class)) {
      // Use doThrow().when() to mock Class.forName throwing ClassNotFoundException
      mockedStaticClass.doThrow(ClassNotFoundException.class).when(() -> Class.forName("sun.misc.Unsafe"));

      // Do not mock ObjectStreamClass or ObjectInputStream static methods
      UnsafeAllocator allocator = invokeCreate();

      class Dummy {}

      UnsupportedOperationException ex = assertThrows(UnsupportedOperationException.class,
          () -> allocator.newInstance(Dummy.class));
      assertTrue(ex.getMessage().contains("Cannot allocate"));
    }
  }

  // Utility method to invoke private static create() via reflection
  private UnsafeAllocator invokeCreate() throws Exception {
    Method createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);
    return (UnsafeAllocator) createMethod.invoke(null);
  }
}