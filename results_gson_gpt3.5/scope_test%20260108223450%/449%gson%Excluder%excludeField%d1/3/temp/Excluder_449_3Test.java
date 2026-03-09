package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class Excluder_449_3Test {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesByModifier() throws NoSuchFieldException {
    Field field = DummyClass.class.getDeclaredField("staticField");
    // staticField is static, modifiers include Modifier.STATIC by default in excluder
    assertTrue(excluder.excludeField(field, true));
    assertTrue(excluder.excludeField(field, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesByVersion_invalidSince() throws Exception {
    Field field = DummyClass.class.getDeclaredField("sinceField");
    // Set version to 1.0 to trigger version check
    excluder = excluder.withVersion(1.0);

    // Since annotation with value 2.0, which is > 1.0, so invalid
    assertTrue(excluder.excludeField(field, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesByVersion_invalidUntil() throws Exception {
    Field field = DummyClass.class.getDeclaredField("untilField");
    excluder = excluder.withVersion(1.0);

    // Until annotation with value 0.5, which is < 1.0, so invalid
    assertTrue(excluder.excludeField(field, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesSyntheticField() throws Exception {
    // We cannot set synthetic true directly, so create a mock Field instead
    Field mockField = mock(Field.class);
    when(mockField.getModifiers()).thenReturn(0);
    when(mockField.isSynthetic()).thenReturn(true);
    when(mockField.getAnnotation(Since.class)).thenReturn(null);
    when(mockField.getAnnotation(Until.class)).thenReturn(null);
    when(mockField.getAnnotation(Expose.class)).thenReturn(null);
    // Fix: cast to raw Class to avoid generic capture error
    when(mockField.getType()).thenReturn((Class) Object.class);

    assertTrue(excluder.excludeField(mockField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesWithoutExposeAnnotationWhenRequired() throws Exception {
    excluder = excluder.excludeFieldsWithoutExposeAnnotation();
    Field field = DummyClass.class.getDeclaredField("noExposeField");
    assertTrue(excluder.excludeField(field, true));
    assertTrue(excluder.excludeField(field, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesWithExposeAnnotationSerializeFalse() throws Exception {
    excluder = excluder.excludeFieldsWithoutExposeAnnotation();
    Field field = DummyClass.class.getDeclaredField("exposeDeserializeOnly");
    // serialize == true but expose.serialize() == false
    assertTrue(excluder.excludeField(field, true));
    // serialize == false and expose.deserialize() == true => do not exclude
    assertFalse(excluder.excludeField(field, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesWithExposeAnnotationDeserializeFalse() throws Exception {
    excluder = excluder.excludeFieldsWithoutExposeAnnotation();
    Field field = DummyClass.class.getDeclaredField("exposeSerializeOnly");
    // serialize == true and expose.serialize() == true => do not exclude
    assertFalse(excluder.excludeField(field, true));
    // serialize == false but expose.deserialize() == false => exclude
    assertTrue(excluder.excludeField(field, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesInnerClassWhenDisabled() throws Exception {
    excluder = excluder.disableInnerClassSerialization();
    Field field = DummyClass.class.getDeclaredField("innerClassField");
    // Access the field's type and ensure it's an inner class
    Class<?> fieldType = field.getType();
    assertTrue(fieldType.getEnclosingClass() == DummyClass.class);
    assertTrue(excluder.excludeField(field, true));
    // deserialization should not exclude inner classes
    assertFalse(excluder.excludeField(field, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesAnonymousOrNonStaticLocalClass() throws Exception {
    Field field = DummyClass.class.getDeclaredField("anonymousClassField");
    Class<?> type = field.getType();
    // The field is declared as Object, but the actual instance is anonymous class
    // So to test properly, we need to mock a Field whose type is anonymous class

    // Create an anonymous class instance
    Object anonymousInstance = new Object() {};
    Class<?> anonymousClass = anonymousInstance.getClass();

    // Mock Field to return the anonymous class as type
    Field mockField = mock(Field.class);
    when(mockField.getModifiers()).thenReturn(0);
    when(mockField.isSynthetic()).thenReturn(false);
    when(mockField.getAnnotation(Since.class)).thenReturn(null);
    when(mockField.getAnnotation(Until.class)).thenReturn(null);
    when(mockField.getAnnotation(Expose.class)).thenReturn(null);
    when(mockField.getType()).thenReturn((Class) anonymousClass);

    assertTrue(excluder.excludeField(mockField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesBySerializationStrategy() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any(FieldAttributes.class))).thenReturn(true);
    excluder = excluder.withExclusionStrategy(strategy, true, false);

    Field field = DummyClass.class.getDeclaredField("noExposeField");
    assertTrue(excluder.excludeField(field, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesByDeserializationStrategy() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any(FieldAttributes.class))).thenReturn(true);
    excluder = excluder.withExclusionStrategy(strategy, false, true);

    Field field = DummyClass.class.getDeclaredField("noExposeField");
    assertTrue(excluder.excludeField(field, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_doesNotExcludeWhenNoConditionsMet() throws Exception {
    Field field = DummyClass.class.getDeclaredField("normalField");
    assertFalse(excluder.excludeField(field, true));
    assertFalse(excluder.excludeField(field, false));
  }

  /*
   * Dummy classes and fields to test various conditions
   */
  static class DummyClass {
    public static int staticField;

    @Since(2.0)
    public int sinceField;

    @Until(0.5)
    public int untilField;

    public int normalField;

    @Expose(serialize = false, deserialize = true)
    public int exposeDeserializeOnly;

    @Expose(serialize = true, deserialize = false)
    public int exposeSerializeOnly;

    public InnerClass innerClassField = new InnerClass();

    public Object anonymousClassField = new Object() {};

    public int noExposeField;

    static class InnerClass {}
  }

  static class SyntheticFieldHolder {
    // We will mock synthetic field instead of using this
    int synthetic;
  }
}