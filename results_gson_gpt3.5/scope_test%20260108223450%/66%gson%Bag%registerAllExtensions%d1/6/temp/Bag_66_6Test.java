package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;

class Bag_66_6Test {

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry() throws Exception {
    ExtensionRegistry registry = mock(ExtensionRegistry.class);
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    var method = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    method.invoke(null, registry);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite() throws Exception {
    ExtensionRegistryLite registryLite = mock(ExtensionRegistryLite.class);
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    var method = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.setAccessible(true);
    method.invoke(null, registryLite);
  }
}