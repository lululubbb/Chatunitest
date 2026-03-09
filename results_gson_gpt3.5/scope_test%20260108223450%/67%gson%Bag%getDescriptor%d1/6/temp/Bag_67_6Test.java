package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Assumptions;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;

class Bag_67_6Test {

  private static FileDescriptor descriptorBackup;

  @BeforeAll
  static void setUp() throws Exception {
    try {
      Class<?> bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
      Field descriptorField = bagClass.getDeclaredField("descriptor");
      descriptorField.setAccessible(true);
      descriptorBackup = (FileDescriptor) descriptorField.get(null);
    } catch (ClassNotFoundException e) {
      descriptorBackup = null;
    }
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_NotNull() throws Exception {
    // Skip test if Bag class is not present
    Class<?> bagClass;
    try {
      bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    } catch (ClassNotFoundException e) {
      Assumptions.assumeTrue(false, "Class com.google.gson.protobuf.generated.Bag not found, skipping test");
      return;
    }

    java.lang.reflect.Method getDescriptorMethod = bagClass.getMethod("getDescriptor");
    FileDescriptor result = (FileDescriptor) getDescriptorMethod.invoke(null);
    assertNotNull(result, "getDescriptor should not return null");
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_ReturnsInjectedDescriptor() throws Exception {
    // Skip test if Bag class is not present
    Class<?> bagClass;
    try {
      bagClass = Class.forName("com.google.gson.protobuf.generated.Bag");
    } catch (ClassNotFoundException e) {
      Assumptions.assumeTrue(false, "Class com.google.gson.protobuf.generated.Bag not found, skipping test");
      return;
    }

    FileDescriptor testDescriptor = FileDescriptor.buildFrom(
        FileDescriptorProto.newBuilder()
          .setName("test.proto").build(),
        new FileDescriptor[] {});

    Field descriptorField = bagClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    // Remove final modifier from the descriptor field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(descriptorField, descriptorField.getModifiers() & ~Modifier.FINAL);

    descriptorField.set(null, testDescriptor);

    java.lang.reflect.Method getDescriptorMethod = bagClass.getMethod("getDescriptor");
    FileDescriptor result = (FileDescriptor) getDescriptorMethod.invoke(null);
    assertSame(testDescriptor, result, "getDescriptor should return the injected descriptor");
  }
}