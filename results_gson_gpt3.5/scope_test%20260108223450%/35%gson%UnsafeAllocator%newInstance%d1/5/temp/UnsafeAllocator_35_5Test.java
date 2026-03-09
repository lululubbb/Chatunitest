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

public class UnsafeAllocator_35_5Test {

    private UnsafeAllocator unsafeAllocator;

    @BeforeEach
    public void setUp() throws Exception {
        // Access the static INSTANCE field
        unsafeAllocator = UnsafeAllocator.INSTANCE;
        assertNotNull(unsafeAllocator);
    }

    @Test
    @Timeout(8000)
    public void testNewInstance_String() throws Exception {
        // Since UnsafeAllocator.newInstance(String.class) returns an instance created without constructor,
        // it may not be a valid String instance (empty string literal), so avoid calling equals on it.
        // Instead, just check it's an instance of String and not null.
        Object instance = unsafeAllocator.newInstance(String.class);
        assertNotNull(instance);
        assertTrue(instance instanceof String);
        // Do not call equals or other String methods to avoid JVM crash.
    }

    @Test
    @Timeout(8000)
    public void testNewInstance_Object() throws Exception {
        Object instance = unsafeAllocator.newInstance(Object.class);
        assertNotNull(instance);
    }

    @Test
    @Timeout(8000)
    public void testNewInstance_CustomClass() throws Exception {
        class CustomClass {
            int x = 5;
        }
        CustomClass instance = unsafeAllocator.newInstance(CustomClass.class);
        assertNotNull(instance);
        // The field 'x' is not initialized because constructor is not called, so default 0 expected
        assertEquals(0, instance.x);
    }

    @Test
    @Timeout(8000)
    public void testNewInstance_AbstractClass() {
        abstract class AbstractClass {}
        Throwable throwable = assertThrows(Throwable.class, () -> {
            unsafeAllocator.newInstance(AbstractClass.class);
        });
        // The UnsafeAllocator throws AssertionError for abstract classes, so accept AssertionError or Exception
        assertTrue(throwable instanceof AssertionError || throwable instanceof Exception);
        assertNotNull(throwable.getMessage());
    }

    @Test
    @Timeout(8000)
    public void testAssertInstantiable_PrivateMethod() throws Exception {
        Method assertInstantiable = UnsafeAllocator.class.getDeclaredMethod("assertInstantiable", Class.class);
        assertInstantiable.setAccessible(true);

        // Should not throw
        assertInstantiable.invoke(null, Object.class);

        // Should throw for interface
        Throwable ex = assertThrows(InvocationTargetException.class, () -> {
            assertInstantiable.invoke(null, Runnable.class);
        });
        // Accept either UnsupportedOperationException or AssertionError (depending on implementation)
        assertTrue(
            ex.getCause() instanceof UnsupportedOperationException ||
            ex.getCause() instanceof AssertionError
        );

        // Should throw for abstract class
        abstract class AbstractClass {}
        ex = assertThrows(InvocationTargetException.class, () -> {
            assertInstantiable.invoke(null, AbstractClass.class);
        });
        assertTrue(
            ex.getCause() instanceof UnsupportedOperationException ||
            ex.getCause() instanceof AssertionError
        );
    }

    @Test
    @Timeout(8000)
    public void testCreate_PrivateMethod() throws Exception {
        Method create = UnsafeAllocator.class.getDeclaredMethod("create");
        create.setAccessible(true);
        UnsafeAllocator allocator = (UnsafeAllocator) create.invoke(null);
        assertNotNull(allocator);
        Object obj = allocator.newInstance(Object.class);
        assertNotNull(obj);
    }
}