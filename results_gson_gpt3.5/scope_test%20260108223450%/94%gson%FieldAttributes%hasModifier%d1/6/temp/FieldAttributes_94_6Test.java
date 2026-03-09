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

public class FieldAttributes_94_6Test {

  private Field mockField;
  private FieldAttributes fieldAttributes;

  // Move Dummy class to top-level static class so it can have static fields
  static class Dummy {
    public int publicField;
    private final String privateFinalField = "test";
    protected static int protectedStaticField;
  }

  @BeforeEach
  void setUp() {
    // For the main tests, create a mock Field to control modifiers easily
    mockField = mock(Field.class);
    fieldAttributes = new FieldAttributes(mockField);
  }

  @Test
    @Timeout(8000)
  void hasModifier_whenModifierIsPresent_returnsTrue() {
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC | Modifier.STATIC);
    assertTrue(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertTrue(fieldAttributes.hasModifier(Modifier.STATIC));
  }

  @Test
    @Timeout(8000)
  void hasModifier_whenModifierIsNotPresent_returnsFalse() {
    when(mockField.getModifiers()).thenReturn(Modifier.PRIVATE);
    assertFalse(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertFalse(fieldAttributes.hasModifier(Modifier.STATIC));
  }

  @Test
    @Timeout(8000)
  void hasModifier_withMultipleModifiers() {
    when(mockField.getModifiers()).thenReturn(Modifier.PROTECTED | Modifier.FINAL);
    assertTrue(fieldAttributes.hasModifier(Modifier.PROTECTED));
    assertTrue(fieldAttributes.hasModifier(Modifier.FINAL));
    assertFalse(fieldAttributes.hasModifier(Modifier.PUBLIC));
  }

  @Test
    @Timeout(8000)
  void hasModifier_withZeroModifier() {
    when(mockField.getModifiers()).thenReturn(Modifier.PUBLIC);
    assertFalse(fieldAttributes.hasModifier(0));
  }

  @Test
    @Timeout(8000)
  void hasModifier_withNoModifiers() {
    when(mockField.getModifiers()).thenReturn(0);
    assertFalse(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertFalse(fieldAttributes.hasModifier(Modifier.PRIVATE));
  }

  @Test
    @Timeout(8000)
  void hasModifier_realField_publicField() throws NoSuchFieldException {
    Field realField = Dummy.class.getField("publicField");
    FieldAttributes fa = new FieldAttributes(realField);
    assertTrue(fa.hasModifier(Modifier.PUBLIC));
    assertFalse(fa.hasModifier(Modifier.PRIVATE));
  }

  @Test
    @Timeout(8000)
  void hasModifier_realField_privateFinalField() throws NoSuchFieldException {
    Field realField = Dummy.class.getDeclaredField("privateFinalField");
    FieldAttributes fa = new FieldAttributes(realField);
    assertTrue(fa.hasModifier(Modifier.PRIVATE));
    assertTrue(fa.hasModifier(Modifier.FINAL));
    assertFalse(fa.hasModifier(Modifier.PUBLIC));
  }

  @Test
    @Timeout(8000)
  void hasModifier_realField_protectedStaticField() throws NoSuchFieldException {
    Field realField = Dummy.class.getDeclaredField("protectedStaticField");
    FieldAttributes fa = new FieldAttributes(realField);
    assertTrue(fa.hasModifier(Modifier.PROTECTED));
    assertTrue(fa.hasModifier(Modifier.STATIC));
    assertFalse(fa.hasModifier(Modifier.PUBLIC));
  }
}