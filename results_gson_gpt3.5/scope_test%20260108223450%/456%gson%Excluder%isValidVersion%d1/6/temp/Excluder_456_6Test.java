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

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Excluder_456_6Test {

  private Excluder excluder;
  private Method isValidVersionMethod;

  @BeforeEach
  void setUp() throws Exception {
    excluder = new Excluder().withVersion(1.5);
    isValidVersionMethod = Excluder.class.getDeclaredMethod("isValidVersion", Since.class, Until.class);
    isValidVersionMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void isValidVersion_bothNull_returnsTrue() throws Exception {
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, null, null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidVersion_validSinceNullValidUntil() throws Exception {
    Until until = mock(Until.class);
    when(until.value()).thenReturn(2.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, null, until);
    // since is null, isValidSince returns true; version=1.5 < until=2.0 => true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidVersion_validSinceInvalidUntil() throws Exception {
    Until until = mock(Until.class);
    when(until.value()).thenReturn(1.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, null, until);
    // version=1.5 > until=1.0 => isValidUntil false => overall false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isValidVersion_validSinceValidUntil() throws Exception {
    Since since = mock(Since.class);
    Until until = mock(Until.class);
    when(since.value()).thenReturn(1.0);
    when(until.value()).thenReturn(2.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, until);
    // version=1.5 >= since=1.0 and version=1.5 < until=2.0 => true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isValidVersion_invalidSinceValidUntil() throws Exception {
    Since since = mock(Since.class);
    Until until = mock(Until.class);
    when(since.value()).thenReturn(2.0);
    when(until.value()).thenReturn(3.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, until);
    // version=1.5 < since=2.0 => isValidSince false => overall false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isValidVersion_invalidSinceInvalidUntil() throws Exception {
    Since since = mock(Since.class);
    Until until = mock(Until.class);
    when(since.value()).thenReturn(2.0);
    when(until.value()).thenReturn(1.0);
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluder, since, until);
    // version=1.5 < since=2.0 (false) && version=1.5 > until=1.0 (false) => false
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isValidVersion_ignoreVersions_returnsTrue() throws Exception {
    // Access private static final double IGNORE_VERSIONS via reflection
    Field ignoreVersionsField = Excluder.class.getDeclaredField("IGNORE_VERSIONS");
    ignoreVersionsField.setAccessible(true);
    double ignoreVersionsValue = ignoreVersionsField.getDouble(null);

    Excluder excluderIgnore = new Excluder().withVersion(ignoreVersionsValue);
    Since since = mock(Since.class);
    Until until = mock(Until.class);
    when(since.value()).thenReturn(10.0);
    when(until.value()).thenReturn(0.5);

    // Use reflection to set the private 'version' field to IGNORE_VERSIONS to ensure the state is correct
    Field versionField = Excluder.class.getDeclaredField("version");
    versionField.setAccessible(true);
    versionField.setDouble(excluderIgnore, ignoreVersionsValue);

    // Instead of invoking isValidVersion, invoke isValidSince and isValidUntil separately to check both return true
    Method isValidSinceMethod = Excluder.class.getDeclaredMethod("isValidSince", Since.class);
    isValidSinceMethod.setAccessible(true);
    Method isValidUntilMethod = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    isValidUntilMethod.setAccessible(true);

    // Mock the version field in the excluderIgnore instance to IGNORE_VERSIONS before invoking methods
    Boolean isValidSince = (Boolean) isValidSinceMethod.invoke(excluderIgnore, since);
    Boolean isValidUntil = (Boolean) isValidUntilMethod.invoke(excluderIgnore, until);

    assertTrue(isValidSince, "Expected isValidSince to return true when version is IGNORE_VERSIONS");
    assertTrue(isValidUntil, "Expected isValidUntil to return true when version is IGNORE_VERSIONS");

    // Now invoke isValidVersion and assert true
    Boolean result = (Boolean) isValidVersionMethod.invoke(excluderIgnore, since, until);
    assertTrue(result);
  }
}