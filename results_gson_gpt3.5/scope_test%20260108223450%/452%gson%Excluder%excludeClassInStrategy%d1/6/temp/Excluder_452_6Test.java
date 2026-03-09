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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

class Excluder_452_6Test {

    private Excluder excluder;

    @BeforeEach
    void setUp() {
        excluder = new Excluder();
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_noStrategies_returnsFalse() throws Exception {
        // By default, serializationStrategies and deserializationStrategies are empty
        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean resultSerialize = (boolean) method.invoke(excluder, String.class, true);
        boolean resultDeserialize = (boolean) method.invoke(excluder, String.class, false);

        assertFalse(resultSerialize);
        assertFalse(resultDeserialize);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_withSerializationStrategy_skipsClass() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(String.class)).thenReturn(true);
        // Set serializationStrategies list with mockStrategy
        setField(excluder, "serializationStrategies", Collections.singletonList(mockStrategy));
        setField(excluder, "deserializationStrategies", Collections.emptyList());

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, String.class, true);

        assertTrue(result);
        verify(mockStrategy, times(1)).shouldSkipClass(String.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_withSerializationStrategy_doesNotSkipClass() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(String.class)).thenReturn(false);
        setField(excluder, "serializationStrategies", Collections.singletonList(mockStrategy));
        setField(excluder, "deserializationStrategies", Collections.emptyList());

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, String.class, true);

        assertFalse(result);
        verify(mockStrategy, times(1)).shouldSkipClass(String.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_withDeserializationStrategy_skipsClass() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(Integer.class)).thenReturn(true);
        setField(excluder, "deserializationStrategies", Collections.singletonList(mockStrategy));
        setField(excluder, "serializationStrategies", Collections.emptyList());

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Integer.class, false);

        assertTrue(result);
        verify(mockStrategy, times(1)).shouldSkipClass(Integer.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_withDeserializationStrategy_doesNotSkipClass() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(Integer.class)).thenReturn(false);
        setField(excluder, "deserializationStrategies", Collections.singletonList(mockStrategy));
        setField(excluder, "serializationStrategies", Collections.emptyList());

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Integer.class, false);

        assertFalse(result);
        verify(mockStrategy, times(1)).shouldSkipClass(Integer.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_multipleStrategies_firstSkips() throws Exception {
        ExclusionStrategy skipStrategy = mock(ExclusionStrategy.class);
        ExclusionStrategy noSkipStrategy = mock(ExclusionStrategy.class);
        when(skipStrategy.shouldSkipClass(Double.class)).thenReturn(true);
        // The second strategy should not be called because first returns true
        when(noSkipStrategy.shouldSkipClass(Double.class)).thenReturn(false);

        setField(excluder, "serializationStrategies", Arrays.asList(skipStrategy, noSkipStrategy));
        setField(excluder, "deserializationStrategies", Collections.emptyList());

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Double.class, true);

        assertTrue(result);
        verify(skipStrategy, times(1)).shouldSkipClass(Double.class);
        verify(noSkipStrategy, never()).shouldSkipClass(Double.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_multipleStrategies_noneSkip() throws Exception {
        ExclusionStrategy strategy1 = mock(ExclusionStrategy.class);
        ExclusionStrategy strategy2 = mock(ExclusionStrategy.class);
        when(strategy1.shouldSkipClass(Float.class)).thenReturn(false);
        when(strategy2.shouldSkipClass(Float.class)).thenReturn(false);

        setField(excluder, "deserializationStrategies", Arrays.asList(strategy1, strategy2));
        setField(excluder, "serializationStrategies", Collections.emptyList());

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Float.class, false);

        assertFalse(result);
        verify(strategy1, times(1)).shouldSkipClass(Float.class);
        verify(strategy2, times(1)).shouldSkipClass(Float.class);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = Excluder.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}