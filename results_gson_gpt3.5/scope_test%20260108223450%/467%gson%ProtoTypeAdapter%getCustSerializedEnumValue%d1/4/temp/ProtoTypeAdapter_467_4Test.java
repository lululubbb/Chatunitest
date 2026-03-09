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

class ProtoTypeAdapter_467_4Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions;

  @BeforeEach
  void setUp() {
    serializedEnumValueExtensions = new HashSet<>();
    try {
      Method newBuilderMethod = ProtoTypeAdapter.class.getDeclaredMethod("newBuilder");
      newBuilderMethod.setAccessible(true);
      Object builder = newBuilderMethod.invoke(null);

      // The constructor parameter types must match exactly the actual constructor in ProtoTypeAdapter
      Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
          ProtoTypeAdapter.EnumSerialization.class,
          CaseFormat.class,
          CaseFormat.class,
          Set.class,
          Set.class);
      constructor.setAccessible(true);
      protoTypeAdapter = constructor.newInstance(
          null,
          null,
          null,
          Collections.emptySet(),
          serializedEnumValueExtensions);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_returnsExtensionValue_whenExtensionPresent() throws Exception {
    @SuppressWarnings("unchecked")
    Extension<EnumValueOptions, String> extension = mock(Extension.class);
    serializedEnumValueExtensions.add(extension);

    EnumValueOptions options = mock(EnumValueOptions.class);
    when(options.hasExtension(extension)).thenReturn(true);
    when(options.getExtension(extension)).thenReturn("customValue");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue",
        EnumValueOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, "defaultValue");

    assertEquals("customValue", result);
    verify(options).hasExtension(extension);
    verify(options).getExtension(extension);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_returnsDefaultValue_whenNoExtensionPresent() throws Exception {
    @SuppressWarnings("unchecked")
    Extension<EnumValueOptions, String> extension = mock(Extension.class);
    serializedEnumValueExtensions.add(extension);

    EnumValueOptions options = mock(EnumValueOptions.class);
    when(options.hasExtension(extension)).thenReturn(false);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue",
        EnumValueOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, "defaultValue");

    assertEquals("defaultValue", result);
    verify(options).hasExtension(extension);
    verify(options, never()).getExtension(extension);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedEnumValue_returnsDefaultValue_whenExtensionSetEmpty() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);

    // Clear extensions set
    serializedEnumValueExtensions.clear();

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue",
        EnumValueOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, "defaultValue");

    assertEquals("defaultValue", result);
    verify(options, never()).hasExtension(any());
    verify(options, never()).getExtension(any());
  }
}