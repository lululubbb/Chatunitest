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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FieldAttributes_92_1Test {

  @Mock
  private Field mockField;

  private FieldAttributes fieldAttributes;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void getAnnotation_existingAnnotation_returnsAnnotation() {
    @SuppressWarnings("unchecked")
    Class<Deprecated> annotationClass = Deprecated.class;
    Deprecated deprecatedAnnotation = mock(Deprecated.class);

    when(mockField.getAnnotation(annotationClass)).thenReturn(deprecatedAnnotation);

    Deprecated result = fieldAttributes.getAnnotation(annotationClass);

    assertSame(deprecatedAnnotation, result);
    verify(mockField).getAnnotation(annotationClass);
  }

  @Test
    @Timeout(8000)
  void getAnnotation_nonExistingAnnotation_returnsNull() {
    @SuppressWarnings("unchecked")
    Class<Override> annotationClass = Override.class;

    when(mockField.getAnnotation(annotationClass)).thenReturn(null);

    Override result = fieldAttributes.getAnnotation(annotationClass);

    assertNull(result);
    verify(mockField).getAnnotation(annotationClass);
  }
}