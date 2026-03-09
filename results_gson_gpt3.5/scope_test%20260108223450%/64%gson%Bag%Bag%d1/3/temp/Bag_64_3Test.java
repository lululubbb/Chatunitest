package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.NoSuchMethodException;

class Bag_64_3Test {

  private static Class<?> bagClass;
  private static Method registerAllExtensionsLiteMethod;
  private static Method registerAllExtensionsMethod;
  private static Method getDescriptorMethod;

  @BeforeAll
  static void setup() throws NoSuchMethodException, ClassNotFoundException {
    // Fix: Use the correct class name as per the error message and likely compiled class name
    bagClass = Class.forName("com.google.gson.protobuf.generated.Bag_64_3");
    registerAllExtensionsLiteMethod = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    registerAllExtensionsMethod = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    getDescriptorMethod = bagClass.getDeclaredMethod("getDescriptor");
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite() throws InvocationTargetException, IllegalAccessException {
    ExtensionRegistryLite registryLite = mock(ExtensionRegistryLite.class);

    Method method = registerAllExtensionsLiteMethod;

    method.invoke(null, registryLite);

    // No exceptions expected, no return value to assert
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry() throws InvocationTargetException, IllegalAccessException {
    ExtensionRegistry registry = mock(ExtensionRegistry.class);

    Method method = registerAllExtensionsMethod;

    method.invoke(null, registry);

    // No exceptions expected, no return value to assert
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor() throws InvocationTargetException, IllegalAccessException {
    Object result = getDescriptorMethod.invoke(null);

    assertNotNull(result);
    assertTrue(result instanceof Descriptors.FileDescriptor);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Constructor<?> constructor = bagClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(bagClass.isInstance(instance));
  }
}