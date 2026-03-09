package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Bag_66_1Test {

  private ExtensionRegistry mockExtensionRegistry;
  private ExtensionRegistryLite mockExtensionRegistryLite;

  @BeforeEach
  void setUp() {
    mockExtensionRegistry = mock(ExtensionRegistry.class);
    mockExtensionRegistryLite = mock(ExtensionRegistryLite.class);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry() throws Exception {
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    Method method = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    method.invoke(null, mockExtensionRegistry);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite_viaReflection() throws Exception {
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    Method method = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.setAccessible(true);
    method.invoke(null, mockExtensionRegistryLite);
  }
}