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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FieldAttributes_93_5Test {

  @Mock
  private Field mockField;

  @Mock
  private Annotation mockAnnotation1;

  @Mock
  private Annotation mockAnnotation2;

  private FieldAttributes fieldAttributes;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void getAnnotations_returnsAllAnnotations() throws Exception {
    Annotation[] annotations = new Annotation[] {mockAnnotation1, mockAnnotation2};
    when(mockField.getAnnotations()).thenReturn(annotations);

    Collection<Annotation> result = fieldAttributes.getAnnotations();

    assertNotNull(result, "Returned collection should not be null");
    assertEquals(2, result.size(), "Should contain exactly 2 annotations");
    assertTrue(result.contains(mockAnnotation1), "Should contain mockAnnotation1");
    assertTrue(result.contains(mockAnnotation2), "Should contain mockAnnotation2");
  }

  @Test
    @Timeout(8000)
  void getAnnotations_returnsEmptyCollectionWhenNoAnnotations() throws Exception {
    when(mockField.getAnnotations()).thenReturn(new Annotation[0]);

    Collection<Annotation> result = fieldAttributes.getAnnotations();

    assertNotNull(result, "Returned collection should not be null");
    assertTrue(result.isEmpty(), "Collection should be empty when no annotations");
  }
}