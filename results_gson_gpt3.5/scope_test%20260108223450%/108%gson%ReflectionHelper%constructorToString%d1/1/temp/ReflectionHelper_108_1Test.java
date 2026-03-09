package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ReflectionHelper_108_1Test {

  @Test
    @Timeout(8000)
  void testConstructorToString_noParameters() throws Exception {
    class TestClassNoParams {
      public TestClassNoParams() {}
    }
    Constructor<?> constructor = TestClassNoParams.class.getDeclaredConstructor();

    String result = ReflectionHelper.constructorToString(constructor);

    assertEquals("com.google.gson.internal.reflect.ReflectionHelperTest$1TestClassNoParams()", result);
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_withParameters() throws Exception {
    class TestClassWithParams {
      public TestClassWithParams(int a, String b) {}
    }
    Constructor<?> constructor = TestClassWithParams.class.getDeclaredConstructor(int.class, String.class);

    String result = ReflectionHelper.constructorToString(constructor);

    assertEquals(
        "com.google.gson.internal.reflect.ReflectionHelperTest$2TestClassWithParams(int,java.lang.String)",
        result);
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_withMockedConstructor() throws Exception {
    @SuppressWarnings("unchecked")
    Constructor<?> constructor = Mockito.mock(Constructor.class);

    // Cast to Class<?> to avoid generic capture issues
    Mockito.when(constructor.getDeclaringClass()).thenReturn((Class<?>) MockedClass.class);
    Mockito.when(constructor.getParameterTypes()).thenReturn(new Class<?>[] {String.class, int.class});
    Mockito.when(constructor.getModifiers()).thenReturn(Modifier.PUBLIC);

    String result = ReflectionHelper.constructorToString(constructor);

    assertEquals("mocked.ClassName(java.lang.String,int)", result);
  }

  static class MockedClass {
    public MockedClass(String s, int i) {}
    public static String getName() {
      return "mocked.ClassName";
    }
  }
}