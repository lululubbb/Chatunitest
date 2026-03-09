package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Bag_64_2Test {

  private static Method registerAllExtensions_Lite;
  private static Method registerAllExtensions_Registry;
  private static Method getDescriptorMethod;

  @BeforeAll
  static void setUp() throws NoSuchMethodException, ClassNotFoundException {
    // Use the current class loader to load the Bag class
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag", true, classLoader);
    registerAllExtensions_Lite = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    registerAllExtensions_Registry = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    getDescriptorMethod = bagClass.getDeclaredMethod("getDescriptor");
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_Lite() throws InvocationTargetException, IllegalAccessException {
    ExtensionRegistryLite registryLite = mock(ExtensionRegistryLite.class);
    registerAllExtensions_Lite.invoke(null, registryLite);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_Registry() throws InvocationTargetException, IllegalAccessException {
    ExtensionRegistry registry = mock(ExtensionRegistry.class);
    registerAllExtensions_Registry.invoke(null, registry);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor() throws InvocationTargetException, IllegalAccessException {
    Object result = getDescriptorMethod.invoke(null);
    assertNotNull(result);
    assertTrue(result instanceof Descriptors.FileDescriptor);
  }
}