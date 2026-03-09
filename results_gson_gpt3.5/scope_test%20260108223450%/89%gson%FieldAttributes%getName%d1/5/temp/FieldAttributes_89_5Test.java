package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FieldAttributes_89_5Test {

  private FieldAttributes fieldAttributes;
  private Field testField;

  static class DummyClass {
    public String publicField;
  }

  @BeforeEach
  public void setUp() throws NoSuchFieldException {
    testField = DummyClass.class.getField("publicField");
    fieldAttributes = new FieldAttributes(testField);
  }

  @Test
    @Timeout(8000)
  public void testGetName_returnsFieldName() {
    String name = fieldAttributes.getName();
    assertEquals("publicField", name);
  }
}