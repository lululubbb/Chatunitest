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
import java.util.List;

class ExcluderExcludeFieldTest {

  private Excluder excluder;
  private Field field;

  static class TestClass {
    public int field1;
    @Expose(serialize = true, deserialize = true)
    public int exposedField;
    @Since(1.0)
    public int sinceField;
    @Until(2.0)
    public int untilField;
    public int syntheticField;
    public static int staticField;
    public transient int transientField;
    public int nonStaticInnerField;
  }

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    excluder = new Excluder();
    excluder = excluder.withVersion(1.5)
        .withModifiers(Modifier.TRANSIENT | Modifier.STATIC)
        .excludeFieldsWithoutExposeAnnotation();
    // Prepare a real field for tests
    field = TestClass.class.getDeclaredField("field1");
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesByModifier() throws NoSuchFieldException {
    Field staticField = TestClass.class.getDeclaredField("staticField");
    Field transientField = TestClass.class.getDeclaredField("transientField");

    // static field should be excluded
    assertTrue(excluder.excludeField(staticField, true));
    // transient field should be excluded
    assertTrue(excluder.excludeField(transientField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesByVersion() throws NoSuchFieldException {
    Excluder excluderVersioned = new Excluder().withVersion(1.0);
    Field sinceField = TestClass.class.getDeclaredField("sinceField");
    Field untilField = TestClass.class.getDeclaredField("untilField");

    // sinceField has @Since(1.0), version 1.0 is valid, so not excluded
    assertFalse(excluderVersioned.excludeField(sinceField, true));
    // untilField has @Until(2.0), version 1.0 is valid, so not excluded
    assertFalse(excluderVersioned.excludeField(untilField, true));

    Excluder excluderVersionLow = new Excluder().withVersion(0.5);

    // sinceField @Since(1.0) excludes version 0.5 < 1.0
    assertTrue(excluderVersionLow.excludeField(sinceField, true));
    // untilField @Until(2.0) version 0.5 < 2.0 so included (not excluded)
    assertFalse(excluderVersionLow.excludeField(untilField, true));

    Excluder excluderVersionHigh = new Excluder().withVersion(3.0);

    // untilField @Until(2.0) version 3.0 > 2.0 so excluded
    assertTrue(excluderVersionHigh.excludeField(untilField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesSyntheticField() throws NoSuchFieldException {
    Field syntheticField = mock(Field.class);
    when(syntheticField.getModifiers()).thenReturn(0);
    when(syntheticField.isSynthetic()).thenReturn(true);

    assertTrue(excluder.excludeField(syntheticField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesWithoutExposeAnnotationWhenRequired() throws NoSuchFieldException {
    Excluder excluderRequireExpose = new Excluder().excludeFieldsWithoutExposeAnnotation();
    Field noExposeField = TestClass.class.getDeclaredField("field1");
    Field exposedField = TestClass.class.getDeclaredField("exposedField");

    // field without @Expose excluded for serialize=true
    assertTrue(excluderRequireExpose.excludeField(noExposeField, true));
    // field with @Expose(serialize=true) included for serialize=true
    assertFalse(excluderRequireExpose.excludeField(exposedField, true));
    // field with @Expose(serialize=true) excluded for serialize=false (deserialize)
    assertTrue(excluderRequireExpose.excludeField(exposedField, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesInnerClassWhenDisabled() throws NoSuchFieldException {
    class Inner {}
    // Inner class has no fields, so innerField is null but unused

    Excluder excluderNoInner = new Excluder().disableInnerClassSerialization();

    // We simulate inner class type by mocking field.getType()
    Field fieldMock = mock(Field.class);
    when(fieldMock.getModifiers()).thenReturn(0);
    when(fieldMock.isSynthetic()).thenReturn(false);
    when(fieldMock.getAnnotation(Expose.class)).thenReturn(null);
    // Fix: cast to Class<?> to avoid generic capture error
    @SuppressWarnings("unchecked")
    Class<?> innerClass = (Class<?>) Inner.class;
    when(fieldMock.getType()).thenReturn(innerClass);

    // Inner class with inner class serialization disabled should exclude
    assertTrue(excluderNoInner.excludeField(fieldMock, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesAnonymousOrNonStaticLocalClass() throws NoSuchFieldException {
    // Anonymous class
    Object anonymous = new Object() {};
    Class<?> anonymousClass = anonymous.getClass();

    Field fieldMock = mock(Field.class);
    when(fieldMock.getModifiers()).thenReturn(0);
    when(fieldMock.isSynthetic()).thenReturn(false);
    when(fieldMock.getAnnotation(Expose.class)).thenReturn(null);
    // Fix: cast to Class<?> to avoid generic capture error
    @SuppressWarnings("unchecked")
    Class<?> anonClass = (Class<?>) anonymousClass;
    when(fieldMock.getType()).thenReturn(anonClass);

    assertTrue(excluder.excludeField(fieldMock, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesBySerializationStrategy() throws NoSuchFieldException {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any(FieldAttributes.class))).thenReturn(true);

    Excluder excluderWithStrategy = new Excluder()
        .withExclusionStrategy(strategy, true, false)
        .excludeFieldsWithoutExposeAnnotation();

    Field fieldMock = TestClass.class.getDeclaredField("field1");

    assertTrue(excluderWithStrategy.excludeField(fieldMock, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesByDeserializationStrategy() throws NoSuchFieldException {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any(FieldAttributes.class))).thenReturn(true);

    Excluder excluderWithStrategy = new Excluder()
        .withExclusionStrategy(strategy, false, true)
        .excludeFieldsWithoutExposeAnnotation();

    Field fieldMock = TestClass.class.getDeclaredField("field1");

    assertTrue(excluderWithStrategy.excludeField(fieldMock, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsFalseWhenNoExclusionApplies() throws NoSuchFieldException, ReflectiveOperationException {
    // Use reflection to get IGNORE_VERSIONS since it's private
    double ignoreVersions;
    try {
      var fieldIgnoreVersions = Excluder.class.getDeclaredField("IGNORE_VERSIONS");
      fieldIgnoreVersions.setAccessible(true);
      ignoreVersions = fieldIgnoreVersions.getDouble(null);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      ignoreVersions = -1.0d; // fallback default
    }

    Excluder excluderNoExclusions = new Excluder()
        .withVersion(ignoreVersions)
        .withModifiers(0)
        .excludeFieldsWithoutExposeAnnotation(); // requireExpose=true

    Field fieldMock = mock(Field.class);
    when(fieldMock.getModifiers()).thenReturn(0);
    when(fieldMock.isSynthetic()).thenReturn(false);
    Expose expose = mock(Expose.class);
    when(expose.serialize()).thenReturn(true);
    when(expose.deserialize()).thenReturn(true);
    when(fieldMock.getAnnotation(Expose.class)).thenReturn(expose);
    when(fieldMock.getType()).thenReturn(String.class);

    // No exclusion strategies
    assertFalse(excluderNoExclusions.excludeField(fieldMock, true));
  }
}