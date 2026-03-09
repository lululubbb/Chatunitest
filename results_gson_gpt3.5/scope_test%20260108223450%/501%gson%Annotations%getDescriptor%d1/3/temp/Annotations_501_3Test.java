package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.google.protobuf.Descriptors.FileDescriptor;
import java.lang.reflect.Field;
import org.mockito.Mockito;

class Annotations_501_3Test {

  @BeforeAll
  static void setup() throws Exception {
    // Use reflection to set the private static descriptor field before tests
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Field descriptorField = annotationsClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    // Create a mock FileDescriptor instance for testing
    FileDescriptor dummyDescriptor = Mockito.mock(FileDescriptor.class);

    descriptorField.set(null, dummyDescriptor);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_returnsDescriptor() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    FileDescriptor descriptor = (FileDescriptor) annotationsClass.getMethod("getDescriptor").invoke(null);
    assertNotNull(descriptor);
  }
}