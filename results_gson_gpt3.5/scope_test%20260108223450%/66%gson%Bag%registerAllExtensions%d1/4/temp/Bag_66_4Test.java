package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Bag_66_4Test {

  private ExtensionRegistry mockExtensionRegistry;
  private ExtensionRegistryLite mockExtensionRegistryLite;

  @BeforeEach
  void setUp() {
    mockExtensionRegistry = mock(ExtensionRegistry.class);
    mockExtensionRegistryLite = mock(ExtensionRegistryLite.class);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry() {
    // Call the public static method registerAllExtensions(ExtensionRegistry)
    com.google.gson.protobuf.generated.Bag.registerAllExtensions(mockExtensionRegistry);
    // No exception means pass; the method delegates to registerAllExtensions(ExtensionRegistryLite)
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite_viaReflection() throws Exception {
    // Use reflection to invoke private static method registerAllExtensions(ExtensionRegistryLite)
    Method method = com.google.gson.protobuf.generated.Bag.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.setAccessible(true);

    method.invoke(null, mockExtensionRegistryLite);
    // No exception means pass; coverage of private method
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor() {
    // Call getDescriptor to ensure no exceptions and coverage
    com.google.gson.protobuf.generated.Bag.getDescriptor();
  }
}