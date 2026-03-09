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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.Since;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class ExcluderIsValidSinceTest {

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
    void testIsValidSince_annotationNull() throws Exception {
        // annotation == null, should return true
        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, new Object[]{null});
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_versionLessThanAnnotationValue() throws Exception {
        setExcluderVersion(excluder, 1.0d);

        Since since = createSinceAnnotation(2.0d);
        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, since);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_versionEqualToAnnotationValue() throws Exception {
        setExcluderVersion(excluder, 2.0d);

        Since since = createSinceAnnotation(2.0d);
        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, since);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void testIsValidSince_versionGreaterThanAnnotationValue() throws Exception {
        setExcluderVersion(excluder, 3.0d);

        Since since = createSinceAnnotation(2.0d);
        Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, since);
        assertTrue(result);
    }

    private void setExcluderVersion(Excluder excluder, double version) throws Exception {
        Field field = Excluder.class.getDeclaredField("version");
        field.setAccessible(true);
        field.setDouble(excluder, version);
    }

    private Since createSinceAnnotation(double value) {
        return new Since() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return Since.class;
            }

            @Override
            public double value() {
                return value;
            }
        };
    }
}