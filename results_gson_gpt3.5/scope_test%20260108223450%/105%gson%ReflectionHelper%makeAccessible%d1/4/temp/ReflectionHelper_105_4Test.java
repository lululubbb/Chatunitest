package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

class ReflectionHelper_105_4Test {

  @Test
    @Timeout(8000)
  void makeAccessible_setsAccessibleTrue() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    // No exception expected
    ReflectionHelper.makeAccessible(accessibleObject);
    verify(accessibleObject).setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void makeAccessible_throwsJsonIOExceptionOnException() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    doThrow(new SecurityException("no access")).when(accessibleObject).setAccessible(true);

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      ReflectionHelper.makeAccessible(accessibleObject);
    });

    assertTrue(thrown.getMessage().contains("Failed making"));
    assertTrue(thrown.getCause() instanceof SecurityException);
    verify(accessibleObject).setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void makeAccessible_usesGetAccessibleObjectDescriptionInMessage() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    doThrow(new RuntimeException("fail")).when(accessibleObject).setAccessible(true);

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> {
      ReflectionHelper.makeAccessible(accessibleObject);
    });

    // The message should contain the description from getAccessibleObjectDescription, which is public static
    String description = ReflectionHelper.getAccessibleObjectDescription(accessibleObject, false);
    assertTrue(thrown.getMessage().contains(description));
  }
}