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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

class Excluder_451_1Test {

    private Excluder excluder;

    @BeforeEach
    void setUp() {
        excluder = new Excluder();
    }

    @Test
    @Timeout(8000)
    void excludeClass_ShouldReturnTrue_WhenExcludeClassChecksIsTrue() throws Exception {
        // Use a class that triggers excludeClassChecks to true:
        // We'll create an anonymous class which should be excluded by isAnonymousOrNonStaticLocal

        Class<?> anonClass = new Object() {}.getClass();

        // Use reflection to set serializeInnerClasses to false to trigger exclusion for inner classes
        setField(excluder, "serializeInnerClasses", false);

        boolean result = excluder.excludeClass(anonClass, true);

        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClass_ShouldReturnTrue_WhenExcludeClassInStrategyIsTrue() throws Exception {
        Class<?> clazz = String.class;

        // Prepare a mock exclusion strategy that excludes the class on serialization
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(clazz)).thenReturn(true);

        List<ExclusionStrategy> serializationStrategies = new ArrayList<>();
        serializationStrategies.add(mockStrategy);

        setField(excluder, "serializationStrategies", serializationStrategies);

        // excludeClassChecks returns false for String.class, so excludeClassInStrategy true should cause true
        boolean result = excluder.excludeClass(clazz, true);

        assertTrue(result);

        // Also test deserialization strategies
        List<ExclusionStrategy> deserializationStrategies = new ArrayList<>();
        deserializationStrategies.add(mockStrategy);

        setField(excluder, "serializationStrategies", new ArrayList<>());
        setField(excluder, "deserializationStrategies", deserializationStrategies);

        result = excluder.excludeClass(clazz, false);

        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClass_ShouldReturnFalse_WhenNeitherChecksNorStrategyExclude() throws Exception {
        Class<?> clazz = String.class;

        // No exclusion strategies
        setField(excluder, "serializationStrategies", new ArrayList<>());
        setField(excluder, "deserializationStrategies", new ArrayList<>());

        // serializeInnerClasses true so inner classes are not excluded by default
        setField(excluder, "serializeInnerClasses", true);

        boolean resultSerialize = excluder.excludeClass(clazz, true);
        boolean resultDeserialize = excluder.excludeClass(clazz, false);

        assertFalse(resultSerialize);
        assertFalse(resultDeserialize);
    }

    @Test
    @Timeout(8000)
    void excludeClass_ShouldExcludeInnerClass_WhenSerializeInnerClassesFalse() throws Exception {
        // Inner class example
        class InnerClass {}
        Class<?> innerClass = InnerClass.class;

        setField(excluder, "serializeInnerClasses", false);

        boolean result = excluder.excludeClass(innerClass, true);

        assertTrue(result);
    }

    // Helper to set private fields via reflection
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = Excluder.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}