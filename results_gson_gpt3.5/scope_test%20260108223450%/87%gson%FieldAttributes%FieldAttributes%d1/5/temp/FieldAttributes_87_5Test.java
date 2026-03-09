package com.google.gson;
import org.junit.jupiter.api.Timeout;
import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldAttributes_87_5Test {

  private Field field;
  private FieldAttributes fieldAttributes;

  static class Dummy {
    public int publicField;
    @Deprecated
    private String privateField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    field = Dummy.class.getDeclaredField("publicField");
    fieldAttributes = new FieldAttributes(field);
  }

  @Test
    @Timeout(8000)
  void constructor_nullField_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> new FieldAttributes(null));
  }

  @Test
    @Timeout(8000)
  void getDeclaringClass_returnsFieldDeclaringClass() {
    assertEquals(Dummy.class, fieldAttributes.getDeclaringClass());
  }

  @Test
    @Timeout(8000)
  void getName_returnsFieldName() {
    assertEquals("publicField", fieldAttributes.getName());
  }

  @Test
    @Timeout(8000)
  void getDeclaredType_returnsFieldGenericType() {
    assertEquals(int.class, fieldAttributes.getDeclaredType());
  }

  @Test
    @Timeout(8000)
  void getDeclaredClass_returnsFieldType() {
    assertEquals(int.class, fieldAttributes.getDeclaredClass());
  }

  @Test
    @Timeout(8000)
  void getAnnotation_existingAnnotation_returnsAnnotation() throws NoSuchFieldException {
    Field privateField = Dummy.class.getDeclaredField("privateField");
    FieldAttributes fa = new FieldAttributes(privateField);
    Deprecated deprecated = fa.getAnnotation(Deprecated.class);
    assertNotNull(deprecated);
  }

  @Test
    @Timeout(8000)
  void getAnnotation_nonExistingAnnotation_returnsNull() {
    Deprecated deprecated = fieldAttributes.getAnnotation(Deprecated.class);
    assertNull(deprecated);
  }

  @Test
    @Timeout(8000)
  void getAnnotations_returnsAllAnnotations() throws NoSuchFieldException {
    Field privateField = Dummy.class.getDeclaredField("privateField");
    FieldAttributes fa = new FieldAttributes(privateField);
    Collection<Annotation> annotations = fa.getAnnotations();
    assertNotNull(annotations);
    assertTrue(annotations.stream().anyMatch(a -> a.annotationType() == Deprecated.class));
  }

  @Test
    @Timeout(8000)
  void hasModifier_checksModifiers() {
    assertTrue(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertFalse(fieldAttributes.hasModifier(Modifier.PRIVATE));
  }

  @Test
    @Timeout(8000)
  void toString_containsFieldName() {
    String s = fieldAttributes.toString();
    assertTrue(s.contains("publicField"));
  }

}