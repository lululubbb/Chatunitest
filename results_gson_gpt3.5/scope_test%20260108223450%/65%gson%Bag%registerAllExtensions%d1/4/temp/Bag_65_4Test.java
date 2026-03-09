package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;

class Bag_65_4Test {

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite() throws Exception {
    ExtensionRegistryLite registry = mock(ExtensionRegistryLite.class);
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    bagClass.getMethod("registerAllExtensions", ExtensionRegistryLite.class).invoke(null, registry);
  }
}