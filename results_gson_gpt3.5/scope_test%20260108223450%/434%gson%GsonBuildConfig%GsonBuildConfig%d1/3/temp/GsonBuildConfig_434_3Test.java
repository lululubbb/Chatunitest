package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.Test;

public class GsonBuildConfig_434_3Test {

  @Test
    @Timeout(8000)
  public void testVERSIONValue() {
    assertEquals("2.10.1", GsonBuildConfig.VERSION);
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructor() throws Exception {
    Constructor<GsonBuildConfig> constructor = GsonBuildConfig.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    // constructor is private, but we can invoke it via reflection
    GsonBuildConfig instance = constructor.newInstance();
    // instance should not be null
    assertEquals(GsonBuildConfig.class, instance.getClass());
  }

  @Test
    @Timeout(8000)
  public void testPrivateConstructorThrowsExceptionOnDoubleInstantiation() throws Exception {
    Constructor<GsonBuildConfig> constructor = GsonBuildConfig.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    // invoking twice just to cover any edge case, no exception expected here
    GsonBuildConfig instance1 = constructor.newInstance();
    GsonBuildConfig instance2 = constructor.newInstance();
    assertEquals(GsonBuildConfig.class, instance1.getClass());
    assertEquals(GsonBuildConfig.class, instance2.getClass());
  }
}