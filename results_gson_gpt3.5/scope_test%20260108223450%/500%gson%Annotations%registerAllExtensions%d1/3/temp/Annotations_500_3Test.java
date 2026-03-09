package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

class Annotations_500_3Test {

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry() throws Exception {
    ExtensionRegistry registry = mock(ExtensionRegistry.class);

    // Invoke the public static method registerAllExtensions(ExtensionRegistry)
    com.google.gson.protobuf.generated.Annotations.registerAllExtensions(registry);

    // Use reflection to invoke private static method registerAllExtensions(ExtensionRegistryLite)
    Method privateRegisterAllExtensions =
        com.google.gson.protobuf.generated.Annotations.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    privateRegisterAllExtensions.setAccessible(true);
    privateRegisterAllExtensions.invoke(null, registry);

    // Validate getDescriptor is not null and returns a FileDescriptor
    assertNotNull(com.google.gson.protobuf.generated.Annotations.getDescriptor());
    assertEquals(com.google.protobuf.Descriptors.FileDescriptor.class, com.google.gson.protobuf.generated.Annotations.getDescriptor().getClass());
  }
}