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

public class FieldAttributes_88_3Test {

  private Field mockField;
  private FieldAttributes fieldAttributes;

  static class DeclaringClass {
    public int testField;
  }

  @BeforeEach
  public void setUp() throws NoSuchFieldException {
    mockField = DeclaringClass.class.getDeclaredField("testField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  public void testGetDeclaringClass() {
    Class<?> declaringClass = fieldAttributes.getDeclaringClass();
    assertEquals(DeclaringClass.class, declaringClass);
  }
}