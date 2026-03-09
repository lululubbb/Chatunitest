package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class FieldAttributes_90_2Test {

  private FieldAttributes fieldAttributes;
  private Field field;

  private static class DummyClass {
    public int dummyField;
  }

  @BeforeEach
  public void setUp() throws NoSuchFieldException {
    field = DummyClass.class.getField("dummyField");
    fieldAttributes = new FieldAttributes(field);
  }

  @Test
    @Timeout(8000)
  public void testGetDeclaredType() {
    Type declaredType = fieldAttributes.getDeclaredType();
    assertEquals(field.getGenericType(), declaredType);
  }
}