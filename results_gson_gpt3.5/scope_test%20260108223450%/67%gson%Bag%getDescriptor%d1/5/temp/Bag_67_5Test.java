package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import com.google.protobuf.Descriptors.FileDescriptor;

import java.lang.reflect.Field;

class Bag_67_5Test {

  @Test
    @Timeout(8000)
  void testGetDescriptor_whenDescriptorIsSet_returnsDescriptor() throws Exception {
    // Use reflection to set the private static field 'descriptor'
    Field descriptorField = Class.forName("com.google.gson.protobuf.generated.Bag").getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    FileDescriptor mockDescriptor = Mockito.mock(FileDescriptor.class);
    descriptorField.set(null, mockDescriptor);

    FileDescriptor result = (FileDescriptor) Class.forName("com.google.gson.protobuf.generated.Bag")
        .getMethod("getDescriptor")
        .invoke(null);

    assertSame(mockDescriptor, result);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_whenDescriptorIsNull_returnsNull() throws Exception {
    // Use reflection to set the private static field 'descriptor' to null
    Field descriptorField = Class.forName("com.google.gson.protobuf.generated.Bag").getDeclaredField("descriptor");
    descriptorField.setAccessible(true);
    descriptorField.set(null, null);

    FileDescriptor result = (FileDescriptor) Class.forName("com.google.gson.protobuf.generated.Bag")
        .getMethod("getDescriptor")
        .invoke(null);

    assertNull(result);
  }
}