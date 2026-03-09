package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldAttributes_93_2Test {

  @Retention(RetentionPolicy.RUNTIME)
  private @interface TestAnnotation1 {
  }

  @Retention(RetentionPolicy.RUNTIME)
  private @interface TestAnnotation2 {
  }

  private FieldAttributes fieldAttributes;
  private Field field;

  @Test
    @Timeout(8000)
  void getAnnotations_returnsAllAnnotations() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("fieldWithAnnotations");
    FieldAttributes fa = new FieldAttributes(field);

    Collection<Annotation> annotations = fa.getAnnotations();

    assertNotNull(annotations);
    assertEquals(2, annotations.size());
    assertTrue(annotations.stream().anyMatch(a -> a.annotationType() == TestAnnotation1.class));
    assertTrue(annotations.stream().anyMatch(a -> a.annotationType() == TestAnnotation2.class));
  }

  @Test
    @Timeout(8000)
  void getAnnotations_emptyWhenNoAnnotations() throws NoSuchFieldException {
    Field field = SampleClass.class.getDeclaredField("fieldWithoutAnnotations");
    FieldAttributes fa = new FieldAttributes(field);

    Collection<Annotation> annotations = fa.getAnnotations();

    assertNotNull(annotations);
    assertTrue(annotations.isEmpty());
  }

  // Sample class with annotated and non-annotated fields for testing
  private static class SampleClass {
    @TestAnnotation1
    @TestAnnotation2
    private int fieldWithAnnotations;

    private int fieldWithoutAnnotations;
  }
}