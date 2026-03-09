package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;

class Annotations_498_6Test {

  private ExtensionRegistryLite mockRegistryLite;
  private ExtensionRegistry mockRegistry;

  @BeforeEach
  void setUp() {
    mockRegistryLite = mock(ExtensionRegistryLite.class);
    mockRegistry = mock(ExtensionRegistry.class);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_ExtensionRegistryLite() {
    // Just verify that method can be called without exceptions
    Annotations.registerAllExtensions(mockRegistryLite);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_ExtensionRegistry() {
    // Just verify that method can be called without exceptions
    Annotations.registerAllExtensions(mockRegistry);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor() {
    Descriptors.FileDescriptor descriptor = Annotations.getDescriptor();
    assertNotNull(descriptor);
  }

  @Test
    @Timeout(8000)
  void testConstants() {
    assertEquals(92066888, Annotations.SERIALIZED_NAME_FIELD_NUMBER);
    assertEquals(92066888, Annotations.SERIALIZED_VALUE_FIELD_NUMBER);

    assertNotNull(Annotations.serializedName);
    assertEquals(String.class, Annotations.serializedName.getDescriptor().getOptions().getClass().getSuperclass());

    assertNotNull(Annotations.serializedValue);
    assertEquals(String.class, Annotations.serializedValue.getDescriptor().getOptions().getClass().getSuperclass());
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
    var constructor = Annotations.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof Annotations);
  }
}