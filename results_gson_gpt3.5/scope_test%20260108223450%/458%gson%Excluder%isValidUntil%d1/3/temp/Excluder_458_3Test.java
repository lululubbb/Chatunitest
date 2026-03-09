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

class Excluder_458_3Test {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  @Test
    @Timeout(8000)
  void isValidUntil_withNullAnnotation_returnsTrue() throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);
    Boolean result = (Boolean) method.invoke(excluder, (Until) null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidUntil_withVersionLessThanAnnotationValue_returnsTrue() throws Exception {
    // Set version field to 1.0
    setVersion(excluder, 1.0d);

    Until until = mock(Until.class);
    when(until.value()).thenReturn(2.0d);

    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);
    Boolean result = (Boolean) method.invoke(excluder, until);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidUntil_withVersionEqualToAnnotationValue_returnsFalse() throws Exception {
    // Set version field to 2.0
    setVersion(excluder, 2.0d);

    Until until = mock(Until.class);
    when(until.value()).thenReturn(2.0d);

    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);
    Boolean result = (Boolean) method.invoke(excluder, until);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isValidUntil_withVersionGreaterThanAnnotationValue_returnsFalse() throws Exception {
    // Set version field to 3.0
    setVersion(excluder, 3.0d);

    Until until = mock(Until.class);
    when(until.value()).thenReturn(2.0d);

    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);
    Boolean result = (Boolean) method.invoke(excluder, until);
    assertFalse(result);
  }

  private void setVersion(Excluder excluder, double version) throws Exception {
    var versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.setDouble(excluder, version);
  }
}