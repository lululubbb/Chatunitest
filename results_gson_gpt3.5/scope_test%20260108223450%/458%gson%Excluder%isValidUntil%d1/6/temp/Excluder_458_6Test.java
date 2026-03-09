package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.Since;
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

import com.google.gson.annotations.Until;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Excluder_isValidUntil_Test {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  @Test
    @Timeout(8000)
  void isValidUntil_annotationNull_returnsTrue() throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);

    Boolean result = (Boolean) method.invoke(excluder, new Object[] {null});
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidUntil_versionLessThanAnnotationValue_returnsTrue() throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);

    // Set version field to 1.0
    setVersion(excluder, 1.0);

    Until annotation = mock(Until.class);
    when(annotation.value()).thenReturn(2.0);

    Boolean result = (Boolean) method.invoke(excluder, annotation);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidUntil_versionEqualToAnnotationValue_returnsFalse() throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);

    // Set version field to 2.0
    setVersion(excluder, 2.0);

    Until annotation = mock(Until.class);
    when(annotation.value()).thenReturn(2.0);

    Boolean result = (Boolean) method.invoke(excluder, annotation);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isValidUntil_versionGreaterThanAnnotationValue_returnsFalse() throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);

    // Set version field to 3.0
    setVersion(excluder, 3.0);

    Until annotation = mock(Until.class);
    when(annotation.value()).thenReturn(2.0);

    Boolean result = (Boolean) method.invoke(excluder, annotation);
    assertFalse(result);
  }

  private void setVersion(Excluder excluder, double version) throws Exception {
    java.lang.reflect.Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.setDouble(excluder, version);
  }
}