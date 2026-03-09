package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

class FieldAttributes_92_5Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    mockField = mock(Field.class);
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetAnnotation_returnsAnnotation() {
    Deprecated deprecatedAnnotation = Deprecated.class.getAnnotation(Deprecated.class);
    when(mockField.getAnnotation(Deprecated.class)).thenReturn(deprecatedAnnotation);

    Deprecated result = fieldAttributes.getAnnotation(Deprecated.class);

    assertEquals(deprecatedAnnotation, result);
    verify(mockField).getAnnotation(Deprecated.class);
  }

  @Test
    @Timeout(8000)
  void testGetAnnotation_returnsNullWhenAnnotationNotPresent() {
    when(mockField.getAnnotation(SuppressWarnings.class)).thenReturn(null);

    SuppressWarnings result = fieldAttributes.getAnnotation(SuppressWarnings.class);

    assertNull(result);
    verify(mockField).getAnnotation(SuppressWarnings.class);
  }
}