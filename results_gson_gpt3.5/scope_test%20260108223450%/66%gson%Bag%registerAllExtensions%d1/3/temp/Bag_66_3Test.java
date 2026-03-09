package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;

class Bag_66_3Test {

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry() throws Exception {
    // Arrange
    ExtensionRegistry registry = mock(ExtensionRegistry.class);

    // Act
    com.google.gson.protobuf.generated.Bag.registerAllExtensions(registry);

    // No assertions needed as method is void and just calls the other overload
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite() throws Exception {
    // Arrange
    ExtensionRegistryLite registryLite = mock(ExtensionRegistryLite.class);

    // Use reflection to invoke private static registerAllExtensions(ExtensionRegistryLite)
    var method = com.google.gson.protobuf.generated.Bag.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.setAccessible(true);

    // Act
    method.invoke(null, registryLite);

    // No assertions needed; just cover the method invocation
  }
}