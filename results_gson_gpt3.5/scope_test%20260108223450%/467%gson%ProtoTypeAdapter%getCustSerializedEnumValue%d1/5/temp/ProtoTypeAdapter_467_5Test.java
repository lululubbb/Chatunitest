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

class ProtoTypeAdapter_467_5Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private Extension<EnumValueOptions, String> extensionMock1;
  private Extension<EnumValueOptions, String> extensionMock2;
  private EnumValueOptions optionsMock;

  @BeforeEach
  void setUp() throws Exception {
    // Create mocks for dependencies
    extensionMock1 = mock(Extension.class);
    extensionMock2 = mock(Extension.class);
    optionsMock = mock(EnumValueOptions.class);

    // Prepare a set of extensions with two mocks
    Set<Extension<EnumValueOptions, String>> extensions = new HashSet<>();
    extensions.add(extensionMock1);
    extensions.add(extensionMock2);

    // Use reflection to create ProtoTypeAdapter instance with private constructor
    // Replace EnumSerialization.class with Object.class to fix compilation error
    Class<?> clazz = ProtoTypeAdapter.class;
    Constructor<?> constructor = clazz.getDeclaredConstructor(
        Object.class,
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
        extensions);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_extensionPresent_returnsExtensionValue() throws Exception {
    // Setup: options.hasExtension(extensionMock1) returns true
    when(optionsMock.hasExtension(extensionMock1)).thenReturn(true);
    when(optionsMock.hasExtension(extensionMock2)).thenReturn(false);
    when(optionsMock.getExtension(extensionMock1)).thenReturn("customValue");

    // Use reflection to invoke private method
    String defaultValue = "default";
    String result = invokeGetCustSerializedEnumValue(optionsMock, defaultValue);

    assertEquals("customValue", result);

    verify(optionsMock).hasExtension(extensionMock1);
    verify(optionsMock, never()).getExtension(extensionMock2);
    verify(optionsMock).getExtension(extensionMock1);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_noExtensionPresent_returnsDefaultValue() throws Exception {
    // Setup: options.hasExtension(...) returns false for all
    when(optionsMock.hasExtension(extensionMock1)).thenReturn(false);
    when(optionsMock.hasExtension(extensionMock2)).thenReturn(false);

    String defaultValue = "defaultValue";

    String result = invokeGetCustSerializedEnumValue(optionsMock, defaultValue);

    assertEquals(defaultValue, result);

    verify(optionsMock).hasExtension(extensionMock1);
    verify(optionsMock).hasExtension(extensionMock2);
    verify(optionsMock, never()).getExtension(any());
  }

  @SuppressWarnings("unchecked")
  private String invokeGetCustSerializedEnumValue(EnumValueOptions options, String defaultValue) throws Exception {
    var method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);
    return (String) method.invoke(protoTypeAdapter, options, defaultValue);
  }
}