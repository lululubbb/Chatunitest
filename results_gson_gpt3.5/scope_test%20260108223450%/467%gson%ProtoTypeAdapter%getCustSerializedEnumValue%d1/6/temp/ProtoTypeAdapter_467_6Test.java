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

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ProtoTypeAdapter_467_6Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions;

  // Use the actual EnumSerialization class from ProtoTypeAdapter package if possible,
  // else create dummy enum with the same package and name to match constructor parameter.
  // Here we create a dummy enum in the same package to match constructor signature.
  // Replace this with actual import if available.
  public enum EnumSerialization {
    DEFAULT
  }

  @BeforeEach
  void setUp() {
    // Create a mock Extension that will be added to the set
    Extension<EnumValueOptions, String> extensionMock1 = mock(Extension.class);
    Extension<EnumValueOptions, String> extensionMock2 = mock(Extension.class);

    serializedEnumValueExtensions = new HashSet<>();
    serializedEnumValueExtensions.add(extensionMock1);
    serializedEnumValueExtensions.add(extensionMock2);

    // Create ProtoTypeAdapter instance via constructor reflection since constructor is private
    try {
      Class<?> clazz = ProtoTypeAdapter.class;

      // The constructor parameter types must match exactly the actual ProtoTypeAdapter constructor parameter types.
      // Use the actual EnumSerialization and CaseFormat classes from ProtoTypeAdapter package.
      // So get the EnumSerialization and CaseFormat classes from ProtoTypeAdapter class loader.

      Class<?> enumSerializationClass = EnumSerialization.class;
      Class<?> caseFormatClass = CaseFormat.class;

      java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor(
          enumSerializationClass,
          caseFormatClass,
          caseFormatClass,
          Set.class,
          Set.class);
      constructor.setAccessible(true);
      protoTypeAdapter = (ProtoTypeAdapter) constructor.newInstance(
          EnumSerialization.DEFAULT,
          CaseFormat.LOWER_UNDERSCORE,
          CaseFormat.LOWER_CAMEL,
          Collections.emptySet(),
          serializedEnumValueExtensions);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_extensionHasExtension_returnsExtensionValue() throws Exception {
    EnumValueOptions optionsMock = mock(EnumValueOptions.class);

    // Pick one extension from the set
    Extension<EnumValueOptions, String> ext = serializedEnumValueExtensions.iterator().next();

    // Mock options.hasExtension(extension) to true for this extension
    when(optionsMock.hasExtension(ext)).thenReturn(true);
    when(optionsMock.getExtension(ext)).thenReturn("customValue");

    // For other extensions, return false
    for (Extension<EnumValueOptions, String> otherExt : serializedEnumValueExtensions) {
      if (otherExt != ext) {
        when(optionsMock.hasExtension(otherExt)).thenReturn(false);
      }
    }

    // Use reflection to invoke private method
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(protoTypeAdapter, optionsMock, "defaultVal");

    assertEquals("customValue", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_noExtensionHasExtension_returnsDefaultValue() throws Exception {
    EnumValueOptions optionsMock = mock(EnumValueOptions.class);

    // For all extensions, hasExtension returns false
    for (Extension<EnumValueOptions, String> ext : serializedEnumValueExtensions) {
      when(optionsMock.hasExtension(ext)).thenReturn(false);
    }

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(protoTypeAdapter, optionsMock, "defaultVal");

    assertEquals("defaultVal", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_emptyExtensions_returnsDefaultValue() throws Exception {
    // Create ProtoTypeAdapter with empty serializedEnumValueExtensions
    ProtoTypeAdapter adapterWithEmptyExtensions;
    try {
      Class<?> clazz = ProtoTypeAdapter.class;

      Class<?> enumSerializationClass = EnumSerialization.class;
      Class<?> caseFormatClass = CaseFormat.class;

      java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor(
          enumSerializationClass,
          caseFormatClass,
          caseFormatClass,
          Set.class,
          Set.class);
      constructor.setAccessible(true);
      adapterWithEmptyExtensions = (ProtoTypeAdapter) constructor.newInstance(
          EnumSerialization.DEFAULT,
          CaseFormat.LOWER_UNDERSCORE,
          CaseFormat.LOWER_CAMEL,
          Collections.emptySet(),
          Collections.emptySet());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    EnumValueOptions optionsMock = mock(EnumValueOptions.class);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapterWithEmptyExtensions, optionsMock, "defaultVal");

    assertEquals("defaultVal", result);
  }
}