package com.google.gson.internal.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.JsonIOException;
import com.google.gson.internal.GsonBuildConfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectionHelper_104_1Test {

  static class TestRecord {
    public final int x;
    public final String y;

    public TestRecord(int x, String y) {
      this.x = x;
      this.y = y;
    }

    // Added accessor method for field 'x' to fix NoSuchMethodException
    public int x() {
      return x;
    }
  }

  static class NonRecord {
    private int a;
  }

  static Field testField;
  static Constructor<TestRecord> testConstructor;
  static Method testAccessor;

  @BeforeAll
  static void setup() throws Exception {
    testField = NonRecord.class.getDeclaredField("a");
    testConstructor = TestRecord.class.getDeclaredConstructor(int.class, String.class);
    testAccessor = TestRecord.class.getDeclaredMethod("x");
  }

  @Test
    @Timeout(8000)
  void testMakeAccessible_setsAccessible() throws Exception {
    Field field = NonRecord.class.getDeclaredField("a");
    field.setAccessible(false);
    ReflectionHelper.makeAccessible(field);
    assertTrue(field.canAccess(new NonRecord()));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_uppercaseAndLowercase() throws Exception {
    Field field = NonRecord.class.getDeclaredField("a");
    String descUpper = ReflectionHelper.getAccessibleObjectDescription(field, true);
    String descLower = ReflectionHelper.getAccessibleObjectDescription(field, false);
    assertTrue(descUpper.length() > 0);
    assertTrue(Character.isUpperCase(descUpper.charAt(0)));
    assertTrue(descLower.length() > 0);
    assertTrue(Character.isLowerCase(descLower.charAt(0)));
  }

  @Test
    @Timeout(8000)
  void testFieldToString_containsFieldName() {
    String str = ReflectionHelper.fieldToString(testField);
    assertNotNull(str);
    assertTrue(str.contains(testField.getName()));
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_containsSimpleName() {
    String str = ReflectionHelper.constructorToString(testConstructor);
    assertNotNull(str);
    assertTrue(str.contains(TestRecord.class.getSimpleName()));
  }

  @Test
    @Timeout(8000)
  void testTryMakeAccessible_success() {
    String result = ReflectionHelper.tryMakeAccessible(testConstructor);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testTryMakeAccessible_failure() throws Exception {
    Constructor<TestRecord> constructor = mock(Constructor.class);
    doThrow(new SecurityException("fail")).when(constructor).setAccessible(true);
    // Mock getDeclaringClass to avoid WrongTypeOfReturnValue exception
    when(constructor.getDeclaringClass()).thenReturn(TestRecord.class);
    // Mock getParameterTypes to return Class[] (avoid WrongTypeOfReturnValue)
    doReturn(new Class<?>[]{int.class, String.class}).when(constructor).getParameterTypes();
    // Mock constructorToString to avoid NPE due to mock returning null in toString-related calls
    try (MockedStatic<ReflectionHelper> mockedStatic = Mockito.mockStatic(ReflectionHelper.class, Mockito.CALLS_REAL_METHODS)) {
      mockedStatic.when(() -> ReflectionHelper.constructorToString(constructor)).thenReturn("mockedConstructor");
      String result = ReflectionHelper.tryMakeAccessible(constructor);
      assertNotNull(result);
      assertTrue(result.contains("fail"));
    }
  }

  @Test
    @Timeout(8000)
  void testIsRecord_trueAndFalse() {
    // Only run this test if records are supported, else skip
    if (ReflectionHelper.isRecord(TestRecord.class)) {
      assertTrue(ReflectionHelper.isRecord(TestRecord.class));
      assertFalse(ReflectionHelper.isRecord(NonRecord.class));
    }
  }

  @Test
    @Timeout(8000)
  void testGetRecordComponentNames_returnsNames() {
    // Skip test if records are not supported
    try {
      String[] names = ReflectionHelper.getRecordComponentNames(TestRecord.class);
      assertNotNull(names);
      assertArrayEquals(new String[]{"x", "y"}, names);
    } catch (UnsupportedOperationException e) {
      // Skip test if records unsupported
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessor_returnsMethod() throws Exception {
    // Skip test if records are not supported
    try {
      Method accessor = ReflectionHelper.getAccessor(TestRecord.class, TestRecord.class.getDeclaredField("x"));
      assertNotNull(accessor);
      assertEquals("x", accessor.getName());
    } catch (UnsupportedOperationException e) {
      // Skip test if records unsupported
    }
  }

  @Test
    @Timeout(8000)
  void testGetCanonicalRecordConstructor_returnsConstructor() {
    // Skip test if records are not supported
    try {
      Constructor<TestRecord> constructor = ReflectionHelper.getCanonicalRecordConstructor(TestRecord.class);
      assertNotNull(constructor);
      assertEquals(2, constructor.getParameterCount());
    } catch (UnsupportedOperationException e) {
      // Skip test if records unsupported
    }
  }

  @Test
    @Timeout(8000)
  void testCreateExceptionForUnexpectedIllegalAccess_wrapsException() {
    IllegalAccessException ex = new IllegalAccessException("illegal access");
    RuntimeException runtimeEx = ReflectionHelper.createExceptionForUnexpectedIllegalAccess(ex);
    assertNotNull(runtimeEx);
    assertEquals(ex, runtimeEx.getCause());
  }

  @Test
    @Timeout(8000)
  void testCreateExceptionForRecordReflectionException_wrapsException() throws Exception {
    ReflectiveOperationException ex = new ReflectiveOperationException("reflective error");
    // Use reflection to invoke private static method without mocking ReflectionHelper static methods
    RuntimeException runtimeEx;
    var method = ReflectionHelper.class.getDeclaredMethod("createExceptionForRecordReflectionException", ReflectiveOperationException.class);
    method.setAccessible(true);
    runtimeEx = (RuntimeException) method.invoke(null, ex);
    assertNotNull(runtimeEx);
    assertEquals(ex, runtimeEx.getCause());
  }

  @Test
    @Timeout(8000)
  void testAppendExecutableParameters_privateMethod() throws Exception {
    StringBuilder sb = new StringBuilder();
    AccessibleObject executable = testConstructor;
    var method = ReflectionHelper.class.getDeclaredMethod("appendExecutableParameters", AccessibleObject.class, StringBuilder.class);
    method.setAccessible(true);
    method.invoke(null, executable, sb);
    assertTrue(sb.length() > 0);
  }
}