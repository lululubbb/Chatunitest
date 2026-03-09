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

class FieldAttributes_95_3Test {

  private FieldAttributes fieldAttributes;
  private Field testField;

  static class TestClass {
    public int testInt;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    testField = TestClass.class.getField("testInt");
    fieldAttributes = new FieldAttributes(testField);
  }

  @Test
    @Timeout(8000)
  void testToString_returnsFieldToString() {
    String expected = testField.toString();
    String actual = fieldAttributes.toString();
    assertEquals(expected, actual);
  }
}