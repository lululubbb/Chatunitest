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

class FieldAttributes_92_6Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  @BeforeEach
  void setUp() throws NoSuchFieldException, SecurityException {
    mockField = mock(Field.class);
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetAnnotation_existingAnnotation() {
    Deprecated deprecated = mock(Deprecated.class);
    when(mockField.getAnnotation(Deprecated.class)).thenReturn(deprecated);

    Deprecated result = fieldAttributes.getAnnotation(Deprecated.class);

    assertSame(deprecated, result);
    verify(mockField).getAnnotation(Deprecated.class);
  }

  @Test
    @Timeout(8000)
  void testGetAnnotation_noAnnotation() {
    when(mockField.getAnnotation(SuppressWarnings.class)).thenReturn(null);

    SuppressWarnings result = fieldAttributes.getAnnotation(SuppressWarnings.class);

    assertNull(result);
    verify(mockField).getAnnotation(SuppressWarnings.class);
  }
}