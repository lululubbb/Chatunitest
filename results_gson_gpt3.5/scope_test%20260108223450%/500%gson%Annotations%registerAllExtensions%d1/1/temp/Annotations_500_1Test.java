package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class Annotations_500_1Test {

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry() {
    ExtensionRegistry registry = mock(ExtensionRegistry.class);

    // Use reflection to call the public static method registerAllExtensions(ExtensionRegistry)
    try {
      Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
      Method method = annotationsClass.getMethod("registerAllExtensions", ExtensionRegistry.class);
      method.invoke(null, registry);
    } catch (ClassNotFoundException e) {
      fail("Class com.google.gson.protobuf.generated.Annotations not found: " + e);
    } catch (Exception e) {
      fail("Invocation of registerAllExtensions(ExtensionRegistry) failed: " + e);
    }
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite() {
    ExtensionRegistryLite registryLite = mock(ExtensionRegistryLite.class);

    // Use reflection to call the public static method registerAllExtensions(ExtensionRegistryLite)
    try {
      Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
      Method method = annotationsClass.getMethod("registerAllExtensions", ExtensionRegistryLite.class);
      method.invoke(null, registryLite);
    } catch (ClassNotFoundException e) {
      fail("Class com.google.gson.protobuf.generated.Annotations not found: " + e);
    } catch (Exception e) {
      fail("Invocation of registerAllExtensions(ExtensionRegistryLite) failed: " + e);
    }
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() {
    try {
      Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
      var constructor = annotationsClass.getDeclaredConstructor();
      constructor.setAccessible(true);
      Object instance = constructor.newInstance();
      assertNotNull(instance);
      assertTrue(annotationsClass.isInstance(instance));
    } catch (ClassNotFoundException e) {
      fail("Class com.google.gson.protobuf.generated.Annotations not found: " + e);
    } catch (Exception e) {
      fail("Failed to invoke private constructor: " + e);
    }
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor() {
    try {
      Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
      Method getDescriptorMethod = annotationsClass.getMethod("getDescriptor");
      Object descriptor = getDescriptorMethod.invoke(null);
      assertTrue(descriptor == null || descriptor instanceof com.google.protobuf.Descriptors.FileDescriptor);
    } catch (ClassNotFoundException e) {
      fail("Class com.google.gson.protobuf.generated.Annotations not found: " + e);
    } catch (Exception e) {
      fail("Invocation of getDescriptor() failed: " + e);
    }
  }
}