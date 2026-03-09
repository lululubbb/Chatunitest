package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldAttributes_94_3Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  @BeforeEach
  void setUp() throws Exception {
    mockField = mock(Field.class);
    // Use reflection to create FieldAttributes instance with private final field set
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void testHasModifier_withModifierPresent() {
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC | Modifier.STATIC);

    assertTrue(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertTrue(fieldAttributes.hasModifier(Modifier.STATIC));
  }

  @Test
    @Timeout(8000)
  void testHasModifier_withModifierAbsent() {
    when(mockField.getModifiers()).thenReturn(Modifier.PRIVATE);

    assertFalse(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertFalse(fieldAttributes.hasModifier(Modifier.STATIC));
  }

  @Test
    @Timeout(8000)
  void testHasModifier_withNoModifiers() {
    when(mockField.getModifiers()).thenReturn(0);

    assertFalse(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertFalse(fieldAttributes.hasModifier(Modifier.PRIVATE));
  }

  @Test
    @Timeout(8000)
  void testHasModifier_withAllModifiers() {
    int allModifiers = Modifier.PUBLIC | Modifier.PRIVATE | Modifier.PROTECTED | Modifier.STATIC | Modifier.FINAL | Modifier.TRANSIENT | Modifier.VOLATILE | Modifier.SYNCHRONIZED | Modifier.NATIVE | Modifier.INTERFACE | Modifier.ABSTRACT | Modifier.STRICT;
    when(mockField.getModifiers()).thenReturn(allModifiers);

    for (int i = 1; i <= allModifiers; i = i << 1) {
      if ((allModifiers & i) != 0) {
        assertTrue(fieldAttributes.hasModifier(i), "Modifier " + i + " should be present");
      }
    }
  }
}