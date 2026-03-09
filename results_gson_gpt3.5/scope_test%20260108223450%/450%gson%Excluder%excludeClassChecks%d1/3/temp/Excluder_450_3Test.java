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

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class ExcluderExcludeClassChecksTest {

    private Excluder excluder;
    private Method excludeClassChecksMethod;
    private double ignoreVersionsValue;

    @BeforeEach
    void setUp() throws Exception {
        excluder = new Excluder();
        excludeClassChecksMethod = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
        excludeClassChecksMethod.setAccessible(true);

        // Access Excluder.IGNORE_VERSIONS via reflection since it's private
        Field ignoreVersionsField = Excluder.class.getDeclaredField("IGNORE_VERSIONS");
        ignoreVersionsField.setAccessible(true);
        ignoreVersionsValue = ignoreVersionsField.getDouble(null); // static field
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_versionIgnored_returnsFalse() throws Exception {
        // version == IGNORE_VERSIONS disables version check
        setField(excluder, "version", ignoreVersionsValue);
        // class with no annotations, serializeInnerClasses true by default
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, DummyClass.class);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_versionInvalidSince_returnsTrue() throws Exception {
        setField(excluder, "version", 1.0);
        // use class annotated with Since=2.0 > version 1.0 => invalid
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, Since2Class.class);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_versionInvalidUntil_returnsTrue() throws Exception {
        setField(excluder, "version", 3.0);
        // use class annotated with Until=2.0 < version 3.0 => invalid
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, Until2Class.class);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_serializeInnerClassesFalseAndInnerClass_returnsTrue() throws Exception {
        setField(excluder, "version", ignoreVersionsValue);
        setField(excluder, "serializeInnerClasses", false);
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, InnerClass.class);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_serializeInnerClassesTrueAndInnerClass_returnsFalse() throws Exception {
        setField(excluder, "version", ignoreVersionsValue);
        setField(excluder, "serializeInnerClasses", true);
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, InnerClass.class);
        // Since serializeInnerClasses is true, next check is isAnonymousOrNonStaticLocal
        // InnerClass is static nested class, so isAnonymousOrNonStaticLocal returns false
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_anonymousClass_returnsTrue() throws Exception {
        setField(excluder, "version", ignoreVersionsValue);
        setField(excluder, "serializeInnerClasses", true);
        Class<?> anonymousClass = new Runnable() {
            @Override
            public void run() {}
        }.getClass();
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, anonymousClass);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_nonStaticLocalClass_returnsTrue() throws Exception {
        setField(excluder, "version", ignoreVersionsValue);
        setField(excluder, "serializeInnerClasses", true);
        Class<?> localClass = createNonStaticLocalClass().getClass();
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, localClass);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassChecks_staticLocalClass_returnsFalse() throws Exception {
        setField(excluder, "version", ignoreVersionsValue);
        setField(excluder, "serializeInnerClasses", true);
        Class<?> localClass = createStaticLocalClass();
        boolean result = (boolean) excludeClassChecksMethod.invoke(excluder, localClass);
        assertFalse(result);
    }

    // Helpers

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        var field = Excluder.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    static class DummyClass {}

    @Since(2.0)
    static class Since2Class {}

    @Until(2.0)
    static class Until2Class {}

    static class OuterClass {
        class InnerNonStatic {}
        static class InnerStatic {}
    }

    static class InnerClass extends OuterClass.InnerStatic {}

    private Object createNonStaticLocalClass() {
        class NonStaticLocal {}
        return new NonStaticLocal();
    }

    private Class<?> createStaticLocalClass() {
        // Return static nested class's Class object directly
        return StaticLocalClass.class;
    }

    static class StaticLocalClass {}
}