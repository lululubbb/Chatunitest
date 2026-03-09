package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

class Annotations_498_4Test {

  private ExtensionRegistryLite mockRegistryLite;
  private ExtensionRegistry mockRegistry;

  @BeforeEach
  void setUp() {
    mockRegistryLite = mock(ExtensionRegistryLite.class);
    mockRegistry = mock(ExtensionRegistry.class);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_ExtensionRegistryLite_notNull() throws Exception {
    // Use reflection to call static method Annotations.registerAllExtensions(ExtensionRegistryLite)
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Method registerAllExtensionsLite = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    registerAllExtensionsLite.invoke(null, mockRegistryLite);
    verifyNoMoreInteractions(mockRegistryLite);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_ExtensionRegistry_notNull() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Method registerAllExtensions = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    registerAllExtensions.invoke(null, mockRegistry);
    verifyNoMoreInteractions(mockRegistry);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_notNull() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Method getDescriptor = annotationsClass.getDeclaredMethod("getDescriptor");
    Object descriptor = getDescriptor.invoke(null);
    assertNotNull(descriptor);
    assertTrue(descriptor instanceof Descriptors.FileDescriptor);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    var constructor = annotationsClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(annotationsClass.isInstance(instance));
  }

  @Test
    @Timeout(8000)
  void testSerializedNameFieldNumber() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    int serializedNameFieldNumber = annotationsClass.getField("SERIALIZED_NAME_FIELD_NUMBER").getInt(null);
    assertEquals(92066888, serializedNameFieldNumber);
  }

  @Test
    @Timeout(8000)
  void testSerializedValueFieldNumber() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    int serializedValueFieldNumber = annotationsClass.getField("SERIALIZED_VALUE_FIELD_NUMBER").getInt(null);
    assertEquals(92066888, serializedValueFieldNumber);
  }

  @Test
    @Timeout(8000)
  void testSerializedNameExtensionNotNull() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Object serializedName = annotationsClass.getField("serializedName").get(null);
    assertNotNull(serializedName);
  }

  @Test
    @Timeout(8000)
  void testSerializedValueExtensionNotNull() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Object serializedValue = annotationsClass.getField("serializedValue").get(null);
    assertNotNull(serializedValue);
  }
}