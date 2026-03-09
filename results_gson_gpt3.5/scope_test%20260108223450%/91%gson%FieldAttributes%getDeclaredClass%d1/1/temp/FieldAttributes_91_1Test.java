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

public class FieldAttributes_91_1Test {

  private FieldAttributes fieldAttributes;
  private Field testField;

  private static class Dummy {
    private int intField;
  }

  @BeforeEach
  public void setup() throws NoSuchFieldException {
    testField = Dummy.class.getDeclaredField("intField");
    fieldAttributes = new FieldAttributes(testField);
  }

  @Test
    @Timeout(8000)
  public void testGetDeclaredClass() {
    Class<?> declaredClass = fieldAttributes.getDeclaredClass();
    assertEquals(int.class, declaredClass);
  }
}