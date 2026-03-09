package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.gson.JsonDeserializer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.base.CaseFormat;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Extension;
import com.google.protobuf.Message;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ProtoTypeAdapter_463_3Test {

  private ProtoTypeAdapter protoTypeAdapter;

  private EnumSerialization enumSerializationMock;
  private CaseFormat protoFormatMock;
  private CaseFormat jsonFormatMock;
  private Set<Extension<FieldOptions, String>> serializedNameExtensionsMock;
  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensionsMock;

  @BeforeEach
  void setUp() {
    enumSerializationMock = mock(EnumSerialization.class);
    protoFormatMock = CaseFormat.LOWER_UNDERSCORE;
    jsonFormatMock = CaseFormat.LOWER_CAMEL;
    serializedNameExtensionsMock = new HashSet<>();
    serializedEnumValueExtensionsMock = new HashSet<>();
    protoTypeAdapter = createInstance(enumSerializationMock, protoFormatMock, jsonFormatMock,
        serializedNameExtensionsMock, serializedEnumValueExtensionsMock);
  }

  private ProtoTypeAdapter createInstance(EnumSerialization enumSerialization,
      CaseFormat protoFormat,
      CaseFormat jsonFormat,
      Set<Extension<FieldOptions, String>> serializedNameExtensions,
      Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions) {
    try {
      var constructor = ProtoTypeAdapter.class.getDeclaredConstructor(EnumSerialization.class,
          CaseFormat.class, CaseFormat.class,
          Set.class, Set.class);
      constructor.setAccessible(true);
      return (ProtoTypeAdapter) constructor.newInstance(enumSerialization, protoFormat, jsonFormat,
          serializedNameExtensions, serializedEnumValueExtensions);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_withExtensionReturnsValue() throws Exception {
    FieldOptions optionsMock = mock(FieldOptions.class);
    Extension<FieldOptions, String> extensionMock = mock(Extension.class);
    String defaultName = "defaultName";

    serializedNameExtensionsMock.add(extensionMock);
    when(extensionMock.getNumber()).thenReturn(123);
    when(optionsMock.hasExtension(extensionMock)).thenReturn(true);
    when(optionsMock.getExtension(extensionMock)).thenReturn("customName");

    String result = invokeGetCustSerializedName(protoTypeAdapter, optionsMock, defaultName);

    assertEquals("customName", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_withoutExtensionReturnsDefault() throws Exception {
    FieldOptions optionsMock = mock(FieldOptions.class);
    String defaultName = "defaultName";

    // No extensions added, so no extension found
    String result = invokeGetCustSerializedName(protoTypeAdapter, optionsMock, defaultName);

    assertEquals(defaultName, result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_withExtensionReturnsValue() throws Exception {
    EnumValueOptions optionsMock = mock(EnumValueOptions.class);
    Extension<EnumValueOptions, String> extensionMock = mock(Extension.class);
    String defaultValue = "defaultValue";

    serializedEnumValueExtensionsMock.add(extensionMock);
    when(extensionMock.getNumber()).thenReturn(456);
    when(optionsMock.hasExtension(extensionMock)).thenReturn(true);
    when(optionsMock.getExtension(extensionMock)).thenReturn("customEnumValue");

    String result = invokeGetCustSerializedEnumValue(protoTypeAdapter, optionsMock, defaultValue);

    assertEquals("customEnumValue", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_withoutExtensionReturnsDefault() throws Exception {
    EnumValueOptions optionsMock = mock(EnumValueOptions.class);
    String defaultValue = "defaultValue";

    // No extensions added, so no extension found
    String result = invokeGetCustSerializedEnumValue(protoTypeAdapter, optionsMock, defaultValue);

    assertEquals(defaultValue, result);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_returnsNumberIfOrdinal() throws Exception {
    EnumValueDescriptor enumValueDescriptorMock = mock(EnumValueDescriptor.class);
    when(enumValueDescriptorMock.getNumber()).thenReturn(42);
    when(enumSerializationMock == EnumSerialization.ORDINAL).thenReturn(true);

    // We need to set enumSerialization to ORDINAL for this test
    ProtoTypeAdapter adapter = createInstance(EnumSerialization.ORDINAL, protoFormatMock, jsonFormatMock,
        serializedNameExtensionsMock, serializedEnumValueExtensionsMock);

    Object result = invokeGetEnumValue(adapter, enumValueDescriptorMock);

    assertEquals(42, result);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_returnsNameIfName() throws Exception {
    EnumValueDescriptor enumValueDescriptorMock = mock(EnumValueDescriptor.class);
    when(enumValueDescriptorMock.getNumber()).thenReturn(42);
    when(enumValueDescriptorMock.getName()).thenReturn("ENUM_NAME");

    ProtoTypeAdapter adapter = createInstance(EnumSerialization.NAME, protoFormatMock, jsonFormatMock,
        serializedNameExtensionsMock, serializedEnumValueExtensionsMock);

    Object result = invokeGetEnumValue(adapter, enumValueDescriptorMock);

    assertEquals("ENUM_NAME", result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_returnsNullIfJsonNull() throws Exception {
    EnumDescriptor enumDescriptorMock = mock(EnumDescriptor.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(true);

    Object result = invokeFindValueByNameAndExtension(protoTypeAdapter, enumDescriptorMock, jsonElement);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_returnsValueFromExtension() throws Exception {
    EnumDescriptor enumDescriptorMock = mock(EnumDescriptor.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonElement.isJsonPrimitive()).thenReturn(true);
    when(jsonElement.getAsString()).thenReturn("customValue");

    // Prepare serializedEnumValueExtensions with one extension
    Extension<EnumValueOptions, String> extensionMock = mock(Extension.class);
    serializedEnumValueExtensionsMock.add(extensionMock);
    when(extensionMock.getNumber()).thenReturn(789);

    EnumValueDescriptor enumValueDescriptorMock = mock(EnumValueDescriptor.class);
    when(enumDescriptorMock.getValues()).thenReturn(Collections.singletonList(enumValueDescriptorMock));
    when(enumValueDescriptorMock.getOptions()).thenReturn(mock(EnumValueOptions.class));
    when(enumValueDescriptorMock.getName()).thenReturn("ENUM_NAME");
    when(protoTypeAdapter.getCustSerializedEnumValue(any(), any())).thenCallRealMethod();

    // Mock getCustSerializedEnumValue to return matching string
    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn("customValue").when(spyAdapter).getCustSerializedEnumValue(any(), eq("customValue"));

    Object result = invokeFindValueByNameAndExtension(spyAdapter, enumDescriptorMock, jsonElement);

    assertNotNull(result);
    assertSame(enumValueDescriptorMock, result);
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_returnsMethodAndCaches() throws Exception {
    Method method1 = ProtoTypeAdapter.class.getDeclaredMethod("getCachedMethod", Class.class, String.class, Class[].class);

    Method cachedMethod = invokeGetCachedMethod(ProtoTypeAdapter.class, "getCachedMethod", Class.class, String.class, Class[].class);
    assertNotNull(cachedMethod);

    // Call again to confirm cache hit (no exception, same method)
    Method cachedMethod2 = invokeGetCachedMethod(ProtoTypeAdapter.class, "getCachedMethod", Class.class, String.class, Class[].class);
    assertSame(cachedMethod, cachedMethod2);
  }

  // Helper methods to invoke private methods via reflection

  private String invokeGetCustSerializedName(ProtoTypeAdapter adapter, FieldOptions options, String defaultName) throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);
    return (String) method.invoke(adapter, options, defaultName);
  }

  private String invokeGetCustSerializedEnumValue(ProtoTypeAdapter adapter, EnumValueOptions options, String defaultValue) throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);
    return (String) method.invoke(adapter, options, defaultValue);
  }

  private Object invokeGetEnumValue(ProtoTypeAdapter adapter, EnumValueDescriptor enumValueDescriptor) throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    method.setAccessible(true);
    return method.invoke(adapter, enumValueDescriptor);
  }

  private Object invokeFindValueByNameAndExtension(ProtoTypeAdapter adapter, EnumDescriptor desc, JsonElement jsonElement) throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);
    return method.invoke(adapter, desc, jsonElement);
  }

  private Method invokeGetCachedMethod(Class<?> clazz, String methodName, Class<?>... paramTypes) throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCachedMethod", Class.class, String.class, Class[].class);
    method.setAccessible(true);
    return (Method) method.invoke(null, clazz, methodName, paramTypes);
  }
}