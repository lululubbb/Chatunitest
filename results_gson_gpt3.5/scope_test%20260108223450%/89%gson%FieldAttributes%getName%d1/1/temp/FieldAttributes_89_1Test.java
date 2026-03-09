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

public class FieldAttributes_89_1Test {

  private FieldAttributes fieldAttributes;
  private Field testField;

  static class TestClass {
    public String testName;
  }

  @BeforeEach
  public void setUp() throws NoSuchFieldException {
    testField = TestClass.class.getField("testName");
    fieldAttributes = new FieldAttributes(testField);
  }

  @Test
    @Timeout(8000)
  public void testGetName() {
    String name = fieldAttributes.getName();
    assertEquals("testName", name);
  }
}