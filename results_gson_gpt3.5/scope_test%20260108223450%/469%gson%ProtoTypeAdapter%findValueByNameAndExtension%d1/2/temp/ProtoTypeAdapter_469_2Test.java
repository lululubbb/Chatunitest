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

class ProtoTypeAdapter_469_2Test {

  private ProtoTypeAdapter protoTypeAdapterName;
  private ProtoTypeAdapter protoTypeAdapterValue;

  private EnumDescriptor enumDescriptorMock;
  private EnumValueDescriptor enumValueDescriptorMock1;
  private EnumValueDescriptor enumValueDescriptorMock2;
  private JsonElement jsonElementMock;

  @BeforeEach
  void setUp() throws Exception {
    // Construct ProtoTypeAdapter instances with EnumSerialization.NAME and EnumSerialization.VALUE
    // Using reflection to invoke private constructor
    Set serializedNameExtensions = Collections.emptySet();
    Set serializedEnumValueExtensions = Collections.emptySet();

    Class<?> enumSerializationClass = Class.forName("com.google.gson.protobuf.EnumSerialization");
    Object enumSerializationName = Enum.valueOf((Class<Enum>) enumSerializationClass, "NAME");
    Object enumSerializationValue = Enum.valueOf((Class<Enum>) enumSerializationClass, "VALUE");

    var constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        enumSerializationClass,
        com.google.common.base.CaseFormat.class,
        com.google.common.base.CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);

    protoTypeAdapterName = (ProtoTypeAdapter) constructor.newInstance(
        enumSerializationName,
        com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        serializedNameExtensions,
        serializedEnumValueExtensions);

    protoTypeAdapterValue = (ProtoTypeAdapter) constructor.newInstance(
        enumSerializationValue,
        com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        serializedNameExtensions,
        serializedEnumValueExtensions);

    enumDescriptorMock = mock(EnumDescriptor.class);
    enumValueDescriptorMock1 = mock(EnumValueDescriptor.class);
    enumValueDescriptorMock2 = mock(EnumValueDescriptor.class);
    jsonElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_enumSerializationName_matchFound() throws Exception {
    // Setup enum values
    when(enumValueDescriptorMock1.getOptions()).thenReturn(mock(EnumValueOptions.class));
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");
    when(enumValueDescriptorMock2.getOptions()).thenReturn(mock(EnumValueOptions.class));
    when(enumValueDescriptorMock2.getName()).thenReturn("ENUM_TWO");

    when(enumDescriptorMock.getValues()).thenReturn(
        java.util.List.of(enumValueDescriptorMock1, enumValueDescriptorMock2));

    // Mock getAsString to match second enum value's custom serialized value
    when(jsonElementMock.getAsString()).thenReturn("customEnumTwo");

    // We need to mock getCustSerializedEnumValue to return "customEnumTwo" for enumValueDescriptorMock2
    // Use reflection to replace getCustSerializedEnumValue temporarily with a stub that returns desired values
    Method findValueByNameAndExtension = ProtoTypeAdapter.class.getDeclaredMethod(
        "findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    findValueByNameAndExtension.setAccessible(true);

    // Spy protoTypeAdapterName to mock private getCustSerializedEnumValue via reflection
    ProtoTypeAdapter spyAdapter = org.mockito.Mockito.spy(protoTypeAdapterName);

    Method getCustSerializedEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    getCustSerializedEnumValueMethod.setAccessible(true);

    doAnswer(invocation -> {
      EnumValueOptions options = invocation.getArgument(0);
      String name = invocation.getArgument(1);
      if ("ENUM_ONE".equals(name)) {
        return "customEnumOne";
      } else if ("ENUM_TWO".equals(name)) {
        return "customEnumTwo";
      }
      return name;
    }).when(spyAdapter).getClass()
        .getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class)
        .invoke(spyAdapter, any(EnumValueOptions.class), anyString());

    // Instead of above (which is invalid), we do the following workaround:
    // We override the method invocation by using Mockito's doAnswer on spyAdapter, but since the method is private,
    // Mockito cannot stub it directly.
    // So we use reflection to replace the method behavior by creating a proxy method.

    // The correct way is to use a helper to intercept calls via reflection.
    // So we override spyAdapter's getCustSerializedEnumValue by using a custom InvocationHandler proxy is complicated.
    // Instead, we create a subclass with overridden method.

    ProtoTypeAdapter proxyAdapter = new ProtoTypeAdapterProxy(protoTypeAdapterName) {
      @Override
      protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
        if ("ENUM_ONE".equals(defaultValue)) {
          return "customEnumOne";
        } else if ("ENUM_TWO".equals(defaultValue)) {
          return "customEnumTwo";
        }
        return defaultValue;
      }
    };

    EnumValueDescriptor result = (EnumValueDescriptor) findValueByNameAndExtension.invoke(proxyAdapter, enumDescriptorMock, jsonElementMock);
    assertSame(enumValueDescriptorMock2, result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_enumSerializationName_noMatch_throws() throws Exception {
    when(enumValueDescriptorMock1.getOptions()).thenReturn(mock(EnumValueOptions.class));
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");
    when(enumDescriptorMock.getValues()).thenReturn(java.util.List.of(enumValueDescriptorMock1));
    when(jsonElementMock.getAsString()).thenReturn("notFound");

    Method findValueByNameAndExtension = ProtoTypeAdapter.class.getDeclaredMethod(
        "findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    findValueByNameAndExtension.setAccessible(true);

    ProtoTypeAdapter proxyAdapter = new ProtoTypeAdapterProxy(protoTypeAdapterName) {
      @Override
      protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
        return "customEnumOne";
      }
    };

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> findValueByNameAndExtension.invoke(proxyAdapter, enumDescriptorMock, jsonElementMock));

    assertTrue(thrown.getCause().getMessage().contains("Unrecognized enum name: notFound"));
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_enumSerializationValue_matchFound() throws Exception {
    when(jsonElementMock.getAsInt()).thenReturn(2);
    when(enumDescriptorMock.findValueByNumber(2)).thenReturn(enumValueDescriptorMock1);

    Method findValueByNameAndExtension = ProtoTypeAdapter.class.getDeclaredMethod(
        "findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    findValueByNameAndExtension.setAccessible(true);

    EnumValueDescriptor result = (EnumValueDescriptor) findValueByNameAndExtension.invoke(protoTypeAdapterValue, enumDescriptorMock, jsonElementMock);
    assertSame(enumValueDescriptorMock1, result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_enumSerializationValue_noMatch_throws() throws Exception {
    when(jsonElementMock.getAsInt()).thenReturn(99);
    when(enumDescriptorMock.findValueByNumber(99)).thenReturn(null);

    Method findValueByNameAndExtension = ProtoTypeAdapter.class.getDeclaredMethod(
        "findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    findValueByNameAndExtension.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> findValueByNameAndExtension.invoke(protoTypeAdapterValue, enumDescriptorMock, jsonElementMock));

    assertTrue(thrown.getCause().getMessage().contains("Unrecognized enum value: 99"));
  }

  // Proxy subclass to override private method for testing
  private abstract static class ProtoTypeAdapterProxy extends ProtoTypeAdapter {
    private final ProtoTypeAdapter delegate;

    ProtoTypeAdapterProxy(ProtoTypeAdapter delegate) throws Exception {
      super(getEnumSerialization(delegate),
          getProtoFormat(delegate),
          getJsonFormat(delegate),
          getSerializedNameExtensions(delegate),
          getSerializedEnumValueExtensions(delegate));
      this.delegate = delegate;
    }

    @Override
    protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
      // default implementation delegates to original instance
      try {
        Method m = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
        m.setAccessible(true);
        return (String) m.invoke(delegate, options, defaultValue);
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    // Delegate other methods if needed (none needed for current tests)

    // Reflection helpers to get private final fields from delegate
    private static EnumSerialization getEnumSerialization(ProtoTypeAdapter instance) throws Exception {
      var f = ProtoTypeAdapter.class.getDeclaredField("enumSerialization");
      f.setAccessible(true);
      return (EnumSerialization) f.get(instance);
    }

    private static com.google.common.base.CaseFormat getProtoFormat(ProtoTypeAdapter instance) throws Exception {
      var f = ProtoTypeAdapter.class.getDeclaredField("protoFormat");
      f.setAccessible(true);
      return (com.google.common.base.CaseFormat) f.get(instance);
    }

    private static com.google.common.base.CaseFormat getJsonFormat(ProtoTypeAdapter instance) throws Exception {
      var f = ProtoTypeAdapter.class.getDeclaredField("jsonFormat");
      f.setAccessible(true);
      return (com.google.common.base.CaseFormat) f.get(instance);
    }

    private static Set getSerializedNameExtensions(ProtoTypeAdapter instance) throws Exception {
      var f = ProtoTypeAdapter.class.getDeclaredField("serializedNameExtensions");
      f.setAccessible(true);
      return (Set) f.get(instance);
    }

    private static Set getSerializedEnumValueExtensions(ProtoTypeAdapter instance) throws Exception {
      var f = ProtoTypeAdapter.class.getDeclaredField("serializedEnumValueExtensions");
      f.setAccessible(true);
      return (Set) f.get(instance);
    }
  }
}