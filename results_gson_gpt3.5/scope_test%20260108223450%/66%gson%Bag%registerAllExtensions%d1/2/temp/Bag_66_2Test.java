package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Bag_66_2Test {

  private ExtensionRegistry mockExtensionRegistry;
  private ExtensionRegistryLite mockExtensionRegistryLite;

  private Class<?> bagClass;

  @BeforeEach
  void setUp() throws Exception {
    mockExtensionRegistry = mock(ExtensionRegistry.class);
    mockExtensionRegistryLite = mock(ExtensionRegistryLite.class);
    bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry() throws Exception {
    Method method = bagClass.getMethod("registerAllExtensions", ExtensionRegistry.class);
    method.invoke(null, mockExtensionRegistry);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite() throws Exception {
    Method method = bagClass.getMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.invoke(null, mockExtensionRegistryLite);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor() throws Exception {
    Method method = bagClass.getMethod("getDescriptor");
    method.invoke(null);
  }
}