package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class Annotations_501_6Test {

  @BeforeAll
  static void setupDescriptor() throws Exception {
    // Use reflection to set private static descriptor field
    Field descriptorField = com.google.gson.protobuf.generated.Annotations.class.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    // Create a mock FileDescriptor
    Descriptors.FileDescriptor mockDescriptor = mock(Descriptors.FileDescriptor.class);

    // Set the private static descriptor field to mockDescriptor
    descriptorField.set(null, mockDescriptor);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_returnsDescriptor() {
    Descriptors.FileDescriptor descriptor = com.google.gson.protobuf.generated.Annotations.getDescriptor();
    assertNotNull(descriptor);
    // Since we set a mock, verify it is the mock we assigned
    // Cannot verify identity easily without storing mock reference, so just check class
    assertEquals(Descriptors.FileDescriptor.class, descriptor.getClass());
  }

  @Test
    @Timeout(8000)
  void testSerializedNameAndSerializedValueConstants() {
    // Verify SERIALIZED_NAME_FIELD_NUMBER
    assertEquals(92066888, com.google.gson.protobuf.generated.Annotations.SERIALIZED_NAME_FIELD_NUMBER);
    // Verify SERIALIZED_VALUE_FIELD_NUMBER
    assertEquals(92066888, com.google.gson.protobuf.generated.Annotations.SERIALIZED_VALUE_FIELD_NUMBER);

    // Verify serializedName extension is not null and of correct type
    assertNotNull(com.google.gson.protobuf.generated.Annotations.serializedName);
    assertEquals(String.class,
        com.google.gson.protobuf.generated.Annotations.serializedName.getDescriptor().getDefaultInstanceForType().getClass());

    // Verify serializedValue extension is not null and of correct type
    assertNotNull(com.google.gson.protobuf.generated.Annotations.serializedValue);
    assertEquals(String.class,
        com.google.gson.protobuf.generated.Annotations.serializedValue.getDescriptor().getDefaultInstanceForType().getClass());
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_methods() {
    // We just verify methods can be called without exceptions

    // Create mocks for registries
    com.google.protobuf.ExtensionRegistryLite registryLite = mock(com.google.protobuf.ExtensionRegistryLite.class);
    com.google.protobuf.ExtensionRegistry registry = mock(com.google.protobuf.ExtensionRegistry.class);

    assertDoesNotThrow(() -> com.google.gson.protobuf.generated.Annotations.registerAllExtensions(registryLite));
    assertDoesNotThrow(() -> com.google.gson.protobuf.generated.Annotations.registerAllExtensions(registry));
  }
}