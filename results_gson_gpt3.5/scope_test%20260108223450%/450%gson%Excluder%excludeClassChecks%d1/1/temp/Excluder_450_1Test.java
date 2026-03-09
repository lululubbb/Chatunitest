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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class Excluder_450_1Test {

    private Excluder excluder;
    private Method excludeClassChecksMethod;

    @BeforeEach
    void setUp() throws Exception {
        excluder = new Excluder();
        excludeClassChecksMethod = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
        excludeClassChecksMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_versionIgnoreVersions() throws Exception {
        // version == IGNORE_VERSIONS (-1.0), so version check returns false, skips first if
        // serializeInnerClasses == true, so second if skips
        // returns isAnonymousOrNonStaticLocal(clazz)

        // Use a normal class (not anonymous or local) to test final return false
        Class<?> clazz = NormalClass.class;

        // Default version is IGNORE_VERSIONS
        // serializeInnerClasses default true
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, clazz);
        // NormalClass is not anonymous or non-static local, so should return false
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_versionInvalidSince() throws Exception {
        // Setup version != IGNORE_VERSIONS to trigger version check
        excluder = excluder.withVersion(1.0);

        // Create a class with @Since annotation with value greater than 1.0 (invalid)
        Class<?> clazz = SinceInvalidClass.class;

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, clazz);
        // Since value 2.0 > version 1.0 => invalid version => excludeClassChecks returns true
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_versionInvalidUntil() throws Exception {
        // Setup version != IGNORE_VERSIONS to trigger version check
        excluder = excluder.withVersion(1.5);

        // Create a class with @Until annotation with value less than version (invalid)
        Class<?> clazz = UntilInvalidClass.class;

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, clazz);
        // Until value 1.0 < version 1.5 => invalid version => excludeClassChecks returns true
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_serializeInnerClassesFalseAndInnerClass() throws Exception {
        // Setup serializeInnerClasses = false
        excluder = excluder.disableInnerClassSerialization();

        // Use a non-static inner class to test second if condition
        Class<?> clazz = OuterClass.NonStaticInner.class;

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, clazz);
        // Because serializeInnerClasses == false and class is inner class => returns true
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_serializeInnerClassesFalseAndStaticInnerClass() throws Exception {
        // Setup serializeInnerClasses = false
        excluder = excluder.disableInnerClassSerialization();

        // Use a static inner class to test second if condition (should not exclude)
        Class<?> clazz = OuterClass.StaticInner.class;

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, clazz);
        // Static inner class is not excluded by second if, so returns isAnonymousOrNonStaticLocal(clazz)
        // StaticInner is not anonymous or non-static local, so expect false
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_anonymousClass() throws Exception {
        // Use anonymous class to test last return
        Class<?> clazz = new Runnable() {
            @Override
            public void run() {
            }
        }.getClass();

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, clazz);
        // anonymous class returns true from isAnonymousOrNonStaticLocal, so excludeClassChecks returns true
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_nonStaticLocalClass() throws Exception {
        // Create a local class inside this method (non-static local class)
        class LocalClass {
        }
        Class<?> clazz = LocalClass.class;

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, clazz);
        // LocalClass is non-static local class, so isAnonymousOrNonStaticLocal returns true => excludeClassChecks returns true
        assertTrue(result);
    }

    // Helper classes for tests

    @Since(2.0)
    static class SinceInvalidClass {
    }

    @Until(1.0)
    static class UntilInvalidClass {
    }

    static class NormalClass {
    }

    static class OuterClass {
        class NonStaticInner {
        }

        static class StaticInner {
        }
    }
}