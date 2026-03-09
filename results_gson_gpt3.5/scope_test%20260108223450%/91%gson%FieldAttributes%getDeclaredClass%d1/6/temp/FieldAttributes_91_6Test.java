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
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class FieldAttributes_91_6Test {

  private Field mockField;
  private FieldAttributes fieldAttributes;

  static class DummyClass {
    public int publicField;
    private String privateField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    mockField = DummyClass.class.getField("publicField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetDeclaredClass_returnsFieldType() {
    Class<?> declaredClass = fieldAttributes.getDeclaredClass();
    assertEquals(int.class, declaredClass);
  }

  @Test
    @Timeout(8000)
  void testGetDeclaredClass_privateField() throws NoSuchFieldException {
    Field privateField = DummyClass.class.getDeclaredField("privateField");
    FieldAttributes fa = new FieldAttributes(privateField);
    assertEquals(String.class, fa.getDeclaredClass());
  }
}