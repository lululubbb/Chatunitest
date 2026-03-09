package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collection;

class FieldAttributes_93_4Test {

  private Field mockField;
  private FieldAttributes fieldAttributes;

  @BeforeEach
  void setUp() {
    mockField = mock(Field.class);
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void getAnnotations_returnsAnnotationsList() {
    Annotation annotation1 = mock(Annotation.class);
    Annotation annotation2 = mock(Annotation.class);
    Annotation[] annotations = new Annotation[] {annotation1, annotation2};

    when(mockField.getAnnotations()).thenReturn(annotations);

    Collection<Annotation> result = fieldAttributes.getAnnotations();

    assertNotNull(result);
    assertEquals(2, result.size());
    assertTrue(result.contains(annotation1));
    assertTrue(result.contains(annotation2));
  }

  @Test
    @Timeout(8000)
  void getAnnotations_returnsEmptyListWhenNoAnnotations() {
    Annotation[] emptyAnnotations = new Annotation[0];
    when(mockField.getAnnotations()).thenReturn(emptyAnnotations);

    Collection<Annotation> result = fieldAttributes.getAnnotations();

    assertNotNull(result);
    assertTrue(result.isEmpty());
  }
}