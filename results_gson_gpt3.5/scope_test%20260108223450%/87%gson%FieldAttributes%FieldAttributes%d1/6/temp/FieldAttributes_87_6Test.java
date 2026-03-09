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

class FieldAttributes_87_6Test {

  private Field stringField;
  private FieldAttributes fieldAttributes;

  static class TestClass {
    public String publicField;
    private int privateField;
    @Deprecated
    protected double annotatedField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    stringField = TestClass.class.getDeclaredField("publicField");
    fieldAttributes = new FieldAttributes(stringField);
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
  void getName_returnsFieldName() {
    assertEquals("publicField", fieldAttributes.getName());
  }

  @Test
    @Timeout(8000)
  void getDeclaredType_returnsFieldType() {
    Type declaredType = fieldAttributes.getDeclaredType();
    assertEquals(String.class, declaredType);
  }

  @Test
    @Timeout(8000)
  void getDeclaredClass_returnsFieldClass() {
    Class<?> declaredClass = fieldAttributes.getDeclaredClass();
    assertEquals(String.class, declaredClass);
  }

  @Test
    @Timeout(8000)
  void getAnnotation_existingAnnotation_returnsAnnotation() throws NoSuchFieldException {
    Field annotatedField = TestClass.class.getDeclaredField("annotatedField");
    FieldAttributes fa = new FieldAttributes(annotatedField);
    Deprecated annotation = fa.getAnnotation(Deprecated.class);
    assertNotNull(annotation);
    assertTrue(annotation instanceof Deprecated);
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
    Field annotatedField = TestClass.class.getDeclaredField("annotatedField");
    FieldAttributes fa = new FieldAttributes(annotatedField);
    Collection<Annotation> annotations = fa.getAnnotations();
    assertNotNull(annotations);
    assertFalse(annotations.isEmpty());
    boolean foundDeprecated = annotations.stream().anyMatch(a -> a.annotationType() == Deprecated.class);
    assertTrue(foundDeprecated);
  }

  @Test
    @Timeout(8000)
  void hasModifier_returnsTrueForPublic() {
    assertTrue(fieldAttributes.hasModifier(Modifier.PUBLIC));
  }

  @Test
    @Timeout(8000)
  void hasModifier_returnsFalseForPrivate() {
    assertFalse(fieldAttributes.hasModifier(Modifier.PRIVATE));
  }

  @Test
    @Timeout(8000)
  void toString_containsFieldName() {
    String toString = fieldAttributes.toString();
    assertNotNull(toString);
    assertTrue(toString.contains("publicField"));
  }
}