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
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

class Excluder_449_5Test {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  // Helper class with fields for reflection testing
  static class TestClass {
    int normalField;
    transient int transientField;
    static int staticField;
    @Since(1)
    int sinceField;
    @Until(2)
    int untilField;
    @Expose(serialize = true, deserialize = false)
    int exposeFieldSerializeOnly;
    @Expose(serialize = false, deserialize = true)
    int exposeFieldDeserializeOnly;
    int syntheticField;
  }

  // Synthetic field simulation helper
  static Field makeSyntheticField(Field field) throws Exception {
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() | Modifier.TRANSIENT);
    Field syntheticField = Field.class.getDeclaredField("synthetic");
    syntheticField.setAccessible(true);
    syntheticField.setBoolean(field, true);
    return field;
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesByModifier() throws Exception {
    // Set excluder modifiers to transient and static
    excluder = excluder.withModifiers(Modifier.TRANSIENT, Modifier.STATIC);

    Field transientField = TestClass.class.getDeclaredField("transientField");
    Field staticField = TestClass.class.getDeclaredField("staticField");
    Field normalField = TestClass.class.getDeclaredField("normalField");

    assertTrue(excluder.excludeField(transientField, true));
    assertTrue(excluder.excludeField(staticField, true));
    assertFalse(excluder.excludeField(normalField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesByVersion() throws Exception {
    excluder = excluder.withVersion(1.5);

    Field sinceField = TestClass.class.getDeclaredField("sinceField");
    Field untilField = TestClass.class.getDeclaredField("untilField");
    Field normalField = TestClass.class.getDeclaredField("normalField");

    // since=1, version=1.5 valid (1 <= 1.5)
    assertFalse(excluder.excludeField(sinceField, true));
    // until=2, version=1.5 valid (1.5 < 2)
    assertFalse(excluder.excludeField(untilField, true));

    // Now test with version below since
    excluder = excluder.withVersion(0.5);
    assertTrue(excluder.excludeField(sinceField, true)); // version < since => exclude
    assertFalse(excluder.excludeField(untilField, true)); // version < until => include

    // Now test with version above until
    excluder = excluder.withVersion(2.5);
    assertFalse(excluder.excludeField(sinceField, true)); // version > since => include
    assertTrue(excluder.excludeField(untilField, true)); // version > until => exclude
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesSyntheticField() throws Exception {
    Field field = TestClass.class.getDeclaredField("normalField");
    // Use reflection to set synthetic to true
    Field syntheticField = Field.class.getDeclaredField("synthetic");
    syntheticField.setAccessible(true);
    syntheticField.setBoolean(field, true);
    assertTrue(excluder.excludeField(field, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesWithoutExposeWhenRequired() throws Exception {
    excluder = excluder.excludeFieldsWithoutExposeAnnotation();

    Field exposeFieldSerializeOnly = TestClass.class.getDeclaredField("exposeFieldSerializeOnly");
    Field exposeFieldDeserializeOnly = TestClass.class.getDeclaredField("exposeFieldDeserializeOnly");
    Field normalField = TestClass.class.getDeclaredField("normalField");

    // serialize = true => exposeFieldSerializeOnly serialize()=true => included (false)
    assertFalse(excluder.excludeField(exposeFieldSerializeOnly, true));
    // serialize = false => exposeFieldSerializeOnly deserialize()=false => excluded (true)
    assertTrue(excluder.excludeField(exposeFieldSerializeOnly, false));

    // serialize = true => exposeFieldDeserializeOnly serialize()=false => excluded (true)
    assertTrue(excluder.excludeField(exposeFieldDeserializeOnly, true));
    // serialize = false => exposeFieldDeserializeOnly deserialize()=true => included (false)
    assertFalse(excluder.excludeField(exposeFieldDeserializeOnly, false));

    // normalField no Expose annotation => excluded always when requireExpose is true
    assertTrue(excluder.excludeField(normalField, true));
    assertTrue(excluder.excludeField(normalField, false));
  }

  static class InnerClass {}
  static class OuterClass {
    class NonStaticInner {}
    static class StaticInner {}
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesInnerClassesWhenDisabled() throws Exception {
    excluder = excluder.disableInnerClassSerialization();

    Field innerClassField = InnerClassHolder.class.getDeclaredField("inner");
    Field staticInnerClassField = StaticInnerClassHolder.class.getDeclaredField("staticInner");
    Field normalField = TestClass.class.getDeclaredField("normalField");

    // inner class field type is inner class => excluded
    assertTrue(excluder.excludeField(innerClassField, true));
    // static inner class field type is static inner class => not excluded
    assertFalse(excluder.excludeField(staticInnerClassField, true));
    // normal field => not excluded
    assertFalse(excluder.excludeField(normalField, true));
  }

  static class InnerClassHolder {
    InnerClass inner;
  }

  static class StaticInnerClassHolder {
    OuterClass.StaticInner staticInner;
  }

  static class AnonymousClassHolder {
    Object anon = new Runnable() {
      @Override
      public void run() {}
    };
  }

  static class NonStaticLocalClassHolder {
    Object nonStaticLocal = new Object() {
    };
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesAnonymousOrNonStaticLocal() throws Exception {
    Field anonField = AnonymousClassHolder.class.getDeclaredField("anon");
    Field nonStaticLocalField = NonStaticLocalClassHolder.class.getDeclaredField("nonStaticLocal");
    Field normalField = TestClass.class.getDeclaredField("normalField");

    assertTrue(excluder.excludeField(anonField, true));
    assertTrue(excluder.excludeField(nonStaticLocalField, true));
    assertFalse(excluder.excludeField(normalField, true));
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesBySerializationStrategies() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any(FieldAttributes.class))).thenReturn(true);

    excluder = excluder.withExclusionStrategy(strategy, true, false);

    Field normalField = TestClass.class.getDeclaredField("normalField");

    assertTrue(excluder.excludeField(normalField, true));
    assertFalse(excluder.excludeField(normalField, false));

    ArgumentCaptor<FieldAttributes> captor = ArgumentCaptor.forClass(FieldAttributes.class);
    verify(strategy, times(1)).shouldSkipField(captor.capture());
    assertEquals(normalField, captor.getValue().getField());
  }

  @Test
    @Timeout(8000)
  void excludeField_excludesByDeserializationStrategies() throws Exception {
    ExclusionStrategy strategy = mock(ExclusionStrategy.class);
    when(strategy.shouldSkipField(any(FieldAttributes.class))).thenReturn(true);

    excluder = excluder.withExclusionStrategy(strategy, false, true);

    Field normalField = TestClass.class.getDeclaredField("normalField");

    assertTrue(excluder.excludeField(normalField, false));
    assertFalse(excluder.excludeField(normalField, true));

    ArgumentCaptor<FieldAttributes> captor = ArgumentCaptor.forClass(FieldAttributes.class);
    verify(strategy, times(1)).shouldSkipField(captor.capture());
    assertEquals(normalField, captor.getValue().getField());
  }

  @Test
    @Timeout(8000)
  void excludeField_returnsFalseIfNoExclusion() throws Exception {
    Field normalField = TestClass.class.getDeclaredField("normalField");
    // Default excluder should not exclude normal field
    assertFalse(excluder.excludeField(normalField, true));
    assertFalse(excluder.excludeField(normalField, false));
  }
}