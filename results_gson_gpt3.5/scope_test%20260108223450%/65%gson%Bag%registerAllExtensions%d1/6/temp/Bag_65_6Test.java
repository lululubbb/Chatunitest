package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;

class Bag_65_6Test {

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite() throws Exception {
    ExtensionRegistryLite registry = mock(ExtensionRegistryLite.class);
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    var method = bagClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.setAccessible(true);
    method.invoke(null, registry);
    // No exception means success as method is empty
  }
}