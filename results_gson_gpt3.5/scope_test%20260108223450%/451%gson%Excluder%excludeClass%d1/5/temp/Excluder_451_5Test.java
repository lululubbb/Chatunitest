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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;

class Excluder_451_5Test {

    private Excluder excluder;

    @BeforeEach
    void setUp() {
        excluder = new Excluder();
    }

    @Test
    @Timeout(8000)
    void excludeClass_excludeClassChecksTrue() throws Exception {
        // Use reflection to get the private excludeClassChecks method
        Method excludeClassChecksMethod = Excluder.class.getDeclaredMethod("excludeClassChecks", Class.class);
        excludeClassChecksMethod.setAccessible(true);

        // We test excludeClassChecks on anonymous class which should return true
        boolean excludeClassChecksResult = (boolean) excludeClassChecksMethod.invoke(excluder, new Object(){}.getClass());
        assertTrue(excludeClassChecksResult);

        // Now test excludeClass for anonymous class, excludeClass should return true because excludeClassChecks returns true
        boolean result = excluder.excludeClass(new Object(){}.getClass(), true);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void excludeClass_excludeClassChecksFalse_excludeClassInStrategyTrue() throws Exception {
        // Use reflection to create Excluder instance with serializationStrategies or deserializationStrategies that return true for shouldSkipClass

        ExclusionStrategy strategy = mock(ExclusionStrategy.class);
        when(strategy.shouldSkipClass(String.class)).thenReturn(true);

        setField(excluder, "serializationStrategies", Collections.singletonList(strategy));
        setField(excluder, "deserializationStrategies", Collections.singletonList(strategy));

        // excludeClassChecks returns false for String.class normally
        // excludeClass should return true because excludeClassInStrategy returns true

        boolean resultSerialize = excluder.excludeClass(String.class, true);
        boolean resultDeserialize = excluder.excludeClass(String.class, false);

        // Because strategy returns true for shouldSkipClass, excludeClassInStrategy returns true for both serialize true or false
        assertTrue(resultSerialize);
        assertTrue(resultDeserialize);
    }

    @Test
    @Timeout(8000)
    void excludeClass_excludeClassChecksFalse_excludeClassInStrategyFalse() throws Exception {
        // Use reflection to create Excluder instance with serializationStrategies and deserializationStrategies that return false for shouldSkipClass

        ExclusionStrategy strategy = mock(ExclusionStrategy.class);
        when(strategy.shouldSkipClass(String.class)).thenReturn(false);

        setField(excluder, "serializationStrategies", Collections.singletonList(strategy));
        setField(excluder, "deserializationStrategies", Collections.singletonList(strategy));

        // excludeClassChecks returns false for String.class normally
        // excludeClassInStrategy returns false because strategy returns false

        boolean resultSerialize = excluder.excludeClass(String.class, true);
        boolean resultDeserialize = excluder.excludeClass(String.class, false);

        assertFalse(resultSerialize);
        assertFalse(resultDeserialize);
    }

    @Test
    @Timeout(8000)
    void excludeClass_withRealExcludeClassChecksAndExcludeClassInStrategy() throws Exception {
        // Use reflection to set serializationStrategies and deserializationStrategies to lists containing mock ExclusionStrategy

        ExclusionStrategy serializationStrategy = mock(ExclusionStrategy.class);
        when(serializationStrategy.shouldSkipClass(String.class)).thenReturn(false);
        ExclusionStrategy deserializationStrategy = mock(ExclusionStrategy.class);
        when(deserializationStrategy.shouldSkipClass(String.class)).thenReturn(true);

        setField(excluder, "serializationStrategies", Collections.singletonList(serializationStrategy));
        setField(excluder, "deserializationStrategies", Collections.singletonList(deserializationStrategy));

        // Test serialize = true, excludeClassInStrategy should return false (serializationStrategy returns false)
        boolean serializeResult = excluder.excludeClass(String.class, true);
        // excludeClassChecks returns false for String.class (real method)
        // excludeClassInStrategy returns false because serializationStrategy.shouldSkipClass returns false
        assertFalse(serializeResult);

        // Test serialize = false, excludeClassInStrategy should return true (deserializationStrategy returns true)
        boolean deserializeResult = excluder.excludeClass(String.class, false);
        assertTrue(deserializeResult);
    }

    // Helper method to set private fields via reflection
    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = Excluder.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}