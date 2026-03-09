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

class FieldAttributes_88_5Test {

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
  void testGetDeclaringClass_returnsDeclaringClass() throws Exception {
    // Use real Field instance via reflection to avoid mocking generic Class<?> return
    Field realField = String.class.getDeclaredField("value");
    fieldAttributes = new FieldAttributes(realField);

    Class<?> result = fieldAttributes.getDeclaringClass();

    assertSame(String.class, result);
  }
}