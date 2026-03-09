package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.Extension;
import com.google.common.base.CaseFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ProtoTypeAdapter_467_1Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private EnumValueOptions optionsMock;
  private Extension<EnumValueOptions, String> extensionMock;
  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions;

  @BeforeEach
  void setUp() throws Exception {
    // Create mocks
    optionsMock = mock(EnumValueOptions.class);
    extensionMock = mock(Extension.class);

    // Create a set with the mocked extension
    serializedEnumValueExtensions = new HashSet<>();
    serializedEnumValueExtensions.add(extensionMock);

    // Use reflection to create ProtoTypeAdapter instance with private constructor
    Class<?> protoTypeAdapterClass = ProtoTypeAdapter.class;
    Constructor<?> constructor = protoTypeAdapterClass.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    protoTypeAdapter = (ProtoTypeAdapter) constructor.newInstance(
        null,
        null,
        null,
        Collections.emptySet(),
        serializedEnumValueExtensions);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_returnsExtensionValueWhenPresent() throws Exception {
    // Arrange
    String expectedValue = "customValue";
    when(optionsMock.hasExtension(extensionMock)).thenReturn(true);
    when(optionsMock.getExtension(extensionMock)).thenReturn(expectedValue);

    // Use reflection to invoke private method
    var method = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCustSerializedEnumValue",
        EnumValueOptions.class,
        String.class);
    method.setAccessible(true);

    // Act
    String actual = (String) method.invoke(protoTypeAdapter, optionsMock, "defaultValue");

    // Assert
    assertEquals(expectedValue, actual);
    verify(optionsMock).hasExtension(extensionMock);
    verify(optionsMock).getExtension(extensionMock);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_returnsDefaultValueWhenNoExtensionPresent() throws Exception {
    // Arrange
    when(optionsMock.hasExtension(extensionMock)).thenReturn(false);

    // Use reflection to invoke private method
    var method = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCustSerializedEnumValue",
        EnumValueOptions.class,
        String.class);
    method.setAccessible(true);

    // Act
    String actual = (String) method.invoke(protoTypeAdapter, optionsMock, "defaultValue");

    // Assert
    assertEquals("defaultValue", actual);
    verify(optionsMock).hasExtension(extensionMock);
    verify(optionsMock, never()).getExtension(extensionMock);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_returnsDefaultValueWhenNoExtensions() throws Exception {
    // Arrange
    // Create ProtoTypeAdapter with empty serializedEnumValueExtensions set
    Class<?> protoTypeAdapterClass = ProtoTypeAdapter.class;
    Constructor<?> constructor = protoTypeAdapterClass.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    ProtoTypeAdapter adapterWithEmptyExtensions = (ProtoTypeAdapter) constructor.newInstance(
        null,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet());

    // Use reflection to invoke private method
    var method = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCustSerializedEnumValue",
        EnumValueOptions.class,
        String.class);
    method.setAccessible(true);

    // Act
    String actual = (String) method.invoke(adapterWithEmptyExtensions, optionsMock, "defaultValue");

    // Assert
    assertEquals("defaultValue", actual);
    verifyNoInteractions(optionsMock);
  }
}