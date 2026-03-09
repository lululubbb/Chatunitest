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
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
import java.util.Collections;

class Excluder_451_2Test {

    private Excluder excluder;

    @BeforeEach
    void setUp() {
        excluder = new Excluder();
    }

    @Test
    @Timeout(8000)
    void excludeClass_excludeClassChecksTrue() throws Exception {
        // Prepare a class that will cause excludeClassChecks to return true
        Class<?> clazz = AnonymousClass.class;

        // Use reflection to set serializeInnerClasses to false to trigger excludeClassChecks true for inner class
        setField(excluder, "serializeInnerClasses", false);

        // anonymous or non-static local class returns true in excludeClassChecks
        Class<?> anonClass = new Object() {}.getClass();

        // excludeClassChecks should return true for anonymous or non-static local class
        Method excludeClassChecks = getPrivateMethod("excludeClassChecks", Class.class);
        boolean result1 = (boolean) excludeClassChecks.invoke(excluder, anonClass);
        assertTrue(result1);

        // excludeClassChecks should return true for inner class when serializeInnerClasses is false
        boolean result2 = (boolean) excludeClassChecks.invoke(excluder, clazz);
        assertTrue(result2);

        // excludeClass should return true if excludeClassChecks returns true, no matter excludeClassInStrategy
        boolean excludeClassResult = excluder.excludeClass(clazz, true);
        assertTrue(excludeClassResult);
    }

    @Test
    @Timeout(8000)
    void excludeClass_excludeClassChecksFalse_excludeClassInStrategyTrue() throws Exception {
        Class<?> clazz = String.class;

        Excluder spyExcluder = Mockito.spy(excluder);

        // Mock private method excludeClassChecks to return false
        doReturn(false).when(spyExcluder).excludeClassChecks(clazz);

        // Mock private method excludeClassInStrategy to return true
        doReturn(true).when(spyExcluder).excludeClassInStrategy(clazz, true);

        boolean result = spyExcluder.excludeClass(clazz, true);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClass_excludeClassChecksFalse_excludeClassInStrategyFalse() throws Exception {
        Class<?> clazz = Integer.class;

        Excluder spyExcluder = Mockito.spy(excluder);

        doReturn(false).when(spyExcluder).excludeClassChecks(clazz);

        doReturn(false).when(spyExcluder).excludeClassInStrategy(clazz, false);

        boolean result = spyExcluder.excludeClass(clazz, false);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializationTrue_strategyReturnsTrue() throws Exception {
        ExclusionStrategy strategy = mock(ExclusionStrategy.class);
        when(strategy.shouldSkipClass(String.class)).thenReturn(true);

        setField(excluder, "serializationStrategies", Collections.singletonList(strategy));
        setField(excluder, "deserializationStrategies", Collections.emptyList());

        Method excludeClassInStrategy = getPrivateMethod("excludeClassInStrategy", Class.class, boolean.class);

        boolean result = (boolean) excludeClassInStrategy.invoke(excluder, String.class, true);
        assertTrue(result);
        verify(strategy).shouldSkipClass(String.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializationTrue_strategyReturnsFalse() throws Exception {
        ExclusionStrategy strategy = mock(ExclusionStrategy.class);
        when(strategy.shouldSkipClass(Integer.class)).thenReturn(false);

        setField(excluder, "serializationStrategies", Collections.singletonList(strategy));
        setField(excluder, "deserializationStrategies", Collections.emptyList());

        Method excludeClassInStrategy = getPrivateMethod("excludeClassInStrategy", Class.class, boolean.class);

        boolean result = (boolean) excludeClassInStrategy.invoke(excluder, Integer.class, true);
        assertFalse(result);
        verify(strategy).shouldSkipClass(Integer.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_deserializationTrue_strategyReturnsTrue() throws Exception {
        ExclusionStrategy strategy = mock(ExclusionStrategy.class);
        when(strategy.shouldSkipClass(Double.class)).thenReturn(true);

        setField(excluder, "serializationStrategies", Collections.emptyList());
        setField(excluder, "deserializationStrategies", Collections.singletonList(strategy));

        Method excludeClassInStrategy = getPrivateMethod("excludeClassInStrategy", Class.class, boolean.class);

        boolean result = (boolean) excludeClassInStrategy.invoke(excluder, Double.class, false);
        assertTrue(result);
        verify(strategy).shouldSkipClass(Double.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_deserializationTrue_strategyReturnsFalse() throws Exception {
        ExclusionStrategy strategy = mock(ExclusionStrategy.class);
        when(strategy.shouldSkipClass(Float.class)).thenReturn(false);

        setField(excluder, "serializationStrategies", Collections.emptyList());
        setField(excluder, "deserializationStrategies", Collections.singletonList(strategy));

        Method excludeClassInStrategy = getPrivateMethod("excludeClassInStrategy", Class.class, boolean.class);

        boolean result = (boolean) excludeClassInStrategy.invoke(excluder, Float.class, false);
        assertFalse(result);
        verify(strategy).shouldSkipClass(Float.class);
    }

    // Helper class for inner class test
    static class AnonymousClass {}

    private void setField(Object obj, String fieldName, Object value) {
        try {
            Field field = Excluder.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Method getPrivateMethod(String name, Class<?>... parameterTypes) {
        try {
            Method method = Excluder.class.getDeclaredMethod(name, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}