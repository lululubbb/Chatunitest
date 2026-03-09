package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class Bag_67_3Test {

  private static Descriptors.FileDescriptor mockDescriptor;

  @BeforeAll
  static void setUp() throws Exception {
    mockDescriptor = mock(Descriptors.FileDescriptor.class);

    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");

    Field descriptorField = bagClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    // Remove final modifier if present (Java 12+ may not allow this, but keep for compatibility)
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(descriptorField, descriptorField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    descriptorField.set(null, mockDescriptor);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_returnsDescriptorField() throws Exception {
    Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    java.lang.reflect.Method getDescriptorMethod = bagClass.getDeclaredMethod("getDescriptor");
    Object result = getDescriptorMethod.invoke(null);
    assertNotNull(result);
    assertSame(mockDescriptor, result);
  }
}