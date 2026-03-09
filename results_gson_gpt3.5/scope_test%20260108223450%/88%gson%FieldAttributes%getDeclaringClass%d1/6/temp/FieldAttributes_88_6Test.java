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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FieldAttributes_88_6Test {

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
  void testGetDeclaringClass_returnsDeclaringClassFromField() throws Exception {
    Class<?> expectedClass = String.class;

    // Use doReturn() instead of when() to avoid generic capture issues
    doReturn(expectedClass).when(mockField).getDeclaringClass();

    Class<?> actualClass = fieldAttributes.getDeclaringClass();

    assertSame(expectedClass, actualClass);
    verify(mockField).getDeclaringClass();
  }
}