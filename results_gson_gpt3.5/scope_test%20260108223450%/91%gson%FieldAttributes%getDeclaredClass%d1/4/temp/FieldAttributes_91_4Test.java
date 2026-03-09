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

public class FieldAttributes_91_4Test {

  private FieldAttributes fieldAttributes;
  private Field testField;

  static class Dummy {
    private int intValue;
    public String stringValue;
  }

  @BeforeEach
  public void setUp() throws NoSuchFieldException {
    testField = Dummy.class.getDeclaredField("intValue");
    fieldAttributes = new FieldAttributes(testField);
  }

  @Test
    @Timeout(8000)
  public void testGetDeclaredClass_returnsFieldType() {
    Class<?> declaredClass = fieldAttributes.getDeclaredClass();
    assertEquals(int.class, declaredClass);
  }

  @Test
    @Timeout(8000)
  public void testGetDeclaredClass_withDifferentField() throws NoSuchFieldException {
    Field stringField = Dummy.class.getDeclaredField("stringValue");
    FieldAttributes fa = new FieldAttributes(stringField);
    Class<?> declaredClass = fa.getDeclaredClass();
    assertEquals(String.class, declaredClass);
  }
}