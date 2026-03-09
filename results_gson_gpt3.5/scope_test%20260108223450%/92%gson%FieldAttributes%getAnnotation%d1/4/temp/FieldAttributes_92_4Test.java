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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class FieldAttributes_92_4Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  @BeforeEach
  void setUp() throws NoSuchFieldException, SecurityException {
    // Create a real Field instance from a test class for realistic behavior
    mockField = TestClass.class.getDeclaredField("annotatedField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetAnnotation_existingAnnotation() {
    Deprecated annotation = fieldAttributes.getAnnotation(Deprecated.class);
    assertNotNull(annotation);
    assertEquals(Deprecated.class, annotation.annotationType());
  }

  @Test
    @Timeout(8000)
  void testGetAnnotation_nonExistingAnnotation() {
    Override annotation = fieldAttributes.getAnnotation(Override.class);
    assertNull(annotation);
  }

  @Test
    @Timeout(8000)
  void testGetAnnotation_nullAnnotationClass_throwsException() {
    assertThrows(NullPointerException.class, () -> fieldAttributes.getAnnotation(null));
  }

  // Helper test class with annotated field
  private static class TestClass {
    @Deprecated
    private String annotatedField;
  }
}