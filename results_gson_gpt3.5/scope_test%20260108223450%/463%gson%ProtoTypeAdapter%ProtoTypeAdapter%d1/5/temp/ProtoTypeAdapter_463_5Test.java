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
import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProtoTypeAdapter_463_5Test {

  private ProtoTypeAdapter adapter;
  private Object enumSerialization;  // Changed from EnumSerialization to Object to avoid missing class error
  private CaseFormat protoFormat;
  private CaseFormat jsonFormat;
  private Set<Extension<FieldOptions, String>> serializedNameExtensions;
  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions;

  @BeforeEach
  void setUp() throws Exception {
    enumSerialization = mock(Object.class);  // Mock as Object to avoid missing class error
    protoFormat = CaseFormat.LOWER_UNDERSCORE;
    jsonFormat = CaseFormat.LOWER_CAMEL;
    serializedNameExtensions = new HashSet<>();
    serializedEnumValueExtensions = new HashSet<>();

    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        Object.class,  // Changed from EnumSerialization.class to Object.class
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class
    );
    constructor.setAccessible(true);
    adapter = constructor.newInstance(
        enumSerialization,
        protoFormat,
        jsonFormat,
        serializedNameExtensions,
        serializedEnumValueExtensions
    );
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_withExtensionAndDefaultName() throws Exception {
    FieldOptions options = mock(FieldOptions.class);
    String defaultName = "defaultName";

    @SuppressWarnings("unchecked")
    Extension<FieldOptions, String> extension = mock(Extension.class);
    serializedNameExtensions.add(extension);

    // Use reflection to mock extension.get(options)
    Method getMethod = Extension.class.getDeclaredMethod("get", Object.class);
    when(getMethod.invoke(extension, options)).thenReturn("customName");

    // Since we cannot mock invoke on reflection directly, we mock extension.get by mocking extension.get() via Mockito spy
    // Instead, mock extension.get(options) by stubbing extension.get() via doReturn
    // But since extension.get is not accessible, we mock extension.get() by using Mockito's when with a spy:
    // Alternatively, use Answer to simulate get call.

    // So we replace extension mock with spy and stub get method using doAnswer:
    Extension<FieldOptions, String> spyExtension = spy(extension);
    doReturn("customName").when(spyExtension).get(options);
    serializedNameExtensions.clear();
    serializedNameExtensions.add(spyExtension);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapter, options, defaultName);

    assertEquals("customName", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_withExtensionReturningNull() throws Exception {
    FieldOptions options = mock(FieldOptions.class);
    String defaultName = "defaultName";

    @SuppressWarnings("unchecked")
    Extension<FieldOptions, String> extension = mock(Extension.class);

    Extension<FieldOptions, String> spyExtension = spy(extension);
    doReturn(null).when(spyExtension).get(options);
    serializedNameExtensions.clear();
    serializedNameExtensions.add(spyExtension);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapter, options, defaultName);

    assertEquals(defaultName, result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_withNoExtensions() throws Exception {
    FieldOptions options = mock(FieldOptions.class);
    String defaultName = "defaultName";

    serializedNameExtensions.clear();

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapter, options, defaultName);

    assertEquals(defaultName, result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_withExtensionAndDefaultValue() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);
    String defaultValue = "defaultValue";

    @SuppressWarnings("unchecked")
    Extension<EnumValueOptions, String> extension = mock(Extension.class);

    Extension<EnumValueOptions, String> spyExtension = spy(extension);
    doReturn("customEnumValue").when(spyExtension).get(options);
    serializedEnumValueExtensions.clear();
    serializedEnumValueExtensions.add(spyExtension);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapter, options, defaultValue);

    assertEquals("customEnumValue", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_withExtensionReturningNull() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);
    String defaultValue = "defaultValue";

    @SuppressWarnings("unchecked")
    Extension<EnumValueOptions, String> extension = mock(Extension.class);

    Extension<EnumValueOptions, String> spyExtension = spy(extension);
    doReturn(null).when(spyExtension).get(options);
    serializedEnumValueExtensions.clear();
    serializedEnumValueExtensions.add(spyExtension);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapter, options, defaultValue);

    assertEquals(defaultValue, result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_withNoExtensions() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);
    String defaultValue = "defaultValue";

    serializedEnumValueExtensions.clear();

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(adapter, options, defaultValue);

    assertEquals(defaultValue, result);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_returnsCorrectValue() throws Exception {
    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);

    when(enumValueDescriptor.getNumber()).thenReturn(42);
    when(enumValueDescriptor.getOptions()).thenReturn(null);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    method.setAccessible(true);

    Object result = method.invoke(adapter, enumValueDescriptor);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_withNullJsonElement() throws Exception {
    EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
    when(enumDescriptor.findValueByName(anyString())).thenReturn(null);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    JsonElement jsonElement = null;
    Object result = method.invoke(adapter, enumDescriptor, jsonElement);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_withValidName() throws Exception {
    EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);

    when(enumDescriptor.findValueByName("testName")).thenReturn(enumValueDescriptor);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);

    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.getAsString()).thenReturn("testName");

    Object result = method.invoke(adapter, enumDescriptor, jsonElement);

    assertSame(enumValueDescriptor, result);
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_cachesAndReturnsMethod() throws Exception {
    Class<?> clazz = SampleClass.class;
    String methodName = "sampleMethod";

    Method method1 = ProtoTypeAdapter.class.getDeclaredMethod("getCachedMethod", Class.class, String.class, Class[].class);
    method1.setAccessible(true);

    Method result1 = (Method) method1.invoke(null, clazz, methodName, new Class<?>[0]);
    Method result2 = (Method) method1.invoke(null, clazz, methodName, new Class<?>[0]);

    assertNotNull(result1);
    assertSame(result1, result2);
  }

  public static class SampleClass {
    public void sampleMethod() {
    }
  }
}