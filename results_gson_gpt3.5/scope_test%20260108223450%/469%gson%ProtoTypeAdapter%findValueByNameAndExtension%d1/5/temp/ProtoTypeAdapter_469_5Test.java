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

import com.google.gson.JsonElement;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

class ProtoTypeAdapter_469_5Test {

  private ProtoTypeAdapter protoTypeAdapterName;
  private ProtoTypeAdapter protoTypeAdapterValue;

  private EnumDescriptor enumDescriptorMock;
  private EnumValueDescriptor enumValueDescriptorMock1;
  private EnumValueDescriptor enumValueDescriptorMock2;

  private JsonElement jsonElementMock;

  private Set<com.google.protobuf.Extension<com.google.protobuf.DescriptorProtos.FieldOptions, String>> serializedNameExtensions;
  private Set<com.google.protobuf.Extension<com.google.protobuf.DescriptorProtos.EnumValueOptions, String>> serializedEnumValueExtensions;

  private Class<?> enumSerializationClass;

  @BeforeEach
  void setUp() throws Exception {
    serializedNameExtensions = new HashSet<>();
    serializedEnumValueExtensions = new HashSet<>();

    // Use reflection to get EnumSerialization enum and its constants
    enumSerializationClass = null;
    for (Class<?> innerClass : ProtoTypeAdapter.class.getDeclaredClasses()) {
      if (innerClass.getSimpleName().equals("EnumSerialization")) {
        enumSerializationClass = innerClass;
        break;
      }
    }
    assertNotNull(enumSerializationClass, "EnumSerialization enum not found");

    Object enumName = Enum.valueOf((Class<Enum>) enumSerializationClass, "NAME");
    Object enumValue = Enum.valueOf((Class<Enum>) enumSerializationClass, "VALUE");

    protoTypeAdapterName = createProtoTypeAdapter(enumName);
    protoTypeAdapterValue = createProtoTypeAdapter(enumValue);

    enumDescriptorMock = mock(EnumDescriptor.class);
    enumValueDescriptorMock1 = mock(EnumValueDescriptor.class);
    enumValueDescriptorMock2 = mock(EnumValueDescriptor.class);

    jsonElementMock = mock(JsonElement.class);
  }

  private ProtoTypeAdapter createProtoTypeAdapter(Object enumSerialization) throws Exception {
    Class<?> clazz = ProtoTypeAdapter.class;
    Constructor<?> constructor = clazz.getDeclaredConstructor(
        enumSerializationClass,
        com.google.common.base.CaseFormat.class,
        com.google.common.base.CaseFormat.class,
        Set.class,
        Set.class
    );
    constructor.setAccessible(true);
    return (ProtoTypeAdapter) constructor.newInstance(
        enumSerialization,
        com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        serializedNameExtensions,
        serializedEnumValueExtensions
    );
  }

  // Create a subclass to override private method for testing
  private static class TestProtoTypeAdapter extends ProtoTypeAdapter {
    private final EnumSerialization enumSerialization;

    private TestProtoTypeAdapter(EnumSerialization enumSerialization,
                                com.google.common.base.CaseFormat protoFormat,
                                com.google.common.base.CaseFormat jsonFormat,
                                Set<com.google.protobuf.Extension<com.google.protobuf.DescriptorProtos.FieldOptions, String>> serializedNameExtensions,
                                Set<com.google.protobuf.Extension<com.google.protobuf.DescriptorProtos.EnumValueOptions, String>> serializedEnumValueExtensions) throws Exception {
      super(enumSerialization, protoFormat, jsonFormat, serializedNameExtensions, serializedEnumValueExtensions);
      this.enumSerialization = enumSerialization;
    }

    @Override
    protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
      if ("ENUM_ONE".equals(defaultValue)) {
        return "customValue";
      } else if ("ENUM_TWO".equals(defaultValue)) {
        return "otherValue";
      }
      return defaultValue;
    }
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_NameMatchFound() throws Exception {
    when(enumDescriptorMock.getValues()).thenReturn(Arrays.asList(enumValueDescriptorMock1, enumValueDescriptorMock2));
    when(enumValueDescriptorMock1.getOptions()).thenReturn(mock(EnumValueOptions.class));
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");
    when(enumValueDescriptorMock2.getOptions()).thenReturn(mock(EnumValueOptions.class));
    when(enumValueDescriptorMock2.getName()).thenReturn("ENUM_TWO");

    when(jsonElementMock.getAsString()).thenReturn("customValue");

    TestProtoTypeAdapter testAdapter = new TestProtoTypeAdapter(
        getPrivateField(protoTypeAdapterName, "enumSerialization"),
        getPrivateField(protoTypeAdapterName, "protoFormat"),
        getPrivateField(protoTypeAdapterName, "jsonFormat"),
        serializedNameExtensions,
        serializedEnumValueExtensions);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    EnumValueDescriptor result = (EnumValueDescriptor) method.invoke(testAdapter, enumDescriptorMock, jsonElementMock);

    assertEquals(enumValueDescriptorMock1, result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_NameNoMatch_Throws() throws Exception {
    when(enumDescriptorMock.getValues()).thenReturn(Arrays.asList(enumValueDescriptorMock1));
    when(enumValueDescriptorMock1.getOptions()).thenReturn(mock(EnumValueOptions.class));
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");

    when(jsonElementMock.getAsString()).thenReturn("noMatchValue");

    TestProtoTypeAdapter testAdapter = new TestProtoTypeAdapter(
        getPrivateField(protoTypeAdapterName, "enumSerialization"),
        getPrivateField(protoTypeAdapterName, "protoFormat"),
        getPrivateField(protoTypeAdapterName, "jsonFormat"),
        serializedNameExtensions,
        serializedEnumValueExtensions) {
      @Override
      protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
        return "someValue";
      }
    };

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      try {
        method.invoke(testAdapter, enumDescriptorMock, jsonElementMock);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertTrue(thrown.getMessage().contains("Unrecognized enum name: noMatchValue"));
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_ValueMatchFound() throws Exception {
    when(jsonElementMock.getAsInt()).thenReturn(5);
    when(enumDescriptorMock.findValueByNumber(5)).thenReturn(enumValueDescriptorMock1);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    EnumValueDescriptor result = (EnumValueDescriptor) method.invoke(protoTypeAdapterValue, enumDescriptorMock, jsonElementMock);

    assertEquals(enumValueDescriptorMock1, result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_ValueNoMatch_Throws() throws Exception {
    when(jsonElementMock.getAsInt()).thenReturn(99);
    when(enumDescriptorMock.findValueByNumber(99)).thenReturn(null);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      try {
        method.invoke(protoTypeAdapterValue, enumDescriptorMock, jsonElementMock);
      } catch (java.lang.reflect.InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertTrue(thrown.getMessage().contains("Unrecognized enum value: 99"));
  }

  private static <T> T getPrivateField(Object instance, String fieldName) throws Exception {
    Class<?> clazz = instance.getClass();
    while (clazz != null) {
      try {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return (T) field.get(instance);
      } catch (NoSuchFieldException e) {
        clazz = clazz.getSuperclass();
      }
    }
    throw new NoSuchFieldException("Field " + fieldName + " not found in class hierarchy");
  }

  // Helper interface to cast enumSerialization object
  private interface EnumSerialization {
  }
}