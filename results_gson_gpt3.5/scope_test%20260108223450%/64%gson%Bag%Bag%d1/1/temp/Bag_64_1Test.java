package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;

public class Bag_64_1Test {

  @Test
    @Timeout(8000)
  public void testBagConstructorIsPrivate() throws NoSuchMethodException, IllegalAccessException,
      InvocationTargetException, InstantiationException, ClassNotFoundException {
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    Constructor<?> constructor = bagClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
  }

  @Test
    @Timeout(8000)
  public void testGetDescriptorMethod() throws Exception {
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    Method getDescriptor = bagClass.getDeclaredMethod("getDescriptor");
    getDescriptor.setAccessible(true);
    Object descriptor = getDescriptor.invoke(null);
    assertNotNull(descriptor);
  }

  @Test
    @Timeout(8000)
  public void testRegisterAllExtensionsWithExtensionRegistryLite() throws Exception {
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    Class<?> extensionRegistryLiteClass = Class.forName("com.google.protobuf.ExtensionRegistryLite");
    Method registerAllExtensions = bagClass.getDeclaredMethod("registerAllExtensions", extensionRegistryLiteClass);
    registerAllExtensions.setAccessible(true);
    Object registryLite = extensionRegistryLiteClass.getMethod("getEmptyRegistry").invoke(null);
    registerAllExtensions.invoke(null, registryLite);
  }

  @Test
    @Timeout(8000)
  public void testRegisterAllExtensionsWithExtensionRegistry() throws Exception {
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    Class<?> extensionRegistryClass = Class.forName("com.google.protobuf.ExtensionRegistry");
    Method registerAllExtensions = bagClass.getDeclaredMethod("registerAllExtensions", extensionRegistryClass);
    registerAllExtensions.setAccessible(true);
    Object registry = extensionRegistryClass.getMethod("newInstance").invoke(null);
    registerAllExtensions.invoke(null, registry);
  }
}