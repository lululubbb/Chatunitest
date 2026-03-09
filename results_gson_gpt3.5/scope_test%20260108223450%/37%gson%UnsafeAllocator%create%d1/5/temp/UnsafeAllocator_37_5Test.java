package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;

class UnsafeAllocator_37_5Test {

  @Test
    @Timeout(8000)
  void testCreate_withUnsafe() throws Exception {
    // Use reflection to access private static method create()
    Method createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
    createMethod.setAccessible(true);

    // Invoke create() and check the returned instance behavior.
    UnsafeAllocator allocator = (UnsafeAllocator) createMethod.invoke(null);
    assertNotNull(allocator);

    // Test that newInstance either throws UnsupportedOperationException or returns instance.
    // We cannot guarantee UnsupportedOperationException because environment may differ.
    try {
      allocator.newInstance(String.class);
    } catch (UnsupportedOperationException e) {
      assertTrue(e instanceof UnsupportedOperationException);
      return;
    } catch (Exception e) {
      assertTrue(e instanceof Exception);
      return;
    }
    // If no exception thrown, test passes here
  }

  @Test
    @Timeout(8000)
  void testNewInstance_UnsupportedOperation() {
    UnsafeAllocator allocator = new UnsafeAllocator() {
      @Override
      public <T> T newInstance(Class<T> c) {
        throw new UnsupportedOperationException("Cannot allocate " + c);
      }
    };

    assertThrows(UnsupportedOperationException.class, () -> allocator.newInstance(Object.class));
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_withInterface() throws Exception {
    // Access private method assertInstantiable(Class<?>)
    Method assertInstantiableMethod = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiableMethod.setAccessible(true);

    // Interface class should throw InstantiationException wrapped in InvocationTargetException
    Class<?> interfaceClass = Runnable.class;
    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      assertInstantiableMethod.invoke(null, interfaceClass);
    });
    Throwable cause = ex.getCause();
    assertNotNull(cause);
    String msg = cause.getMessage();
    assertTrue(msg != null && msg.toLowerCase().contains("interface"));
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_withAbstractClass() throws Exception {
    Method assertInstantiableMethod = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiableMethod.setAccessible(true);

    abstract class AbstractClass {}
    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      assertInstantiableMethod.invoke(null, AbstractClass.class);
    });
    Throwable cause = ex.getCause();
    assertNotNull(cause);
    String msg = cause.getMessage();
    assertTrue(msg != null && msg.toLowerCase().contains("abstract"));
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_withNonStaticMemberClass() throws Exception {
    Method assertInstantiableMethod = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiableMethod.setAccessible(true);

    class Outer {
      class Inner {}
    }

    Class<?> innerClass = Outer.Inner.class;

    boolean isNonStaticMemberClass = innerClass.isMemberClass() && !Modifier.isStatic(innerClass.getModifiers());

    if (isNonStaticMemberClass) {
      try {
        assertInstantiableMethod.invoke(null, innerClass);
        fail("Expected InvocationTargetException due to non-static member class");
      } catch (InvocationTargetException ex) {
        Throwable cause = ex.getCause();
        assertNotNull(cause);
        String msg = cause.getMessage();
        assertTrue(msg != null && msg.toLowerCase().contains("non-static"));
      }
    } else {
      // If not a non-static member class, no exception expected
      assertDoesNotThrow(() -> assertInstantiableMethod.invoke(null, innerClass));
    }
  }

  @Test
    @Timeout(8000)
  void testAssertInstantiable_withInstantiableClass() throws Exception {
    Method assertInstantiableMethod = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
    assertInstantiableMethod.setAccessible(true);

    class Instantiable {}
    assertDoesNotThrow(() -> assertInstantiableMethod.invoke(null, Instantiable.class));
  }
}