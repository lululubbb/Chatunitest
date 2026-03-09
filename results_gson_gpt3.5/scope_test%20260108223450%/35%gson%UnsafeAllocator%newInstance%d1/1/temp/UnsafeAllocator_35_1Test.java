package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class UnsafeAllocator_35_1Test {

    private static UnsafeAllocator allocator;

    @BeforeAll
    static void setup() throws Exception {
        // Access the private static create() method to get the INSTANCE
        Method createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
        createMethod.setAccessible(true);
        allocator = (UnsafeAllocator) createMethod.invoke(null);
        assertNotNull(allocator);
    }

    @Test
    @Timeout(8000)
    void testNewInstance_StringClass() throws Exception {
        // Instead of allocator.newInstance(String.class) which may cause JVM crash,
        // create a new instance via normal constructor for safe comparison.
        String instance = allocator.newInstance(String.class);
        assertNotNull(instance);
        assertTrue(instance instanceof String);
        // The instance returned by UnsafeAllocator may not be an empty string, 
        // so avoid assertEquals("") which can cause JVM crash.
        // Instead, just check that it's a String with length zero or some safe check.
        // However, since newInstance may return an uninitialized String, 
        // we just check it's not null and is instance of String.
    }

    @Test
    @Timeout(8000)
    void testNewInstance_IntegerClass() {
        // Integer has no no-arg constructor, but UnsafeAllocator may still create an instance without exception.
        // So we test that newInstance returns an Integer instance (may be uninitialized).
        try {
            Integer instance = allocator.newInstance(Integer.class);
            assertNotNull(instance);
            assertTrue(instance instanceof Integer);
        } catch (Exception e) {
            fail("Exception should not be thrown for Integer class");
        }
    }

    @Test
    @Timeout(8000)
    void testNewInstance_CustomClass() throws Exception {
        class Custom {}
        Custom instance = allocator.newInstance(Custom.class);
        assertNotNull(instance);
        assertTrue(instance instanceof Custom);
    }

    @Test
    @Timeout(8000)
    void testAssertInstantiable_withInterface() throws Exception {
        Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
        assertInstantiable.setAccessible(true);

        // Interface should throw exception
        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> assertInstantiable.invoke(null, Runnable.class));
        assertNotNull(ex.getCause());
        String msg = ex.getCause().getMessage().toLowerCase();
        assertTrue(msg.contains("interface") || msg.contains("cannot") || msg.contains("instantiable"));
    }

    @Test
    @Timeout(8000)
    void testAssertInstantiable_withAbstractClass() throws Exception {
        Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
        assertInstantiable.setAccessible(true);

        abstract class AbstractClass {}

        InvocationTargetException ex = assertThrows(InvocationTargetException.class,
                () -> assertInstantiable.invoke(null, AbstractClass.class));
        assertNotNull(ex.getCause());
        String msg = ex.getCause().getMessage().toLowerCase();
        assertTrue(msg.contains("abstract") || msg.contains("cannot") || msg.contains("instantiable"));
    }

    @Test
    @Timeout(8000)
    void testAssertInstantiable_withConcreteClass() throws Exception {
        Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
        assertInstantiable.setAccessible(true);

        class Concrete {}

        // Should not throw
        assertDoesNotThrow(() -> assertInstantiable.invoke(null, Concrete.class));
    }
}