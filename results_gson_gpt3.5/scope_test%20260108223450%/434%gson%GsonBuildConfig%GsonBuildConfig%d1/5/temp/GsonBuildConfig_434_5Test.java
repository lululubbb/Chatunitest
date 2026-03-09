package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

class GsonBuildConfig_434_5Test {

  @Test
    @Timeout(8000)
  void testVERSIONValue() {
    assertEquals("2.10.1", GsonBuildConfig.VERSION);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    var constructor = GsonBuildConfig.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    var instance = constructor.newInstance();
    // The instance should be non-null and of type GsonBuildConfig
    assertEquals(GsonBuildConfig.class, instance.getClass());
  }
}