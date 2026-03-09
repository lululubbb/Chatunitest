package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class Bag_67_1Test {

  private static Descriptors.FileDescriptor mockFileDescriptor;

  @BeforeAll
  static void setUp() throws Exception {
    // Create a mock FileDescriptor
    mockFileDescriptor = mock(Descriptors.FileDescriptor.class);

    // Use reflection to get the Bag class by name
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    Field descriptorField = bagClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    // Remove final modifier from the 'descriptor' field if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(descriptorField, descriptorField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    descriptorField.set(null, mockFileDescriptor);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_returnsDescriptor() throws Exception {
    // Use reflection to get the Bag class by name
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    java.lang.reflect.Method getDescriptorMethod = bagClass.getDeclaredMethod("getDescriptor");
    Descriptors.FileDescriptor descriptor = (Descriptors.FileDescriptor) getDescriptorMethod.invoke(null);
    assertNotNull(descriptor, "Descriptor should not be null");
    assertSame(mockFileDescriptor, descriptor, "Returned descriptor should be the mocked instance");
  }
}