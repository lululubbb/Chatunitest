package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;

class Bag_65_1Test {

  @Test
    @Timeout(8000)
  void registerAllExtensions_withExtensionRegistryLite_doesNotThrow() throws Exception {
    ExtensionRegistryLite mockRegistry = mock(ExtensionRegistryLite.class);
    ClassLoader classLoader = Bag.class.getClassLoader();
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag", true, classLoader);
    assertDoesNotThrow(() -> {
      try {
        // Try method with ExtensionRegistryLite parameter
        bagClass.getMethod("registerAllExtensions", ExtensionRegistryLite.class).invoke(null, mockRegistry);
      } catch (NoSuchMethodException e) {
        // fallback to ExtensionRegistry parameter
        Class<?> extensionRegistryClass = Class.forName("com.google.protobuf.ExtensionRegistry", true, classLoader);
        Object registryInstance;
        if (extensionRegistryClass.isInstance(mockRegistry)) {
          registryInstance = mockRegistry;
        } else {
          registryInstance = mock(extensionRegistryClass);
        }
        bagClass.getMethod("registerAllExtensions", extensionRegistryClass).invoke(null, registryInstance);
      }
    });
  }
}