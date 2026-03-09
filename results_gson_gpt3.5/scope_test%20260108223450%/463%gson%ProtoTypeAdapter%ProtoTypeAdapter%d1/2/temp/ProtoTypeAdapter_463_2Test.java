package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.common.base.CaseFormat;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Extension;
import com.google.protobuf.Message;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProtoTypeAdapter_463_2Test {

  private ProtoTypeAdapter protoTypeAdapter;
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

    // Use reflection to invoke private constructor
    var constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    protoTypeAdapter = (ProtoTypeAdapter) constructor.newInstance(
        enumSerialization,
        protoFormat,
        jsonFormat,
        serializedNameExtensions,
        serializedEnumValueExtensions);
  }

  @Test
    @Timeout(8000)
  void testSerialize_withEmptyMessage_returnsJsonObject() throws Exception {
    Message message = mock(Message.class);
    when(message.getAllFields()).thenReturn(Collections.emptyMap());
    JsonSerializationContext context = mock(JsonSerializationContext.class);

    JsonElement result = protoTypeAdapter.serialize(message, Message.class, context);

    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertEquals(0, result.getAsJsonObject().size());
  }

  @Test
    @Timeout(8000)
  void testSerialize_withEnumField_serializesEnumValue() throws Exception {
    // Setup mocks for enum field
    Message message = mock(Message.class);
    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.ENUM);
    when(message.getAllFields()).thenReturn(Collections.singletonMap(fieldDescriptor, enumValueDescriptor));
    when(fieldDescriptor.getName()).thenReturn("test_field");

    JsonSerializationContext context = mock(JsonSerializationContext.class);
    // Fix: mock the serialize method on enumSerialization with the correct signature
    when(enumSerialization.serialize(any(EnumValueDescriptor.class))).thenReturn("enum_serialized");

    // Use reflection to access private getEnumValue method
    Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    getEnumValueMethod.setAccessible(true);
    Object enumValue = getEnumValueMethod.invoke(protoTypeAdapter, enumValueDescriptor);
    assertNotNull(enumValue);

    JsonElement result = protoTypeAdapter.serialize(message, Message.class, context);

    assertNotNull(result);
    assertTrue(result.isJsonObject());
    assertTrue(result.getAsJsonObject().has("testField"));
    assertEquals("enum_serialized", result.getAsJsonObject().get("testField").getAsString());
  }

  @Test
    @Timeout(8000)
  void testDeserialize_withEmptyJsonObject_returnsEmptyMessage() throws Exception {
    JsonObject jsonObject = new JsonObject();
    var context = mock(com.google.gson.JsonDeserializationContext.class);

    Descriptor descriptor = mock(Descriptor.class);
    DynamicMessage.Builder builder = mock(DynamicMessage.Builder.class);
    when(builder.build()).thenReturn(mock(DynamicMessage.class));
    when(descriptor.getFields()).thenReturn(Collections.emptyList());

    Type type = DynamicMessage.class;

    Message result = protoTypeAdapter.deserialize(jsonObject, type, context);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_returnsDefaultNameIfNoExtension() throws Exception {
    FieldOptions options = mock(FieldOptions.class);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(protoTypeAdapter, options, "default_name");

    assertEquals("default_name", result);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedEnumValue_returnsDefaultValueIfNoExtension() throws Exception {
    EnumValueOptions options = mock(EnumValueOptions.class);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    method.setAccessible(true);
    String result = (String) method.invoke(protoTypeAdapter, options, "default_value");

    assertEquals("default_value", result);
  }

  @Test
    @Timeout(8000)
  void testFindValueByNameAndExtension_returnsNullForUnknown() throws Exception {
    EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonPrimitive()).thenReturn(true);
    when(jsonElement.getAsString()).thenReturn("unknown");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);
    EnumValueDescriptor result = (EnumValueDescriptor) method.invoke(protoTypeAdapter, enumDescriptor, jsonElement);

    assertNull(result);
  }

  @Test
    @Timeout(8000)
  void testGetCachedMethod_cachesAndReturnsMethod() throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCachedMethod", Class.class, String.class, Class[].class);
    method.setAccessible(true);

    Method result1 = (Method) method.invoke(null, ProtoTypeAdapter.class, "getCustSerializedName", new Class[]{FieldOptions.class, String.class});
    Method result2 = (Method) method.invoke(null, ProtoTypeAdapter.class, "getCustSerializedName", new Class[]{FieldOptions.class, String.class});

    assertNotNull(result1);
    assertSame(result1, result2); // cached method should be same object
  }
}