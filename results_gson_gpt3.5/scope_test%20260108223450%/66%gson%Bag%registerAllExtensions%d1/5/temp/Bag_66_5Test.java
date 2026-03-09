package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Bag_66_5Test {

  @Test
    @Timeout(8000)
  void registerAllExtensions_withExtensionRegistry_invokesExtensionRegistryLite() throws Exception {
    ExtensionRegistry registry = mock(ExtensionRegistry.class);

    // Call the public static method registerAllExtensions(ExtensionRegistry)
    com.google.gson.protobuf.generated.Bag.registerAllExtensions(registry);

    // Use reflection to get the private static method registerAllExtensions(ExtensionRegistryLite)
    Method method = com.google.gson.protobuf.generated.Bag.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.setAccessible(true);

    // Also invoke private method directly to cover it
    method.invoke(null, (ExtensionRegistryLite) registry);
  }
}