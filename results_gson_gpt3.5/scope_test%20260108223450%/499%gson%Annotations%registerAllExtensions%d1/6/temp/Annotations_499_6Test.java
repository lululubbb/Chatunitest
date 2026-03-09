package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.*;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.ExtensionRegistry;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Annotations_499_6Test {

  @Test
    @Timeout(8000)
  void registerAllExtensions_usesExtensionRegistryLite_addsBothExtensions() throws Exception {
    ExtensionRegistryLite registryLite = mock(ExtensionRegistryLite.class);

    // Load Annotations class by name using reflection
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Field serializedNameField = annotationsClass.getDeclaredField("serializedName");
    Object serializedName = serializedNameField.get(null);
    Field serializedValueField = annotationsClass.getDeclaredField("serializedValue");
    Object serializedValue = serializedValueField.get(null);

    Method registerAllExtensionsMethod = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    registerAllExtensionsMethod.invoke(null, registryLite);

    // Cast to appropriate type for verify
    verify(registryLite).add((com.google.protobuf.ExtensionLite<?, ?>) serializedName);
    verify(registryLite).add((com.google.protobuf.ExtensionLite<?, ?>) serializedValue);
    verifyNoMoreInteractions(registryLite);
  }

  @Test
    @Timeout(8000)
  void registerAllExtensions_usesExtensionRegistry_addsBothExtensions() throws Exception {
    ExtensionRegistry registry = mock(ExtensionRegistry.class);

    // Load Annotations class by name using reflection
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Field serializedNameField = annotationsClass.getDeclaredField("serializedName");
    Object serializedName = serializedNameField.get(null);
    Field serializedValueField = annotationsClass.getDeclaredField("serializedValue");
    Object serializedValue = serializedValueField.get(null);

    Method registerAllExtensionsMethod = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    registerAllExtensionsMethod.invoke(null, registry);

    // Cast to appropriate type for verify
    verify(registry).add((com.google.protobuf.Extension<?, ?>) serializedName);
    verify(registry).add((com.google.protobuf.Extension<?, ?>) serializedValue);
    verifyNoMoreInteractions(registry);
  }
}