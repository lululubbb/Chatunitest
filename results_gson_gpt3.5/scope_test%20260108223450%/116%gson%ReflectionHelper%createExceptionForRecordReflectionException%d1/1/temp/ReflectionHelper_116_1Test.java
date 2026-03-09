package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class ReflectionHelper_116_1Test {

  @Test
    @Timeout(8000)
  void testCreateExceptionForRecordReflectionException_throwsRuntimeExceptionWithCause() throws Exception {
    ReflectiveOperationException reflectiveException = new ReflectiveOperationException("test reflective exception");

    // Use reflection to access private static method
    var method = ReflectionHelper.class.getDeclaredMethod("createExceptionForRecordReflectionException", ReflectiveOperationException.class);
    method.setAccessible(true);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      try {
        method.invoke(null, reflectiveException);
      } catch (InvocationTargetException ite) {
        // unwrap the actual exception thrown by the method
        throw ite.getCause();
      }
    });

    String expectedMessageStart = "Unexpected ReflectiveOperationException occurred (Gson ";
    String actualMessage = thrown.getMessage();
    // Check message contains expected start and version string
    assertEquals(true, actualMessage.startsWith(expectedMessageStart));
    assertEquals(reflectiveException, thrown.getCause());
  }
}