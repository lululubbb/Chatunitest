package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

class Excluder_450_5Test {

    private Excluder excluder;
    private Method excludeClassChecksMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        excluder = new Excluder();
        excludeClassChecksMethod = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
        excludeClassChecksMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testExcludeClassChecks_versionIgnoreVersions() throws Exception {
        // version = IGNORE_VERSIONS disables version check, so isValidVersion is not called and returns true
        // So excludeClassChecks returns false except inner class and anonymous checks

        // Use a class with no annotations
        Class<?> clazz = String.class;

        // set version to IGNORE_VERSIONS (-1.0d)
        setField(excluder, "version", -1.0d);
        // serializeInnerClasses true by default
        setField(excluder, "serializeInnerClasses", true);

        // For String.class, isInnerClass false, isAnonymousOrNonStaticLocal false
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, clazz);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testExcludeClassChecks_versionInvalid() throws Exception {
        // version != IGNORE_VERSIONS and isValidVersion returns false => returns true

        Class<?> clazz = ClassWithSinceUntil.class;

        setField(excluder, "version", 1.0d);
        setField(excluder, "serializeInnerClasses", true);

        Excluder spyExcluder = spy(excluder);

        // Use reflection to mock private method isValidVersion
        doReturn(false).when(spyExcluder).isValidVersionReflection(any(), any());

        boolean result = (boolean) excludeClassChecksMethod.invoke(spyExcluder, clazz);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testExcludeClassChecks_serializeInnerClassesFalseAndIsInnerClassTrue() throws Exception {
        // version != IGNORE_VERSIONS and valid version, so first check false
        // serializeInnerClasses = false and isInnerClass returns true => returns true

        Class<?> clazz = InnerClass.class;

        setField(excluder, "version", 1.0d);
        setField(excluder, "serializeInnerClasses", false);

        Excluder spyExcluder = spy(excluder);

        doReturn(true).when(spyExcluder).isValidVersionReflection(any(), any());
        doReturn(true).when(spyExcluder).isInnerClassReflection(any());
        doReturn(false).when(spyExcluder).isAnonymousOrNonStaticLocalReflection(any());

        boolean result = (boolean) excludeClassChecksMethod.invoke(spyExcluder, clazz);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testExcludeClassChecks_anonymousOrNonStaticLocalTrue() throws Exception {
        // version != IGNORE_VERSIONS and valid version, serializeInnerClasses true, isInnerClass false
        // isAnonymousOrNonStaticLocal returns true => returns true

        Class<?> clazz = AnonymousClassHolder.getAnonymousClass();

        setField(excluder, "version", 1.0d);
        setField(excluder, "serializeInnerClasses", true);

        Excluder spyExcluder = spy(excluder);

        doReturn(true).when(spyExcluder).isValidVersionReflection(any(), any());
        doReturn(false).when(spyExcluder).isInnerClassReflection(any());
        doReturn(true).when(spyExcluder).isAnonymousOrNonStaticLocalReflection(any());

        boolean result = (boolean) excludeClassChecksMethod.invoke(spyExcluder, clazz);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testExcludeClassChecks_allFalse() throws Exception {
        // version != IGNORE_VERSIONS and valid version, serializeInnerClasses true, isInnerClass false
        // isAnonymousOrNonStaticLocal false => returns false

        Class<?> clazz = String.class;

        setField(excluder, "version", 1.0d);
        setField(excluder, "serializeInnerClasses", true);

        Excluder spyExcluder = spy(excluder);

        doReturn(true).when(spyExcluder).isValidVersionReflection(any(), any());
        doReturn(false).when(spyExcluder).isInnerClassReflection(any());
        doReturn(false).when(spyExcluder).isAnonymousOrNonStaticLocalReflection(any());

        boolean result = (boolean) excludeClassChecksMethod.invoke(spyExcluder, clazz);
        assertFalse(result);
    }

    private static void setField(Object obj, String fieldName, Object value) {
        try {
            java.lang.reflect.Field f = Excluder.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            f.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper class with @Since and @Until annotations
    @Since(2.0)
    @Until(3.0)
    static class ClassWithSinceUntil {
    }

    // Inner class for testing isInnerClass
    static class InnerClass {
    }

    // Class to get anonymous class for testing isAnonymousOrNonStaticLocal
    static class AnonymousClassHolder {
        static Class<?> getAnonymousClass() {
            Object anon = new Object() {
            };
            return anon.getClass();
        }
    }

    /*
     * Since the private methods cannot be mocked directly due to their private access,
     * create reflection-based proxy methods in ExcluderTest that call the private methods via reflection.
     * We will spy on these proxy methods instead.
     */

    // Add proxy methods to Excluder via reflection dynamically
    // Use a static initializer to add these proxy methods to Excluder class via a dynamic proxy or subclass.
    // Since this is not possible directly, instead create a spy subclass of Excluder with these proxy methods.

    // To do this, create a subclass of Excluder with public proxy methods to call private methods via reflection,
    // then spy on that subclass instead.

    static class ExcluderSpy extends Excluder {
        boolean isValidVersionReflection(Since since, Until until) {
            try {
                Method m = Excluder.class.getDeclaredMethod("isValidVersion", Since.class, Until.class);
                m.setAccessible(true);
                return (boolean) m.invoke(this, since, until);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        boolean isInnerClassReflection(Class<?> clazz) {
            try {
                Method m = Excluder.class.getDeclaredMethod("isInnerClass", Class.class);
                m.setAccessible(true);
                return (boolean) m.invoke(this, clazz);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }

        boolean isAnonymousOrNonStaticLocalReflection(Class<?> clazz) {
            try {
                Method m = Excluder.class.getDeclaredMethod("isAnonymousOrNonStaticLocal", Class.class);
                m.setAccessible(true);
                return (boolean) m.invoke(this, clazz);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @BeforeEach
    void setUpSpy() {
        // Replace excluder with spy of ExcluderSpy for tests that need spying
        // But only if the test needs spying. For simpler, replace excluder with ExcluderSpy instance.
        // For tests that don't need spying, excluder can be normal Excluder.

        // This method is just to illustrate; actual tests create spyExcluder from ExcluderSpy as needed.
    }
}