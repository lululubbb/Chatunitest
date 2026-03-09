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

class FieldAttributes_90_1Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    // Use a real field from a test class to have a real generic type
    class TestClass {
      public java.util.List<String> listField;
    }
    mockField = TestClass.class.getField("listField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetDeclaredType_returnsGenericType() {
    Type declaredType = fieldAttributes.getDeclaredType();
    assertNotNull(declaredType);
    assertEquals(mockField.getGenericType(), declaredType);
  }
}