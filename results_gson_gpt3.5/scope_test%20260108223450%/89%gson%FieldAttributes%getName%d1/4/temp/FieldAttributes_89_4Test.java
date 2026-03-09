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

public class FieldAttributes_89_4Test {

  private FieldAttributes fieldAttributes;
  private Field testField;

  static class TestClass {
    public String publicField;
    private int privateField;
  }

  @BeforeEach
  public void setUp() throws NoSuchFieldException {
    testField = TestClass.class.getDeclaredField("publicField");
    fieldAttributes = new FieldAttributes(testField);
  }

  @Test
    @Timeout(8000)
  public void testGetName_returnsFieldName() {
    String fieldName = fieldAttributes.getName();
    assertEquals("publicField", fieldName);
  }

  @Test
    @Timeout(8000)
  public void testGetName_privateField() throws NoSuchFieldException {
    Field privateField = TestClass.class.getDeclaredField("privateField");
    FieldAttributes fa = new FieldAttributes(privateField);
    assertEquals("privateField", fa.getName());
  }
}