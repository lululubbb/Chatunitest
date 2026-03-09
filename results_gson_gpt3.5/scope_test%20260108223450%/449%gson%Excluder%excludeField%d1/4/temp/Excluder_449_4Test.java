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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class Excluder_449_4Test {

  private Excluder excluder;
  private Field mockField;

  @BeforeEach
  void setUp() throws Exception {
    excluder = new Excluder();
    mockField = Dummy.class.getDeclaredField("field");
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsTrue_whenModifierMatches() throws Exception {
    // Set modifiers to include TRANSIENT (which is set on dummyField)
    Excluder excluderSpy = spy(excluder);
    Field modifiersField = Excluder.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(excluderSpy, mockField.getModifiers());

    boolean result = excluderSpy.excludeField(mockField, true);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsTrue_whenVersionInvalid() throws Exception {
    // Set version to 1.0 to trigger version check
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.setDouble(excluder, 1.0);

    // Create a field with Since annotation with version 2.0 (invalid)
    Field sinceField = DummyWithSince.class.getDeclaredField("field");
    boolean result = excluder.excludeField(sinceField, true);
    assertTrue(result);

    // Create a field with Until annotation with version 0.5 (invalid)
    Field untilField = DummyWithUntil.class.getDeclaredField("field");
    boolean result2 = excluder.excludeField(untilField, true);
    assertTrue(result2);
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsTrue_whenFieldIsSynthetic() throws Exception {
    // Mock Field to simulate synthetic field
    Field syntheticField = mock(Field.class);
    when(syntheticField.getModifiers()).thenReturn(0);
    when(syntheticField.isSynthetic()).thenReturn(true);
    when(syntheticField.getAnnotation(Since.class)).thenReturn(null);
    when(syntheticField.getAnnotation(Until.class)).thenReturn(null);
    when(syntheticField.getAnnotation(Expose.class)).thenReturn(null);

    // Fix: use raw Class for getType() to avoid generic mismatch
    when(syntheticField.getType()).thenReturn((Class) Object.class);

    boolean result = excluder.excludeField(syntheticField, true);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsTrue_whenRequireExposeAndNoExposeAnnotation() throws Exception {
    excluder.excludeFieldsWithoutExposeAnnotation();
    Field noExposeField = Dummy.class.getDeclaredField("field");
    boolean result = excluder.excludeField(noExposeField, true);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsTrue_whenRequireExposeAndAnnotationSerializeFalse() throws Exception {
    excluder.excludeFieldsWithoutExposeAnnotation();
    Field exposeFalseSerializeField = DummyExposeFalseSerialize.class.getDeclaredField("field");
    boolean result = excluder.excludeField(exposeFalseSerializeField, true);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsTrue_whenRequireExposeAndAnnotationDeserializeFalse() throws Exception {
    excluder.excludeFieldsWithoutExposeAnnotation();
    Field exposeFalseDeserializeField = DummyExposeFalseDeserialize.class.getDeclaredField("field");
    boolean result = excluder.excludeField(exposeFalseDeserializeField, false);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsTrue_whenSerializeInnerClassesFalseAndFieldIsInnerClass() throws Exception {
    excluder.disableInnerClassSerialization();
    Field innerClassField = DummyInnerClassHolder.class.getDeclaredField("inner");
    boolean result = excluder.excludeField(innerClassField, true);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsTrue_whenFieldTypeIsAnonymousOrNonStaticLocal() throws Exception {
    // Fix: ensure field type is anonymous class by accessing the actual anonymous instance class
    Field anonField = DummyAnonymousClassHolder.class.getDeclaredField("anon");
    anonField.setAccessible(true);
    DummyAnonymousClassHolder holder = new DummyAnonymousClassHolder();
    Object anonInstance = anonField.get(holder);

    Field anonFieldForTest = mock(Field.class);
    when(anonFieldForTest.getModifiers()).thenReturn(0);
    when(anonFieldForTest.isSynthetic()).thenReturn(false);
    when(anonFieldForTest.getAnnotation(Since.class)).thenReturn(null);
    when(anonFieldForTest.getAnnotation(Until.class)).thenReturn(null);
    when(anonFieldForTest.getAnnotation(Expose.class)).thenReturn(null);
    when(anonFieldForTest.getType()).thenReturn(anonInstance.getClass());

    boolean result = excluder.excludeField(anonFieldForTest, true);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsTrue_whenSerializationStrategySkipsField() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any(FieldAttributes.class))).thenReturn(true);

    excluder = new Excluder()
        .withExclusionStrategy(strategy, true, false);

    boolean result = excluder.excludeField(mockField, true);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsTrue_whenDeserializationStrategySkipsField() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any(FieldAttributes.class))).thenReturn(true);

    excluder = new Excluder()
        .withExclusionStrategy(strategy, false, true);

    boolean result = excluder.excludeField(mockField, false);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsFalse_whenNoExclusionConditionsMet() throws Exception {
    // Use default excluder with default field that should not be excluded
    Field normalField = DummyNormal.class.getDeclaredField("field");
    boolean result = excluder.excludeField(normalField, true);
    assertFalse(result);
  }

  // Dummy classes and fields for testing

  private static class Dummy {
    transient int field;
  }

  private static class DummyWithSince {
    @Since(2.0)
    int field;
  }

  private static class DummyWithUntil {
    @Until(0.5)
    int field;
  }

  private static class DummyExposeFalseSerialize {
    @Expose(serialize = false)
    int field;
  }

  private static class DummyExposeFalseDeserialize {
    @Expose(deserialize = false)
    int field;
  }

  private static class DummyNormal {
    int field;
  }

  private static class DummyInnerClassHolder {
    Inner inner = new Inner();

    static class Inner {}
  }

  private static class DummyAnonymousClassHolder {
    Object anon = new Runnable() {
      @Override public void run() {}
    };
  }
}