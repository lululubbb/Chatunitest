package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.Constructor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonIOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.junit.jupiter.api.Test;

class ReflectionHelper_105_3Test {

  @Test
    @Timeout(8000)
  void makeAccessible_setsAccessibleTrue_whenNoException() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    // No exception thrown by setAccessible
    doNothing().when(accessibleObject).setAccessible(true);

    assertDoesNotThrow(() -> ReflectionHelper.makeAccessible(accessibleObject));

    verify(accessibleObject).setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void makeAccessible_throwsJsonIOException_withProperMessage_whenExceptionThrown() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    doThrow(new SecurityException("denied")).when(accessibleObject).setAccessible(true);

    JsonIOException thrown = assertThrows(JsonIOException.class, () -> ReflectionHelper.makeAccessible(accessibleObject));
    String msg = thrown.getMessage();

    // The message should start with "Failed making "
    assertTrue(msg.startsWith("Failed making "));
    // The message should mention "accessible" and "either increase its visibility"
    assertTrue(msg.contains("accessible"));
    assertTrue(msg.contains("either increase its visibility"));
    // The cause should be the SecurityException
    assertNotNull(thrown.getCause());
    assertEquals(SecurityException.class, thrown.getCause().getClass());
    assertEquals("denied", thrown.getCause().getMessage());

    verify(accessibleObject).setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void makeAccessible_throwsJsonIOException_callsGetAccessibleObjectDescription() throws Exception {
    AccessibleObject accessibleObject = mock(AccessibleObject.class);
    doThrow(new RuntimeException("fail")).when(accessibleObject).setAccessible(true);

    JsonIOException ex = assertThrows(JsonIOException.class, () -> ReflectionHelper.makeAccessible(accessibleObject));
    String message = ex.getMessage();

    // The message should contain description from getAccessibleObjectDescription
    // The description is obtained with uppercaseFirstLetter = false
    // We cannot directly check the exact description string, but it should be part of the message
    assertTrue(message.startsWith("Failed making "));
    assertTrue(message.contains("accessible"));

    verify(accessibleObject).setAccessible(true);
  }
}