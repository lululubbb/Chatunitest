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
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProtoTypeAdapter_470_3Test {

  private static final String TEST_METHOD_NAME = "toString";

  private static class TestClass {
    @SuppressWarnings("unused")
    public String toString(String prefix) {
      return prefix + "test";
    }
  }

  @BeforeEach
  void clearCache() throws Exception {
    // Clear the mapOfMapOfMethods cache before each test
    var field = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
    field.setAccessible(true);
    ConcurrentMap<?, ?> map = (ConcurrentMap<?, ?>) field.get(null);
    map.clear();
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_existingMethod_returnsMethod() throws Exception {
    Method method = invokeGetCachedMethod(TestClass.class, "toString", String.class);
    assertNotNull(method);
    assertEquals("toString", method.getName());
    assertArrayEquals(new Class<?>[] {String.class}, method.getParameterTypes());
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_cacheReturnsSameMethodInstance() throws Exception {
    Method firstCall = invokeGetCachedMethod(TestClass.class, "toString", String.class);
    Method secondCall = invokeGetCachedMethod(TestClass.class, "toString", String.class);
    assertSame(firstCall, secondCall);
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_methodNotFound_throwsNoSuchMethodException() {
    Exception exception = assertThrows(Exception.class,
        () -> invokeGetCachedMethod(TestClass.class, "nonExistentMethod"));
    Throwable cause = exception.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof NoSuchMethodException, "Expected cause to be NoSuchMethodException");
  }

  @Test
    @Timeout(8000)
  void getCachedMethod_differentMethodNames_independentCaches() throws Exception {
    Method m1 = invokeGetCachedMethod(TestClass.class, "toString", String.class);
    Method m2 = invokeGetCachedMethod(TestClass.class, "hashCode");
    assertNotNull(m1);
    assertNotNull(m2);
    assertNotEquals(m1, m2);
  }

  private Method invokeGetCachedMethod(Class<?> clazz, String methodName, Class<?>... paramTypes)
      throws Exception {
    Method getCachedMethod = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCachedMethod", Class.class, String.class, Class[].class);
    getCachedMethod.setAccessible(true);
    return (Method) getCachedMethod.invoke(null, clazz, methodName, paramTypes);
  }
}