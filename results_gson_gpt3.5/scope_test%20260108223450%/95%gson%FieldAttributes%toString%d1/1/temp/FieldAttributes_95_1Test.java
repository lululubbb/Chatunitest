package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldAttributes_95_1Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  @BeforeEach
  void setUp() throws NoSuchFieldException, SecurityException {
    // Use a real Field instance from a dummy class for realistic toString()
    class Dummy {
      @SuppressWarnings("unused")
      private int sampleField;
    }
    mockField = Dummy.class.getDeclaredField("sampleField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testToString_returnsFieldToString() {
    String expected = mockField.toString();
    String actual = fieldAttributes.toString();
    assertEquals(expected, actual);
  }
}