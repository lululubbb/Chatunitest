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

class FieldAttributes_94_4Test {

  private Field mockField;
  private FieldAttributes fieldAttributes;

  @BeforeEach
  void setUp() throws Exception {
    mockField = mock(Field.class);
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void hasModifier_whenModifierPresent_returnsTrue() {
    int modifier = Modifier.PUBLIC;
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC | Modifier.STATIC);

    boolean result = fieldAttributes.hasModifier(modifier);

    assertTrue(result);
    verify(mockField).getModifiers();
  }

  @Test
    @Timeout(8000)
  void hasModifier_whenModifierNotPresent_returnsFalse() {
    int modifier = Modifier.PRIVATE;
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC | Modifier.STATIC);

    boolean result = fieldAttributes.hasModifier(modifier);

    assertFalse(result);
    verify(mockField).getModifiers();
  }

  @Test
    @Timeout(8000)
  void hasModifier_whenNoModifiers_returnsFalse() {
    int modifier = Modifier.FINAL;
    when(mockField.getModifiers()).thenReturn(0);

    boolean result = fieldAttributes.hasModifier(modifier);

    assertFalse(result);
    verify(mockField).getModifiers();
  }

  @Test
    @Timeout(8000)
  void hasModifier_whenMultipleModifiersRequested_returnsTrueIfAnyMatch() {
    int modifier = Modifier.PRIVATE | Modifier.PUBLIC;
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC);

    boolean result = fieldAttributes.hasModifier(modifier);

    assertTrue(result);
    verify(mockField).getModifiers();
  }

  @Test
    @Timeout(8000)
  void hasModifier_whenMultipleModifiersRequested_returnsFalseIfNoneMatch() {
    int modifier = Modifier.PRIVATE | Modifier.FINAL;
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC);

    boolean result = fieldAttributes.hasModifier(modifier);

    assertFalse(result);
    verify(mockField).getModifiers();
  }
}