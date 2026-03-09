package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.base.CaseFormat;
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
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Extension;
import com.google.protobuf.Message;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;

class ProtoTypeAdapter_468_2Test {

  private ProtoTypeAdapter protoTypeAdapterName;
  private ProtoTypeAdapter protoTypeAdapterNumber;

  private EnumValueDescriptor enumValueDescriptorMock;
  private EnumValueOptions enumValueOptionsMock;

  @BeforeEach
  void setUp() throws Exception {
    // Use reflection to create instances of ProtoTypeAdapter since constructor is private
    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        com.google.common.base.CaseFormat.class,
        com.google.common.base.CaseFormat.class,
        java.util.Set.class,
        java.util.Set.class);
    constructor.setAccessible(true);

    protoTypeAdapterName = constructor.newInstance(
        ProtoTypeAdapter.EnumSerialization.NAME,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet()
    );

    protoTypeAdapterNumber = constructor.newInstance(
        ProtoTypeAdapter.EnumSerialization.NUMBER,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet()
    );

    enumValueDescriptorMock = mock(EnumValueDescriptor.class);
    enumValueOptionsMock = mock(EnumValueOptions.class);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_WhenEnumSerializationIsName_ReturnsCustomSerializedEnumValue() throws Exception {
    when(enumValueDescriptorMock.getOptions()).thenReturn(enumValueOptionsMock);
    when(enumValueDescriptorMock.getName()).thenReturn("ENUM_NAME");

    // Use reflection to get private method getEnumValue
    Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    getEnumValueMethod.setAccessible(true);

    // Invoke getEnumValue on protoTypeAdapterName
    Object result = getEnumValueMethod.invoke(protoTypeAdapterName, enumValueDescriptorMock);

    // Since getCustSerializedEnumValue returns defaultValue (which is "ENUM_NAME"), expect "ENUM_NAME"
    assertEquals("ENUM_NAME", result);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_WhenEnumSerializationIsNotName_ReturnsEnumNumber() throws Exception {
    when(enumValueDescriptorMock.getNumber()).thenReturn(42);

    // Use reflection to invoke private getEnumValue on protoTypeAdapterNumber
    Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    getEnumValueMethod.setAccessible(true);
    Object result = getEnumValueMethod.invoke(protoTypeAdapterNumber, enumValueDescriptorMock);

    assertEquals(42, result);
  }
}