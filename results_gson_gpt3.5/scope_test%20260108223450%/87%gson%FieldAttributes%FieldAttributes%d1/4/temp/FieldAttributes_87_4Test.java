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

class FieldAttributes_87_4Test {

  private Field fieldMock;
  private FieldAttributes fieldAttributes;

  static class DummyClass {
    @Deprecated
    public int dummyField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    fieldMock = DummyClass.class.getField("dummyField");
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
    assertEquals(DummyClass.class, fieldAttributes.getDeclaringClass());
  }

  @Test
    @Timeout(8000)
  void getName_returnsFieldName() {
    assertEquals("dummyField", fieldAttributes.getName());
  }

  @Test
    @Timeout(8000)
  void getDeclaredType_returnsFieldGenericType() {
    assertEquals(fieldMock.getGenericType(), fieldAttributes.getDeclaredType());
  }

  @Test
    @Timeout(8000)
  void getDeclaredClass_returnsFieldType() {
    assertEquals(fieldMock.getType(), fieldAttributes.getDeclaredClass());
  }

  @Test
    @Timeout(8000)
  void getAnnotation_existingAnnotation_returnsAnnotation() {
    Deprecated annotation = fieldAttributes.getAnnotation(Deprecated.class);
    assertNotNull(annotation);
    assertTrue(annotation instanceof Deprecated);
  }

  @Test
    @Timeout(8000)
  void getAnnotation_nonExistingAnnotation_returnsNull() {
    Override annotation = fieldAttributes.getAnnotation(Override.class);
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
    String str = fieldAttributes.toString();
    assertNotNull(str);
    assertTrue(str.contains("dummyField"));
  }
}