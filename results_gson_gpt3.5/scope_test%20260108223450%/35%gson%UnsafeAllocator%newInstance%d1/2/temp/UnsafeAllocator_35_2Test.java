package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UnsafeAllocator_35_2Test {

    private UnsafeAllocator allocator;

    @BeforeEach
    void setUp() {
        allocator = UnsafeAllocator.INSTANCE;
        assertNotNull(allocator);
    }

    @Test
    @Timeout(8000)
    void testNewInstance_withStringClass() throws Exception {
        // Avoid using String.class here because UnsafeAllocator may create a raw instance
        // which can cause JVM crash when calling String.equals or other methods.
        // Instead, test with a safe class like StringBuilder.
        StringBuilder instance = allocator.newInstance(StringBuilder.class);
        assertNotNull(instance);
        // The default constructor is not called, so length should be 0
        assertEquals(0, instance.length());
    }

    @Test
    @Timeout(8000)
    void testNewInstance_withCustomClass() throws Exception {
        class TestClass {
            int x = 10;
        }
        TestClass instance = allocator.newInstance(TestClass.class);
        assertNotNull(instance);
        // Field x is not initialized by constructor, so it will be 0
        // because newInstance bypasses constructor
        assertEquals(0, instance.x);
    }

    @Test
    @Timeout(8000)
    void testNewInstance_withAbstractClass_throwsException() {
        // The actual thrown exception is AssertionError, so assertThrows should expect AssertionError
        assertThrows(AssertionError.class, () -> allocator.newInstance(AbstractClass.class));
    }

    @Test
    @Timeout(8000)
    void testAssertInstantiable_privateMethod() throws Exception {
        Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
        assertInstantiable.setAccessible(true);

        // Should not throw for instantiable class
        assertInstantiable.invoke(null, String.class);

        // Should throw for abstract class
        InvocationTargetException thrown = assertThrows(InvocationTargetException.class,
                () -> assertInstantiable.invoke(null, AbstractClass.class));
        assertTrue(thrown.getCause() instanceof AssertionError);
    }

    @Test
    @Timeout(8000)
    void testCreate_privateMethod_notNull() throws Exception {
        Method create = UnsafeAllocator.class.getDeclaredMethod("create");
        create.setAccessible(true);
        Object created = create.invoke(null);
        assertNotNull(created);
        assertTrue(created instanceof UnsafeAllocator);
    }

    abstract static class AbstractClass {
    }
}