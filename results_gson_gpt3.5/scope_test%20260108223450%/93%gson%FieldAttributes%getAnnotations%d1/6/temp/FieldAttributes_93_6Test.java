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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FieldAttributes_93_6Test {

  @Retention(RetentionPolicy.RUNTIME)
  private @interface TestAnnotation1 {}

  @Retention(RetentionPolicy.RUNTIME)
  private @interface TestAnnotation2 {}

  private static class DummyClass {
    @TestAnnotation1
    @TestAnnotation2
    private String annotatedField;

    private int nonAnnotatedField;
  }

  private Field annotatedField;
  private Field nonAnnotatedField;
  private FieldAttributes annotatedFieldAttributes;
  private FieldAttributes nonAnnotatedFieldAttributes;

  @BeforeEach
  public void setUp() throws NoSuchFieldException {
    annotatedField = DummyClass.class.getDeclaredField("annotatedField");
    nonAnnotatedField = DummyClass.class.getDeclaredField("nonAnnotatedField");
    annotatedFieldAttributes = new FieldAttributes(annotatedField);
    nonAnnotatedFieldAttributes = new FieldAttributes(nonAnnotatedField);
  }

  @Test
    @Timeout(8000)
  public void testGetAnnotations_returnsAllAnnotations() {
    Collection<Annotation> annotations = annotatedFieldAttributes.getAnnotations();
    assertNotNull(annotations);
    // Should contain both annotations
    assertTrue(annotations.stream().anyMatch(a -> a.annotationType() == TestAnnotation1.class));
    assertTrue(annotations.stream().anyMatch(a -> a.annotationType() == TestAnnotation2.class));
  }

  @Test
    @Timeout(8000)
  public void testGetAnnotations_returnsEmptyForNoAnnotations() {
    Collection<Annotation> annotations = nonAnnotatedFieldAttributes.getAnnotations();
    assertNotNull(annotations);
    assertTrue(annotations.isEmpty());
  }
}