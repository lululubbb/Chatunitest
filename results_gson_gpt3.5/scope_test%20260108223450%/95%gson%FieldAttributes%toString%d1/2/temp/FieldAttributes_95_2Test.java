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

public class FieldAttributes_95_2Test {

  private FieldAttributes fieldAttributes;
  private Field field;

  static class Dummy {
    int dummyField;
  }

  @BeforeEach
  void setUp() throws Exception {
    field = Dummy.class.getDeclaredField("dummyField");
    fieldAttributes = new FieldAttributes(field);
  }

  @Test
    @Timeout(8000)
  void testToString() {
    // toString() should return the same as field.toString()
    assertEquals(field.toString(), fieldAttributes.toString());
  }
}