package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.base.CaseFormat;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Descriptors.Descriptor;
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
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

class ProtoTypeAdapter_469_1Test {

  private ProtoTypeAdapter protoTypeAdapterName;
  private ProtoTypeAdapter protoTypeAdapterValue;

  private EnumDescriptor enumDescriptorMock;
  private EnumValueDescriptor enumValueDescriptorMock1;
  private EnumValueDescriptor enumValueDescriptorMock2;
  private JsonElement jsonElementMock;

  @BeforeEach
  void init() throws Exception {
    protoTypeAdapterName = createProtoTypeAdapterWithEnumSerialization(getProtoTypeAdapterEnumSerialization("NAME"));
    protoTypeAdapterValue = createProtoTypeAdapterWithEnumSerialization(getProtoTypeAdapterEnumSerialization("VALUE"));

    enumDescriptorMock = mock(EnumDescriptor.class);
    enumValueDescriptorMock1 = mock(EnumValueDescriptor.class);
    enumValueDescriptorMock2 = mock(EnumValueDescriptor.class);
    jsonElementMock = mock(JsonElement.class);
  }

  private ProtoTypeAdapter createProtoTypeAdapterWithEnumSerialization(Object enumSerialization) throws Exception {
    Set<?> emptySet = Collections.emptySet();
    var constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        enumSerialization.getClass(),
        com.google.common.base.CaseFormat.class,
        com.google.common.base.CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    return (ProtoTypeAdapter) constructor.newInstance(enumSerialization,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        emptySet,
        emptySet);
  }

  /**
   * Use reflection to get the EnumSerialization enum instance from ProtoTypeAdapter class.
   */
  private Object getProtoTypeAdapterEnumSerialization(String name) throws Exception {
    Class<?> enumSerializationClass = null;
    for (Class<?> nestedClass : ProtoTypeAdapter.class.getDeclaredClasses()) {
      if ("EnumSerialization".equals(nestedClass.getSimpleName()) && nestedClass.isEnum()) {
        enumSerializationClass = nestedClass;
        break;
      }
    }
    if (enumSerializationClass == null) {
      throw new IllegalStateException("EnumSerialization enum not found in ProtoTypeAdapter");
    }
    Method valueOfMethod = enumSerializationClass.getMethod("valueOf", String.class);
    return valueOfMethod.invoke(null, name);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_NameSerialization_MatchFound() throws Exception {
    // Arrange
    when(enumValueDescriptorMock1.getOptions()).thenReturn(mock(EnumValueOptions.class));
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");
    when(enumValueDescriptorMock2.getOptions()).thenReturn(mock(EnumValueOptions.class));
    when(enumValueDescriptorMock2.getName()).thenReturn("ENUM_TWO");

    when(enumDescriptorMock.getValues()).thenReturn(java.util.List.of(enumValueDescriptorMock1, enumValueDescriptorMock2));
    when(jsonElementMock.getAsString()).thenReturn("ENUM_TWO");

    // Subclass ProtoTypeAdapter to override getCustSerializedEnumValue
    ProtoTypeAdapter adapter = new ProtoTypeAdapter(
        (Enum) getProtoTypeAdapterEnumSerialization("NAME"),
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        Collections.emptySet()) {
      @Override
      protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultName) {
        return defaultName;
      }
    };

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    // Act
    EnumValueDescriptor result = (EnumValueDescriptor) method.invoke(adapter, enumDescriptorMock, jsonElementMock);

    // Assert
    assertSame(enumValueDescriptorMock2, result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_NameSerialization_NotFound() throws Exception {
    when(enumValueDescriptorMock1.getOptions()).thenReturn(mock(EnumValueOptions.class));
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");
    when(enumDescriptorMock.getValues()).thenReturn(java.util.List.of(enumValueDescriptorMock1));
    when(jsonElementMock.getAsString()).thenReturn("UNKNOWN_ENUM");

    ProtoTypeAdapter adapter = new ProtoTypeAdapter(
        (Enum) getProtoTypeAdapterEnumSerialization("NAME"),
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        Collections.emptySet()) {
      @Override
      protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultName) {
        return defaultName;
      }
    };

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      method.invoke(adapter, enumDescriptorMock, jsonElementMock);
    });
    assertTrue(thrown.getCause() instanceof IllegalArgumentException);
    assertEquals("Unrecognized enum name: UNKNOWN_ENUM", thrown.getCause().getMessage());
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_ValueSerialization_Found() throws Exception {
    when(jsonElementMock.getAsInt()).thenReturn(2);
    when(enumDescriptorMock.findValueByNumber(2)).thenReturn(enumValueDescriptorMock1);

    ProtoTypeAdapter adapter = createProtoTypeAdapterWithEnumSerialization(getProtoTypeAdapterEnumSerialization("VALUE"));

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    EnumValueDescriptor result = (EnumValueDescriptor) method.invoke(adapter, enumDescriptorMock, jsonElementMock);

    assertSame(enumValueDescriptorMock1, result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_ValueSerialization_NotFound() throws Exception {
    when(jsonElementMock.getAsInt()).thenReturn(5);
    when(enumDescriptorMock.findValueByNumber(5)).thenReturn(null);

    ProtoTypeAdapter adapter = createProtoTypeAdapterWithEnumSerialization(getProtoTypeAdapterEnumSerialization("VALUE"));

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      method.invoke(adapter, enumDescriptorMock, jsonElementMock);
    });
    assertTrue(thrown.getCause() instanceof IllegalArgumentException);
    assertEquals("Unrecognized enum value: 5", thrown.getCause().getMessage());
  }
}