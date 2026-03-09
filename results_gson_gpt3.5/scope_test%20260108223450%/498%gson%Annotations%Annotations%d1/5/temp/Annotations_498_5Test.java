package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

class Annotations_498_5Test {

  ExtensionRegistryLite mockRegistryLite;
  ExtensionRegistry mockRegistry;

  @BeforeEach
  void setUp() {
    mockRegistryLite = mock(ExtensionRegistryLite.class);
    mockRegistry = mock(ExtensionRegistry.class);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistryLite_doesNotThrow() {
    assertDoesNotThrow(() -> Annotations.registerAllExtensions(mockRegistryLite));
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_withExtensionRegistry_doesNotThrow() {
    assertDoesNotThrow(() -> Annotations.registerAllExtensions(mockRegistry));
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_notNull() {
    Descriptors.FileDescriptor descriptor = Annotations.getDescriptor();
    assertNotNull(descriptor);
  }

  @Test
    @Timeout(8000)
  void testConstants() {
    assertEquals(92066888, Annotations.SERIALIZED_NAME_FIELD_NUMBER);
    assertEquals(92066888, Annotations.SERIALIZED_VALUE_FIELD_NUMBER);
    assertNotNull(Annotations.serializedName);
    assertNotNull(Annotations.serializedValue);
    assertEquals(String.class, Annotations.serializedName.getMessageDefaultInstance().getClass());
    assertEquals(String.class, Annotations.serializedValue.getMessageDefaultInstance().getClass());
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Constructor<Annotations> constructor = Annotations.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(instance instanceof Annotations);
  }

  @Test
    @Timeout(8000)
  void testInvokePrivateRegisterAllExtensionsReflection() throws Exception {
    Method methodLite = Annotations.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    methodLite.setAccessible(true);
    methodLite.invoke(null, mockRegistryLite);

    Method method = Annotations.class.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    method.setAccessible(true);
    method.invoke(null, mockRegistry);
  }
}