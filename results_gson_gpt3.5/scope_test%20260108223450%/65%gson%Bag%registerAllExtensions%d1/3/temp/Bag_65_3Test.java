package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Bag_65_3Test {

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite() throws Exception {
    ExtensionRegistryLite registryLite = mock(ExtensionRegistryLite.class);
    // Use the correct class name with the suffix _65_3 as shown in the error message
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag_65_3");
    Method method = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.invoke(null, registryLite);
    // No exception means pass as method is empty
  }
}