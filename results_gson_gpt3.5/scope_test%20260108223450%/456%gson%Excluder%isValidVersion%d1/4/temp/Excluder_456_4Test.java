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
import static org.mockito.Mockito.*;

import com.google.gson.annotations.Since;
import com.google.gson.annotations.Until;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Excluder_456_4Test {

  private Excluder excluder;
  private Method isValidVersionMethod;

  @BeforeEach
  void setUp() throws Exception {
    excluder = new Excluder();
    // Set version to a known value for testing isValidVersion
    // Using reflection to set private field 'version' since it's private
    var versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.setDouble(excluder, 1.5d);

    isValidVersionMethod = Excluder.class.getDeclaredMethod("isValidVersion", Since.class, Until.class);
    isValidVersionMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_bothNull() throws Exception {
    // since == null, until == null
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, null, null);
    // isValidSince(null) && isValidUntil(null) both true => true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceValid_untilNull() throws Exception {
    Since since = mock(Since.class);
    when(since.value()).thenReturn(1.0);

    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, null);
    // version=1.5 >= 1.0 => isValidSince true, isValidUntil(null) true => true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceInvalid_untilNull() throws Exception {
    Since since = mock(Since.class);
    when(since.value()).thenReturn(2.0);

    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, null);
    // version=1.5 < 2.0 => isValidSince false => false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceNull_untilValid() throws Exception {
    Until until = mock(Until.class);
    when(until.value()).thenReturn(2.0);

    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, null, until);
    // version=1.5 < 2.0 => isValidUntil true, isValidSince(null) true => true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceNull_untilInvalid() throws Exception {
    Until until = mock(Until.class);
    when(until.value()).thenReturn(1.0);

    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, null, until);
    // version=1.5 >= 1.0 => isValidUntil false => false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceValid_untilValid() throws Exception {
    Since since = mock(Since.class);
    Until until = mock(Until.class);
    when(since.value()).thenReturn(1.0);
    when(until.value()).thenReturn(2.0);

    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, until);
    // version=1.5 >= 1.0 and < 2.0 => true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceInvalid_untilValid() throws Exception {
    Since since = mock(Since.class);
    Until until = mock(Until.class);
    when(since.value()).thenReturn(2.0);
    when(until.value()).thenReturn(3.0);

    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, until);
    // version=1.5 < 2.0 => false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceValid_untilInvalid() throws Exception {
    Since since = mock(Since.class);
    Until until = mock(Until.class);
    when(since.value()).thenReturn(1.0);
    when(until.value()).thenReturn(1.0);

    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, until);
    // version=1.5 >= 1.0 but version=1.5 >= 1.0 => isValidUntil false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_versionIgnore() throws Exception {
    // Set version to IGNORE_VERSIONS (-1.0d)
    var versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.setDouble(excluder, -1.0d);

    // Pass null for Since and Until, since version = -1.0 means always valid
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, null, null);
    // When version = -1.0d, isValidSince and isValidUntil always true => true
    assertTrue(result);
  }
}