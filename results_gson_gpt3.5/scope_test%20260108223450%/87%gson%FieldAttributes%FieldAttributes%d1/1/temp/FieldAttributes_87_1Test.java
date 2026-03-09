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

class FieldAttributes_87_1Test {

  private Field fieldMock;
  private FieldAttributes fieldAttributes;

  static class Dummy {
    public int publicField;
    private String privateField;
    @Deprecated
    int annotatedField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    fieldMock = Dummy.class.getDeclaredField("publicField");
    fieldAttributes = new FieldAttributes(fieldMock);
  }

  @Test
    @Timeout(8000)
  void constructor_nullField_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> new FieldAttributes(null));
  }

  @Test
    @Timeout(8000)
  void getDeclaringClass_returnsCorrectClass() {
    assertEquals(Dummy.class, fieldAttributes.getDeclaringClass());
  }

  @Test
    @Timeout(8000)
  void getName_returnsCorrectName() {
    assertEquals("publicField", fieldAttributes.getName());
  }

  @Test
    @Timeout(8000)
  void getDeclaredType_returnsCorrectType() {
    assertEquals(int.class, fieldAttributes.getDeclaredType());
  }

  @Test
    @Timeout(8000)
  void getDeclaredClass_returnsCorrectClass() {
    assertEquals(int.class, fieldAttributes.getDeclaredClass());
  }

  @Test
    @Timeout(8000)
  void getAnnotation_existingAnnotation_returnsAnnotation() throws NoSuchFieldException {
    Field annotatedField = Dummy.class.getDeclaredField("annotatedField");
    FieldAttributes fa = new FieldAttributes(annotatedField);
    Deprecated annotation = fa.getAnnotation(Deprecated.class);
    assertNotNull(annotation);
    assertEquals(Deprecated.class, annotation.annotationType());
  }

  @Test
    @Timeout(8000)
  void getAnnotation_nonExistingAnnotation_returnsNull() {
    Deprecated annotation = fieldAttributes.getAnnotation(Deprecated.class);
    assertNull(annotation);
  }

  @Test
    @Timeout(8000)
  void getAnnotations_returnsAllAnnotations() throws NoSuchFieldException {
    Field annotatedField = Dummy.class.getDeclaredField("annotatedField");
    FieldAttributes fa = new FieldAttributes(annotatedField);
    Collection<Annotation> annotations = fa.getAnnotations();
    assertNotNull(annotations);
    assertTrue(annotations.stream().anyMatch(a -> a.annotationType() == Deprecated.class));
  }

  @Test
    @Timeout(8000)
  void hasModifier_existingModifier_returnsTrue() {
    assertTrue(fieldAttributes.hasModifier(Modifier.PUBLIC));
  }

  @Test
    @Timeout(8000)
  void hasModifier_nonExistingModifier_returnsFalse() {
    assertFalse(fieldAttributes.hasModifier(Modifier.PRIVATE));
  }

  @Test
    @Timeout(8000)
  void toString_containsFieldName() {
    String str = fieldAttributes.toString();
    assertNotNull(str);
    assertTrue(str.contains("publicField"));
  }
}