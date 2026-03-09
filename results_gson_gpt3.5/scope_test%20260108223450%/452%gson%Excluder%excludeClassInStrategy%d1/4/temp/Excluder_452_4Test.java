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
    void excludeClassInStrategy_strategySkipsClass_returnsTrue() throws Exception {
        ExclusionStrategy skipStrategy = mock(ExclusionStrategy.class);
        when(skipStrategy.shouldSkipClass(String.class)).thenReturn(true);

        // Set serializationStrategies to list containing skipStrategy
        setField(excluder, "serializationStrategies", Collections.singletonList(skipStrategy));
        setField(excluder, "deserializationStrategies", Collections.emptyList());

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean resultSerialize = (boolean) method.invoke(excluder, String.class, true);
        assertTrue(resultSerialize);

        // For deserialization, no strategies so should be false
        boolean resultDeserialize = (boolean) method.invoke(excluder, String.class, false);
        assertFalse(resultDeserialize);

        // Now test deserializationStrategies with skip
        setField(excluder, "deserializationStrategies", Collections.singletonList(skipStrategy));
        setField(excluder, "serializationStrategies", Collections.emptyList());

        boolean resultDeserialize2 = (boolean) method.invoke(excluder, String.class, false);
        assertTrue(resultDeserialize2);

        boolean resultSerialize2 = (boolean) method.invoke(excluder, String.class, true);
        assertFalse(resultSerialize2);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_strategyDoesNotSkipClass_returnsFalse() throws Exception {
        ExclusionStrategy noSkipStrategy = mock(ExclusionStrategy.class);
        when(noSkipStrategy.shouldSkipClass(String.class)).thenReturn(false);

        setField(excluder, "serializationStrategies", Collections.singletonList(noSkipStrategy));
        setField(excluder, "deserializationStrategies", Collections.singletonList(noSkipStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean resultSerialize = (boolean) method.invoke(excluder, String.class, true);
        boolean resultDeserialize = (boolean) method.invoke(excluder, String.class, false);

        assertFalse(resultSerialize);
        assertFalse(resultDeserialize);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_multipleStrategies_firstSkips_returnsTrue() throws Exception {
        ExclusionStrategy skipStrategy = mock(ExclusionStrategy.class);
        when(skipStrategy.shouldSkipClass(String.class)).thenReturn(true);

        ExclusionStrategy noSkipStrategy = mock(ExclusionStrategy.class);
        when(noSkipStrategy.shouldSkipClass(String.class)).thenReturn(false);

        List<ExclusionStrategy> serializationStrategies = Arrays.asList(skipStrategy, noSkipStrategy);
        List<ExclusionStrategy> deserializationStrategies = Arrays.asList(noSkipStrategy, skipStrategy);

        setField(excluder, "serializationStrategies", serializationStrategies);
        setField(excluder, "deserializationStrategies", deserializationStrategies);

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        // For serialization, first strategy skips, so true
        boolean resultSerialize = (boolean) method.invoke(excluder, String.class, true);
        assertTrue(resultSerialize);

        // For deserialization, first strategy does not skip, second does, so true
        boolean resultDeserialize = (boolean) method.invoke(excluder, String.class, false);
        assertTrue(resultDeserialize);
    }

    private void setField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = Excluder.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}