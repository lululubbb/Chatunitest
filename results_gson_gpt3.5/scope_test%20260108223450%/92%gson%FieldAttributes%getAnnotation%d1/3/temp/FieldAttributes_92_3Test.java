package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import com.google.gson.FieldAttributes;

class FieldAttributes_92_3Test {

    private Field mockField;
    private FieldAttributes fieldAttributes;

    @BeforeEach
    void setUp() throws NoSuchFieldException, SecurityException {
        mockField = mock(Field.class);
        fieldAttributes = new FieldAttributes(mockField);
    }

    @Test
    @Timeout(8000)
    void testGetAnnotation_whenAnnotationPresent() {
        Deprecated mockAnnotation = mock(Deprecated.class);
        when(mockField.getAnnotation(Deprecated.class)).thenReturn(mockAnnotation);

        Deprecated result = fieldAttributes.getAnnotation(Deprecated.class);

        assertNotNull(result);
        assertEquals(mockAnnotation, result);
        verify(mockField).getAnnotation(Deprecated.class);
    }

    @Test
    @Timeout(8000)
    void testGetAnnotation_whenAnnotationAbsent() {
        when(mockField.getAnnotation(Deprecated.class)).thenReturn(null);

        Deprecated result = fieldAttributes.getAnnotation(Deprecated.class);

        assertNull(result);
        verify(mockField).getAnnotation(Deprecated.class);
    }
}