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
import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Annotations_498_2Test {

  private ExtensionRegistryLite mockRegistryLite;
  private ExtensionRegistry mockRegistry;
  private Class<?> annotationsClass;

  @BeforeEach
  void setUp() {
    try {
      // Adjusted class name to match the actual generated class name in the test environment
      annotationsClass = Class.forName(this.getClass().getPackageName() + ".Annotations");
    } catch (ClassNotFoundException e) {
      fail("Annotations class not found: " + e);
    }
    mockRegistryLite = mock(ExtensionRegistryLite.class);
    mockRegistry = mock(ExtensionRegistry.class);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_WithExtensionRegistryLite() {
    try {
      Method method = annotationsClass.getMethod("registerAllExtensions", ExtensionRegistryLite.class);
      method.invoke(null, mockRegistryLite);
    } catch (Exception e) {
      fail("Invocation of registerAllExtensions(ExtensionRegistryLite) failed: " + e);
    }

    verifyNoInteractions(mockRegistryLite);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_WithExtensionRegistry() {
    try {
      Method method = annotationsClass.getMethod("registerAllExtensions", ExtensionRegistry.class);
      method.invoke(null, mockRegistry);
    } catch (Exception e) {
      fail("Invocation of registerAllExtensions(ExtensionRegistry) failed: " + e);
    }

    verifyNoInteractions(mockRegistry);
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_NotNull() {
    try {
      Method getDescriptorMethod = annotationsClass.getMethod("getDescriptor");
      Object descriptor = getDescriptorMethod.invoke(null);
      assertNotNull(descriptor);
      assertTrue(descriptor instanceof Descriptors.FileDescriptor);
    } catch (Exception e) {
      fail("Invocation of getDescriptor() failed: " + e);
    }
  }

  @Test
    @Timeout(8000)
  void testSerializedNameAndSerializedValueFields() {
    try {
      Field serializedNameField = annotationsClass.getField("serializedName");
      Object serializedName = serializedNameField.get(null);
      assertNotNull(serializedName);
      Method getExtensionTypeMethod = serializedName.getClass().getMethod("getExtensionType");
      Object extensionType = getExtensionTypeMethod.invoke(serializedName);
      assertEquals(String.class, extensionType);

      Field serializedValueField = annotationsClass.getField("serializedValue");
      Object serializedValue = serializedValueField.get(null);
      assertNotNull(serializedValue);
      Object extensionType2 = getExtensionTypeMethod.invoke(serializedValue);
      assertEquals(String.class, extensionType2);

      Field serializedNameFieldNumberField = annotationsClass.getField("SERIALIZED_NAME_FIELD_NUMBER");
      int serializedNameFieldNumber = serializedNameFieldNumberField.getInt(null);
      assertEquals(92066888, serializedNameFieldNumber);

      Field serializedValueFieldNumberField = annotationsClass.getField("SERIALIZED_VALUE_FIELD_NUMBER");
      int serializedValueFieldNumber = serializedValueFieldNumberField.getInt(null);
      assertEquals(92066888, serializedValueFieldNumber);

    } catch (Exception e) {
      fail("Reflection failed for serializedName/serializedValue fields: " + e);
    }
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() {
    try {
      Constructor<?> constructor = annotationsClass.getDeclaredConstructor();
      constructor.setAccessible(true);
      Object instance = constructor.newInstance();
      assertNotNull(instance);
      assertEquals(annotationsClass, instance.getClass());
    } catch (Exception e) {
      fail("Instantiation of private constructor failed: " + e);
    }
  }
}