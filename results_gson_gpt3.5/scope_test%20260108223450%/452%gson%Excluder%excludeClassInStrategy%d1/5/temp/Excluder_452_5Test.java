package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class ExcluderExcludeClassInStrategyTest {

    private Excluder excluder;

    @BeforeEach
    void setUp() {
        excluder = new Excluder();
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializeEmptyList_returnsFalse() throws Exception {
        setStrategies(Collections.emptyList(), Collections.emptyList());

        boolean result = invokeExcludeClassInStrategy(String.class, true);

        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_deserializeEmptyList_returnsFalse() throws Exception {
        setStrategies(Collections.emptyList(), Collections.emptyList());

        boolean result = invokeExcludeClassInStrategy(String.class, false);

        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializeStrategySkipsClass_returnsTrue() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(String.class)).thenReturn(true);
        setStrategies(Collections.singletonList(mockStrategy), Collections.emptyList());

        boolean result = invokeExcludeClassInStrategy(String.class, true);

        assertTrue(result);
        verify(mockStrategy).shouldSkipClass(String.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializeStrategyDoesNotSkipClass_returnsFalse() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(String.class)).thenReturn(false);
        setStrategies(Collections.singletonList(mockStrategy), Collections.emptyList());

        boolean result = invokeExcludeClassInStrategy(String.class, true);

        assertFalse(result);
        verify(mockStrategy).shouldSkipClass(String.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_deserializeStrategySkipsClass_returnsTrue() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(Integer.class)).thenReturn(true);
        setStrategies(Collections.emptyList(), Collections.singletonList(mockStrategy));

        boolean result = invokeExcludeClassInStrategy(Integer.class, false);

        assertTrue(result);
        verify(mockStrategy).shouldSkipClass(Integer.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_deserializeStrategyDoesNotSkipClass_returnsFalse() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(Integer.class)).thenReturn(false);
        setStrategies(Collections.emptyList(), Collections.singletonList(mockStrategy));

        boolean result = invokeExcludeClassInStrategy(Integer.class, false);

        assertFalse(result);
        verify(mockStrategy).shouldSkipClass(Integer.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_multipleStrategiesOneSkips_returnsTrue() throws Exception {
        ExclusionStrategy skipStrategy = mock(ExclusionStrategy.class);
        when(skipStrategy.shouldSkipClass(Number.class)).thenReturn(false);
        ExclusionStrategy skipStrategy2 = mock(ExclusionStrategy.class);
        when(skipStrategy2.shouldSkipClass(Number.class)).thenReturn(true);

        setStrategies(Arrays.asList(skipStrategy, skipStrategy2), Collections.emptyList());

        boolean result = invokeExcludeClassInStrategy(Number.class, true);

        assertTrue(result);
        verify(skipStrategy).shouldSkipClass(Number.class);
        verify(skipStrategy2).shouldSkipClass(Number.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_multipleStrategiesNoneSkip_returnsFalse() throws Exception {
        ExclusionStrategy s1 = mock(ExclusionStrategy.class);
        ExclusionStrategy s2 = mock(ExclusionStrategy.class);
        when(s1.shouldSkipClass(Object.class)).thenReturn(false);
        when(s2.shouldSkipClass(Object.class)).thenReturn(false);

        setStrategies(Arrays.asList(s1, s2), Collections.emptyList());

        boolean result = invokeExcludeClassInStrategy(Object.class, true);

        assertFalse(result);
        verify(s1).shouldSkipClass(Object.class);
        verify(s2).shouldSkipClass(Object.class);
    }

    // Helper to set private fields serializationStrategies and deserializationStrategies
    private void setStrategies(List<ExclusionStrategy> serialization, List<ExclusionStrategy> deserialization) {
        try {
            var serializationField = Excluder.class.getDeclaredField("serializationStrategies");
            serializationField.setAccessible(true);
            serializationField.set(excluder, serialization);

            var deserializationField = Excluder.class.getDeclaredField("deserializationStrategies");
            deserializationField.setAccessible(true);
            deserializationField.set(excluder, deserialization);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Helper to invoke private method excludeClassInStrategy
    private boolean invokeExcludeClassInStrategy(Class<?> clazz, boolean serialize) throws Exception {
        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);
        return (boolean) method.invoke(excluder, clazz, serialize);
    }
}