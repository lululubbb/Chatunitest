package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.ExtensionRegistry;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

class Annotations_499_5Test {

  private static Object getStaticField(Class<?> clazz, String fieldName) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(null);
  }

  private static void invokeRegisterAllExtensions(Object registry) throws Exception {
    Class<?> annotationsClass = Annotations.class;
    Method method = null;
    Class<?> registryClass = registry.getClass().getInterfaces().length > 0 ? registry.getClass().getInterfaces()[0] : registry.getClass();
    // Try to find the method with parameter type ExtensionRegistryLite or ExtensionRegistry
    for (Method m : annotationsClass.getDeclaredMethods()) {
      if (m.getName().equals("registerAllExtensions")) {
        Class<?>[] params = m.getParameterTypes();
        if (params.length == 1 && params[0].isAssignableFrom(registryClass)) {
          method = m;
          break;
        }
      }
    }
    if (method == null) {
      // fallback: try ExtensionRegistryLite
      method = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    }
    method.setAccessible(true);
    method.invoke(null, registry);
  }

  @SuppressWarnings("unchecked")
  @Test
    @Timeout(8000)
  void registerAllExtensions_WithExtensionRegistryLite_AddsSerializedExtensions() throws Exception {
    ExtensionRegistryLite registryLite = Mockito.mock(ExtensionRegistryLite.class);

    invokeRegisterAllExtensions(registryLite);

    Class<?> annotationsClass = Annotations.class;
    Object serializedName = getStaticField(annotationsClass, "serializedName");
    Object serializedValue = getStaticField(annotationsClass, "serializedValue");

    // Cast to the correct type for Mockito verify
    verify(registryLite, times(1)).add((com.google.protobuf.ExtensionLite<?, ?>) serializedName);
    verify(registryLite, times(1)).add((com.google.protobuf.ExtensionLite<?, ?>) serializedValue);
  }

  @SuppressWarnings("unchecked")
  @Test
    @Timeout(8000)
  void registerAllExtensions_WithExtensionRegistry_AddsSerializedExtensions() throws Exception {
    ExtensionRegistry registry = Mockito.mock(ExtensionRegistry.class);

    invokeRegisterAllExtensions(registry);

    Class<?> annotationsClass = Annotations.class;
    Object serializedName = getStaticField(annotationsClass, "serializedName");
    Object serializedValue = getStaticField(annotationsClass, "serializedValue");

    // Cast to the correct type for Mockito verify
    verify(registry, times(1)).add((com.google.protobuf.Extension<?, ?>) serializedName);
    verify(registry, times(1)).add((com.google.protobuf.Extension<?, ?>) serializedValue);
  }
}