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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class Excluder_457_2Test {

    private Excluder excluder;
    private Method isValidSinceMethod;

    @BeforeEach
    void setUp() throws NoSuchMethodException {
        excluder = new Excluder();
        // Set version field to test different cases
        // Access private method isValidSince via reflection
        isValidSinceMethod = Excluder.class.getDeclaredMethod("isValidSince", Since.class);
        isValidSinceMethod.setAccessible(true);
    }

    @Test
    @Timeout(8000)
    void isValidSince_annotationNull_returnsTrue() throws InvocationTargetException, IllegalAccessException {
        // annotation is null => should return true
        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, new Object[]{null});
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void isValidSince_versionLessThanAnnotationVersion_returnsFalse() throws Exception {
        // Set version less than annotation value
        setVersion(1.0d);

        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(2.0d);

        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, annotation);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void isValidSince_versionEqualToAnnotationVersion_returnsTrue() throws Exception {
        setVersion(2.0d);

        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(2.0d);

        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, annotation);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void isValidSince_versionGreaterThanAnnotationVersion_returnsTrue() throws Exception {
        setVersion(3.0d);

        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(2.0d);

        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, annotation);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void isValidSince_versionIgnoreVersions_returnsTrue() throws Exception {
        // version is IGNORE_VERSIONS constant (-1.0d), which is less than any annotation version,
        // but since version < annotationVersion, it should return false per method logic.
        // Actually, the method returns version >= annotationVersion, so -1.0 >= annotationVersion (e.g. 1.0) is false.
        // So it should return false.

        setVersion(-1.0d);

        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(1.0d);

        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, annotation);
        assertFalse(result);
    }

    private void setVersion(double version) throws NoSuchFieldException, IllegalAccessException {
        var versionField = Excluder.class.getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.setDouble(excluder, version);
    }
}