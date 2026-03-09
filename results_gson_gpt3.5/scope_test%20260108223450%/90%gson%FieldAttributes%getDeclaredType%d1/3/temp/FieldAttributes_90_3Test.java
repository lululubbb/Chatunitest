package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.FieldAttributes;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldAttributes_90_3Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    // Using a real Field instance from a test class
    mockField = TestClass.class.getDeclaredField("testField");
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testGetDeclaredType_returnsFieldGenericType() {
    Type declaredType = fieldAttributes.getDeclaredType();
    assertNotNull(declaredType);
    assertEquals(mockField.getGenericType(), declaredType);
  }

  // Helper class with a field to obtain Field instance from reflection
  private static class TestClass {
    private String testField;
  }
}