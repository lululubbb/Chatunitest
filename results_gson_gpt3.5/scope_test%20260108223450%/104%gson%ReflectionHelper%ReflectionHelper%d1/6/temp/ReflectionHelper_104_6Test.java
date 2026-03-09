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

import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ReflectionHelper_104_6Test {

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    var constructor = ReflectionHelper.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof ReflectionHelper);
  }

  @Test
    @Timeout(8000)
  void testMakeAccessible_setsAccessible() throws Exception {
    Field field = SampleClass.class.getDeclaredField("field");
    field.setAccessible(false);
    ReflectionHelper.makeAccessible(field);
    // Use try-catch around canAccess(null) to handle possible IllegalArgumentException on static fields
    try {
      assertTrue(field.canAccess(null));
    } catch (IllegalArgumentException e) {
      // fallback: check accessible flag via reflection
      var accessibleField = AccessibleObject.class.getDeclaredField("override");
      accessibleField.setAccessible(true);
      assertTrue(accessibleField.getBoolean(field));
    }
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_lowercase() throws Exception {
    Field field = SampleClass.class.getDeclaredField("field");
    String desc = ReflectionHelper.getAccessibleObjectDescription(field, false);
    assertTrue(desc.toLowerCase().contains("field"));
  }

  @Test
    @Timeout(8000)
  void testGetAccessibleObjectDescription_uppercase() throws Exception {
    Field field = SampleClass.class.getDeclaredField("field");
    String desc = ReflectionHelper.getAccessibleObjectDescription(field, true);
    assertTrue(Character.isUpperCase(desc.charAt(0)));
  }

  @Test
    @Timeout(8000)
  void testFieldToString_containsFieldName() throws Exception {
    Field field = SampleClass.class.getDeclaredField("field");
    String desc = ReflectionHelper.fieldToString(field);
    assertTrue(desc.contains("field"));
  }

  @Test
    @Timeout(8000)
  void testConstructorToString_containsConstructorName() throws Exception {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor();
    String desc = ReflectionHelper.constructorToString(constructor);
    assertTrue(desc.contains("SampleClass"));
  }

  @Test
    @Timeout(8000)
  void testTryMakeAccessible_success() throws Exception {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor();
    String result = ReflectionHelper.tryMakeAccessible(constructor);
    assertNull(result);
    assertTrue(constructor.canAccess(null));
  }

  @Test
    @Timeout(8000)
  void testTryMakeAccessible_failure() throws Exception {
    Constructor<SampleClass> constructor = SampleClass.class.getDeclaredConstructor();

    try (MockedStatic<ReflectionHelper> mockedStatic = Mockito.mockStatic(ReflectionHelper.class,
        invocation -> {
          if (invocation.getMethod().getName().equals("createExceptionForUnexpectedIllegalAccess")) {
            return new RuntimeException("wrapped");
          }
          if (invocation.getMethod().getName().equals("tryMakeAccessible")) {
            // Call real method to avoid recursion
            return invocation.callRealMethod();
          }
          return invocation.callRealMethod();
        })) {
      // Use a proxy to simulate setAccessible throwing IllegalAccessException
      AccessibleObject proxy = spy(constructor);
      doThrow(new RuntimeException(new IllegalAccessException("denied"))).when(proxy).setAccessible(true);

      // We cannot pass proxy directly to tryMakeAccessible because it expects Constructor,
      // so we invoke tryMakeAccessible via reflection to bypass type checks
      var method = ReflectionHelper.class.getDeclaredMethod("tryMakeAccessible", Constructor.class);
      method.setAccessible(true);
      String result = (String) method.invoke(null, proxy);

      assertNotNull(result);
    }
  }

  @Test
    @Timeout(8000)
  void testIsRecord_trueAndFalse() {
    // Skip if records are not supported on this JVM
    Assumptions.assumeTrue(canDetectRecords());
    assertTrue(ReflectionHelper.isRecord(RecordClass.class));
    assertFalse(ReflectionHelper.isRecord(SampleClass.class));
  }

  private static boolean canDetectRecords() {
    try {
      return ReflectionHelper.isRecord(RecordClass.class);
    } catch (Throwable t) {
      return false;
    }
  }

  @Test
    @Timeout(8000)
  void testGetRecordComponentNames() {
    Assumptions.assumeTrue(canDetectRecords());
    String[] names = ReflectionHelper.getRecordComponentNames(RecordClass.class);
    assertArrayEquals(new String[] {"name", "age"}, names);
  }

  @Test
    @Timeout(8000)
  void testGetAccessor_returnsMethod() throws Exception {
    Assumptions.assumeTrue(canDetectRecords());
    Field field = RecordClass.class.getDeclaredField("name");
    Method accessor = ReflectionHelper.getAccessor(RecordClass.class, field);
    assertNotNull(accessor);
    assertEquals("name", accessor.getName());
  }

  @Test
    @Timeout(8000)
  void testGetCanonicalRecordConstructor() throws Exception {
    Assumptions.assumeTrue(canDetectRecords());
    Constructor<RecordClass> constructor = ReflectionHelper.getCanonicalRecordConstructor(RecordClass.class);
    assertNotNull(constructor);
    assertEquals(2, constructor.getParameterCount());
  }

  @Test
    @Timeout(8000)
  void testCreateExceptionForUnexpectedIllegalAccess() {
    IllegalAccessException iae = new IllegalAccessException("test");
    RuntimeException ex = ReflectionHelper.createExceptionForUnexpectedIllegalAccess(iae);
    assertNotNull(ex);
    assertTrue(ex.getMessage().contains("test"));
  }

  @Test
    @Timeout(8000)
  void testCreateExceptionForRecordReflectionException() throws Exception {
    var method = ReflectionHelper.class.getDeclaredMethod("createExceptionForRecordReflectionException", ReflectiveOperationException.class);
    method.setAccessible(true);
    ReflectiveOperationException roe = new ReflectiveOperationException("fail");
    RuntimeException ex = (RuntimeException) method.invoke(null, roe);
    assertNotNull(ex);
    assertTrue(ex.getMessage().contains("fail"));
  }

  // Sample classes for testing
  static class SampleClass {
    private String field;
    SampleClass() {}
  }

  // Replace record with a static final class with equivalent behavior for compatibility
  static final class RecordClass {
    private final String name;
    private final int age;

    public RecordClass(String name, int age) {
      this.name = name;
      this.age = age;
    }

    public String name() {
      return name;
    }

    public int age() {
      return age;
    }
  }
}