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

class Excluder_457_4Test {

    private Excluder excluder;

    @BeforeEach
    void setUp() {
        excluder = new Excluder();
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_annotationNull() throws Exception {
        Method method = Excluder.class.getDeclaredMethod("isValidSince", Since.class);
        method.setAccessible(true);
        Boolean result = (Boolean) method.invoke(excluder, new Object[]{null});
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_versionLessThanAnnotation() throws Exception {
        // Set version less than annotation value
        Excluder excluderWithVersion = excluder.withVersion(1.0d);

        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(2.0d);

        Method method = Excluder.class.getDeclaredMethod("isValidSince", Since.class);
        method.setAccessible(true);
        Boolean result = (Boolean) method.invoke(excluderWithVersion, annotation);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_versionEqualToAnnotation() throws Exception {
        Excluder excluderWithVersion = excluder.withVersion(2.0d);

        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(2.0d);

        Method method = Excluder.class.getDeclaredMethod("isValidSince", Since.class);
        method.setAccessible(true);
        Boolean result = (Boolean) method.invoke(excluderWithVersion, annotation);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_versionGreaterThanAnnotation() throws Exception {
        Excluder excluderWithVersion = excluder.withVersion(3.0d);

        Since annotation = Mockito.mock(Since.class);
        Mockito.when(annotation.value()).thenReturn(2.0d);

        Method method = Excluder.class.getDeclaredMethod("isValidSince", Since.class);
        method.setAccessible(true);
        Boolean result = (Boolean) method.invoke(excluderWithVersion, annotation);
        assertTrue(result);
    }
}