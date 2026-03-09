package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.base.CaseFormat;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Extension;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

public class ProtoTypeAdapter_465_4Test {

  private ProtoTypeAdapter protoTypeAdapter;

  private EnumSerialization enumSerialization;
  private com.google.common.base.CaseFormat protoFormat;
  private com.google.common.base.CaseFormat jsonFormat;
  private Set<com.google.protobuf.Extension<com.google.protobuf.DescriptorProtos.FieldOptions, String>> serializedNameExtensions;
  private Set<com.google.protobuf.Extension<com.google.protobuf.DescriptorProtos.EnumValueOptions, String>> serializedEnumValueExtensions;

  @BeforeEach
  public void setUp() throws Exception {
    // Use reflection to create instance because constructor is private
    protoFormat = com.google.common.base.CaseFormat.LOWER_UNDERSCORE;
    jsonFormat = com.google.common.base.CaseFormat.LOWER_CAMEL;
    serializedNameExtensions = new HashSet<>();
    serializedEnumValueExtensions = new HashSet<>();
    enumSerialization = EnumSerialization.DEFAULT; // assuming EnumSerialization.DEFAULT exists

    Method constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        EnumSerialization.class,
        com.google.common.base.CaseFormat.class,
        com.google.common.base.CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    protoTypeAdapter = (ProtoTypeAdapter) constructor.invoke(null, enumSerialization, protoFormat, jsonFormat, serializedNameExtensions, serializedEnumValueExtensions);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withValidProtoClassAndJson_buildsMessage() throws Exception {
    // Prepare mocks
    JsonElement jsonElement = mock(JsonElement.class);
    JsonObject jsonObject = mock(JsonObject.class);
    when(jsonElement.getAsJsonObject()).thenReturn(jsonObject);

    // Create a dummy proto class extending Message
    class DummyMessage extends Message {
      public static Builder newBuilder() {
        return new DummyBuilder();
      }
      public static DummyMessage getDefaultInstance() {
        return new DummyMessage();
      }
      public static Descriptor getDescriptor() {
        return descriptor;
      }
      @Override public Builder newBuilderForType() { return null; }
      @Override public Builder toBuilder() { return null; }
      @Override public void writeTo(com.google.protobuf.CodedOutputStream output) {}
      @Override public int getSerializedSize() { return 0; }
      @Override public com.google.protobuf.Parser<? extends Message> getParserForType() { return null; }
      @Override public com.google.protobuf.Message getDefaultInstanceForType() { return null; }
      @Override public boolean isInitialized() { return true; }
      @Override public com.google.protobuf.UnknownFieldSet getUnknownFields() { return null; }
      @Override public Map<FieldDescriptor, Object> getAllFields() { return null; }
      @Override public boolean hasField(FieldDescriptor field) { return false; }
      @Override public Object getField(FieldDescriptor field) { return fieldDefaultValues.get(field); }
      @Override public int getRepeatedFieldCount(FieldDescriptor field) { return 0; }
      @Override public Object getRepeatedField(FieldDescriptor field, int index) { return null; }
      @Override public UnknownFieldSet getUnknownFields() { return null; }
    }
    class DummyBuilder extends Builder {
      private final Map<FieldDescriptor, Object> fields = new HashMap<>();
      @Override public Builder setField(FieldDescriptor field, Object value) {
        fields.put(field, value);
        return this;
      }
      @Override public Message build() {
        return new DummyMessage();
      }
      @Override public Builder clear() { return this; }
      @Override public Builder clone() { return this; }
      @Override public boolean isInitialized() { return true; }
      @Override public Builder mergeFrom(Message other) { return this; }
      @Override public Builder mergeFrom(com.google.protobuf.CodedInputStream input, com.google.protobuf.ExtensionRegistryLite extensionRegistry) { return this; }
      @Override public com.google.protobuf.Descriptors.Descriptor getDescriptorForType() { return null; }
      @Override public Message getDefaultInstanceForType() { return null; }
      @Override public Map<FieldDescriptor, Object> getAllFields() { return fields; }
      @Override public boolean hasField(FieldDescriptor field) { return fields.containsKey(field); }
      @Override public Object getField(FieldDescriptor field) { return fields.get(field); }
      @Override public Builder clearField(FieldDescriptor field) { fields.remove(field); return this; }
      @Override public Builder setRepeatedField(FieldDescriptor field, int index, Object value) { return this; }
      @Override public Builder addRepeatedField(FieldDescriptor field, Object value) { return this; }
    }

    // Prepare Descriptor and FieldDescriptor mocks
    Descriptor descriptor = mock(Descriptor.class);
    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);

    Map<FieldDescriptor, Object> fieldDefaultValues = new HashMap<>();

    when(descriptor.getFields()).thenReturn(Collections.singletonList(fieldDescriptor));
    when(fieldDescriptor.getName()).thenReturn("test_field");
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.INT32);
    when(fieldDescriptor.isRepeated()).thenReturn(false);
    when(fieldDescriptor.getEnumType()).thenReturn(enumDescriptor);

    // Setup defaultInstance.getField to return an Integer class instance
    Integer defaultFieldValue = 0;
    fieldDefaultValues.put(fieldDescriptor, defaultFieldValue);

    // Setup jsonObject.get to return a JsonElement (mock)
    JsonElement fieldJsonElement = mock(JsonElement.class);
    when(jsonObject.get("test_field")).thenReturn(fieldJsonElement);
    when(fieldJsonElement.isJsonNull()).thenReturn(false);

    // Mock JsonDeserializationContext.deserialize to return a value
    JsonDeserializationContext context = mock(JsonDeserializationContext.class);
    when(context.deserialize(fieldJsonElement, Integer.class)).thenReturn(42);

    // Mock static getCachedMethod to return actual methods from DummyMessage class
    Method newBuilderMethod = DummyMessage.class.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = DummyMessage.class.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = DummyMessage.class.getDeclaredMethod("getDescriptor");

    // Use reflection to set mapOfMapOfMethods for getCachedMethod to return above methods
    Field mapField = ProtoTypeAdapter.class.getDeclaredField("mapOfMapOfMethods");
    mapField.setAccessible(true);
    @SuppressWarnings("unchecked")
    Map<String, Map<Class<?>, Method>> mapOfMap = (Map<String, Map<Class<?>, Method>>) mapField.get(null);
    Map<Class<?>, Method> mapForNewBuilder = new HashMap<>();
    Map<Class<?>, Method> mapForGetDefaultInstance = new HashMap<>();
    Map<Class<?>, Method> mapForGetDescriptor = new HashMap<>();
    mapForNewBuilder.put(DummyMessage.class, newBuilderMethod);
    mapForGetDefaultInstance.put(DummyMessage.class, getDefaultInstanceMethod);
    mapForGetDescriptor.put(DummyMessage.class, getDescriptorMethod);
    mapOfMap.put("newBuilder", mapForNewBuilder);
    mapOfMap.put("getDefaultInstance", mapForGetDefaultInstance);
    mapOfMap.put("getDescriptor", mapForGetDescriptor);

    // Mock getCustSerializedName to just return the field name
    Method getCustSerializedName = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", Object.class, String.class);
    getCustSerializedName.setAccessible(true);

    // Mock findValueByNameAndExtension to return enumValueDescriptor if needed
    Method findValueByNameAndExtension = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    findValueByNameAndExtension.setAccessible(true);

    // Spy protoTypeAdapter to mock private methods getCustSerializedName and findValueByNameAndExtension
    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn("test_field").when(spyAdapter).getCustSerializedName(null, "test_field");
    doReturn(enumValueDescriptor).when(spyAdapter).findValueByNameAndExtension(enumDescriptor, fieldJsonElement);

    // Use reflection to set protoTypeAdapter to spyAdapter for testing
    protoTypeAdapter = spyAdapter;

    Message result = protoTypeAdapter.deserialize(jsonElement, DummyMessage.class, context);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withDynamicMessage_throwsIllegalStateException() {
    JsonElement jsonElement = mock(JsonElement.class);
    JsonObject jsonObject = mock(JsonObject.class);
    when(jsonElement.getAsJsonObject()).thenReturn(jsonObject);

    JsonDeserializationContext context = mock(JsonDeserializationContext.class);

    Class<?> dynamicMessageClass = com.google.protobuf.DynamicMessage.class;

    IllegalStateException thrown = assertThrows(IllegalStateException.class, () -> {
      protoTypeAdapter.deserialize(jsonElement, dynamicMessageClass, context);
    });

    assertEquals("only generated messages are supported", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withException_throwsJsonParseException() {
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.getAsJsonObject()).thenThrow(new RuntimeException("fail"));

    JsonDeserializationContext context = mock(JsonDeserializationContext.class);

    JsonParseException thrown = assertThrows(JsonParseException.class, () -> {
      protoTypeAdapter.deserialize(jsonElement, Message.class, context);
    });

    assertTrue(thrown.getMessage().contains("Error while parsing proto"));
  }
}