package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;

class Annotations_499_2Test {

  private ExtensionRegistryLite registryLite;
  private Class<?> annotationsClass;
  private Object serializedName;
  private Object serializedValue;

  @BeforeEach
  void setUp() throws Exception {
    registryLite = Mockito.mock(ExtensionRegistryLite.class);
    // Fix: Use the correct class name "Annotations_499_2" instead of "Annotations"
    annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations_499_2");
    Field serializedNameField = annotationsClass.getField("serializedName");
    Field serializedValueField = annotationsClass.getField("serializedValue");
    serializedName = serializedNameField.get(null);
    serializedValue = serializedValueField.get(null);
  }

  @Test
    @Timeout(8000)
  void registerAllExtensions_registersSerializedNameAndSerializedValue() throws Exception {
    annotationsClass.getMethod("registerAllExtensions", ExtensionRegistryLite.class)
        .invoke(null, registryLite);

    verify(registryLite, times(1)).add((com.google.protobuf.ExtensionLite<?, ?>) serializedName);
    verify(registryLite, times(1)).add((com.google.protobuf.ExtensionLite<?, ?>) serializedValue);
  }
}