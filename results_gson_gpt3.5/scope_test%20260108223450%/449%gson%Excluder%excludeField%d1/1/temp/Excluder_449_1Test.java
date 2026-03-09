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

class Excluder_449_1Test {

  private Excluder excluder;
  private Field mockField;

  @BeforeEach
  void setUp() throws NoSuchFieldException {
    excluder = new Excluder();

    // Create a dummy field to test with reflection
    mockField = DummyClass.class.getDeclaredField("field");
  }

  @Test
    @Timeout(8000)
  void excludeField_excludeByModifier() throws Exception {
    // Set modifiers to include TRANSIENT (which is set on dummyField)
    excluder = excluder.withModifiers(Modifier.TRANSIENT);

    Field transientField = DummyClass.class.getDeclaredField("transientField");
    assertTrue(excluder.excludeField(transientField, true));
    assertTrue(excluder.excludeField(transientField, false));

    Field normalField = DummyClass.class.getDeclaredField("field");
    assertFalse(excluder.excludeField(normalField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludeByVersion_invalidSince() throws Exception {
    // Set version to 1.0
    excluder = excluder.withVersion(1.0);

    Field sinceField = DummyClass.class.getDeclaredField("sinceField");
    // since = 2.0 > 1.0 so invalid version -> exclude true
    assertTrue(excluder.excludeField(sinceField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludeByVersion_invalidUntil() throws Exception {
    // Set version to 3.0
    excluder = excluder.withVersion(3.0);

    Field untilField = DummyClass.class.getDeclaredField("untilField");
    // until = 2.0 < 3.0 so invalid version -> exclude true
    assertTrue(excluder.excludeField(untilField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludeSyntheticField() throws Exception {
    Field syntheticField = DummyClass.class.getDeclaredField("syntheticField");
    // Manually make isSynthetic return true by mocking Field
    Field field = mock(Field.class);
    when(field.getModifiers()).thenReturn(0);
    when(field.isSynthetic()).thenReturn(true);

    assertTrue(excluder.excludeField(field, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_requireExpose_serialize() throws Exception {
    excluder = excluder.excludeFieldsWithoutExposeAnnotation();

    Field exposedField = DummyClass.class.getDeclaredField("exposeSerializeTrue");
    Field exposedFieldNoSerialize = DummyClass.class.getDeclaredField("exposeSerializeFalse");

    assertFalse(excluder.excludeField(exposedField, true));
    assertTrue(excluder.excludeField(exposedFieldNoSerialize, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_requireExpose_deserialize() throws Exception {
    excluder = excluder.excludeFieldsWithoutExposeAnnotation();

    Field exposedField = DummyClass.class.getDeclaredField("exposeDeserializeTrue");
    Field exposedFieldNoDeserialize = DummyClass.class.getDeclaredField("exposeDeserializeFalse");

    assertFalse(excluder.excludeField(exposedField, false));
    assertTrue(excluder.excludeField(exposedFieldNoDeserialize, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludeInnerClassSerialization() throws Exception {
    excluder = excluder.disableInnerClassSerialization();

    Field innerClassField = DummyClass.class.getDeclaredField("innerClassField");
    // To ensure isInnerClass returns true, the field type must be a non-static inner class (DummyClass.InnerClass is static)
    // So create a non-static inner class for this test
    class NonStaticInnerClass {}
    Field nonStaticInnerField = DummyClassWithNonStaticInnerClass.class.getDeclaredField("innerClassField");

    assertTrue(excluder.excludeField(nonStaticInnerField, true));

    // Deserialization should not exclude
    assertFalse(excluder.excludeField(nonStaticInnerField, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludeAnonymousOrNonStaticLocal() throws Exception {
    Field anonClassField = DummyClass.class.getDeclaredField("anonClassField");
    assertTrue(excluder.excludeField(anonClassField, true));
    assertTrue(excluder.excludeField(anonClassField, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludeBySerializationStrategy() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any())).thenReturn(true);

    excluder = excluder.withExclusionStrategy(strategy, true, false);

    Field field = DummyClass.class.getDeclaredField("field");
    assertTrue(excluder.excludeField(field, true));
    assertFalse(excluder.excludeField(field, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludeByDeserializationStrategy() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any())).thenReturn(true);

    excluder = excluder.withExclusionStrategy(strategy, false, true);

    Field field = DummyClass.class.getDeclaredField("field");
    assertFalse(excluder.excludeField(field, true));
    assertTrue(excluder.excludeField(field, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_notExcludedWhenNoConditionsMet() throws Exception {
    Field normalField = DummyClass.class.getDeclaredField("field");
    assertFalse(excluder.excludeField(normalField, true));
    assertFalse(excluder.excludeField(normalField, false));
  }

  // Dummy class with annotated fields for testing
  private static class DummyClass {
    public int field;

    public transient int transientField;

    @Since(2.0)
    public int sinceField;

    @Until(2.0)
    public int untilField;

    @Expose(serialize = true, deserialize = false)
    public int exposeSerializeTrue;

    @Expose(serialize = false, deserialize = true)
    public int exposeSerializeFalse;

    @Expose(serialize = false, deserialize = true)
    public int exposeDeserializeTrue;

    @Expose(serialize = true, deserialize = false)
    public int exposeDeserializeFalse;

    public InnerClass innerClassField;

    public Object anonClassField = new Object() {};

    public int syntheticField; // We mock this to be synthetic

    static class InnerClass {}
  }

  // Added this class to provide a non-static inner class for testing isInnerClass correctly
  private static class DummyClassWithNonStaticInnerClass {
    public NonStaticInnerClass innerClassField;

    class NonStaticInnerClass {}
  }
}