package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class Excluder_449_2Test {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  static class DummyClass {
    public int normalField;
    public transient int transientField;
    public static int staticField;
    @Expose(serialize = true, deserialize = true)
    public int exposedField;
    @Since(1.0)
    public int sinceField;
    @Until(2.0)
    public int untilField;
    int syntheticField;
  }

  @Test
    @Timeout(8000)
  void excludeField_modifiersMatch_returnsTrue() throws Exception {
    Field field = DummyClass.class.getField("transientField");
    // transient modifier is in default excluder.modifiers (TRANSIENT | STATIC)
    assertTrue(excluder.excludeField(field, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_versionNotValid_returnsTrue() throws Exception {
    excluder = excluder.withVersion(1.5);
    Field sinceField = DummyClass.class.getField("sinceField");
    // Since=1.0 < 1.5 is valid, so excludeField should return false for sinceField
    assertFalse(excluder.excludeField(sinceField, true));

    Field untilField = DummyClass.class.getField("untilField");
    // Until=2.0 > 1.5 so valid, excludeField should return false
    assertFalse(excluder.excludeField(untilField, true));

    // Now test with a version that excludes sinceField
    excluder = excluder.withVersion(0.5);
    assertTrue(excluder.excludeField(sinceField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_syntheticField_returnsTrue() throws Exception {
    Field syntheticField = DummyClass.class.getDeclaredField("syntheticField");
    // We simulate synthetic field by mocking isSynthetic() to true
    Field spyField = spy(syntheticField);
    doReturn(true).when(spyField).isSynthetic();

    assertTrue(excluder.excludeField(spyField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_requireExposeAndNoAnnotation_returnsTrue() throws Exception {
    excluder = excluder.excludeFieldsWithoutExposeAnnotation();
    Field field = DummyClass.class.getField("normalField");
    assertTrue(excluder.excludeField(field, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_requireExposeAndAnnotationSerializeFalse_returnsTrue() throws Exception {
    excluder = excluder.excludeFieldsWithoutExposeAnnotation();
    // Create a field with @Expose(serialize=false)
    class ExposeFalse {
      @Expose(serialize = false, deserialize = true)
      int field;
    }
    Field field = ExposeFalse.class.getDeclaredField("field");
    assertTrue(excluder.excludeField(field, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_requireExposeAndAnnotationDeserializeFalse_returnsTrue() throws Exception {
    excluder = excluder.excludeFieldsWithoutExposeAnnotation();
    class ExposeFalse {
      @Expose(serialize = true, deserialize = false)
      int field;
    }
    Field field = ExposeFalse.class.getDeclaredField("field");
    assertTrue(excluder.excludeField(field, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_serializeInnerClassesFalseAndInnerClass_returnsTrue() throws Exception {
    excluder = excluder.disableInnerClassSerialization();
    class Inner {}
    Field field = Inner.class.getDeclaredFields().length > 0 ?
      Inner.class.getDeclaredFields()[0] :
      Inner.class.getDeclaredField("class"); // fallback, won't matter

    // Use a field of type Inner class
    Field dummyField = DummyClass.class.getDeclaredField("normalField");
    Field spyField = spy(dummyField);
    doReturn(Inner.class).when(spyField).getType();

    assertTrue(excluder.excludeField(spyField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_anonymousOrNonStaticLocalClass_returnsTrue() throws Exception {
    // Create anonymous class
    Object anonymous = new Object() {};
    Class<?> anonClass = anonymous.getClass();

    Field dummyField = DummyClass.class.getDeclaredField("normalField");
    Field spyField = spy(dummyField);
    doReturn(anonClass).when(spyField).getType();

    assertTrue(excluder.excludeField(spyField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_serializationStrategySkipsField_returnsTrue() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any(FieldAttributes.class))).thenReturn(true);
    excluder = excluder.withExclusionStrategy(strategy, true, false);

    Field field = DummyClass.class.getDeclaredField("normalField");
    assertTrue(excluder.excludeField(field, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_deserializationStrategySkipsField_returnsTrue() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any(FieldAttributes.class))).thenReturn(true);
    excluder = excluder.withExclusionStrategy(strategy, false, true);

    Field field = DummyClass.class.getDeclaredField("normalField");
    assertTrue(excluder.excludeField(field, false));
  }

  @Test
    @Timeout(8000)
  void excludeField_noConditionsMet_returnsFalse() throws Exception {
    // Use default excluder with no version set, no requireExpose, no strategies
    Field field = DummyClass.class.getDeclaredField("normalField");
    assertFalse(excluder.excludeField(field, true));
    assertFalse(excluder.excludeField(field, false));
  }
}