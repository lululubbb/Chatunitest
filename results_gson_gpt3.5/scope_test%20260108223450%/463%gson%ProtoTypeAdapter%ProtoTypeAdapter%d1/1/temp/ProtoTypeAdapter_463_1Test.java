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

class ProtoTypeAdapter_463_1Test {

  private ProtoTypeAdapter protoTypeAdapter;

  private CaseFormat protoFormat;
  private CaseFormat jsonFormat;
  private Set<Extension<FieldOptions, String>> serializedNameExtensions;
  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions;

  @BeforeEach
  void setUp() throws Exception {
    // Find the EnumSerialization class (which is a class, not an interface)
    Class<?> enumSerializationClass = null;
    for (Class<?> inner : ProtoTypeAdapter.class.getDeclaredClasses()) {
      if ("EnumSerialization".equals(inner.getSimpleName())) {
        enumSerializationClass = inner;
        break;
      }
    }
    if (enumSerializationClass == null) {
      throw new IllegalStateException("EnumSerialization class not found");
    }

    // Instead of creating a Proxy (which requires an interface),
    // create a mock instance of the EnumSerialization class using Mockito
    Object enumSerialization = mock(enumSerializationClass);

    protoFormat = CaseFormat.LOWER_UNDERSCORE;
    jsonFormat = CaseFormat.LOWER_CAMEL;
    serializedNameExtensions = new HashSet<>();
    serializedEnumValueExtensions = new HashSet<>();

    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        enumSerializationClass,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    protoTypeAdapter = constructor.newInstance(enumSerialization, protoFormat, jsonFormat,
        serializedNameExtensions, serializedEnumValueExtensions);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_withExtensionValue() throws Exception {
    FieldOptions options = mock(FieldOptions.class);
    Extension<FieldOptions, String> extension = mock(Extension.class);
    serializedNameExtensions.add(extension);

    when(extension.getNumber()).thenReturn(123);
    // Mock extension.get to return a custom serialized name to avoid default formatting
    when(options.getExtension(extension)).thenReturn("customName");

    String defaultName = "defaultName";

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, defaultName);
    assertEquals("customName", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_withoutExtension() throws Exception {
    FieldOptions options = mock(FieldOptions.class);
    String defaultName = "defaultName";

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, defaultName);
    // The method applies jsonFormat.to() which converts LOWER_UNDERSCORE to LOWER_CAMEL
    // So "defaultName" (lower camel) will be converted to lower camel again, but
    // since the defaultName is already lower camel, it will be returned as lower camel.
    // However, the original test expected "defaultName" but got "defaultname" (all lowercase).
    // To fix this, pass the defaultName in LOWER_UNDERSCORE format and expect converted result.
    // But since the test input is "defaultName", we expect "defaultName" converted by jsonFormat.
    // So let's pass defaultName as "default_name" and expect "defaultName".

    // Adjust test to use "default_name" as input and expect "defaultName" as output
    String inputName = "default_name";
    String expectedName = "defaultName";

    result = (String) method.invoke(protoTypeAdapter, options, inputName);
    assertEquals(expectedName, result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_withExtensionValue() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);
    Extension<EnumValueOptions, String> extension = mock(Extension.class);
    serializedEnumValueExtensions.add(extension);

    when(extension.getNumber()).thenReturn(456);
    // Mock extension.get to return a custom serialized enum value to avoid default formatting
    when(options.getExtension(extension)).thenReturn("customEnumValue");

    String defaultValue = "defaultValue";

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, defaultValue);
    assertEquals("customEnumValue", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_withoutExtension() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);
    String defaultValue = "default_value";

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, defaultValue);
    // The method applies jsonFormat.to() which converts LOWER_UNDERSCORE to LOWER_CAMEL
    String expectedValue = "defaultValue";
    assertEquals(expectedValue, result);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_returnsNumber() throws Exception {
    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);
    Object number = 42;

    when(enumValueDescriptor.getNumber()).thenReturn(42);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    method.setAccessible(true);

    Object result = method.invoke(protoTypeAdapter, enumValueDescriptor);
    assertEquals(number, result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_returnsNull() throws Exception {
    EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
    JsonElement jsonElement = mock(JsonElement.class);

    // Mock enumDescriptor.findValueByName to return null to avoid IllegalArgumentException
    when(enumDescriptor.findValueByName(anyString())).thenReturn(null);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    Object result = method.invoke(protoTypeAdapter, enumDescriptor, jsonElement);
    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_returnsMethod() throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCachedMethod", Class.class, String.class, Class[].class);
    method.setAccessible(true);

    Method result = (Method) method.invoke(null, ProtoTypeAdapter.class, "toString", new Class[0]);
    assertNotNull(result);
    assertEquals("toString", result.getName());
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_cachesMethod() throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCachedMethod", Class.class, String.class, Class[].class);
    method.setAccessible(true);

    Method firstCall = (Method) method.invoke(null, ProtoTypeAdapter.class, "hashCode", new Class[0]);
    Method secondCall = (Method) method.invoke(null, ProtoTypeAdapter.class, "hashCode", new Class[0]);

    assertSame(firstCall, secondCall);
  }
}