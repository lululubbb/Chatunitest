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
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.annotations.Until;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class ExcluderIsValidUntilTest {

  private Excluder excluder;

  @BeforeEach
  void setUp() {
    excluder = new Excluder();
  }

  @Test
    @Timeout(8000)
  void isValidUntil_nullAnnotation_returnsTrue() throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);

    Boolean result = (Boolean) method.invoke(excluder, (Until) null);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidUntil_versionLessThanAnnotationVersion_returnsTrue() throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);

    setVersion(1.0);

    Until untilAnnotation = mock(Until.class);
    when(untilAnnotation.value()).thenReturn(2.0);

    Boolean result = (Boolean) method.invoke(excluder, untilAnnotation);

    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidUntil_versionEqualToAnnotationVersion_returnsFalse() throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);

    setVersion(2.0);

    Until untilAnnotation = mock(Until.class);
    when(untilAnnotation.value()).thenReturn(2.0);

    Boolean result = (Boolean) method.invoke(excluder, untilAnnotation);

    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isValidUntil_versionGreaterThanAnnotationVersion_returnsFalse() throws Exception {
    Method method = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    method.setAccessible(true);

    setVersion(3.0);

    Until untilAnnotation = mock(Until.class);
    when(untilAnnotation.value()).thenReturn(2.0);

    Boolean result = (Boolean) method.invoke(excluder, untilAnnotation);

    assertFalse(result);
  }

  private void setVersion(double v) throws Exception {
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.set(excluder, v);
  }
}