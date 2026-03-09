package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

class Excluder_456_3Test {

  private Excluder excluder;
  private Method isValidVersionMethod;

  @BeforeEach
  void setUp() throws Exception {
    excluder = new Excluder();
    isValidVersionMethod = Excluder.class.getDeclaredMethod("isValidVersion", Since.class, Until.class);
    isValidVersionMethod.setAccessible(true);
  }

  private Since createSince(final double value) {
    return new Since() {
      @Override
      public double value() {
        return value;
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return Since.class;
      }
    };
  }

  private Until createUntil(final double value) {
    return new Until() {
      @Override
      public double value() {
        return value;
      }

      @Override
      public Class<? extends Annotation> annotationType() {
        return Until.class;
      }
    };
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_noAnnotations() throws Exception {
    // since=null, until=null
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, null, null);
    // isValidSince(null) and isValidUntil(null) both return true by source code logic, so expect true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceValid_untilNull() throws Exception {
    Since since = createSince(0.0);
    // Set version to 1.0 to make sinceValid meaningful
    excluder = excluder.withVersion(1.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceInvalid_untilNull() throws Exception {
    Since since = createSince(2.0);
    // set version to 1.0 to make since invalid (since.value > version)
    excluder = excluder.withVersion(1.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, null);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceNull_untilValid() throws Exception {
    Until until = createUntil(2.0);
    excluder = excluder.withVersion(1.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, null, until);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceNull_untilInvalid() throws Exception {
    Until until = createUntil(0.5);
    excluder = excluder.withVersion(1.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, null, until);
    // until.value < version, so isValidUntil returns false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceValid_untilValid() throws Exception {
    Since since = createSince(0.5);
    Until until = createUntil(2.0);
    excluder = excluder.withVersion(1.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, until);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceInvalid_untilValid() throws Exception {
    Since since = createSince(2.0);
    Until until = createUntil(2.0);
    excluder = excluder.withVersion(1.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, until);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceValid_untilInvalid() throws Exception {
    Since since = createSince(0.5);
    Until until = createUntil(0.5);
    excluder = excluder.withVersion(1.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, until);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceInvalid_untilInvalid() throws Exception {
    Since since = createSince(2.0);
    Until until = createUntil(0.5);
    excluder = excluder.withVersion(1.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, until);
    assertFalse(result);
  }
}