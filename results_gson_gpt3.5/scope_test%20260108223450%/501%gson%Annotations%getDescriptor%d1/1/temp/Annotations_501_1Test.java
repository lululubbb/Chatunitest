package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors.FileDescriptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

class Annotations_501_1Test {

  private static FileDescriptor mockDescriptor;

  @BeforeAll
  static void setup() throws Exception {
    mockDescriptor = mock(FileDescriptor.class);
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Field descriptorField = annotationsClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    // Remove final modifier if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(descriptorField, descriptorField.getModifiers() & ~Modifier.FINAL);

    descriptorField.set(null, mockDescriptor);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_returnsDescriptor() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    java.lang.reflect.Method getDescriptorMethod = annotationsClass.getMethod("getDescriptor");
    Object result = getDescriptorMethod.invoke(null);
    assertNotNull(result);
    assertSame(mockDescriptor, result);
  }
}