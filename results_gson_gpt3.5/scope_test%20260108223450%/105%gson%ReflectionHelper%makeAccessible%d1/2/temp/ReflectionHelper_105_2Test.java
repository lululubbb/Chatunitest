package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.GsonBuildConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReflectionHelper_105_2Test {

  @Test
    @Timeout(8000)
  void makeAccessible_success() {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    assertDoesNotThrow(() -> ReflectionHelper.makeAccessible(accessibleObject));
    try {
      verify(accessibleObject).setAccessible(true);
    } catch (Exception e) {
      fail("setAccessible threw exception unexpectedly");
    }
  }

  @Test
    @Timeout(8000)
  void makeAccessible_failure_throwsJsonIOException() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    doThrow(new SecurityException("deny access")).when(accessibleObject).setAccessible(true);

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> ReflectionHelper.makeAccessible(accessibleObject));
    assertTrue(thrown.getMessage().startsWith("Failed making "));
    assertTrue(thrown.getMessage().contains("accessible"));
    assertNotNull(thrown.getCause());
    assertEquals(SecurityException.class, thrown.getCause().getClass());
  }

  @Test
    @Timeout(8000)
  void makeAccessible_failure_callsGetAccessibleObjectDescription() throws Exception {
    // Use a real AccessibleObject subclass to test getAccessibleObjectDescription indirectly
    Field field = Sample.class.getDeclaredField("sampleField");
    AccessibleObject accessibleObject = spy(field);

    doThrow(new SecurityException("deny access")).when(accessibleObject).setAccessible(true);

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> ReflectionHelper.makeAccessible(accessibleObject));
    String message = thrown.getMessage();

    // The description should contain the field name "sampleField"
    assertTrue(message.contains("sampleField"));
  }

  static class Sample {
    private int sampleField;
  }
}