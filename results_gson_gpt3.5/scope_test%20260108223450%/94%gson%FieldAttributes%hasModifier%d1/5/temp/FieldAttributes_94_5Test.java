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
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FieldAttributes_94_5Test {

  @Mock
  private Field mockField;

  private FieldAttributes fieldAttributes;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void hasModifier_whenModifierIsPresent_returnsTrue() {
    int modifier = Modifier.PUBLIC;
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC | Modifier.STATIC);

    boolean result = fieldAttributes.hasModifier(modifier);

    assertTrue(result);
    verify(mockField).getModifiers();
  }

  @Test
    @Timeout(8000)
  void hasModifier_whenModifierIsNotPresent_returnsFalse() {
    int modifier = Modifier.PRIVATE;
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC | Modifier.STATIC);

    boolean result = fieldAttributes.hasModifier(modifier);

    assertFalse(result);
    verify(mockField).getModifiers();
  }

  @Test
    @Timeout(8000)
  void hasModifier_whenModifierIsZero_returnsFalse() {
    int modifier = 0;
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC);

    boolean result = fieldAttributes.hasModifier(modifier);

    assertFalse(result);
    verify(mockField).getModifiers();
  }
}