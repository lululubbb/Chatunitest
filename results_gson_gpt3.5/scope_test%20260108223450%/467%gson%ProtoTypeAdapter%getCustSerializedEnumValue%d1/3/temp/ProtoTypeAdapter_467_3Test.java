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
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ProtoTypeAdapter_467_3Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions;

  private Extension<EnumValueOptions, String> mockExtension1;
  private Extension<EnumValueOptions, String> mockExtension2;
  private EnumValueOptions mockOptions;

  @BeforeEach
  void setUp() throws Exception {
    mockExtension1 = mock(Extension.class);
    mockExtension2 = mock(Extension.class);
    serializedEnumValueExtensions = new HashSet<>();
    serializedEnumValueExtensions.add(mockExtension1);
    serializedEnumValueExtensions.add(mockExtension2);

    // Use reflection to access the private constructor
    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);

    // Pass null for EnumSerialization and CaseFormat parameters as needed
    protoTypeAdapter = constructor.newInstance(
        null,
        null,
        null,
        Collections.emptySet(),
        serializedEnumValueExtensions);

    mockOptions = mock(EnumValueOptions.class);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_extensionPresent_returnsExtensionValue() throws Exception {
    when(mockOptions.hasExtension(mockExtension1)).thenReturn(false);
    when(mockOptions.hasExtension(mockExtension2)).thenReturn(true);
    when(mockOptions.getExtension(mockExtension2)).thenReturn("customValue");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, mockOptions, "defaultValue");
    assertEquals("customValue", result);

    verify(mockOptions).hasExtension(mockExtension1);
    verify(mockOptions).hasExtension(mockExtension2);
    verify(mockOptions).getExtension(mockExtension2);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_noExtensionPresent_returnsDefaultValue() throws Exception {
    when(mockOptions.hasExtension(mockExtension1)).thenReturn(false);
    when(mockOptions.hasExtension(mockExtension2)).thenReturn(false);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, mockOptions, "defaultValue");
    assertEquals("defaultValue", result);

    verify(mockOptions).hasExtension(mockExtension1);
    verify(mockOptions).hasExtension(mockExtension2);
    verify(mockOptions, never()).getExtension(any());
  }
}