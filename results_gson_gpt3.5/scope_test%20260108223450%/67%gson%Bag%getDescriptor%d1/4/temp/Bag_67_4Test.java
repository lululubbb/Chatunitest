package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.assertSame;

import java.lang.reflect.Field;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.DescriptorProtos.FileDescriptorProto;

public class Bag_67_4Test {

  private FileDescriptor mockDescriptor;

  @BeforeEach
  public void setUp() throws Exception {
    mockDescriptor = FileDescriptor.buildFrom(
        FileDescriptorProto.newBuilder()
            .setName("test.proto")
            .build(),
        new FileDescriptor[] {});

    // Use reflection to set the private static field 'descriptor' in Bag class
    Class<?> bagClass = Bag.class;  // Use direct class reference instead of Class.forName

    Field descriptorField = bagClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    // Remove final modifier from the 'descriptor' field if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(descriptorField, descriptorField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    descriptorField.set(null, mockDescriptor);
  }

  @Test
    @Timeout(8000)
  public void testGetDescriptor_returnsDescriptor() throws Exception {
    FileDescriptor returnedDescriptor = Bag.getDescriptor();
    assertSame(mockDescriptor, returnedDescriptor, "getDescriptor should return the static descriptor field");
  }
}