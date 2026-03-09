package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Annotations_498_1Test {

  private ExtensionRegistryLite mockRegistryLite;
  private ExtensionRegistry mockRegistry;

  @BeforeEach
  void setUp() {
    mockRegistryLite = mock(ExtensionRegistryLite.class);
    mockRegistry = mock(ExtensionRegistry.class);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_ExtensionRegistryLite() {
    // Call the static method
    com.google.gson.protobuf.generated.Annotations.registerAllExtensions(mockRegistryLite);
    // No exception means success, verify no interactions expected
    verifyNoInteractions(mockRegistryLite);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_ExtensionRegistry() {
    // Call the static method
    com.google.gson.protobuf.generated.Annotations.registerAllExtensions(mockRegistry);
    // No exception means success
    verifyNoInteractions(mockRegistry);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_notNull() {
    Descriptors.FileDescriptor descriptor = com.google.gson.protobuf.generated.Annotations.getDescriptor();
    assertNotNull(descriptor);
    // Check descriptor contains the extensions for serializedName and serializedValue
    assertNotNull(descriptor.findExtensionByName("serializedName"));
    assertNotNull(descriptor.findExtensionByName("serializedValue"));
  }

  @Test
    @Timeout(8000)
  void testConstants() {
    assertEquals(92066888, com.google.gson.protobuf.generated.Annotations.SERIALIZED_NAME_FIELD_NUMBER);
    assertEquals(92066888, com.google.gson.protobuf.generated.Annotations.SERIALIZED_VALUE_FIELD_NUMBER);

    assertNotNull(com.google.gson.protobuf.generated.Annotations.serializedName);
    assertEquals(String.class, com.google.gson.protobuf.generated.Annotations.serializedName.getDescriptor().getMessageType().getFile().getOptions().getClass());

    assertNotNull(com.google.gson.protobuf.generated.Annotations.serializedValue);
    assertEquals(String.class, com.google.gson.protobuf.generated.Annotations.serializedValue.getDescriptor().getMessageType().getFile().getOptions().getClass());
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    var constructor = com.google.gson.protobuf.generated.Annotations.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    var instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof com.google.gson.protobuf.generated.Annotations);
  }

  @Test
    @Timeout(8000)
  void testInvokePrivateRegisterAllExtensionsUsingReflection() throws Exception {
    Method methodLite = com.google.gson.protobuf.generated.Annotations.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    methodLite.setAccessible(true);
    methodLite.invoke(null, mockRegistryLite);

    Method method = com.google.gson.protobuf.generated.Annotations.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    method.setAccessible(true);
    method.invoke(null, mockRegistry);
  }
}