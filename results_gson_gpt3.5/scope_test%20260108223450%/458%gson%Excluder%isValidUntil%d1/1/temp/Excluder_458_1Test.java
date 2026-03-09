package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.Until;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class Excluder_458_1Test {

    private Excluder excluder;
    private Method isValidUntilMethod;

    @BeforeEach
    void setUp() throws Exception {
        excluder = new Excluder();
        isValidUntilMethod = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
        isValidUntilMethod.setAccessible(true);

        // Set the private field 'version' to a specific value for testing
        java.lang.reflect.Field versionField = Excluder.class.getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.setDouble(excluder, 1.5d);
    }

    @Test
    @Timeout(8000)
    void isValidUntil_WithNullAnnotation_ReturnsTrue() throws Exception {
        Boolean result = (Boolean) isValidUntilMethod.invoke(excluder, new Object[]{null});
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void isValidUntil_WithAnnotationValueGreaterThanVersion_ReturnsTrue() throws Exception {
        Until untilMock = Mockito.mock(Until.class);
        Mockito.when(untilMock.value()).thenReturn(2.0d);

        Boolean result = (Boolean) isValidUntilMethod.invoke(excluder, untilMock);
        assertTrue(result);
    }

    @Test
    @Timeout(8000)
    void isValidUntil_WithAnnotationValueEqualToVersion_ReturnsFalse() throws Exception {
        Until untilMock = Mockito.mock(Until.class);
        Mockito.when(untilMock.value()).thenReturn(1.5d);

        Boolean result = (Boolean) isValidUntilMethod.invoke(excluder, untilMock);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void isValidUntil_WithAnnotationValueLessThanVersion_ReturnsFalse() throws Exception {
        Until untilMock = Mockito.mock(Until.class);
        Mockito.when(untilMock.value()).thenReturn(1.0d);

        Boolean result = (Boolean) isValidUntilMethod.invoke(excluder, untilMock);
        assertFalse(result);
    }

    @Test
    @Timeout(8000)
    void isValidUntil_WithIgnoreVersions_ReturnsTrue() throws Exception {
        // Set version to IGNORE_VERSIONS (-1.0d)
        java.lang.reflect.Field versionField = Excluder.class.getDeclaredField("version");
        versionField.setAccessible(true);
        versionField.setDouble(excluder, -1.0d);

        Until untilMock = Mockito.mock(Until.class);
        Mockito.when(untilMock.value()).thenReturn(0.0d);

        // Since version is -1.0, version < annotationVersion is true for any positive annotationVersion
        Boolean result = (Boolean) isValidUntilMethod.invoke(excluder, untilMock);
        assertTrue(result);
    }
}