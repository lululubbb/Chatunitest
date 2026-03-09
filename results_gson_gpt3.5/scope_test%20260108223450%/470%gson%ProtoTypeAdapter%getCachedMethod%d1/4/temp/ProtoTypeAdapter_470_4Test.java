package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.base.CaseFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Extension;
import com.google.protobuf.Message;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.MapMaker;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProtoTypeAdapter_470_4Test {

  // Clear the mapOfMapOfMethods before each test to ensure isolation.
  @BeforeEach
  void setUp() throws Exception {
    // Use reflection to clear the static mapOfMapOfMethods
    var field = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
    field.setAccessible(true);
    ConcurrentMap<?, ?> map = (ConcurrentMap<?, ?>) field.get(null);
    map.clear();
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_CachesAndReturnsMethod() throws Exception {
    // Use a test class with a known public method
    Class<?> clazz = TestClass.class;
    String methodName = "testMethod";

    // Initially mapOfMapOfMethods should be empty
    var mapField = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
    mapField.setAccessible(true);
    ConcurrentMap<String, ConcurrentMap<Class<?>, Method>> outerMap =
        (ConcurrentMap<String, ConcurrentMap<Class<?>, Method>>) mapField.get(null);
    assertTrue(outerMap.isEmpty());

    // Invoke getCachedMethod first time - should add to cache
    Method method1 = invokeGetCachedMethod(clazz, methodName);
    assertNotNull(method1);
    assertEquals(methodName, method1.getName());

    // After first call, outerMap should contain one entry for methodName
    assertTrue(outerMap.containsKey(methodName));
    ConcurrentMap<Class<?>, Method> innerMap = outerMap.get(methodName);
    assertNotNull(innerMap);
    assertTrue(innerMap.containsKey(clazz));
    assertEquals(method1, innerMap.get(clazz));

    // Invoke getCachedMethod second time - should retrieve from cache, no exception
    Method method2 = invokeGetCachedMethod(clazz, methodName);
    assertSame(method1, method2);
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_NoSuchMethodException() {
    Class<?> clazz = TestClass.class;
    String methodName = "nonExistentMethod";

    Exception exception = assertThrows(Exception.class, () -> invokeGetCachedMethod(clazz, methodName));
    // unwrap InvocationTargetException if thrown
    Throwable cause = exception.getCause();
    if (cause instanceof NoSuchMethodException) {
      // expected
    } else if (exception instanceof NoSuchMethodException) {
      // also expected
    } else {
      fail("Expected NoSuchMethodException but got: " + exception);
    }
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_DifferentParamTypes() throws Exception {
    Class<?> clazz = TestClass.class;
    String methodName = "testMethodWithParams";

    Method method = invokeGetCachedMethod(clazz, methodName, String.class, int.class);
    assertNotNull(method);
    assertEquals(methodName, method.getName());
    Class<?>[] params = method.getParameterTypes();
    assertArrayEquals(new Class<?>[] {String.class, int.class}, params);

    // Cache should contain this method now
    var mapField = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
    mapField.setAccessible(true);
    ConcurrentMap<String, ConcurrentMap<Class<?>, Method>> outerMap =
        (ConcurrentMap<String, ConcurrentMap<Class<?>, Method>>) mapField.get(null);

    assertTrue(outerMap.containsKey(methodName));
    ConcurrentMap<Class<?>, Method> innerMap = outerMap.get(methodName);
    assertNotNull(innerMap);
    assertTrue(innerMap.containsKey(clazz));
    assertEquals(method, innerMap.get(clazz));
  }

  // Helper to invoke private static getCachedMethod via reflection
  private Method invokeGetCachedMethod(Class<?> clazz, String methodName, Class<?>... paramTypes)
      throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCachedMethod", Class.class, String.class, Class[].class);
    method.setAccessible(true);
    // varargs require wrapping paramTypes in Object[]
    return (Method) method.invoke(null, clazz, methodName, paramTypes);
  }

  // Test class with methods to reflectively access
  public static class TestClass {
    public void testMethod() {}

    public void testMethodWithParams(String s, int i) {}
  }
}