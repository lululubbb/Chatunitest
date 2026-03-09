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
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Excluder_452_1Test {

    private Excluder excluder;

    @BeforeEach
    void setUp() {
        excluder = new Excluder();
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_noStrategies_returnsFalse() throws Exception {
        // serializationStrategies and deserializationStrategies are empty by default
        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean resultSerialize = (boolean) method.invoke(excluder, String.class, true);
        boolean resultDeserialize = (boolean) method.invoke(excluder, String.class, false);

        assertFalse(resultSerialize);
        assertFalse(resultDeserialize);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializationStrategySkipsClass_returnsTrue() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(String.class)).thenReturn(true);

        // set serializationStrategies to list with mockStrategy
        setField(excluder, "serializationStrategies", Collections.singletonList(mockStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, String.class, true);

        assertTrue(result);
        verify(mockStrategy).shouldSkipClass(String.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializationStrategyDoesNotSkipClass_returnsFalse() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(String.class)).thenReturn(false);

        setField(excluder, "serializationStrategies", Collections.singletonList(mockStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, String.class, true);

        assertFalse(result);
        verify(mockStrategy).shouldSkipClass(String.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_deserializationStrategySkipsClass_returnsTrue() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(Integer.class)).thenReturn(true);

        setField(excluder, "deserializationStrategies", Collections.singletonList(mockStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Integer.class, false);

        assertTrue(result);
        verify(mockStrategy).shouldSkipClass(Integer.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_deserializationStrategyDoesNotSkipClass_returnsFalse() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(Integer.class)).thenReturn(false);

        setField(excluder, "deserializationStrategies", Collections.singletonList(mockStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Integer.class, false);

        assertFalse(result);
        verify(mockStrategy).shouldSkipClass(Integer.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_multipleStrategies_firstSkips_returnsTrue() throws Exception {
        ExclusionStrategy skipStrategy = mock(ExclusionStrategy.class);
        ExclusionStrategy noSkipStrategy = mock(ExclusionStrategy.class);

        when(skipStrategy.shouldSkipClass(Double.class)).thenReturn(true);
        when(noSkipStrategy.shouldSkipClass(Double.class)).thenReturn(false);

        setField(excluder, "serializationStrategies", Arrays.asList(skipStrategy, noSkipStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Double.class, true);

        assertTrue(result);
        verify(skipStrategy).shouldSkipClass(Double.class);
        verify(noSkipStrategy, never()).shouldSkipClass(any());
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_multipleStrategies_noneSkip_returnsFalse() throws Exception {
        ExclusionStrategy s1 = mock(ExclusionStrategy.class);
        ExclusionStrategy s2 = mock(ExclusionStrategy.class);

        when(s1.shouldSkipClass(Float.class)).thenReturn(false);
        when(s2.shouldSkipClass(Float.class)).thenReturn(false);

        setField(excluder, "deserializationStrategies", Arrays.asList(s1, s2));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Float.class, false);

        assertFalse(result);
        verify(s1).shouldSkipClass(Float.class);
        verify(s2).shouldSkipClass(Float.class);
    }

    // helper method to set private fields via reflection
    private void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = Excluder.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}