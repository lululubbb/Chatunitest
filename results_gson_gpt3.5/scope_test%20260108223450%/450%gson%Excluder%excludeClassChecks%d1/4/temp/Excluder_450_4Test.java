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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Excluder_excludeClassChecks_Test {

    private Excluder excluder;
    private Method excludeClassChecksMethod;
    private double IGNORE_VERSIONS;

    @BeforeEach
    void setUp() throws NoSuchMethodException, NoSuchFieldException, IllegalAccessException {
        excluder = new Excluder();
        excludeClassChecksMethod = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
        excludeClassChecksMethod.setAccessible(true);

        // Access private static final IGNORE_VERSIONS field via reflection
        Field ignoreVersionsField = Excluder.class.getDeclaredField("IGNORE_VERSIONS");
        ignoreVersionsField.setAccessible(true);
        IGNORE_VERSIONS = ignoreVersionsField.getDouble(null);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_versionNotIgnoreAndInvalidVersion_returnsTrue() throws Throwable {
        // Arrange
        excluder = excluder.withVersion(1.0); // version != IGNORE_VERSIONS

        // Create a test class annotated with Since(2.0) and Until(3.0) so version 1.0 is invalid
        @Since(2.0)
        @Until(3.0)
        class TestClass {}

        // Act
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, TestClass.class);

        // Assert
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_versionIgnoreVersions_skipsVersionCheck() throws Throwable {
        // Arrange
        excluder = excluder.withVersion(IGNORE_VERSIONS); // version == IGNORE_VERSIONS

        // Create a test class annotated with Since(2.0) and Until(3.0)
        @Since(2.0)
        @Until(3.0)
        class TestClass {}

        // Act
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, TestClass.class);

        // The next checks are done, so version check skipped, so result depends on inner class and anonymous checks
        // TestClass is not inner class or anonymous, so should be false
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_serializeInnerClassesFalseAndInnerClass_returnsTrue() throws Throwable {
        // Arrange
        excluder = excluder.disableInnerClassSerialization();

        // Create a non-static inner class to test
        class Outer {
            class Inner {}
        }
        Class<?> innerClass = Outer.Inner.class;

        // Act
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, innerClass);

        // Assert
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_serializeInnerClassesTrueAndInnerClass_returnsAnonymousOrNonStaticLocalCheck() throws Throwable {
        // Arrange
        excluder = new Excluder(); // serializeInnerClasses true by default

        // Create a non-static inner class
        class Outer {
            class Inner {}
        }
        Class<?> innerClass = Outer.Inner.class;

        // Act
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, innerClass);

        // Since serializeInnerClasses is true, the check returns isAnonymousOrNonStaticLocal(innerClass)
        // Inner class is not anonymous or non-static local, so expect false
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_isAnonymousOrNonStaticLocal_returnsTrueForAnonymousClass() throws Throwable {
        // Arrange
        Class<?> anonymousClass = new Runnable() {
            @Override
            public void run() {}
        }.getClass();

        // Act
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, anonymousClass);

        // Assert
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_isAnonymousOrNonStaticLocal_returnsTrueForNonStaticLocalClass() throws Throwable {
        // Arrange
        class LocalClass {}
        Class<?> localClass = LocalClass.class;

        // Act
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, localClass);

        // Local class is non-static local, so expect true
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_isAnonymousOrNonStaticLocal_returnsFalseForStaticNestedClass() throws Throwable {
        // Arrange
        // Use a top-level static nested class instead of a static class inside a method
        Class<?> staticNestedClass = StaticNested.class;

        // Act
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, staticNestedClass);

        // Static nested class is not anonymous or non-static local, so expect false
        assertFalse(result);
    }

    // Static nested class moved outside method to avoid 'static' modifier error inside method
    static class StaticNested {}
}