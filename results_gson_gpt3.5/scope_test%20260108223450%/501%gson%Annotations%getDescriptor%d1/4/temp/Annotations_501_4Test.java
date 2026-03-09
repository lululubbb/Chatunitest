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

class Annotations_501_4Test {

  @BeforeAll
  static void setUp() throws Exception {
    // Use reflection to set the private static descriptor field to a mock
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Field descriptorField = annotationsClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);
    FileDescriptor mockDescriptor = mock(FileDescriptor.class);
    descriptorField.set(null, mockDescriptor);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_returnsDescriptor() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    var method = annotationsClass.getDeclaredMethod("getDescriptor");
    FileDescriptor descriptor = (FileDescriptor) method.invoke(null);
    assertNotNull(descriptor);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite() throws Exception {
    ExtensionRegistryLite registryLite = mock(ExtensionRegistryLite.class);
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    var method = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.invoke(null, registryLite);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry() throws Exception {
    ExtensionRegistry registry = mock(ExtensionRegistry.class);
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    var method = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    method.invoke(null, registry);
  }

  @Test
    @Timeout(8000)
  void testSerializedNameAndSerializedValueConstants() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    int serializedNameFieldNumber = (int) annotationsClass.getField("SERIALIZED_NAME_FIELD_NUMBER").get(null);
    int serializedValueFieldNumber = (int) annotationsClass.getField("SERIALIZED_VALUE_FIELD_NUMBER").get(null);

    assertEquals(92066888, serializedNameFieldNumber);
    assertEquals(92066888, serializedValueFieldNumber);

    Object serializedName = annotationsClass.getField("serializedName").get(null);
    Object serializedValue = annotationsClass.getField("serializedValue").get(null);

    assertNotNull(serializedName);
    assertNotNull(serializedValue);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    var constructor = annotationsClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    var instance = constructor.newInstance();
    assertNotNull(instance);
  }
}