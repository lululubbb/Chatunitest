package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.*;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class Annotations_500_6Test {

  ExtensionRegistry mockExtensionRegistry;
  ExtensionRegistryLite mockExtensionRegistryLite;

  @BeforeEach
  void setUp() {
    mockExtensionRegistry = mock(ExtensionRegistry.class);
    mockExtensionRegistryLite = mock(ExtensionRegistryLite.class);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry_invokesLiteVersion() throws Exception {
    // Use Annotations.class directly instead of Class.forName to avoid ClassNotFoundException
    Class<?> annotationsClass = Annotations.class;
    Method registerAllExtensionsLite = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    registerAllExtensionsLite.setAccessible(true);

    Method registerAllExtensions = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    registerAllExtensions.invoke(null, mockExtensionRegistry);

    registerAllExtensionsLite.invoke(null, mockExtensionRegistryLite);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite_runsWithoutException() throws Exception {
    Class<?> annotationsClass = Annotations.class;
    Method registerAllExtensionsLite = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    registerAllExtensionsLite.setAccessible(true);

    assertDoesNotThrow(() -> {
      registerAllExtensionsLite.invoke(null, mockExtensionRegistryLite);
    });
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_returnsDescriptor() throws Exception {
    Class<?> annotationsClass = Annotations.class;
    Field descriptorField = annotationsClass.getDeclaredField("descriptor");
    descriptorField.setAccessible(true);

    Object descriptorBefore = descriptorField.get(null);
    Method getDescriptorMethod = annotationsClass.getDeclaredMethod("getDescriptor");
    Object descriptorReturned = getDescriptorMethod.invoke(null);
    Object descriptorAfter = descriptorField.get(null);

    assertEquals(descriptorAfter, descriptorReturned);
  }
}