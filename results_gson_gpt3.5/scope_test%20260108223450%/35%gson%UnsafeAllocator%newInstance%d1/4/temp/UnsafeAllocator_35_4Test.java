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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnsafeAllocator_35_4Test {

    private UnsafeAllocator allocator;

    @BeforeEach
    void setUp() throws Exception {
        // Use reflection to invoke private static create() method to get INSTANCE
        Method createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
        createMethod.setAccessible(true);
        allocator = (UnsafeAllocator) createMethod.invoke(null);
        assertNotNull(allocator);
    }

    @Test
    @Timeout(8000)
    void testNewInstance_PrimitiveWrapper() throws Exception {
        // Instantiate a class with no-arg constructor (e.g. StringBuilder)
        StringBuilder instance = allocator.newInstance(StringBuilder.class);
        assertNotNull(instance);
        assertEquals(StringBuilder.class, instance.getClass());
    }

    @Test
    @Timeout(8000)
    void testNewInstance_CustomClass() throws Exception {
        // Custom class without no-arg constructor
        class NoNoArgConstructor {
            private final int x;
            NoNoArgConstructor(int x) { this.x = x; }
        }
        NoNoArgConstructor obj = allocator.newInstance(NoNoArgConstructor.class);
        assertNotNull(obj);
        assertEquals(NoNoArgConstructor.class, obj.getClass());
    }

    @Test
    @Timeout(8000)
    void testNewInstance_Interface() {
        // Trying to instantiate interface should fail assertInstantiable
        Throwable ex = assertThrows(Throwable.class, () -> allocator.newInstance(Runnable.class));
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("cannot be instantiated"));
    }

    @Test
    @Timeout(8000)
    void testNewInstance_AbstractClass() {
        // Trying to instantiate abstract class should fail assertInstantiable
        abstract class AbstractClass {}
        Throwable ex = assertThrows(Throwable.class, () -> allocator.newInstance(AbstractClass.class));
        assertNotNull(ex.getMessage());
        assertTrue(ex.getMessage().toLowerCase().contains("cannot be instantiated"));
    }

    @Test
    @Timeout(8000)
    void testAssertInstantiable_PrivateMethod() throws Exception {
        Method assertInstantiableMethod = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
        assertInstantiableMethod.setAccessible(true);

        // Should not throw for concrete class
        assertDoesNotThrow(() -> {
            try {
                assertInstantiableMethod.invoke(null, String.class);
            } catch (InvocationTargetException e) {
                // unwrap and rethrow cause if present
                throw e.getCause();
            }
        });

        // Should throw for interface
        Throwable cause = assertThrows(Throwable.class, () -> {
            try {
                assertInstantiableMethod.invoke(null, Runnable.class);
            } catch (InvocationTargetException e) {
                throw e.getCause();
            }
        });
        assertNotNull(cause.getMessage());
        assertTrue(cause.getMessage().toLowerCase().contains("cannot be instantiated"));
    }

    @Test
    @Timeout(8000)
    void testCreate_PrivateMethod() throws Exception {
        Method createMethod = UnsafeAllocator.class.getDeclaredMethod("create");
        createMethod.setAccessible(true);
        UnsafeAllocator createdInstance = (UnsafeAllocator) createMethod.invoke(null);
        assertNotNull(createdInstance);
        // Should be same class as INSTANCE
        assertEquals(UnsafeAllocator.INSTANCE.getClass(), createdInstance.getClass());
    }
}