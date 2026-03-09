package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors.FileDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class Bag_67_2Test {

  @BeforeEach
  void setUp() throws Exception {
    // Use the class directly instead of Class.forName to avoid ClassNotFoundException
    Class<?> bagClass = Bag.class;
    Field descriptorField = bagClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(descriptorField, descriptorField.getModifiers() & ~Modifier.FINAL);

    FileDescriptor mockDescriptor = mock(FileDescriptor.class);
    descriptorField.set(null, mockDescriptor);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_returnsDescriptor() throws Exception {
    Class<?> bagClass = Bag.class;
    FileDescriptor descriptor = (FileDescriptor) bagClass.getMethod("getDescriptor").invoke(null);
    assertNotNull(descriptor, "getDescriptor() should not return null");
  }
}