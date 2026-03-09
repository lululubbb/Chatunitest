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

class FieldAttributes_91_3Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  private static class DummyClass {
    public int intField;
    private String stringField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    mockField = DummyClass.class.getField("intField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void getDeclaredClass_returnsFieldType() {
    Class<?> declaredClass = fieldAttributes.getDeclaredClass();
    assertEquals(int.class, declaredClass);
  }

  @Test
    @Timeout(8000)
  void getDeclaredClass_withPrivateField_returnsFieldType() throws NoSuchFieldException {
    Field privateField = DummyClass.class.getDeclaredField("stringField");
    FieldAttributes fa = new FieldAttributes(privateField);
    Class<?> declaredClass = fa.getDeclaredClass();
    assertEquals(String.class, declaredClass);
  }

}