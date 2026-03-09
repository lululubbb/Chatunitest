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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ProtoTypeAdapter_470_5Test {

  private static class DummyClass {
    public String dummyMethod(String input) {
      return input;
    }
  }

  @BeforeEach
  void setUp() {
    // Clear the static cache before each test via reflection
    try {
      var field = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
      field.setAccessible(true);
      ConcurrentMap<String, ConcurrentMap<Class<?>, Method>> map =
          (ConcurrentMap<String, ConcurrentMap<Class<?>, Method>>) field.get(null);
      map.clear();
    } catch (Exception e) {
      fail("Failed to clear mapOfMapOfMethods before test: " + e);
    }
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_NewCacheAndMethodRetrieved() throws Exception {
    // Use reflection to access private static method
    Method getCachedMethod = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCachedMethod", Class.class, String.class, Class[].class);
    getCachedMethod.setAccessible(true);

    // Call method first time - cache miss, method retrieved by reflection and cached
    Method method = (Method) getCachedMethod.invoke(null, DummyClass.class, "dummyMethod", new Class[]{String.class});
    assertNotNull(method);
    assertEquals("dummyMethod", method.getName());

    // Call method second time - cache hit, same method returned
    Method cachedMethod = (Method) getCachedMethod.invoke(null, DummyClass.class, "dummyMethod", new Class[]{String.class});
    assertSame(method, cachedMethod);
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_ConcurrentMapPutIfAbsentRaceCondition() throws Exception {
    try (MockedStatic<MapMaker> mockedMapMaker = Mockito.mockStatic(MapMaker.class)) {
      // Mock MapMaker to return a MapMaker whose makeMap returns firstMap
      ConcurrentMap<Class<?>, Method> firstMap = new java.util.concurrent.ConcurrentHashMap<>();
      ConcurrentMap<Class<?>, Method> secondMap = new java.util.concurrent.ConcurrentHashMap<>();

      ConcurrentMap<String, ConcurrentMap<Class<?>, Method>> outerMap = new java.util.concurrent.ConcurrentHashMap<>();

      // Create a mock MapMaker
      MapMaker fakeMapMaker = mock(MapMaker.class);
      // Fix: use doReturn to avoid MissingMethodInvocationException
      doReturn(firstMap).when(fakeMapMaker).<Class<?>, Method>makeMap();
      mockedMapMaker.when(MapMaker::new).thenReturn(fakeMapMaker);

      // Replace mapOfMapOfMethods with our own map to simulate race
      var mapField = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
      mapField.setAccessible(true);
      mapField.set(null, outerMap);

      // Put a map for methodName before calling to simulate race condition on putIfAbsent
      outerMap.put("dummyMethod", secondMap);

      Method getCachedMethod = ProtoTypeAdapter.class.getDeclaredMethod(
          "getCachedMethod", Class.class, String.class, Class[].class);
      getCachedMethod.setAccessible(true);

      // The method should be retrieved from secondMap (the existing map)
      Method method = (Method) getCachedMethod.invoke(null, DummyClass.class, "dummyMethod", new Class[]{String.class});
      assertNotNull(method);
      assertEquals("dummyMethod", method.getName());

      // The method should be cached in secondMap, not in firstMap
      assertFalse(firstMap.containsKey(DummyClass.class));
      assertTrue(secondMap.containsKey(DummyClass.class));
    }
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_NoSuchMethodException() throws Exception {
    Method getCachedMethod = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCachedMethod", Class.class, String.class, Class[].class);
    getCachedMethod.setAccessible(true);

    // Call with a method name that does not exist on DummyClass to trigger NoSuchMethodException
    Throwable thrown = assertThrows(Exception.class, () ->
        getCachedMethod.invoke(null, DummyClass.class, "nonExistentMethod", new Class[]{})
    );
    // InvocationTargetException wraps the NoSuchMethodException, unwrap it
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof NoSuchMethodException);
  }
}