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

class Excluder_450_6Test {

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
        // version == IGNORE_VERSIONS disables version check branch
        // Default version is IGNORE_VERSIONS (-1.0d)
        class TestClass {}

        // Because excludeClassChecks also returns true if class is anonymous or non-static local,
        // and TestClass is a local class (non-static), the method returns true.
        // So to test the IGNORE_VERSIONS branch only, we must use a top-level class.
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, TopLevelClass.class);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_versionCheckInvalidSince() throws Exception {
        // Set version to a valid number to enable version check branch
        excluder = excluder.withVersion(1.0);

        // Create a class with @Since annotation with value > 1.0 to fail isValidVersion
        @Since(2.0)
        class SinceTooNew {}

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, SinceTooNew.class);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_versionCheckInvalidUntil() throws Exception {
        excluder = excluder.withVersion(1.0);

        // Create a class with @Until annotation with value < 1.0 to fail isValidVersion
        @Until(0.5)
        class UntilTooOld {}

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, UntilTooOld.class);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_serializeInnerClassesFalseAndIsInnerClass() throws Exception {
        excluder = excluder.disableInnerClassSerialization();

        // Inner class
        class InnerClass {}

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, InnerClass.class);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_serializeInnerClassesFalseAndNotInnerClass() throws Exception {
        excluder = excluder.disableInnerClassSerialization();

        class NotInnerClass {}

        // Use a top-level class to test not inner class
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, TopLevelClass.class);
        // Since serializeInnerClasses == false but class is not inner, branch false, so fallback to isAnonymousOrNonStaticLocal
        // TopLevelClass is not anonymous or non-static local, so expect false
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_isAnonymousOrNonStaticLocalTrue() throws Exception {
        // Use an anonymous class
        Object anonymous = new Object() {};

        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, anonymous.getClass());
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_isAnonymousOrNonStaticLocalFalse() throws Exception {
        // Use a static nested class to ensure isAnonymousOrNonStaticLocal returns false
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, StaticNestedClass.class);
        assertFalse(result);
    }

    // Helper top-level class for testing
    static class TopLevelClass {}

    // Static nested class for testing
    static class StaticNestedClass {}
}