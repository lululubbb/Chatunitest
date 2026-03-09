package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldAttributes_88_1Test {

  private FieldAttributes fieldAttributes;
  private Field field;

  static class DummyClass {
    public int dummyField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    field = DummyClass.class.getField("dummyField");
    fieldAttributes = new FieldAttributes(field);
  }

  @Test
    @Timeout(8000)
  void testGetDeclaringClass() {
    Class<?> declaringClass = fieldAttributes.getDeclaringClass();
    assertEquals(DummyClass.class, declaringClass);
  }
}