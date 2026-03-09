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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.annotations.Since;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class Excluder_457_5Test {

  private Excluder excluder;
  private Method isValidSinceMethod;

  @BeforeEach
  void setUp() throws Exception {
    excluder = new Excluder();
    isValidSinceMethod = Excluder.class.getDeclaredMethod("isValidSince", Since.class);
    isValidSinceMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testIsValidSince_nullAnnotation() throws Exception {
    // When annotation is null, should return true
    Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, (Since) null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidSince_versionLessThanAnnotation() throws Exception {
    // Set excluder.version to 1.0
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.setDouble(excluder, 1.0d);

    // Create Since annotation mock with value 1.5
    Since sinceAnnotation = Mockito.mock(Since.class);
    Mockito.when(sinceAnnotation.value()).thenReturn(1.5d);

    Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, sinceAnnotation);
    // version 1.0 < annotation 1.5, expect false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidSince_versionEqualToAnnotation() throws Exception {
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.setDouble(excluder, 1.5d);

    Since sinceAnnotation = Mockito.mock(Since.class);
    Mockito.when(sinceAnnotation.value()).thenReturn(1.5d);

    Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, sinceAnnotation);
    // version == annotation, expect true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidSince_versionGreaterThanAnnotation() throws Exception {
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.setDouble(excluder, 2.0d);

    Since sinceAnnotation = Mockito.mock(Since.class);
    Mockito.when(sinceAnnotation.value()).thenReturn(1.5d);

    Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, sinceAnnotation);
    // version 2.0 > annotation 1.5, expect true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidSince_versionIgnoreVersions() throws Exception {
    // version = IGNORE_VERSIONS (-1.0d)
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.setDouble(excluder, -1.0d);

    Since sinceAnnotation = Mockito.mock(Since.class);
    Mockito.when(sinceAnnotation.value()).thenReturn(0.0d);

    Boolean result = (Boolean) isValidSinceMethod.invoke(excluder, sinceAnnotation);
    // version -1.0 < 0.0, expect false
    assertFalse(result);
  }
}