package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Until;
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
import static org.mockito.Mockito.*;

import com.google.gson.annotations.Since;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Excluder_isValidSince_Test {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  private Method getIsValidSinceMethod() throws NoSuchMethodException {
    Method method = Excluder.class.getDeclaredMethod("isValidSince", Since.class);
    method.setAccessible(true);
    return method;
  }

  @Test
    @Timeout(8000)
  void isValidSince_nullAnnotation_returnsTrue() throws Exception {
    Method method = getIsValidSinceMethod();

    boolean result = (boolean) method.invoke(excluder, new Object[] {null});

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidSince_versionLessThanAnnotationVersion_returnsFalse() throws Exception {
    Method method = getIsValidSinceMethod();

    // Set version to 1.0
    Excluder excluderWithVersion = excluder.withVersion(1.0);

    Since annotation = mock(Since.class);
    when(annotation.value()).thenReturn(1.5);

    boolean result = (boolean) method.invoke(excluderWithVersion, annotation);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isValidSince_versionEqualToAnnotationVersion_returnsTrue() throws Exception {
    Method method = getIsValidSinceMethod();

    Excluder excluderWithVersion = excluder.withVersion(2.0);

    Since annotation = mock(Since.class);
    when(annotation.value()).thenReturn(2.0);

    boolean result = (boolean) method.invoke(excluderWithVersion, annotation);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidSince_versionGreaterThanAnnotationVersion_returnsTrue() throws Exception {
    Method method = getIsValidSinceMethod();

    Excluder excluderWithVersion = excluder.withVersion(3.0);

    Since annotation = mock(Since.class);
    when(annotation.value()).thenReturn(2.5);

    boolean result = (boolean) method.invoke(excluderWithVersion, annotation);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidSince_defaultVersionIgnore_returnsTrueRegardlessOfAnnotation() throws Exception {
    Method method = getIsValidSinceMethod();

    // Default version is IGNORE_VERSIONS = -1.0d
    Since annotation = mock(Since.class);
    when(annotation.value()).thenReturn(1000.0);

    boolean result = (boolean) method.invoke(excluder, annotation);

    // Since the version is -1.0 (IGNORE_VERSIONS), the method returns true regardless of annotation
    assertTrue(result);
  }
}