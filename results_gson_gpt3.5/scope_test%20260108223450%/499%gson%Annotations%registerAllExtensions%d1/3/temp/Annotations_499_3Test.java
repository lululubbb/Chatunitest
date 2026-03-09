package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class Annotations_499_3Test {

  @Test
    @Timeout(8000)
  void testRegisterAllExtensions() throws Exception {
    ExtensionRegistryLite registry = mock(ExtensionRegistryLite.class);

    // Use the class directly instead of reflection to avoid ClassNotFoundException
    Class<?> annotationsClass = Annotations.class;

    // Get the registerAllExtensions method and invoke it
    annotationsClass.getMethod("registerAllExtensions", ExtensionRegistryLite.class)
        .invoke(null, registry);

    // Access the static fields serializedName and serializedValue via reflection
    Field serializedNameField = annotationsClass.getField("serializedName");
    Object serializedName = serializedNameField.get(null);

    Field serializedValueField = annotationsClass.getField("serializedValue");
    Object serializedValue = serializedValueField.get(null);

    // Cast to GeneratedExtension to match the add method parameter type
    verify(registry).add((GeneratedExtension<?, ?>) serializedName);
    verify(registry).add((GeneratedExtension<?, ?>) serializedValue);
  }
}