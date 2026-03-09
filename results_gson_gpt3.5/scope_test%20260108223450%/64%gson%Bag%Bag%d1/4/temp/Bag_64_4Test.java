package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.lang.reflect.Method;

class Bag_64_4Test {

  private static MockedStatic<com.google.gson.protobuf.generated.Bag> bagStaticMock;

  @BeforeAll
  static void setUp() {
    bagStaticMock = mockStatic(com.google.gson.protobuf.generated.Bag.class, invocation -> {
      // Default behavior: call real method
      return invocation.callRealMethod();
    });
  }

  @AfterAll
  static void tearDown() {
    bagStaticMock.close();
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensionsWithExtensionRegistryLite() throws Exception {
    ExtensionRegistryLite registryLite = ExtensionRegistryLite.getEmptyRegistry();
    // invoke public static method registerAllExtensions(ExtensionRegistryLite)
    Method method = com.google.gson.protobuf.generated.Bag.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.invoke(null, registryLite);
    // No exception and no return value to assert, just ensure no errors
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensionsWithExtensionRegistry() throws Exception {
    ExtensionRegistry registry = ExtensionRegistry.newInstance();
    // invoke public static method registerAllExtensions(ExtensionRegistry)
    Method method = com.google.gson.protobuf.generated.Bag.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    method.invoke(null, registry);
    // No exception and no return value to assert, just ensure no errors
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor() throws Exception {
    // invoke public static method getDescriptor()
    Method method = com.google.gson.protobuf.generated.Bag.class.getDeclaredMethod("getDescriptor");
    Object descriptor = method.invoke(null);
    assertNotNull(descriptor);
    // descriptor is of type com.google.protobuf.Descriptors.FileDescriptor
    assert(descriptor instanceof Descriptors.FileDescriptor);
  }
}