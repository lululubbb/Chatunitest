package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;
package com.google.gson.protobuf.protobuf.generated;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors.FileDescriptor;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class Annotations_501_2Test {

  private static FileDescriptor mockDescriptor;

  @BeforeAll
  static void setup() throws Exception {
    // The actual class name to load is Annotations_501_2 (matching the test class package)
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    mockDescriptor = mock(FileDescriptor.class);
    // Use reflection to set private static field descriptor
    Field descriptorField = annotationsClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);
    descriptorField.set(null, mockDescriptor);
  }

  @Test
    @Timeout(8000)
  void getDescriptor_returnsDescriptor() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    FileDescriptor result = (FileDescriptor) annotationsClass.getMethod("getDescriptor").invoke(null);
    assertNotNull(result);
    assertSame(mockDescriptor, result);
  }

  @Test
    @Timeout(8000)
  void registerAllExtensions_extensionRegistryLite_callsNoException() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    ExtensionRegistryLite registryLite = mock(ExtensionRegistryLite.class);
    annotationsClass.getMethod("registerAllExtensions", ExtensionRegistryLite.class).invoke(null, registryLite);
  }

  @Test
    @Timeout(8000)
  void registerAllExtensions_extensionRegistry_callsNoException() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    ExtensionRegistry registry = mock(ExtensionRegistry.class);
    annotationsClass.getMethod("registerAllExtensions", ExtensionRegistry.class).invoke(null, registry);
  }

  @Test
    @Timeout(8000)
  void serializedName_and_serializedValue_fields_notNull() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Object serializedName = annotationsClass.getField("serializedName").get(null);
    Object serializedValue = annotationsClass.getField("serializedValue").get(null);
    assertNotNull(serializedName);
    assertNotNull(serializedValue);
  }

  @Test
    @Timeout(8000)
  void serializedName_and_serializedValue_haveExpectedFieldNumbers() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    int serializedNameFieldNumber = annotationsClass.getField("SERIALIZED_NAME_FIELD_NUMBER").getInt(null);
    int serializedValueFieldNumber = annotationsClass.getField("SERIALIZED_VALUE_FIELD_NUMBER").getInt(null);
    assertEquals(92066888, serializedNameFieldNumber);
    assertEquals(92066888, serializedValueFieldNumber);
  }
}