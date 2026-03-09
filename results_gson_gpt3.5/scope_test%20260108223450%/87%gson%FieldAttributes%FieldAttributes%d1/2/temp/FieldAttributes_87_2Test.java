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

class FieldAttributes_87_2Test {

  private Field field;
  private FieldAttributes fieldAttributes;

  static class TestClass {
    @Deprecated
    public int publicField;

    private String privateField;

    protected final double finalField = 0.0;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    field = TestClass.class.getDeclaredField("publicField");
    fieldAttributes = new FieldAttributes(field);
  }

  @Test
    @Timeout(8000)
  void constructor_nullField_throwsNullPointerException() {
    assertThrows(NullPointerException.class, () -> new FieldAttributes(null));
  }

  @Test
    @Timeout(8000)
  void getDeclaringClass_returnsCorrectClass() {
    assertEquals(TestClass.class, fieldAttributes.getDeclaringClass());
  }

  @Test
    @Timeout(8000)
  void getName_returnsCorrectName() {
    assertEquals("publicField", fieldAttributes.getName());
  }

  @Test
    @Timeout(8000)
  void getDeclaredType_returnsCorrectType() {
    Type type = fieldAttributes.getDeclaredType();
    assertEquals(int.class, type);
  }

  @Test
    @Timeout(8000)
  void getDeclaredClass_returnsCorrectClass() {
    assertEquals(int.class, fieldAttributes.getDeclaredClass());
  }

  @Test
    @Timeout(8000)
  void getAnnotation_returnsAnnotationIfPresent() {
    Annotation annotation = fieldAttributes.getAnnotation(Deprecated.class);
    assertNotNull(annotation);
    assertTrue(annotation instanceof Deprecated);
  }

  @Test
    @Timeout(8000)
  void getAnnotation_returnsNullIfAnnotationNotPresent() {
    Annotation annotation = fieldAttributes.getAnnotation(SuppressWarnings.class);
    assertNull(annotation);
  }

  @Test
    @Timeout(8000)
  void getAnnotations_returnsAllAnnotations() {
    Collection<Annotation> annotations = fieldAttributes.getAnnotations();
    assertNotNull(annotations);
    assertTrue(annotations.stream().anyMatch(a -> a.annotationType() == Deprecated.class));
  }

  @Test
    @Timeout(8000)
  void hasModifier_returnsTrueIfModifierPresent() throws NoSuchFieldException {
    Field privateField = TestClass.class.getDeclaredField("privateField");
    FieldAttributes fa = new FieldAttributes(privateField);
    assertTrue(fa.hasModifier(Modifier.PRIVATE));
    assertFalse(fa.hasModifier(Modifier.PUBLIC));
  }

  @Test
    @Timeout(8000)
  void hasModifier_returnsFalseIfModifierNotPresent() {
    assertFalse(fieldAttributes.hasModifier(Modifier.PRIVATE));
  }

  @Test
    @Timeout(8000)
  void toString_containsFieldNameAndDeclaringClass() {
    String str = fieldAttributes.toString();
    assertTrue(str.contains("publicField"));
    assertTrue(str.contains("TestClass"));
  }
}