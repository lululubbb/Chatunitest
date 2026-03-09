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

class ReflectionHelper_112_6Test {

  // Define a helper interface matching the methods needed from RecordHelper
  interface RecordHelperInterface {
    String[] getRecordComponentNames(Class<?> raw);
  }

  private static RecordHelperInterface recordHelperMock;

  @BeforeAll
  static void setup() throws Exception {
    recordHelperMock = mock(RecordHelperInterface.class);

    Field field = ReflectionHelper.class.getDeclaredField("RECORD_HELPER");
    field.setAccessible(true);

    // Remove final modifier
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    Class<?> recordHelperClass = field.getType();

    // Create a real instance of RecordHelper (assumed to have no-arg constructor)
    Object realRecordHelperInstance = recordHelperClass.getDeclaredConstructor().newInstance();

    // Create a spy of that instance
    Object spyRecordHelper = spy(realRecordHelperInstance);

    // Override getRecordComponentNames method to delegate to recordHelperMock
    // Use reflection to get the method
    var getRecordComponentNamesMethod = recordHelperClass.getDeclaredMethod("getRecordComponentNames", Class.class);
    getRecordComponentNamesMethod.setAccessible(true);

    // Use Mockito doAnswer to stub the method on spyRecordHelper
    doAnswer(invocation -> {
      Class<?> arg = invocation.getArgument(0);
      return recordHelperMock.getRecordComponentNames(arg);
    }).when(spyRecordHelper).getClass()
      .getMethod("getRecordComponentNames", Class.class)
      .invoke(spyRecordHelper, any());

    // The above doesn't work because we cannot use reflection inside Mockito's when/doAnswer like that.
    // Instead, we use a workaround with Mockito's Answer and an InvocationHandler via a dynamic proxy.

    // So the proper way is to cast spyRecordHelper to the recordHelperClass and stub the method directly.
    // But since the method may be package-private, we use a helper interface and a proxy:

    // Create a proxy implementing the helper interface that delegates to spyRecordHelper
    Object proxy = java.lang.reflect.Proxy.newProxyInstance(
        recordHelperClass.getClassLoader(),
        new Class<?>[] {recordHelperClass},
        (proxyObj, method, args) -> {
          if ("getRecordComponentNames".equals(method.getName()) && args.length == 1) {
            return recordHelperMock.getRecordComponentNames((Class<?>) args[0]);
          }
          return method.invoke(spyRecordHelper, args);
        });

    // Set the proxy instance to the RECORD_HELPER field
    field.set(null, proxy);
  }

  @Test
    @Timeout(8000)
  void testGetRecordComponentNames_returnsNamesFromRecordHelper() {
    String[] expected = new String[] {"component1", "component2"};

    when(recordHelperMock.getRecordComponentNames(String.class)).thenReturn(expected);

    String[] actual = ReflectionHelper.getRecordComponentNames(String.class);

    assertArrayEquals(expected, actual);
  }

  @Test
    @Timeout(8000)
  void testGetRecordComponentNames_nullClass_throwsNPE() {
    when(recordHelperMock.getRecordComponentNames(null)).thenThrow(new NullPointerException());

    assertThrows(NullPointerException.class, () -> ReflectionHelper.getRecordComponentNames(null));
  }
}