package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.Collections;

import com.google.gson.ExclusionStrategy;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExcluderExcludeClassTest {

    private Excluder excluder;

    @BeforeEach
    void setUp() {
        excluder = new Excluder();
    }

    @Test
    @Timeout(8000)
    void excludeClass_shouldReturnTrue_ifExcludeClassChecksTrue() throws Exception {
        Class<?> clazz = DummyClass.class;

        // Use reflection to mock excludeClassChecks to return true
        Method excludeClassChecks = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
        excludeClassChecks.setAccessible(true);

        Excluder spyExcluder = Mockito.spy(excluder);
        doReturn(true).when(spyExcluder).excludeClassChecks(clazz);
        // excludeClassInStrategy should not be called, but mock it to false anyway
        doReturn(false).when(spyExcluder).excludeClassInStrategy(clazz, true);

        boolean result = spyExcluder.excludeClass(clazz, true);

        assertTrue(result);
        verify(spyExcluder, times(1)).excludeClassChecks(clazz);
        verify(spyExcluder, never()).excludeClassInStrategy(clazz, true);
    }

    @Test
    @Timeout(8000)
    void excludeClass_shouldReturnTrue_ifExcludeClassInStrategyTrue_andExcludeClassChecksFalse() throws Exception {
        Class<?> clazz = DummyClass.class;

        Excluder spyExcluder = Mockito.spy(excluder);
        doReturn(false).when(spyExcluder).excludeClassChecks(clazz);
        doReturn(true).when(spyExcluder).excludeClassInStrategy(clazz, false);

        boolean result = spyExcluder.excludeClass(clazz, false);

        assertTrue(result);
        verify(spyExcluder, times(1)).excludeClassChecks(clazz);
        verify(spyExcluder, times(1)).excludeClassInStrategy(clazz, false);
    }

    @Test
    @Timeout(8000)
    void excludeClass_shouldReturnFalse_ifBothChecksFalse() throws Exception {
        Class<?> clazz = DummyClass.class;

        Excluder spyExcluder = Mockito.spy(excluder);
        doReturn(false).when(spyExcluder).excludeClassChecks(clazz);
        doReturn(false).when(spyExcluder).excludeClassInStrategy(clazz, true);

        boolean result = spyExcluder.excludeClass(clazz, true);

        assertFalse(result);
        verify(spyExcluder, times(1)).excludeClassChecks(clazz);
        verify(spyExcluder, times(1)).excludeClassInStrategy(clazz, true);
    }

    @Test
    @Timeout(8000)
    void excludeClass_checksWithAnonymousAndInnerClasses() throws Exception {
        // anonymous class
        Class<?> anonymousClass = new Runnable() {
            @Override
            public void run() {}
        }.getClass();

        Excluder spyExcluder = Mockito.spy(excluder);
        doReturn(false).when(spyExcluder).excludeClassInStrategy(anonymousClass, true);
        // We want excludeClassChecks to call isAnonymousOrNonStaticLocal which returns true for anonymous class
        doCallRealMethod().when(spyExcluder).excludeClassChecks(anonymousClass);

        boolean result = spyExcluder.excludeClass(anonymousClass, true);
        assertTrue(result);

        // static inner class should not cause exclusion by isAnonymousOrNonStaticLocal
        Class<?> staticInnerClass = StaticInnerClass.class;
        doReturn(false).when(spyExcluder).excludeClassInStrategy(staticInnerClass, true);
        doCallRealMethod().when(spyExcluder).excludeClassChecks(staticInnerClass);

        boolean resultStaticInner = spyExcluder.excludeClass(staticInnerClass, true);
        // It depends on isInnerClass and serializeInnerClasses flag (default true), so excludeClassChecks returns false
        assertFalse(resultStaticInner);

        // non-static inner class should cause exclusion if serializeInnerClasses is true
        Class<?> nonStaticInnerClass = NonStaticInnerClass.class;
        doReturn(false).when(spyExcluder).excludeClassInStrategy(nonStaticInnerClass, true);
        doCallRealMethod().when(spyExcluder).excludeClassChecks(nonStaticInnerClass);

        boolean resultNonStaticInner = spyExcluder.excludeClass(nonStaticInnerClass, true);
        assertTrue(resultNonStaticInner);
    }

    @Test
    @Timeout(8000)
    void excludeClass_withExclusionStrategySerialization() throws Exception {
        Class<?> clazz = DummyClass.class;

        ExclusionStrategy serializationStrategy = mock(ExclusionStrategy.class);
        when(serializationStrategy.shouldSkipClass(clazz)).thenReturn(true);

        Excluder excluderWithStrategy = new Excluder()
                .withExclusionStrategy(serializationStrategy, true, false);

        boolean result = excluderWithStrategy.excludeClass(clazz, true);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClass_withExclusionStrategyDeserialization() throws Exception {
        Class<?> clazz = DummyClass.class;

        ExclusionStrategy deserializationStrategy = mock(ExclusionStrategy.class);
        when(deserializationStrategy.shouldSkipClass(clazz)).thenReturn(true);

        Excluder excluderWithStrategy = new Excluder()
                .withExclusionStrategy(deserializationStrategy, false, true);

        boolean result = excluderWithStrategy.excludeClass(clazz, false);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClass_withExclusionStrategyNotApplied() throws Exception {
        Class<?> clazz = DummyClass.class;

        ExclusionStrategy serializationStrategy = mock(ExclusionStrategy.class);
        when(serializationStrategy.shouldSkipClass(clazz)).thenReturn(false);

        ExclusionStrategy deserializationStrategy = mock(ExclusionStrategy.class);
        when(deserializationStrategy.shouldSkipClass(clazz)).thenReturn(false);

        Excluder excluderWithStrategy = new Excluder()
                .withExclusionStrategy(serializationStrategy, true, false)
                .withExclusionStrategy(deserializationStrategy, false, true);

        boolean resultSerialize = excluderWithStrategy.excludeClass(clazz, true);
        boolean resultDeserialize = excluderWithStrategy.excludeClass(clazz, false);

        assertFalse(resultSerialize);
        assertFalse(resultDeserialize);
    }

    // Dummy classes for testing
    private static class DummyClass {}

    private static class StaticInnerClass {}

    class NonStaticInnerClass {}

}