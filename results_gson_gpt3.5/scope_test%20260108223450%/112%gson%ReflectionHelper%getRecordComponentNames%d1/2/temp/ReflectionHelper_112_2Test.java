package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectionHelper_112_2Test {

  private static Class<?> recordClassMock;

  @BeforeAll
  static void setup() {
    recordClassMock = RecordClassMock.class;
  }

  @Test
    @Timeout(8000)
  void testGetRecordComponentNames_returnsExpected() {
    try (MockedStatic<ReflectionHelper> mockedStatic = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      // Get the actual RecordHelper class from ReflectionHelper
      Class<?> recordHelperClass = Class.forName("com.google.gson.internal.reflect.ReflectionHelper$RecordHelper");

      String[] expected = new String[] {"component1", "component2"};

      // Create a proxy instance directly implementing RecordHelper class (which is likely an abstract class)
      // Use java.lang.reflect.Proxy only if RecordHelper is an interface; since it's a class, create a dynamic subclass via a proxy library or use a mock with Mockito
      // Here, use Mockito to create a mock of RecordHelper class

      Object proxy = mock(recordHelperClass, invocation -> {
        if ("getRecordComponentNames".equals(invocation.getMethod().getName()) && invocation.getArguments().length == 1) {
          return expected;
        }
        return invocation.callRealMethod();
      });

      // Use reflection to set private static final RECORD_HELPER field
      Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
      recordHelperField.setAccessible(true);

      // Remove final modifier from the field
      Field modifiersField = Field.class.getDeclaredField("modifiers");
      modifiersField.setAccessible(true);
      modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

      // Set the proxy instance to the RECORD_HELPER field
      recordHelperField.set(null, proxy);

      String[] actual = ReflectionHelper.getRecordComponentNames(recordClassMock);
      assertArrayEquals(expected, actual);
    } catch (Exception e) {
      fail("Exception during test: " + e);
    }
  }

  // Dummy record class mock for testing
  private static class RecordClassMock {
    // Empty class, just for test
  }
}