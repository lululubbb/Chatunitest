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

public class FieldAttributes_95_4Test {

  private FieldAttributes fieldAttributes;
  private Field field;

  static class DummyClass {
    private int dummyField;
  }

  @BeforeEach
  public void setUp() throws NoSuchFieldException {
    field = DummyClass.class.getDeclaredField("dummyField");
    fieldAttributes = new FieldAttributes(field);
  }

  @Test
    @Timeout(8000)
  public void testToString() {
    String expected = field.toString();
    String actual = fieldAttributes.toString();
    assertEquals(expected, actual);
  }
}