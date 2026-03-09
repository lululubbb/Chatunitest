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

class Bag_64_5Test {

  private static Method registerAllExtensionsLiteMethod;
  private static Method registerAllExtensionsMethod;
  private static Method getDescriptorMethod;

  @BeforeAll
  static void setup() throws NoSuchMethodException {
    Class<?> bagClass;
    try {
      bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    } catch (ClassNotFoundException e) {
      // Try alternative class loader (context class loader)
      try {
        bagClass = Thread.currentThread().getContextClassLoader().loadClass("com.google.gson.protobuf.generated.Bag");
      } catch (ClassNotFoundException ex) {
        // Try system class loader as a last resort
        try {
          bagClass = ClassLoader.getSystemClassLoader().loadClass("com.google.gson.protobuf.generated.Bag");
        } catch (ClassNotFoundException exc) {
          throw new RuntimeException(exc);
        }
      }
    }
    registerAllExtensionsLiteMethod = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    registerAllExtensionsMethod = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    getDescriptorMethod = bagClass.getDeclaredMethod("getDescriptor");

    // Make methods accessible if they are not public
    registerAllExtensionsLiteMethod.setAccessible(true);
    registerAllExtensionsMethod.setAccessible(true);
    getDescriptorMethod.setAccessible(true);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensionsLite_invokesWithoutException() throws InvocationTargetException, IllegalAccessException {
    ExtensionRegistryLite registryLite = ExtensionRegistryLite.getEmptyRegistry();
    registerAllExtensionsLiteMethod.invoke(null, registryLite);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_invokesWithoutException() throws InvocationTargetException, IllegalAccessException {
    ExtensionRegistry registry = ExtensionRegistry.newInstance();
    registerAllExtensionsMethod.invoke(null, registry);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_returnsNonNullDescriptor() throws InvocationTargetException, IllegalAccessException {
    Object result = getDescriptorMethod.invoke(null);
    assertNotNull(result);
    assertTrue(result instanceof Descriptors.FileDescriptor);
  }

}