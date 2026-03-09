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

class ProtoTypeAdapter_468_3Test {

  private ProtoTypeAdapter adapterNameSerialization;
  private ProtoTypeAdapter adapterNumberSerialization;
  private EnumValueDescriptor enumValueDescriptorMock;
  private EnumValueOptions enumValueOptionsMock;

  @BeforeEach
  void setUp() throws Exception {
    // Use ProtoTypeAdapter.EnumSerialization from the ProtoTypeAdapter class
    Class<?> enumSerializationClass = Class.forName("com.google.gson.protobuf.ProtoTypeAdapter$EnumSerialization");
    @SuppressWarnings("unchecked")
    Enum<?> enumName = Enum.valueOf((Class<Enum>) enumSerializationClass, "NAME");
    @SuppressWarnings("unchecked")
    Enum<?> enumNumber = Enum.valueOf((Class<Enum>) enumSerializationClass, "NUMBER");

    // Create ProtoTypeAdapter instances with different EnumSerialization values using reflection
    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        enumSerializationClass,
        com.google.common.base.CaseFormat.class,
        com.google.common.base.CaseFormat.class,
        java.util.Set.class,
        java.util.Set.class);
    constructor.setAccessible(true);

    adapterNameSerialization = constructor.newInstance(
        enumName,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet()
    );
    adapterNumberSerialization = constructor.newInstance(
        enumNumber,
        null,
        null,
        Collections.emptySet(),
        Collections.emptySet()
    );

    // Mock EnumValueDescriptor and EnumValueOptions
    enumValueDescriptorMock = mock(EnumValueDescriptor.class);
    enumValueOptionsMock = mock(EnumValueOptions.class);

    when(enumValueDescriptorMock.getOptions()).thenReturn(enumValueOptionsMock);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_enumSerializationName_returnsCustomSerializedEnumValue() throws Exception {
    // Prepare mock behavior
    when(enumValueDescriptorMock.getName()).thenReturn("ENUM_NAME");

    // Get EnumSerialization class and NAME enum constant
    Class<?> enumSerializationClass = Class.forName("com.google.gson.protobuf.ProtoTypeAdapter$EnumSerialization");
    @SuppressWarnings("unchecked")
    Enum<?> enumName = Enum.valueOf((Class<Enum>) enumSerializationClass, "NAME");

    // Create a dynamic proxy subclass of ProtoTypeAdapter to override getCustSerializedEnumValue
    // Since the constructor is private, use reflection to instantiate and then create a proxy-like subclass via reflection

    // Create a subclass of ProtoTypeAdapter dynamically using a Proxy or via reflection is complicated,
    // so instead create an anonymous subclass using reflection on the constructor.

    // Use reflection to create an anonymous subclass overriding getCustSerializedEnumValue
    // This requires use of java.lang.reflect.Proxy is not possible because ProtoTypeAdapter is a class, not interface.
    // So use a helper subclass declared here:

    class ProtoTypeAdapterForTest extends ProtoTypeAdapter {
      ProtoTypeAdapterForTest(Object enumSerialization) throws Exception {
        super(
            enumSerialization,
            null,
            null,
            Collections.emptySet(),
            Collections.emptySet()
        );
      }

      @Override
      protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
        return "customValue";
      }
    }

    // Construct an instance of ProtoTypeAdapterForTest using reflection
    Constructor<ProtoTypeAdapterForTest> subConstructor = ProtoTypeAdapterForTest.class.getDeclaredConstructor(Object.class);
    subConstructor.setAccessible(true);
    ProtoTypeAdapterForTest adapterForTest = subConstructor.newInstance(enumName);

    when(enumValueDescriptorMock.getOptions()).thenReturn(enumValueOptionsMock);
    when(enumValueDescriptorMock.getName()).thenReturn("ENUM_NAME");

    Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    getEnumValueMethod.setAccessible(true);
    Object result = getEnumValueMethod.invoke(adapterForTest, enumValueDescriptorMock);

    assertEquals("customValue", result);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_enumSerializationNumber_returnsNumber() throws Exception {
    // Prepare mock behavior
    when(enumValueDescriptorMock.getNumber()).thenReturn(123);

    Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    getEnumValueMethod.setAccessible(true);
    Object result = getEnumValueMethod.invoke(adapterNumberSerialization, enumValueDescriptorMock);

    assertEquals(123, result);
  }
}