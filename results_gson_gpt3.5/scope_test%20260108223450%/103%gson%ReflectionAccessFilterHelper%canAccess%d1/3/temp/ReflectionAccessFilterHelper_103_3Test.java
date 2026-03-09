package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.Method;
import java.util.List;
import com.google.gson.ReflectionAccessFilter;
import com.google.gson.ReflectionAccessFilter.FilterResult;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReflectionAccessFilterHelper_103_3Test {

  AccessibleObject accessibleObjectMock;
  Object targetObject;

  @BeforeEach
  void setup() {
    accessibleObjectMock = mock(AccessibleObject.class);
    targetObject = new Object();
  }

  private void setFinalStatic(Field field, Object newValue) throws Exception {
    field.setAccessible(true);

    // Remove final modifier from field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(null, newValue);
  }

  @Test
    @Timeout(8000)
  void testCanAccess_delegatesToAccessChecker() throws Exception {
    Class<?> helperClass = ReflectionAccessFilterHelper.class;
    Class<?> accessCheckerClass = null;
    for (Class<?> innerClass : helperClass.getDeclaredClasses()) {
      if ("AccessChecker".equals(innerClass.getSimpleName())) {
        accessCheckerClass = innerClass;
        break;
      }
    }
    assertNotNull(accessCheckerClass, "AccessChecker class not found");

    Field instanceField = accessCheckerClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);

    Object mockAccessChecker = mock(accessCheckerClass, invocation -> {
      if ("canAccess".equals(invocation.getMethod().getName())) {
        return true;
      }
      return invocation.callRealMethod();
    });

    setFinalStatic(instanceField, mockAccessChecker);

    boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject);
    assertTrue(result);

    // Verify canAccess was called once on the mockAccessChecker with correct arguments
    // Use reflection to invoke verify with method name and arguments
    verifyInvoked(mockAccessChecker, "canAccess", accessibleObjectMock, targetObject);
  }

  @Test
    @Timeout(8000)
  void testCanAccess_delegatesToAccessChecker_returnsFalse() throws Exception {
    Class<?> helperClass = ReflectionAccessFilterHelper.class;
    Class<?> accessCheckerClass = null;
    for (Class<?> innerClass : helperClass.getDeclaredClasses()) {
      if ("AccessChecker".equals(innerClass.getSimpleName())) {
        accessCheckerClass = innerClass;
        break;
      }
    }
    assertNotNull(accessCheckerClass, "AccessChecker class not found");

    Field instanceField = accessCheckerClass.getDeclaredField("INSTANCE");
    instanceField.setAccessible(true);

    Object mockAccessChecker = mock(accessCheckerClass, invocation -> {
      if ("canAccess".equals(invocation.getMethod().getName())) {
        return false;
      }
      return invocation.callRealMethod();
    });

    setFinalStatic(instanceField, mockAccessChecker);

    boolean result = ReflectionAccessFilterHelper.canAccess(accessibleObjectMock, targetObject);
    assertFalse(result);

    // No verification needed here
  }

  private void verifyInvoked(Object mock, String methodName, Object... args) throws Exception {
    Class<?> mockClass = mock.getClass().getSuperclass();
    Class<?>[] paramTypes = new Class<?>[args.length];
    for (int i = 0; i < args.length; i++) {
      paramTypes[i] = args[i].getClass();
    }
    // Find the method in the mock class or its superclasses
    java.lang.reflect.Method method = null;
    try {
      method = mockClass.getMethod(methodName, paramTypes);
    } catch (NoSuchMethodException e) {
      // Try to find method by name and compatible parameter types
      for (java.lang.reflect.Method m : mockClass.getMethods()) {
        if (m.getName().equals(methodName) && m.getParameterCount() == args.length) {
          boolean compatible = true;
          Class<?>[] methodParams = m.getParameterTypes();
          for (int i = 0; i < methodParams.length; i++) {
            if (!methodParams[i].isAssignableFrom(paramTypes[i])) {
              compatible = false;
              break;
            }
          }
          if (compatible) {
            method = m;
            break;
          }
        }
      }
      if (method == null) {
        throw e;
      }
    }

    // Use Mockito's verify to verify invocation
    // Because method is not accessible directly, use reflection to invoke verify(mock).method(args)
    Object verification = verify(mock, times(1));
    method.setAccessible(true);
    method.invoke(verification, args);
  }
}