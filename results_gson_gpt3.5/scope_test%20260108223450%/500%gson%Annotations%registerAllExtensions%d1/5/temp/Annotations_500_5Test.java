package com.google.gson.protobuf.generated;
import org.junit.jupiter.api.Timeout;

import static org.mockito.Mockito.mock;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class Annotations_500_5Test {

  private ExtensionRegistry extensionRegistryMock;
  private ExtensionRegistryLite extensionRegistryLiteMock;

  @BeforeEach
  public void setUp() {
    extensionRegistryMock = mock(ExtensionRegistry.class);
    extensionRegistryLiteMock = mock(ExtensionRegistryLite.class);
  }

  @Test
    @Timeout(8000)
  public void testRegisterAllExtensions_withExtensionRegistry_invokesExtensionRegistryLite() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Method method = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistry.class);
    method.invoke(null, extensionRegistryMock);

    // No exception means successful call
  }

  @Test
    @Timeout(8000)
  public void testRegisterAllExtensions_withExtensionRegistryLite() throws Exception {
    Class<?> annotationsClass = Class.forName("com.google.gson.protobuf.generated.Annotations");
    Method method = annotationsClass.getDeclaredMethod("registerAllExtensions", ExtensionRegistryLite.class);
    method.setAccessible(true);
    method.invoke(null, extensionRegistryLiteMock);

    // No exception means method executed
  }
}