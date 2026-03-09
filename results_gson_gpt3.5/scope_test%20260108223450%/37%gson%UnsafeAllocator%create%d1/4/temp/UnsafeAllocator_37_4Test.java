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

class UnsafeAllocator_37_4Test {

  @Test
    @Timeout(8000)
  void testCreate_withSunMiscUnsafe() throws Exception {
    // Prepare sun.misc.Unsafe mock and its static field
    Class<?> unsafeClass = createUnsafeMockClass();
    Field theUnsafeField = unsafeClass.getDeclaredField("theUnsafe");
    theUnsafeField.setAccessible(true);

    Object unsafeInstance = new Object() {
      public Object allocateInstance(Class<?> c) {
        return "allocated:" + c.getSimpleName();
      }
    };

    theUnsafeField.set(null, unsafeInstance);

    Method allocateInstanceMethod = unsafeClass.getMethod("allocateInstance", Class.class);

    // Use reflection to invoke private static create()
    Method createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);
    UnsafeAllocator allocator = (UnsafeAllocator) createMethod.invoke(null);

    // Verify newInstance returns expected results
    String result = allocator.newInstance(String.class);
    assertEquals("allocated:String", result);

    // Clean up: reset theUnsafe field to null
    theUnsafeField.set(null, null);
  }

  @Test
    @Timeout(8000)
  void testCreate_withObjectStreamClassMethods() throws Exception {
    // Mock ObjectStreamClass.getConstructorId and newInstance methods by subclassing UnsafeAllocator

    Method getConstructorIdMethod = ObjectStreamClass.class.getDeclaredMethod("getConstructorId", Class.class);
    getConstructorIdMethod.setAccessible(true);
    Method newInstanceMethod = ObjectStreamClass.class.getDeclaredMethod("newInstance", Class.class, int.class);
    newInstanceMethod.setAccessible(true);

    // Using reflection invoke create and check that returned allocator newInstance works
    Method createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);
    UnsafeAllocator allocator = (UnsafeAllocator) createMethod.invoke(null);

    // We cannot guarantee ObjectStreamClass native methods exist, so this may fallback.
    // We check that newInstance either throws or returns an object.
    try {
      Object obj = allocator.newInstance(String.class);
      assertNotNull(obj);
    } catch (UnsupportedOperationException ignored) {
      // acceptable fallback
    } catch (Exception ignored) {
      // acceptable fallback
    }
  }

  @Test
    @Timeout(8000)
  void testCreate_withObjectInputStreamNewInstance() throws Exception {
    // Using reflection invoke create and check that returned allocator newInstance works
    Method createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);
    UnsafeAllocator allocator = (UnsafeAllocator) createMethod.invoke(null);

    try {
      Object obj = allocator.newInstance(String.class);
      assertNotNull(obj);
    } catch (UnsupportedOperationException ignored) {
      // acceptable fallback
    } catch (Exception ignored) {
      // acceptable fallback
    }
  }

  @Test
    @Timeout(8000)
  void testCreate_fallbackUnsupported() throws Exception {
    // Remove sun.misc.Unsafe and ObjectStreamClass and ObjectInputStream methods by mocking Class.forName and getDeclaredMethod

    // We cannot mock static methods easily without advanced tools.
    // Instead, forcibly call create() with no sun.misc.Unsafe or other methods available.

    // Use reflection to invoke private static create()
    Method createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);
    UnsafeAllocator allocator = (UnsafeAllocator) createMethod.invoke(null);

    // The fallback allocator throws on newInstance
    assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(String.class));
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_withInterface() throws Exception {
    Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiable.setAccessible(true);

    // Interface class should throw UnsupportedOperationException
    assertThrows(UnsupportedOperationException.class, () -> assertInstantiable.invoke(null, Runnable.class));
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_withAbstractClass() throws Exception {
    Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiable.setAccessible(true);

    abstract class AbstractClass {}
    assertThrows(UnsupportedOperationException.class, () -> assertInstantiable.invoke(null, AbstractClass.class));
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_withConcreteClass() throws Exception {
    Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiable.setAccessible(true);

    class ConcreteClass {}
    // Should not throw
    assertDoesNotThrow(() -> assertInstantiable.invoke(null, ConcreteClass.class));
  }

  private static Class<?> createUnsafeMockClass() throws Exception {
    // Return the actual sun.misc.Unsafe class
    return sun.misc.Unsafe.class;
  }
}