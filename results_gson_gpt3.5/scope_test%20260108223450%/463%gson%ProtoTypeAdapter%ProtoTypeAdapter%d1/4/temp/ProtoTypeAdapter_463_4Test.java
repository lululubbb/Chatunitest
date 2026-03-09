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
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.base.CaseFormat;
import com.google.gson.JsonElement;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Extension;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProtoTypeAdapter_463_4Test {

  private ProtoTypeAdapter adapter;
  private ProtoTypeAdapter.EnumSerialization enumSerialization;
  private CaseFormat protoFormat;
  private CaseFormat jsonFormat;
  private Set<Extension<FieldOptions, String>> serializedNameExtensions;
  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions;

  @BeforeEach
  void setUp() throws Exception {
    enumSerialization = mock(ProtoTypeAdapter.EnumSerialization.class);
    protoFormat = CaseFormat.LOWER_UNDERSCORE;
    jsonFormat = CaseFormat.LOWER_CAMEL;
    serializedNameExtensions = new HashSet<>();
    serializedEnumValueExtensions = new HashSet<>();

    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class, CaseFormat.class, CaseFormat.class,
        Set.class, Set.class);
    constructor.setAccessible(true);
    adapter = constructor.newInstance(enumSerialization, protoFormat, jsonFormat,
        serializedNameExtensions, serializedEnumValueExtensions);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_withExtensionAndDefaultName() throws Exception {
    FieldOptions options = mock(FieldOptions.class);
    String defaultName = "defaultName";

    Extension<FieldOptions, String> extension = mock(Extension.class);
    serializedNameExtensions.add(extension);

    when(extension.getNumber()).thenReturn(123);
    when(options.getExtension(extension)).thenReturn("customName");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName",
        FieldOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapter, options, defaultName);

    assertEquals("customName", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_withNoExtensionReturnsDefaultName() throws Exception {
    FieldOptions options = mock(FieldOptions.class);
    String defaultName = "defaultName";

    // No extensions added, so should return defaultName
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName",
        FieldOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapter, options, defaultName);

    assertEquals(defaultName, result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_withExtensionAndDefaultValue() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);
    String defaultValue = "defaultValue";

    Extension<EnumValueOptions, String> extension = mock(Extension.class);
    serializedEnumValueExtensions.add(extension);

    when(extension.getNumber()).thenReturn(456);
    when(options.getExtension(extension)).thenReturn("customEnumValue");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue",
        EnumValueOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapter, options, defaultValue);

    assertEquals("customEnumValue", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_withNoExtensionReturnsDefaultValue() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);
    String defaultValue = "defaultValue";

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue",
        EnumValueOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapter, options, defaultValue);

    assertEquals(defaultValue, result);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_returnsCorrectValue() throws Exception {
    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);
    when(enumValueDescriptor.getNumber()).thenReturn(123);
    when(enumValueDescriptor.getName()).thenReturn("NAME");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue",
        EnumValueDescriptor.class);
    method.setAccessible(true);

    Object result = method.invoke(adapter, enumValueDescriptor);

    // Should return Integer number 123 as enum value
    assertEquals(123, result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_returnsEnumValueDescriptor() throws Exception {
    EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
    JsonElement jsonElement = mock(JsonElement.class);

    // Prepare mocked behavior for jsonElement.getAsString()
    when(jsonElement.getAsString()).thenReturn("ENUM_NAME");

    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);
    when(enumDescriptor.findValueByName("ENUM_NAME")).thenReturn(enumValueDescriptor);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension",
        EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    EnumValueDescriptor result = (EnumValueDescriptor) method.invoke(adapter,
        enumDescriptor, jsonElement);

    assertEquals(enumValueDescriptor, result);
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_returnsMethodAndCaches() throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCachedMethod",
        Class.class, String.class, Class[].class);
    method.setAccessible(true);

    // Use getDeclaredMethod instead of getMethod to find private method
    Method result1 = (Method) method.invoke(null, ProtoTypeAdapter.class, "getEnumValue",
        new Class[]{EnumValueDescriptor.class});
    assertNotNull(result1);

    Method result2 = (Method) method.invoke(null, ProtoTypeAdapter.class, "getEnumValue",
        new Class[]{EnumValueDescriptor.class});
    assertSame(result1, result2);
  }
}