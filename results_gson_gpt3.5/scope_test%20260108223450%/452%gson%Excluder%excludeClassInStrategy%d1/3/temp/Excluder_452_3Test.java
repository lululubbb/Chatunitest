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
import java.util.Collections;

class ExcluderExcludeClassInStrategyTest {

    private Excluder excluder;

    @BeforeEach
    void setUp() {
        excluder = new Excluder();
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializeTrue_strategySkipsClass_returnsTrue() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(String.class)).thenReturn(true);

        // Use reflection to set private field serializationStrategies
        setPrivateField(excluder, "serializationStrategies", Collections.singletonList(mockStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, String.class, true);

        assertTrue(result);
        verify(mockStrategy).shouldSkipClass(String.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializeTrue_strategyDoesNotSkip_returnsFalse() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(Integer.class)).thenReturn(false);

        setPrivateField(excluder, "serializationStrategies", Collections.singletonList(mockStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Integer.class, true);

        assertFalse(result);
        verify(mockStrategy).shouldSkipClass(Integer.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializeFalse_strategySkipsClass_returnsTrue() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(Double.class)).thenReturn(true);

        setPrivateField(excluder, "deserializationStrategies", Collections.singletonList(mockStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Double.class, false);

        assertTrue(result);
        verify(mockStrategy).shouldSkipClass(Double.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_serializeFalse_strategyDoesNotSkip_returnsFalse() throws Exception {
        ExclusionStrategy mockStrategy = mock(ExclusionStrategy.class);
        when(mockStrategy.shouldSkipClass(Float.class)).thenReturn(false);

        setPrivateField(excluder, "deserializationStrategies", Collections.singletonList(mockStrategy));

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean result = (boolean) method.invoke(excluder, Float.class, false);

        assertFalse(result);
        verify(mockStrategy).shouldSkipClass(Float.class);
    }

    @Test
    @Timeout(8000)
    void excludeClassInStrategy_emptyStrategies_returnsFalse() throws Exception {
        setPrivateField(excluder, "serializationStrategies", Collections.emptyList());
        setPrivateField(excluder, "deserializationStrategies", Collections.emptyList());

        Method method = Excluder.class.getDeclaredMethod("excludeClassInStrategy", Class.class, boolean.class);
        method.setAccessible(true);

        boolean resultSerialize = (boolean) method.invoke(excluder, Object.class, true);
        boolean resultDeserialize = (boolean) method.invoke(excluder, Object.class, false);

        assertFalse(resultSerialize);
        assertFalse(resultDeserialize);
    }

    private void setPrivateField(Object target, String fieldName, Object value) throws Exception {
        java.lang.reflect.Field field = Excluder.class.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}