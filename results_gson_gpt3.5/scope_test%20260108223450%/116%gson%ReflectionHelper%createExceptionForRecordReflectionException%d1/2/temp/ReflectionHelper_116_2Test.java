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

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

class ReflectionHelper_116_2Test {

  @Test
    @Timeout(8000)
  void testCreateExceptionForRecordReflectionException_throwsRuntimeExceptionWithCause() throws Throwable {
    ReflectiveOperationException cause = new ReflectiveOperationException("reflective error");

    var method = ReflectionHelper.class.getDeclaredMethod("createExceptionForRecordReflectionException", ReflectiveOperationException.class);
    method.setAccessible(true);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      try {
        method.invoke(null, cause);
      } catch (InvocationTargetException e) {
        // rethrow the underlying exception thrown by the invoked method
        throw e.getCause();
      }
    });

    assertNotNull(thrown);
    assertEquals("Unexpected ReflectiveOperationException occurred (Gson " + GsonBuildConfig.VERSION + ")."
        + " To support Java records, reflection is utilized to read out information"
        + " about records. All these invocations happens after it is established"
        + " that records exist in the JVM. This exception is unexpected behavior.", thrown.getMessage());
    assertSame(cause, thrown.getCause());
  }
}