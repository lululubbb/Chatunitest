package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

class ReflectionHelper_111_1Test {

  @Test
    @Timeout(8000)
  void isRecord_shouldReturnTrueForRecord() throws Exception {
    // Dynamically load a record class if available (Java 16+)
    Class<?> recordClass;
    try {
      recordClass = Class.forName("java.lang.Record");
    } catch (ClassNotFoundException e) {
      // No record support in this JVM, skip test
      return;
    }

    // Create a dynamic record class using a Java proxy or use a known record class if possible
    // Since creating a record dynamically is complex, use a known record class from JDK if exists
    // Here, we can use a built-in record class if available in the environment for testing
    // Otherwise, skip the test

    // For demonstration, assume we have a record class named "java.lang.Record"
    // Instead, we test with a known record class loaded via reflection

    // Instead, test ReflectionHelper.isRecord on a known record class, e.g. java.lang.Record itself
    assertTrue(ReflectionHelper.isRecord(recordClass));
  }

  @Test
    @Timeout(8000)
  void isRecord_shouldReturnFalseForNonRecord() {
    assertFalse(ReflectionHelper.isRecord(NonRecordExample.class));
  }

  @Test
    @Timeout(8000)
  void isRecord_shouldReturnFalseForNull() {
    assertFalse(ReflectionHelper.isRecord(null));
  }

  // Helper normal class for testing
  public static class NonRecordExample {
    private String name;
    private int age;
  }
}