package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Annotations_500_2Test {

  @Test
    @Timeout(8000)
  public void testRegisterAllExtensions_withExtensionRegistry() throws Exception {
    ExtensionRegistry registry = mock(ExtensionRegistry.class);

    // Call the public static method registerAllExtensions(ExtensionRegistry)
    com.google.gson.protobuf.generated.Annotations.registerAllExtensions(registry);

    // Use reflection to invoke private static registerAllExtensions(ExtensionRegistryLite)
    Method privateMethod = com.google.gson.protobuf.generated.Annotations.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    privateMethod.setAccessible(true);
    ExtensionRegistryLite registryLite = mock(ExtensionRegistryLite.class);
    privateMethod.invoke(null, registryLite);
  }
}