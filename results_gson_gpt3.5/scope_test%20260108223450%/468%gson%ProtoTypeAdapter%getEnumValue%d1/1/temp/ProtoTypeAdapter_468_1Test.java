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

class ProtoTypeAdapter_468_1Test {

  private ProtoTypeAdapter protoTypeAdapterName;
  private ProtoTypeAdapter protoTypeAdapterNumber;

  private EnumValueDescriptor enumValueDescriptorMock;
  private EnumValueOptions enumValueOptionsMock;

  enum EnumSerialization {
    NAME, NUMBER
  }

  @BeforeEach
  void setUp() throws Exception {
    // Create ProtoTypeAdapter instance with enumSerialization = EnumSerialization.NAME
    protoTypeAdapterName = createProtoTypeAdapterWithEnumSerialization(EnumSerialization.NAME);
    // Create ProtoTypeAdapter instance with enumSerialization = EnumSerialization.NUMBER
    protoTypeAdapterNumber = createProtoTypeAdapterWithEnumSerialization(EnumSerialization.NUMBER);

    // Mock EnumValueDescriptor and EnumValueOptions
    enumValueDescriptorMock = mock(EnumValueDescriptor.class);
    enumValueOptionsMock = mock(EnumValueOptions.class);

    when(enumValueDescriptorMock.getOptions()).thenReturn(enumValueOptionsMock);
  }

  private ProtoTypeAdapter createProtoTypeAdapterWithEnumSerialization(EnumSerialization enumSerialization) throws Exception {
    Class<?> protoTypeAdapterClass = ProtoTypeAdapter.class;
    Class<?> enumSerializationClass = null;
    for (Class<?> innerClass : protoTypeAdapterClass.getDeclaredClasses()) {
      if ("EnumSerialization".equals(innerClass.getSimpleName())) {
        enumSerializationClass = innerClass;
        break;
      }
    }
    if (enumSerializationClass == null) {
      throw new IllegalStateException("EnumSerialization class not found in ProtoTypeAdapter");
    }

    Object enumSerializationValue = null;
    for (Object constant : enumSerializationClass.getEnumConstants()) {
      if (constant.toString().equals(enumSerialization.name())) {
        enumSerializationValue = constant;
        break;
      }
    }
    if (enumSerializationValue == null) {
      throw new IllegalStateException("EnumSerialization enum constant not found: " + enumSerialization.name());
    }

    Constructor<?> constructor = protoTypeAdapterClass.getDeclaredConstructor(
        enumSerializationClass,
        com.google.common.base.CaseFormat.class,
        com.google.common.base.CaseFormat.class,
        java.util.Set.class,
        java.util.Set.class);
    constructor.setAccessible(true);
    return (ProtoTypeAdapter) constructor.newInstance(
        enumSerializationValue,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet());
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_whenEnumSerializationIsName_returnsCustomSerializedEnumValue() throws Exception {
    // Arrange
    when(enumValueDescriptorMock.getName()).thenReturn("ENUM_NAME");

    // Create a dynamic proxy subclass to override getCustSerializedEnumValue via reflection
    // Since the constructor is private, create instance via reflection and then override method via proxy is complicated.
    // Instead, use reflection to invoke getEnumValue on an anonymous subclass created via reflection that overrides getCustSerializedEnumValue.

    Class<?> protoTypeAdapterClass = ProtoTypeAdapter.class;
    Class<?> enumSerializationClass = null;
    for (Class<?> innerClass : protoTypeAdapterClass.getDeclaredClasses()) {
      if ("EnumSerialization".equals(innerClass.getSimpleName())) {
        enumSerializationClass = innerClass;
        break;
      }
    }
    if (enumSerializationClass == null) {
      throw new IllegalStateException("EnumSerialization class not found in ProtoTypeAdapter");
    }

    Object enumSerializationValue = null;
    for (Object constant : enumSerializationClass.getEnumConstants()) {
      if (constant.toString().equals("NAME")) {
        enumSerializationValue = constant;
        break;
      }
    }
    if (enumSerializationValue == null) {
      throw new IllegalStateException("EnumSerialization enum constant not found: NAME");
    }

    // Create a proxy subclass dynamically to override getCustSerializedEnumValue
    // Use java.lang.reflect.Proxy is not possible for classes, so use bytecode generation is out of scope.
    // Instead, use reflection to create an anonymous subclass via reflection and override method via MethodHandles (Java 9+), or use a helper class in the test package.

    // Since the constructor is private, but accessible, create an anonymous subclass via reflection:

    // Create subclass in runtime via anonymous class in test:
    ProtoTypeAdapter protoTypeAdapterForTest = new ProtoTypeAdapter(enumSerializationValue,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet()) {
      @Override
      protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
        return "customValue";
      }
    };

    // Use reflection to get getEnumValue method
    Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    getEnumValueMethod.setAccessible(true);

    // Act
    Object result = getEnumValueMethod.invoke(protoTypeAdapterForTest, enumValueDescriptorMock);

    // Assert
    assertEquals("customValue", result);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_whenEnumSerializationIsNotName_returnsEnumNumber() throws Exception {
    // Arrange
    when(enumValueDescriptorMock.getNumber()).thenReturn(42);

    // Act
    Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    getEnumValueMethod.setAccessible(true);
    Object result = getEnumValueMethod.invoke(protoTypeAdapterNumber, enumValueDescriptorMock);

    // Assert
    assertEquals(42, result);
  }
}