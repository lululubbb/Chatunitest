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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class FieldAttributes_94_2Test {

  private FieldAttributes fieldAttributes;
  private Field mockField;

  @BeforeEach
  public void setUp() throws Exception {
    mockField = mock(Field.class);
    // Use reflection to create FieldAttributes instance with private final field
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  public void testHasModifier_whenModifierPresent() {
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC | Modifier.STATIC);

    assertTrue(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertTrue(fieldAttributes.hasModifier(Modifier.STATIC));
    assertTrue(fieldAttributes.hasModifier(Modifier.PUBLIC | Modifier.STATIC));
  }

  @Test
    @Timeout(8000)
  public void testHasModifier_whenModifierNotPresent() {
    when(mockField.getModifiers()).thenReturn(Modifier.PRIVATE);

    assertFalse(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertFalse(fieldAttributes.hasModifier(Modifier.STATIC));
    assertFalse(fieldAttributes.hasModifier(Modifier.PUBLIC | Modifier.STATIC));
  }

  @Test
    @Timeout(8000)
  public void testHasModifier_whenNoModifiers() {
    when(mockField.getModifiers()).thenReturn(0);

    assertFalse(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertFalse(fieldAttributes.hasModifier(Modifier.PRIVATE));
    assertFalse(fieldAttributes.hasModifier(Modifier.STATIC));
  }
}