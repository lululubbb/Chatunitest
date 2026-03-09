package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FieldAttributes_90_6Test {

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
  void testGetDeclaredType_returnsFieldGenericType() throws NoSuchFieldException, SecurityException {
    Type expectedType = String.class.getDeclaredField("value").getGenericType();
    when(mockField.getGenericType()).thenReturn(expectedType);

    Type actualType = fieldAttributes.getDeclaredType();

    assertEquals(expectedType, actualType);
    verify(mockField).getGenericType();
  }
}