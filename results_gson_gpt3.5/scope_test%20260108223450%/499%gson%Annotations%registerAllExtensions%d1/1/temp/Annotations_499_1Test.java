package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.*;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.ExtensionLite;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Annotations_499_1Test {

  @Test
    @Timeout(8000)
  void registerAllExtensions_withExtensionRegistryLite_callsAddTwice() throws Exception {
    ExtensionRegistryLite registryLite = Mockito.mock(ExtensionRegistryLite.class);

    // Use the class directly instead of Class.forName to avoid ClassNotFoundException
    Class<?> annotationsClass = Annotations.class;

    Field serializedNameField = annotationsClass.getDeclaredField("serializedName");
    Object serializedName = serializedNameField.get(null);
    Field serializedValueField = annotationsClass.getDeclaredField("serializedValue");
    Object serializedValue = serializedValueField.get(null);

    ExtensionLite<?, ?> extSerializedName = (ExtensionLite<?, ?>) serializedName;
    ExtensionLite<?, ?> extSerializedValue = (ExtensionLite<?, ?>) serializedValue;

    Method registerAllExtensionsMethod = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);

    registerAllExtensionsMethod.invoke(null, registryLite);

    verify(registryLite, times(1)).add(extSerializedName);
    verify(registryLite, times(1)).add(extSerializedValue);
    verifyNoMoreInteractions(registryLite);
  }

  @Test
    @Timeout(8000)
  void registerAllExtensions_withExtensionRegistry_callsAddTwice() throws Exception {
    ExtensionRegistry registry = Mockito.mock(ExtensionRegistry.class);

    // Use the class directly instead of Class.forName to avoid ClassNotFoundException
    Class<?> annotationsClass = Annotations.class;

    Field serializedNameField = annotationsClass.getDeclaredField("serializedName");
    Object serializedName = serializedNameField.get(null);
    Field serializedValueField = annotationsClass.getDeclaredField("serializedValue");
    Object serializedValue = serializedValueField.get(null);

    GeneratedMessage.GeneratedExtension<?, ?> extSerializedName = (GeneratedMessage.GeneratedExtension<?, ?>) serializedName;
    GeneratedMessage.GeneratedExtension<?, ?> extSerializedValue = (GeneratedMessage.GeneratedExtension<?, ?>) serializedValue;

    Method registerAllExtensionsMethod = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);

    registerAllExtensionsMethod.invoke(null, registry);

    verify(registry, times(1)).add(extSerializedName);
    verify(registry, times(1)).add(extSerializedValue);
    verifyNoMoreInteractions(registry);
  }
}