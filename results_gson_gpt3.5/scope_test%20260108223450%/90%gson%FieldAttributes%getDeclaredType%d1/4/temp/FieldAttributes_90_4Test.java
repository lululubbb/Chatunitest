package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class FieldAttributes_90_4Test {

  private Field mockField;
  private FieldAttributes fieldAttributes;

  static class Dummy {
    public String dummyField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    mockField = Dummy.class.getField("dummyField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetDeclaredType() {
    Type declaredType = fieldAttributes.getDeclaredType();
    assertNotNull(declaredType);
    assertEquals(String.class, declaredType);
  }
}