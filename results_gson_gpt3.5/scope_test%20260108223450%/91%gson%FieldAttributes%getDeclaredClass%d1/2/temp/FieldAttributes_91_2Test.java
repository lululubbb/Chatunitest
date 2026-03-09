package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldAttributes_91_2Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  static class DummyClass {
    int intField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    mockField = DummyClass.class.getDeclaredField("intField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetDeclaredClass_returnsFieldType() {
    Class<?> declaredClass = fieldAttributes.getDeclaredClass();
    assertEquals(int.class, declaredClass);
  }
}