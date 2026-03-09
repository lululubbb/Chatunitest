package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.Since;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class Excluder_457_6Test {

    private Excluder excluder;
    private Method isValidSinceMethod;

    @BeforeEach
    void setUp() throws Exception {
        excluder = new Excluder();
        isValidSinceMethod = Excluder.class.getDeclaredMethod("isValidSince", Since.class);
        isValidSinceMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_NullAnnotation_ReturnsTrue() throws Exception {
        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, new Object[]{null});
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_VersionLessThanAnnotationVersion_ReturnsFalse() throws Exception {
        // Set version lower than annotation value
        Excluder excluderWithVersion = excluder.withVersion(1.0);
        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(2.0);

        Boolean result = (Boolean) isValidSinceMethod.invoke(excluderWithVersion, annotation);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_VersionEqualToAnnotationVersion_ReturnsTrue() throws Exception {
        Excluder excluderWithVersion = excluder.withVersion(2.0);
        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(2.0);

        Boolean result = (Boolean) isValidSinceMethod.invoke(excluderWithVersion, annotation);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_VersionGreaterThanAnnotationVersion_ReturnsTrue() throws Exception {
        Excluder excluderWithVersion = excluder.withVersion(3.0);
        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(2.0);

        Boolean result = (Boolean) isValidSinceMethod.invoke(excluderWithVersion, annotation);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_VersionIgnore_ReturnsTrue() throws Exception {
        // IGNORE_VERSIONS is -1.0, so if version is -1.0 and annotation value > 0, result should be false
        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(0.5);

        // Use default excluder which has version = -1.0 (IGNORE_VERSIONS)
        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, annotation);
        // Since version = -1.0 < annotation value 0.5, expect false
        assertFalse(result);
    }
}