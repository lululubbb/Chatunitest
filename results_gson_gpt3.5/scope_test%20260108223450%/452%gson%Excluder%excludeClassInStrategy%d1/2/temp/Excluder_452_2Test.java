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
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Excluder_452_2Test {

    private Excluder excluder;

    @BeforeEach
    void setUp() {
        excluder = new Excluder();
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_noStrategies_returnsFalse() throws Exception {
        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        // serializationStrategies and deserializationStrategies are empty lists by default
        boolean resultSerialize = (boolean) method.invoke(excluder, String.class, true);
        boolean resultDeserialize = (boolean) method.invoke(excluder, String.class, false);

        assertFalse(resultSerialize);
        assertFalse(resultDeserialize);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializationStrategy_skipsClass_returnsTrue() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(String.class)).thenReturn(true);

        setField(excluder, "serializationStrategies", Collections.singletonList(mockStrategy));
        setField(excluder, "deserializationStrategies", Collections.emptyList());

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, String.class, true);
        assertTrue(result);

        // verify that shouldSkipClass was called
        verify(mockStrategy).shouldSkipClass(String.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializationStrategy_doesNotSkipClass_returnsFalse() throws Exception {
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
    void excludeClassInStrategy_deserializationStrategy_skipsClass_returnsTrue() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(Integer.class)).thenReturn(true);

        setField(excluder, "deserializationStrategies", Collections.singletonList(mockStrategy));
        setField(excluder, "serializationStrategies", Collections.emptyList());

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Integer.class, false);
        assertTrue(result);

        verify(mockStrategy).shouldSkipClass(Integer.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_multipleStrategies_firstSkips_returnsTrue() throws Exception {
        ExclusionStrategy skipStrategy = mock(ExclusionStrategy.class);
        ExclusionStrategy noSkipStrategy = mock(ExclusionStrategy.class);

        when(skipStrategy.shouldSkipClass(Long.class)).thenReturn(true);
        when(noSkipStrategy.shouldSkipClass(Long.class)).thenReturn(false);

        setField(excluder, "serializationStrategies", Arrays.asList(skipStrategy, noSkipStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Long.class, true);
        assertTrue(result);

        verify(skipStrategy).shouldSkipClass(Long.class);
        // noSkipStrategy.shouldSkipClass should not be called because first strategy returns true
        verify(noSkipStrategy, never()).shouldSkipClass(any());
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_multipleStrategies_noneSkip_returnsFalse() throws Exception {
        ExclusionStrategy s1 = mock(ExclusionStrategy.class);
        ExclusionStrategy s2 = mock(ExclusionStrategy.class);

        when(s1.shouldSkipClass(Double.class)).thenReturn(false);
        when(s2.shouldSkipClass(Double.class)).thenReturn(false);

        setField(excluder, "deserializationStrategies", Arrays.asList(s1, s2));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Double.class, false);
        assertFalse(result);

        verify(s1).shouldSkipClass(Double.class);
        verify(s2).shouldSkipClass(Double.class);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = Excluder.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}