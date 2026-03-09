package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.base.CaseFormat;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Extension;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import com.google.protobuf.Message.Builder;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ProtoTypeAdapterDeserializeTest {

  private ProtoTypeAdapter protoTypeAdapter;
  private JsonDeserializationContext context;

  @BeforeEach
  void setUp() throws Exception {
    // Create ProtoTypeAdapter instance via reflection (constructor is private)
    protoTypeAdapter =
        (ProtoTypeAdapter)
            ProtoTypeAdapter.class
                .getDeclaredConstructor(
                    EnumSerialization.class,
                    com.google.common.base.CaseFormat.class,
                    com.google.common.base.CaseFormat.class,
                    Set.class,
                    Set.class)
                .newInstance(
                    EnumSerialization.DEFAULT,
                    com.google.common.base.CaseFormat.LOWER_CAMEL,
                    com.google.common.base.CaseFormat.LOWER_CAMEL,
                    Collections.emptySet(),
                    Collections.emptySet());
    context = mock(JsonDeserializationContext.class);
  }

  @Test
    @Timeout(8000)
  void deserialize_dynamicMessage_throwsIllegalStateException() {
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(new JsonObject());
    Type typeOfT = DynamicMessage.class;

    IllegalStateException ex =
        assertThrows(
            IllegalStateException.class,
            () -> protoTypeAdapter.deserialize(json, typeOfT, context));
    assertEquals("only generated messages are supported", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void deserialize_validProtoMessage_deserializesSuccessfully() throws Exception {
    // Mocks
    JsonObject jsonObject = mock(JsonObject.class);
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    @SuppressWarnings("unchecked")
    Class<? extends Message> protoClass = TestProtoMessage.class;

    // Mock protoClass static methods
    Method newBuilderMethod = protoClass.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = protoClass.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = protoClass.getDeclaredMethod("getDescriptor");

    Builder builder = mock(Builder.class);
    when(newBuilderMethod.invoke(null)).thenReturn(builder);

    Message defaultInstance = mock(Message.class);
    when(getDefaultInstanceMethod.invoke(null)).thenReturn(defaultInstance);

    Descriptor descriptor = mock(Descriptor.class);
    when(getDescriptorMethod.invoke(null)).thenReturn(descriptor);

    // Mock descriptor fields
    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    when(descriptor.getFields()).thenReturn(Collections.singletonList(fieldDescriptor));
    when(fieldDescriptor.getName()).thenReturn("fieldName");
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.INT32);
    when(fieldDescriptor.isRepeated()).thenReturn(false);

    // JsonObject returns a non-null element for the field
    JsonElement fieldJsonElement = mock(JsonElement.class);
    when(jsonObject.get("fieldName")).thenReturn(fieldJsonElement);
    when(fieldJsonElement.isJsonNull()).thenReturn(false);

    // defaultInstance.getField returns an Integer
    when(defaultInstance.getField(fieldDescriptor)).thenReturn(0);

    // context.deserialize returns a value for the field
    when(context.deserialize(fieldJsonElement, Integer.class)).thenReturn(123);

    // builder.setField returns builder for chaining
    when(builder.setField(fieldDescriptor, 123)).thenReturn(builder);
    Message builtMessage = mock(Message.class);
    when(builder.build()).thenReturn(builtMessage);

    // Spy on protoTypeAdapter to mock getCachedMethod and getCustSerializedName and findValueByNameAndExtension
    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");
    doReturn("fieldName").when(spyAdapter).getCustSerializedName(null, "fieldName");
    doReturn(null).when(spyAdapter).findValueByNameAndExtension(any(), any());

    // Replace protoTypeAdapter with spyAdapter in test
    Message result = spyAdapter.deserialize(json, protoClass, context);
    assertSame(builtMessage, result);
  }

  @Test
    @Timeout(8000)
  void deserialize_repeatedField_deserializesRepeatedField() throws Exception {
    JsonObject jsonObject = mock(JsonObject.class);
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    @SuppressWarnings("unchecked")
    Class<? extends Message> protoClass = TestProtoMessage.class;

    Method newBuilderMethod = protoClass.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = protoClass.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = protoClass.getDeclaredMethod("getDescriptor");

    Builder builder = mock(Builder.class);
    when(newBuilderMethod.invoke(null)).thenReturn(builder);

    Message defaultInstance = mock(Message.class);
    when(getDefaultInstanceMethod.invoke(null)).thenReturn(defaultInstance);

    Descriptor descriptor = mock(Descriptor.class);
    when(getDescriptorMethod.invoke(null)).thenReturn(descriptor);

    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    when(descriptor.getFields()).thenReturn(Collections.singletonList(fieldDescriptor));
    when(fieldDescriptor.getName()).thenReturn("repeatedField");
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.INT32);
    when(fieldDescriptor.isRepeated()).thenReturn(true);

    JsonElement fieldJsonElement = mock(JsonElement.class);
    when(jsonObject.get("repeatedField")).thenReturn(fieldJsonElement);
    when(fieldJsonElement.isJsonNull()).thenReturn(false);

    // The proto repeated field has a backing field with _ suffix
    Field protoArrayField = protoClass.getDeclaredField("repeatedField_");
    when(fieldDescriptor.getName()).thenReturn("repeatedField");

    // context.deserialize returns a list for the repeated field
    Type protoArrayFieldType = protoArrayField.getGenericType();
    when(context.deserialize(fieldJsonElement, protoArrayFieldType)).thenReturn(Collections.singletonList(42));

    when(builder.setField(fieldDescriptor, Collections.singletonList(42))).thenReturn(builder);
    Message builtMessage = mock(Message.class);
    when(builder.build()).thenReturn(builtMessage);

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");
    doReturn("repeatedField").when(spyAdapter).getCustSerializedName(null, "repeatedField");
    doReturn(null).when(spyAdapter).findValueByNameAndExtension(any(), any());

    Message result = spyAdapter.deserialize(json, protoClass, context);
    assertSame(builtMessage, result);
  }

  @Test
    @Timeout(8000)
  void deserialize_enumField_deserializesEnumField() throws Exception {
    JsonObject jsonObject = mock(JsonObject.class);
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    @SuppressWarnings("unchecked")
    Class<? extends Message> protoClass = TestProtoMessage.class;

    Method newBuilderMethod = protoClass.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = protoClass.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = protoClass.getDeclaredMethod("getDescriptor");

    Builder builder = mock(Builder.class);
    when(newBuilderMethod.invoke(null)).thenReturn(builder);

    Message defaultInstance = mock(Message.class);
    when(getDefaultInstanceMethod.invoke(null)).thenReturn(defaultInstance);

    Descriptor descriptor = mock(Descriptor.class);
    when(getDescriptorMethod.invoke(null)).thenReturn(descriptor);

    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    when(descriptor.getFields()).thenReturn(Collections.singletonList(fieldDescriptor));
    when(fieldDescriptor.getName()).thenReturn("enumField");
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.ENUM);
    when(fieldDescriptor.isRepeated()).thenReturn(false);

    JsonElement fieldJsonElement = mock(JsonElement.class);
    when(jsonObject.get("enumField")).thenReturn(fieldJsonElement);
    when(fieldJsonElement.isJsonNull()).thenReturn(false);
    when(fieldJsonElement.isJsonArray()).thenReturn(false);

    EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
    when(fieldDescriptor.getEnumType()).thenReturn(enumDescriptor);

    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");
    doReturn("enumField").when(spyAdapter).getCustSerializedName(null, "enumField");
    doReturn(enumValueDescriptor).when(spyAdapter).findValueByNameAndExtension(enumDescriptor, fieldJsonElement);

    when(builder.setField(fieldDescriptor, enumValueDescriptor)).thenReturn(builder);
    Message builtMessage = mock(Message.class);
    when(builder.build()).thenReturn(builtMessage);

    Message result = spyAdapter.deserialize(json, protoClass, context);
    assertSame(builtMessage, result);
  }

  // Dummy generated proto message class for reflection
  public static class TestProtoMessage extends Message {
    public static Builder newBuilder() {
      return mock(Builder.class);
    }

    public static Message getDefaultInstance() {
      return mock(Message.class);
    }

    public static Descriptor getDescriptor() {
      return mock(Descriptor.class);
    }

    // Backing field for repeated field test
    public java.util.List<Integer> repeatedField_;

    @Override
    public Descriptor getDescriptorForType() {
      return null;
    }

    @Override
    public Map<FieldDescriptor, Object> getAllFields() {
      return null;
    }

    @Override
    public boolean hasField(FieldDescriptor field) {
      return false;
    }

    @Override
    public Object getField(FieldDescriptor field) {
      return null;
    }

    @Override
    public int getRepeatedFieldCount(FieldDescriptor field) {
      return 0;
    }

    @Override
    public Object getRepeatedField(FieldDescriptor field, int index) {
      return null;
    }

    @Override
    public UnknownFieldSet getUnknownFields() {
      return null;
    }
  }
}