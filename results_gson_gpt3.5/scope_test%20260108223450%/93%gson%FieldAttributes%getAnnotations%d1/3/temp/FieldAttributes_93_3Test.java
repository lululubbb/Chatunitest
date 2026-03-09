package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldAttributes_93_3Test {

  Field mockField;
  FieldAttributes fieldAttributes;

  @BeforeEach
  void setUp() throws NoSuchFieldException, SecurityException {
    mockField = mock(Field.class);
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetAnnotations_returnsAnnotationsFromField() {
    Annotation annotation1 = mock(Annotation.class);
    Annotation annotation2 = mock(Annotation.class);
    Annotation[] annotations = new Annotation[] {annotation1, annotation2};
    when(mockField.getAnnotations()).thenReturn(annotations);

    Collection<Annotation> result = fieldAttributes.getAnnotations();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains(annotation1));
    assertTrue(result.contains(annotation2));
    verify(mockField).getAnnotations();
  }

  @Test
    @Timeout(8000)
  void testGetAnnotations_returnsEmptyCollectionIfNoAnnotations() {
    Annotation[] annotations = new Annotation[0];
    when(mockField.getAnnotations()).thenReturn(annotations);

    Collection<Annotation> result = fieldAttributes.getAnnotations();

    assertNotNull(result);
    assertTrue(result.isEmpty());
    verify(mockField).getAnnotations();
  }
}