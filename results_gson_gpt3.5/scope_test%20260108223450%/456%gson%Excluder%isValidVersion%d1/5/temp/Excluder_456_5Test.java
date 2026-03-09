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

class Excluder_456_5Test {

  private Excluder excluder;
  private Method isValidVersionMethod;

  @BeforeEach
  void setUp() throws Exception {
    excluder = new Excluder();
    isValidVersionMethod = Excluder.class.getDeclaredMethod("isValidVersion", Since.class, Until.class);
    isValidVersionMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_bothNull() throws Exception {
    // since = null, until = null
    boolean result = (boolean) isValidVersionMethod.invoke(excluder, null, null);
    // isValidSince(null) and isValidUntil(null) should both be true, so result true
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceValid_untilNull() throws Exception {
    Since since = mock(Since.class);
    when(since.value()).thenReturn(1.0);

    // Set excluder version to 2.0 to be >= since.value()
    excluder = excluder.withVersion(2.0);

    boolean result = (boolean) isValidVersionMethod.invoke(excluder, since, null);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceInvalid_untilNull() throws Exception {
    Since since = mock(Since.class);
    when(since.value()).thenReturn(3.0);

    // Set excluder version to 2.0 to be < since.value()
    excluder = excluder.withVersion(2.0);

    boolean result = (boolean) isValidVersionMethod.invoke(excluder, since, null);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceNull_untilValid() throws Exception {
    Until until = mock(Until.class);
    when(until.value()).thenReturn(3.0);

    // Set excluder version to 2.0 to be < until.value()
    excluder = excluder.withVersion(2.0);

    boolean result = (boolean) isValidVersionMethod.invoke(excluder, null, until);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceNull_untilInvalid() throws Exception {
    Until until = mock(Until.class);
    when(until.value()).thenReturn(1.0);

    // Set excluder version to 2.0 to be > until.value()
    excluder = excluder.withVersion(2.0);

    boolean result = (boolean) isValidVersionMethod.invoke(excluder, null, until);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceValid_untilValid() throws Exception {
    Since since = mock(Since.class);
    Until until = mock(Until.class);

    when(since.value()).thenReturn(1.0);
    when(until.value()).thenReturn(3.0);

    excluder = excluder.withVersion(2.0);

    boolean result = (boolean) isValidVersionMethod.invoke(excluder, since, until);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceInvalid_untilValid() throws Exception {
    Since since = mock(Since.class);
    Until until = mock(Until.class);

    when(since.value()).thenReturn(3.0);
    when(until.value()).thenReturn(5.0);

    excluder = excluder.withVersion(2.0);

    boolean result = (boolean) isValidVersionMethod.invoke(excluder, since, until);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceValid_untilInvalid() throws Exception {
    Since since = mock(Since.class);
    Until until = mock(Until.class);

    when(since.value()).thenReturn(1.0);
    when(until.value()).thenReturn(1.5);

    excluder = excluder.withVersion(2.0);

    boolean result = (boolean) isValidVersionMethod.invoke(excluder, since, until);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_sinceInvalid_untilInvalid() throws Exception {
    Since since = mock(Since.class);
    Until until = mock(Until.class);

    when(since.value()).thenReturn(3.0);
    when(until.value()).thenReturn(1.5);

    excluder = excluder.withVersion(2.0);

    boolean result = (boolean) isValidVersionMethod.invoke(excluder, since, until);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testIsValidVersion_ignoreVersion() throws Exception {
    // IGNORE_VERSIONS = -1.0 disables version checks
    Since since = mock(Since.class);
    Until until = mock(Until.class);

    when(since.value()).thenReturn(100.0);
    when(until.value()).thenReturn(0.5);

    // Use a new Excluder instance with version set to IGNORE_VERSIONS (-1.0)
    excluder = new Excluder().withVersion(-1.0);

    // Because withVersion returns a new instance, we must get the isValidVersion method from that instance
    Method method = Excluder.class.getDeclaredMethod("isValidVersion", Since.class, Until.class);
    method.setAccessible(true);

    // Use reflection to also access isValidSince and isValidUntil methods to verify IGNORE_VERSIONS behavior
    Method isValidSinceMethod = Excluder.class.getDeclaredMethod("isValidSince", Since.class);
    Method isValidUntilMethod = Excluder.class.getDeclaredMethod("isValidUntil", Until.class);
    isValidSinceMethod.setAccessible(true);
    isValidUntilMethod.setAccessible(true);

    // Invoke isValidSince and isValidUntil on the same excluder instance (with version -1.0)
    boolean validSince = (boolean) isValidSinceMethod.invoke(excluder, since);
    boolean validUntil = (boolean) isValidUntilMethod.invoke(excluder, until);
    assertTrue(validSince, "isValidSince should return true when version is IGNORE_VERSIONS");
    assertTrue(validUntil, "isValidUntil should return true when version is IGNORE_VERSIONS");

    boolean result = (boolean) method.invoke(excluder, since, until);
    // Should be true because version is ignored
    assertTrue(result);
  }
}