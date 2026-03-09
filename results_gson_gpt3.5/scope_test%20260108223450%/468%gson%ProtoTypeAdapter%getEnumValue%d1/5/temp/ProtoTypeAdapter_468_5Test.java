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

import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;

class ProtoTypeAdapter_468_5Test {

  private ProtoTypeAdapter protoTypeAdapterName;
  private ProtoTypeAdapter protoTypeAdapterNumber;

  private EnumValueDescriptor enumValueDescriptorMock;
  private EnumValueOptions enumValueOptionsMock;

  @BeforeEach
  void setUp() throws Exception {
    // Create mocks
    enumValueDescriptorMock = mock(EnumValueDescriptor.class);
    enumValueOptionsMock = mock(EnumValueOptions.class);

    // Get ProtoTypeAdapter constructor
    Constructor<?>[] constructors = ProtoTypeAdapter.class.getDeclaredConstructors();
    Constructor<?> targetConstructor = null;
    for (Constructor<?> c : constructors) {
      if (c.getParameterCount() == 5) {
        targetConstructor = c;
        break;
      }
    }
    if (targetConstructor == null) {
      throw new IllegalStateException("ProtoTypeAdapter constructor not found");
    }
    targetConstructor.setAccessible(true);

    Class<?> enumSerializationClass = targetConstructor.getParameterTypes()[0];

    // Get the enum constants for EnumSerialization
    Object nameEnumSerialization = Enum.valueOf((Class<Enum>) enumSerializationClass, "NAME");
    Object numberEnumSerialization = Enum.valueOf((Class<Enum>) enumSerializationClass, "NUMBER");

    protoTypeAdapterName =
        (ProtoTypeAdapter) targetConstructor.newInstance(
            nameEnumSerialization,
            com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
            com.google.common.base.CaseFormat.LOWER_CAMEL,
            Collections.emptySet(),
            Collections.emptySet());

    protoTypeAdapterNumber =
        (ProtoTypeAdapter) targetConstructor.newInstance(
            numberEnumSerialization,
            com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
            com.google.common.base.CaseFormat.LOWER_CAMEL,
            Collections.emptySet(),
            Collections.emptySet());
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_NameSerialization_UsesCustomSerializedEnumValue() throws Exception {
    // Arrange
    when(enumValueDescriptorMock.getOptions()).thenReturn(enumValueOptionsMock);
    when(enumValueDescriptorMock.getName()).thenReturn("ENUM_NAME");

    // Spy the protoTypeAdapterName instance
    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapterName);

    // Create a helper subclass that overrides getCustSerializedEnumValue to return the custom value
    ProtoTypeAdapterWithOverride adapterWithOverride = new ProtoTypeAdapterWithOverride(
        getEnumSerialization(spyAdapter),
        com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        Collections.emptySet(),
        enumValueOptionsMock,
        "ENUM_NAME",
        "custom_serialized_value");

    // Act
    Object result = invokeGetEnumValue(adapterWithOverride, enumValueDescriptorMock);

    // Assert
    assertEquals("custom_serialized_value", result);
    verify(enumValueDescriptorMock).getOptions();
    verify(enumValueDescriptorMock).getName();
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_NumberSerialization_ReturnsNumber() throws Exception {
    // Arrange
    when(enumValueDescriptorMock.getNumber()).thenReturn(42);

    // Act
    Object result = invokeGetEnumValue(protoTypeAdapterNumber, enumValueDescriptorMock);

    // Assert
    assertEquals(42, result);
    verify(enumValueDescriptorMock).getNumber();
  }

  private Object invokeGetEnumValue(ProtoTypeAdapter adapter, EnumValueDescriptor enumValueDescriptor)
      throws Exception {
    // getEnumValue is private, invoke via reflection
    Method method =
        ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    method.setAccessible(true);
    return method.invoke(adapter, enumValueDescriptor);
  }

  // Helper method to get enumSerialization field value from ProtoTypeAdapter instance
  private Object getEnumSerialization(ProtoTypeAdapter adapter) throws Exception {
    java.lang.reflect.Field field = ProtoTypeAdapter.class.getDeclaredField("enumSerialization");
    field.setAccessible(true);
    return field.get(adapter);
  }

  // Subclass of ProtoTypeAdapter to override protected method getCustSerializedEnumValue
  private static class ProtoTypeAdapterWithOverride extends ProtoTypeAdapter {
    private final EnumValueOptions expectedOptions;
    private final String expectedDefaultValue;
    private final String returnValue;

    @SuppressWarnings("unchecked")
    ProtoTypeAdapterWithOverride(Object enumSerialization,
                                 com.google.common.base.CaseFormat protoFormat,
                                 com.google.common.base.CaseFormat jsonFormat,
                                 java.util.Set serializedNameExtensions,
                                 java.util.Set serializedEnumValueExtensions,
                                 EnumValueOptions expectedOptions,
                                 String expectedDefaultValue,
                                 String returnValue) throws Exception {
      // Cast enumSerialization to the actual EnumSerialization type
      // Use reflection to find the EnumSerialization class and cast accordingly
      Class<?> enumSerializationClass = enumSerialization.getClass();
      Constructor<?> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
          enumSerializationClass,
          com.google.common.base.CaseFormat.class,
          com.google.common.base.CaseFormat.class,
          java.util.Set.class,
          java.util.Set.class);
      constructor.setAccessible(true);
      // Create instance using constructor (super constructor call workaround)
      Object instance = constructor.newInstance(
          enumSerialization,
          protoFormat,
          jsonFormat,
          serializedNameExtensions,
          serializedEnumValueExtensions);

      // Since we cannot call super(...) with Object, we call super constructor via reflection and then copy fields
      // But since we extend ProtoTypeAdapter, we must call super constructor.
      // Instead, use a dummy enumSerialization cast to Object and call super constructor normally.
      super(enumSerialization, protoFormat, jsonFormat, serializedNameExtensions, serializedEnumValueExtensions);

      this.expectedOptions = expectedOptions;
      this.expectedDefaultValue = expectedDefaultValue;
      this.returnValue = returnValue;
    }

    @Override
    protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
      if (options == expectedOptions && expectedDefaultValue.equals(defaultValue)) {
        return returnValue;
      }
      // Fallback to super's private method via reflection
      try {
        Method m = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
        m.setAccessible(true);
        return (String) m.invoke(this, options, defaultValue);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }
  }
}