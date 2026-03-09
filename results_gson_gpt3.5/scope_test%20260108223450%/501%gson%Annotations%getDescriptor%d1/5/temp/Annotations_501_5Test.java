package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors.FileDescriptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class Annotations_501_5Test {

  @BeforeAll
  static void setUp() throws Exception {
    // Use reflection to set the private static descriptor field in the Annotations class
    Class<?> annotationsClass = Annotations.class;
    Field descriptorField = annotationsClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    FileDescriptor mockDescriptor = mock(FileDescriptor.class);
    descriptorField.set(null, mockDescriptor);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_returnsDescriptor() throws Exception {
    FileDescriptor descriptor = Annotations.getDescriptor();
    assertNotNull(descriptor);
  }
}