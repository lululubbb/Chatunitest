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

class FieldAttributes_87_3Test {

  private Field fieldMock;
  private FieldAttributes fieldAttributes;

  static class DummyClass {
    @Deprecated
    private String dummyField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    fieldMock = DummyClass.class.getDeclaredField("dummyField");
    fieldAttributes = new FieldAttributes(fieldMock);
  }

  @Test
    @Timeout(8000)
  void constructor_nullField_throwsNPE() {
    assertThrows(NullPointerException.class, () -> new FieldAttributes(null));
  }

  @Test
    @Timeout(8000)
  void getDeclaringClass_returnsDeclaringClass() {
    assertEquals(DummyClass.class, fieldAttributes.getDeclaringClass());
  }

  @Test
    @Timeout(8000)
  void getName_returnsFieldName() {
    assertEquals("dummyField", fieldAttributes.getName());
  }

  @Test
    @Timeout(8000)
  void getDeclaredType_returnsGenericType() {
    Type type = fieldAttributes.getDeclaredType();
    assertEquals(String.class, type);
  }

  @Test
    @Timeout(8000)
  void getDeclaredClass_returnsFieldClass() {
    Class<?> clazz = fieldAttributes.getDeclaredClass();
    assertEquals(String.class, clazz);
  }

  @Test
    @Timeout(8000)
  void getAnnotation_existingAnnotation_returnsAnnotation() {
    Deprecated annotation = fieldAttributes.getAnnotation(Deprecated.class);
    assertNotNull(annotation);
    assertEquals(Deprecated.class, annotation.annotationType());
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
    assertFalse(annotations.isEmpty());
    boolean hasDeprecated = annotations.stream()
        .anyMatch(a -> a.annotationType() == Deprecated.class);
    assertTrue(hasDeprecated);
  }

  @Test
    @Timeout(8000)
  void hasModifier_checksModifiers() throws NoSuchFieldException {
    // dummyField is private
    assertTrue(fieldAttributes.hasModifier(Modifier.PRIVATE));
    assertFalse(fieldAttributes.hasModifier(Modifier.PUBLIC));
    assertFalse(fieldAttributes.hasModifier(Modifier.STATIC));

    // test a static field
    Field staticField = StaticDummy.class.getDeclaredField("staticField");
    FieldAttributes staticFieldAttributes = new FieldAttributes(staticField);
    assertTrue(staticFieldAttributes.hasModifier(Modifier.STATIC));
  }

  @Test
    @Timeout(8000)
  void toString_containsFieldName() {
    String str = fieldAttributes.toString();
    assertNotNull(str);
    assertTrue(str.contains("dummyField"));
  }

  static class StaticDummy {
    static int staticField;
  }
}