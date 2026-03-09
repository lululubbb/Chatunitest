package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
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
import com.google.common.base.CaseFormat;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Extension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Set;

class ProtoTypeAdapter_469_3Test {

  private ProtoTypeAdapter protoTypeAdapterName;
  private ProtoTypeAdapter protoTypeAdapterValue;

  private EnumDescriptor enumDescriptorMock;
  private JsonElement jsonElementMock;

  private EnumValueDescriptor enumValueDescriptorMock1;
  private EnumValueDescriptor enumValueDescriptorMock2;

  private EnumValueOptions enumValueOptionsMock;

  private Set<Extension<FieldOptions, String>> serializedNameExtensions = Collections.emptySet();
  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions = Collections.emptySet();

  private Class<?> enumSerializationClass;

  @BeforeEach
  void setUp() throws Exception {
    // Load EnumSerialization class once
    enumSerializationClass = Class.forName("com.google.gson.protobuf.ProtoTypeAdapter$EnumSerialization");

    // Create instances of ProtoTypeAdapter with EnumSerialization.NAME and EnumSerialization.VALUE
    protoTypeAdapterName = createProtoTypeAdapter(getEnumSerializationName());
    protoTypeAdapterValue = createProtoTypeAdapter(getEnumSerializationValue());

    enumDescriptorMock = mock(EnumDescriptor.class);
    jsonElementMock = mock(JsonElement.class);

    enumValueDescriptorMock1 = mock(EnumValueDescriptor.class);
    enumValueDescriptorMock2 = mock(EnumValueDescriptor.class);

    enumValueOptionsMock = mock(EnumValueOptions.class);
  }

  private Object getEnumSerializationName() throws Exception {
    for (Object constant : enumSerializationClass.getEnumConstants()) {
      if (constant.toString().equals("NAME")) {
        return constant;
      }
    }
    throw new IllegalArgumentException("EnumSerialization NAME not found");
  }

  private Object getEnumSerializationValue() throws Exception {
    for (Object constant : enumSerializationClass.getEnumConstants()) {
      if (constant.toString().equals("VALUE")) {
        return constant;
      }
    }
    throw new IllegalArgumentException("EnumSerialization VALUE not found");
  }

  private ProtoTypeAdapter createProtoTypeAdapter(Object enumSerialization) throws Exception {
    // Use reflection to invoke private constructor
    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        enumSerializationClass,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    return constructor.newInstance(
        enumSerialization,
        CaseFormat.LOWER_UNDERSCORE,
        CaseFormat.LOWER_CAMEL,
        serializedNameExtensions,
        serializedEnumValueExtensions);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_enumSerializationName_matchFound() throws Exception {
    when(enumDescriptorMock.getValues()).thenReturn(
        java.util.List.of(enumValueDescriptorMock1, enumValueDescriptorMock2));

    when(enumValueDescriptorMock1.getOptions()).thenReturn(enumValueOptionsMock);
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");
    when(enumValueDescriptorMock2.getOptions()).thenReturn(enumValueOptionsMock);
    when(enumValueDescriptorMock2.getName()).thenReturn("ENUM_TWO");

    // Mock jsonElement to return string matching second enum value
    when(jsonElementMock.getAsString()).thenReturn("custom_two");

    // Create a dynamic proxy to intercept calls to private method getCustSerializedEnumValue
    ProtoTypeAdapter proxyAdapter = createProxyWithGetCustSerializedEnumValue(protoTypeAdapterName);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    EnumValueDescriptor result = (EnumValueDescriptor) method.invoke(proxyAdapter, enumDescriptorMock, jsonElementMock);

    assertSame(enumValueDescriptorMock2, result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_enumSerializationName_noMatch_throws() throws Exception {
    when(enumDescriptorMock.getValues()).thenReturn(
        java.util.List.of(enumValueDescriptorMock1));

    when(enumValueDescriptorMock1.getOptions()).thenReturn(enumValueOptionsMock);
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");

    when(jsonElementMock.getAsString()).thenReturn("not_found");

    ProtoTypeAdapter proxyAdapter = createProxyWithGetCustSerializedEnumValue(protoTypeAdapterName);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> method.invoke(proxyAdapter, enumDescriptorMock, jsonElementMock),
        "Expected to throw IllegalArgumentException");

    // InvocationTargetException expected because of reflection, unwrap cause
    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalArgumentException);
    assertEquals("Unrecognized enum name: not_found", cause.getMessage());
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_enumSerializationValue_matchFound() throws Exception {
    when(jsonElementMock.getAsInt()).thenReturn(2);

    when(enumDescriptorMock.findValueByNumber(2)).thenReturn(enumValueDescriptorMock1);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    EnumValueDescriptor result = (EnumValueDescriptor) method.invoke(protoTypeAdapterValue, enumDescriptorMock, jsonElementMock);

    assertSame(enumValueDescriptorMock1, result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_enumSerializationValue_noMatch_throws() throws Exception {
    when(jsonElementMock.getAsInt()).thenReturn(99);

    when(enumDescriptorMock.findValueByNumber(99)).thenReturn(null);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> method.invoke(protoTypeAdapterValue, enumDescriptorMock, jsonElementMock),
        "Expected to throw IllegalArgumentException");

    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalArgumentException);
    assertEquals("Unrecognized enum value: 99", cause.getMessage());
  }

  private ProtoTypeAdapter createProxyWithGetCustSerializedEnumValue(ProtoTypeAdapter original) throws Exception {
    Class<?> protoTypeAdapterClass = ProtoTypeAdapter.class;
    Method getCustSerializedEnumValueMethod = protoTypeAdapterClass.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    getCustSerializedEnumValueMethod.setAccessible(true);

    return (ProtoTypeAdapter) Proxy.newProxyInstance(
        protoTypeAdapterClass.getClassLoader(),
        new Class<?>[]{protoTypeAdapterClass},
        (proxy, method, args) -> {
          if ("getCustSerializedEnumValue".equals(method.getName())
              && args.length == 2
              && args[0] instanceof EnumValueOptions
              && args[1] instanceof String) {
            String name = (String) args[1];
            if ("ENUM_ONE".equals(name)) {
              return "custom_one";
            }
            if ("ENUM_TWO".equals(name)) {
              return "custom_two";
            }
            return name;
          } else {
            return method.invoke(original, args);
          }
        });
  }
}