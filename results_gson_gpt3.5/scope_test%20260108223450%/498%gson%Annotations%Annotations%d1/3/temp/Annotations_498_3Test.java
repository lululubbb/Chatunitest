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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class Annotations_498_3Test {

  private ExtensionRegistryLite mockRegistryLite;
  private ExtensionRegistry mockRegistry;

  @BeforeEach
  void setUp() {
    mockRegistryLite = mock(ExtensionRegistryLite.class);
    mockRegistry = mock(ExtensionRegistry.class);
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_ExtensionRegistryLite() throws Exception {
    Class<?> annotationsClass = Annotations.class;
    Method method = annotationsClass.getMethod("registerAllExtensions", ExtensionRegistryLite.class);
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, mockRegistryLite);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions_ExtensionRegistry() throws Exception {
    Class<?> annotationsClass = Annotations.class;
    Method method = annotationsClass.getMethod("registerAllExtensions", ExtensionRegistry.class);
    assertDoesNotThrow(() -> {
      try {
        method.invoke(null, mockRegistry);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
  }

  @Test
    @Timeout(8000)
  void testGetDescriptor_notNull() throws Exception {
    Class<?> annotationsClass = Annotations.class;
    Method method = annotationsClass.getMethod("getDescriptor");
    Object result = method.invoke(null);
    assertNotNull(result);
    assertTrue(result instanceof Descriptors.FileDescriptor);
  }

  @Test
    @Timeout(8000)
  void testConstants() throws Exception {
    Class<?> annotationsClass = Annotations.class;
    assertEquals(92066888, annotationsClass.getField("SERIALIZED_NAME_FIELD_NUMBER").getInt(null));
    assertEquals(92066888, annotationsClass.getField("SERIALIZED_VALUE_FIELD_NUMBER").getInt(null));
    assertNotNull(annotationsClass.getField("serializedName").get(null));
    assertNotNull(annotationsClass.getField("serializedValue").get(null));
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Class<?> annotationsClass = Annotations.class;
    Constructor<?> constructor = annotationsClass.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
    assertTrue(annotationsClass.isInstance(instance));
  }
}