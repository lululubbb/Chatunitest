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
import static org.mockito.ArgumentMatchers.any;
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

public class ProtoTypeAdapter_465_6Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private JsonDeserializationContext context;

  // Dummy EnumSerialization and CaseFormat enums/classes for constructor (since private, use reflection)
  private enum DummyEnumSerialization {}
  private enum DummyCaseFormat {
    LOWER_CAMEL;
    public String to(DummyCaseFormat dummyCaseFormat, String name) {
      return name;
    }
  }

  @BeforeEach
  public void setUp() throws Exception {
    // Create instance of ProtoTypeAdapter via reflection (constructor is private)
    Class<ProtoTypeAdapter> clazz = ProtoTypeAdapter.class;
    var constructor = clazz.getDeclaredConstructor(
        EnumSerialization.class,
        com.google.common.base.CaseFormat.class,
        com.google.common.base.CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    protoTypeAdapter = constructor.newInstance(
        null, // enumSerialization unused in deserialize
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        Collections.emptySet());

    context = mock(JsonDeserializationContext.class);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withGeneratedMessage_success() throws Exception {
    // Mock JsonElement to return JsonObject
    JsonElement jsonElement = mock(JsonElement.class);
    JsonObject jsonObject = new JsonObject();
    // Add field with non-null value
    jsonObject.addProperty("fieldName", "value");
    when(jsonElement.getAsJsonObject()).thenReturn(jsonObject);

    // Prepare a dummy proto class extending Message
    class DummyMessage extends Message {
      public static Builder newBuilder() {
        return new DummyBuilder();
      }
      public static DummyMessage getDefaultInstance() {
        return new DummyMessage();
      }
      public static Descriptor getDescriptor() {
        Descriptor descriptor = mock(Descriptor.class);
        FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
        when(fieldDescriptor.getName()).thenReturn("fieldName");
        when(fieldDescriptor.getOptions()).thenReturn(null);
        when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.INT32);
        when(fieldDescriptor.isRepeated()).thenReturn(false);
        when(descriptor.getFields()).thenReturn(Collections.singletonList(fieldDescriptor));
        return descriptor;
      }
      @Override
      public Builder newBuilderForType() {
        return newBuilder();
      }
      @Override
      public Descriptor getDescriptorForType() {
        return getDescriptor();
      }
      @Override
      public Map<FieldDescriptor, Object> getAllFields() {
        return Collections.emptyMap();
      }
      @Override
      public boolean hasField(FieldDescriptor field) {
        return false;
      }
      @Override
      public Object getField(FieldDescriptor field) {
        return 0;
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
      @Override
      public Builder toBuilder() {
        return newBuilder();
      }
    }

    class DummyBuilder extends Builder {
      private final java.util.Map<FieldDescriptor, Object> fields = new java.util.HashMap<>();
      @Override
      public Builder setField(FieldDescriptor field, Object value) {
        fields.put(field, value);
        return this;
      }
      @Override
      public Message build() {
        return new DummyMessage();
      }
      @Override
      public Builder clear() {
        fields.clear();
        return this;
      }
      @Override
      public Builder clone() {
        return this;
      }
      @Override
      public boolean isInitialized() {
        return true;
      }
      @Override
      public Message getDefaultInstanceForType() {
        return new DummyMessage();
      }
      @Override
      public Builder mergeFrom(Message other) {
        return this;
      }
      @Override
      public Builder mergeFrom(com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry) {
        return this;
      }
    }

    // Use DummyMessage class for typeOfT
    Type typeOfT = DummyMessage.class;

    // Mock context.deserialize to return a value for the field
    when(context.deserialize(any(JsonElement.class), any(Type.class))).thenReturn(123);

    // Call deserialize
    Message result = protoTypeAdapter.deserialize(jsonElement, typeOfT, context);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withDynamicMessage_throwsIllegalStateException() {
    JsonElement jsonElement = mock(JsonElement.class);
    JsonObject jsonObject = new JsonObject();
    when(jsonElement.getAsJsonObject()).thenReturn(jsonObject);

    Type dynamicMessageType = com.google.protobuf.DynamicMessage.class;

    IllegalStateException thrown = assertThrows(IllegalStateException.class,
        () -> protoTypeAdapter.deserialize(jsonElement, dynamicMessageType, context));
    assertEquals("only generated messages are supported", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withNullJsonElement_throwsJsonParseException() {
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.getAsJsonObject()).thenThrow(new IllegalStateException("fail"));

    Type typeOfT = Message.class;

    JsonParseException ex = assertThrows(JsonParseException.class,
        () -> protoTypeAdapter.deserialize(jsonElement, typeOfT, context));
    assertTrue(ex.getMessage().contains("Error while parsing proto"));
  }

}