package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldAttributes_88_4Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  @BeforeEach
  void setUp() throws NoSuchFieldException, SecurityException {
    // Create a real Field instance for testing
    mockField = SampleClass.class.getDeclaredField("sampleField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetDeclaringClass_returnsDeclaringClassOfField() {
    Class<?> declaringClass = fieldAttributes.getDeclaringClass();
    assertEquals(SampleClass.class, declaringClass);
  }

  // Helper class to get a Field instance
  private static class SampleClass {
    private int sampleField;
  }
}