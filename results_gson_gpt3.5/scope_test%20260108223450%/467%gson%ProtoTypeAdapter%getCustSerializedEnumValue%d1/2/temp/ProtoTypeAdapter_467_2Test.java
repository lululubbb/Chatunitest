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

class ProtoTypeAdapter_467_2Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions;

  // Mock class to replace EnumSerialization for tests
  static class MockEnumSerialization {}

  @BeforeEach
  void setUp() throws Exception {
    // Prepare a mock Extension
    @SuppressWarnings("unchecked")
    Extension<EnumValueOptions, String> mockExtension = mock(Extension.class);

    serializedEnumValueExtensions = new HashSet<>();
    serializedEnumValueExtensions.add(mockExtension);

    // Use the mock class instead of Class.forName
    Class<?> enumSerializationClass = MockEnumSerialization.class;
    Object enumSerialization = mock(enumSerializationClass);

    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        enumSerializationClass,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class
    );
    constructor.setAccessible(true);

    CaseFormat protoFormat = CaseFormat.LOWER_UNDERSCORE;
    CaseFormat jsonFormat = CaseFormat.LOWER_CAMEL;
    Set<Extension<com.google.protobuf.DescriptorProtos.FieldOptions, String>> serializedNameExtensions = Collections.emptySet();

    protoTypeAdapter = constructor.newInstance(
        enumSerialization,
        protoFormat,
        jsonFormat,
        serializedNameExtensions,
        serializedEnumValueExtensions
    );
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_returnsExtensionValue_whenExtensionPresent() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);
    Extension<EnumValueOptions, String> extension = serializedEnumValueExtensions.iterator().next();

    // Mock options.hasExtension(extension) to true
    when(options.hasExtension(extension)).thenReturn(true);
    // Mock options.getExtension(extension) to return a test string
    when(options.getExtension(extension)).thenReturn("customValue");

    // Use reflection to invoke private method
    var method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, "defaultValue");

    assertEquals("customValue", result);
    verify(options).hasExtension(extension);
    verify(options).getExtension(extension);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_returnsDefaultValue_whenNoExtensionPresent() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);

    // Mock options.hasExtension(extension) to false for all extensions
    for (Extension<EnumValueOptions, String> extension : serializedEnumValueExtensions) {
      when(options.hasExtension(extension)).thenReturn(false);
    }

    var method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, "defaultValue");

    assertEquals("defaultValue", result);
    for (Extension<EnumValueOptions, String> extension : serializedEnumValueExtensions) {
      verify(options).hasExtension(extension);
      verify(options, never()).getExtension(extension);
    }
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_returnsDefaultValue_whenExtensionsEmpty() throws Exception {
    // Use the mock class instead of Class.forName
    Class<?> enumSerializationClass = MockEnumSerialization.class;
    Object enumSerialization = mock(enumSerializationClass);

    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        enumSerializationClass,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class
    );
    constructor.setAccessible(true);

    CaseFormat protoFormat = CaseFormat.LOWER_UNDERSCORE;
    CaseFormat jsonFormat = CaseFormat.LOWER_CAMEL;
    Set<Extension<com.google.protobuf.DescriptorProtos.FieldOptions, String>> serializedNameExtensions = Collections.emptySet();
    Set<Extension<EnumValueOptions, String>> emptyExtensions = Collections.emptySet();

    ProtoTypeAdapter adapterWithEmptyExtensions = constructor.newInstance(
        enumSerialization,
        protoFormat,
        jsonFormat,
        serializedNameExtensions,
        emptyExtensions
    );

    EnumValueOptions options = mock(EnumValueOptions.class);

    var method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(adapterWithEmptyExtensions, options, "defaultValue");

    assertEquals("defaultValue", result);
  }
}