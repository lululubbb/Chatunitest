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

class FieldAttributes_93_1Test {

  FieldAttributes fieldAttributes;
  Field mockField;

  @BeforeEach
  void setUp() throws NoSuchFieldException, SecurityException {
    // Use a real Field instance from a dummy class with annotations
    class Dummy {
      @Deprecated
      public int annotatedField;
    }
    mockField = Dummy.class.getField("annotatedField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetAnnotationsReturnsAllAnnotations() {
    Collection<Annotation> annotations = fieldAttributes.getAnnotations();
    assertNotNull(annotations);
    // The field should have at least the @Deprecated annotation
    boolean hasDeprecated = annotations.stream()
        .anyMatch(a -> a.annotationType().equals(Deprecated.class));
    assertTrue(hasDeprecated);
  }

  @Test
    @Timeout(8000)
  void testGetAnnotationsEmptyWhenNoAnnotations() throws NoSuchFieldException {
    class DummyNoAnno {
      public int noAnnotationField;
    }
    Field noAnnoField = DummyNoAnno.class.getField("noAnnotationField");
    FieldAttributes fa = new FieldAttributes(noAnnoField);
    Collection<Annotation> annotations = fa.getAnnotations();
    assertNotNull(annotations);
    assertTrue(annotations.isEmpty());
  }
}