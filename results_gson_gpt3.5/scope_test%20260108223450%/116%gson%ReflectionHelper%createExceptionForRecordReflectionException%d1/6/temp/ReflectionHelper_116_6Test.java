package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

class ReflectionHelper_116_6Test {

  @Test
    @Timeout(8000)
  void createExceptionForRecordReflectionException_throwsRuntimeExceptionWithCorrectMessageAndCause() throws Throwable {
    ReflectiveOperationException cause = new ReflectiveOperationException("reflective failure");

    Method method = ReflectionHelper.class.getDeclaredMethod(
        "createExceptionForRecordReflectionException", ReflectiveOperationException.class);
    method.setAccessible(true);

    RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
      try {
        method.invoke(null, cause);
      } catch (InvocationTargetException ite) {
        // unwrap the InvocationTargetException to get the underlying exception thrown by the method
        throw ite.getCause();
      }
    });

    String expectedMessageStart = "Unexpected ReflectiveOperationException occurred (Gson ";
    String expectedMessageContains = "To support Java records, reflection is utilized to read out information";
    assertEquals(RuntimeException.class, thrown.getClass());
    String actualMessage = thrown.getMessage();
    // Check the message starts with expected prefix
    org.junit.jupiter.api.Assertions.assertTrue(actualMessage.startsWith(expectedMessageStart));
    // Check message contains explanation about records
    org.junit.jupiter.api.Assertions.assertTrue(actualMessage.contains(expectedMessageContains));
    // Check cause is the original Exception
    assertEquals(cause, thrown.getCause());
  }
}