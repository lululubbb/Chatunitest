package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.*;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class Annotations_499_4Test {

  private ExtensionRegistryLite registryLiteMock;

  private GeneratedExtension<?, ?> serializedName;
  private GeneratedExtension<?, ?> serializedValue;

  @BeforeEach
  void setUp() throws Exception {
    registryLiteMock = mock(ExtensionRegistryLite.class);

    Class<?> annotationsClass = Annotations.class;

    Field serializedNameField = annotationsClass.getDeclaredField("serializedName");
    serializedNameField.setAccessible(true);
    serializedName = (GeneratedExtension<?, ?>) serializedNameField.get(null);

    Field serializedValueField = annotationsClass.getDeclaredField("serializedValue");
    serializedValueField.setAccessible(true);
    serializedValue = (GeneratedExtension<?, ?>) serializedValueField.get(null);
  }

  @Test
    @Timeout(8000)
  void registerAllExtensions_addsSerializedNameAndSerializedValue() throws Exception {
    Annotations.registerAllExtensions(registryLiteMock);

    verify(registryLiteMock).add(serializedName);
    verify(registryLiteMock).add(serializedValue);
    verifyNoMoreInteractions(registryLiteMock);
  }
}