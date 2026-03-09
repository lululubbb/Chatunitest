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

class ReflectionHelper_112_3Test {

  private static Class<?> recordClassMock;

  interface RecordHelper {
    String[] getRecordComponentNames(Class<?> raw);
  }

  @BeforeAll
  static void setup() {
    recordClassMock = RecordClassMock.class;
  }

  @Test
    @Timeout(8000)
  void testGetRecordComponentNames_returnsExpectedNames() throws Exception {
    // Use reflection to access private static final field RECORD_HELPER
    Field recordHelperField = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    recordHelperField.setAccessible(true);

    // Remove final modifier from the field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() & ~Modifier.FINAL);

    Object originalRecordHelper = recordHelperField.get(null);

    // Create a mock of the private ReflectionHelper.RecordHelper interface using reflection
    Class<?> recordHelperClass = null;
    for (Class<?> innerClass : ReflectionHelper.class.getDeclaredClasses()) {
      if ("RecordHelper".equals(innerClass.getSimpleName())) {
        recordHelperClass = innerClass;
        break;
      }
    }
    assertNotNull(recordHelperClass, "RecordHelper class not found");

    Object recordHelperMock = mock(recordHelperClass);

    String[] expected = new String[] {"component1", "component2"};

    // Setup mock behavior via reflection
    when(recordHelperClass.getMethod("getRecordComponentNames", Class.class)
        .invoke(recordHelperMock, recordClassMock)).thenReturn(expected);

    // Alternatively, use Mockito's when with reflection proxy:
    // But since the interface is private, use Mockito's with Answer or doReturn:
    // Here we use doReturn to avoid calling real method:
    org.mockito.stubbing.OngoingStubbing<?> stubbing = doReturn(expected)
        .when(recordHelperMock);
    recordHelperClass.getMethod("getRecordComponentNames", Class.class).invoke(recordHelperMock, recordClassMock);

    try (MockedStatic<ReflectionHelper> reflectionHelperStatic = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      // Replace the private static final RECORD_HELPER field with our mock using reflection
      recordHelperField.set(null, recordHelperMock);

      String[] actual = ReflectionHelper.getRecordComponentNames(recordClassMock);

      assertArrayEquals(expected, actual);

      // Verify the method was called on the mock
      verify(recordHelperMock).getClass()
          .getMethod("getRecordComponentNames", Class.class)
          .invoke(recordHelperMock, recordClassMock);
    } finally {
      // Restore original RECORD_HELPER field value
      recordHelperField.set(null, originalRecordHelper);

      // Restore final modifier (optional)
      modifiersField.setInt(recordHelperField, recordHelperField.getModifiers() | Modifier.FINAL);
    }
  }

  // Dummy class to simulate a record (cannot use 'record' keyword)
  private static class RecordClassMock {
    private final String component1;
    private final int component2;

    public RecordClassMock(String component1, int component2) {
      this.component1 = component1;
      this.component2 = component2;
    }

    public String component1() {
      return component1;
    }

    public int component2() {
      return component2;
    }
  }
}