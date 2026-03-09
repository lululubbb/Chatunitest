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
import org.mockito.ArgumentCaptor;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class ProtoTypeAdapterDeserializeTest {

  ProtoTypeAdapter protoTypeAdapter;

  EnumSerialization enumSerialization = EnumSerialization.DEFAULT; // assuming EnumSerialization enum with DEFAULT
  CaseFormat protoFormat = CaseFormat.LOWER_UNDERSCORE;
  CaseFormat jsonFormat = CaseFormat.LOWER_CAMEL;
  Set serializedNameExtensions = Collections.emptySet();
  Set serializedEnumValueExtensions = Collections.emptySet();

  @BeforeEach
  void setUp() throws Exception {
    // Use reflection to create instance of ProtoTypeAdapter since constructor is private
    var constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        EnumSerialization.class, CaseFormat.class, CaseFormat.class, Set.class, Set.class);
    constructor.setAccessible(true);
    protoTypeAdapter = (ProtoTypeAdapter) constructor.newInstance(
        enumSerialization, protoFormat, jsonFormat, serializedNameExtensions, serializedEnumValueExtensions);
  }

  @Test
    @Timeout(8000)
  void deserialize_dynamicMessageClass_throwsIllegalStateException() {
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(new JsonObject());

    Type typeOfT = DynamicMessage.class;

    JsonDeserializationContext context = mock(JsonDeserializationContext.class);

    IllegalStateException thrown = assertThrows(IllegalStateException.class,
        () -> protoTypeAdapter.deserialize(json, typeOfT, context));

    assertEquals("only generated messages are supported", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  void deserialize_validProtoClass_buildsMessage() throws Exception {
    JsonObject jsonObject = mock(JsonObject.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonElement.isJsonArray()).thenReturn(false);

    when(jsonObject.get(anyString())).thenReturn(jsonElement);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    // Mock protoClass which is a subclass of Message but not DynamicMessage
    @SuppressWarnings("unchecked")
    Class<? extends Message> protoClass = (Class<? extends Message>) TestProto.class;

    // Mock static getCachedMethod to return methods
    Method newBuilderMethod = TestProto.class.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = TestProto.class.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = TestProto.class.getDeclaredMethod("getDescriptor");

    // Spy on ProtoTypeAdapter to mock getCachedMethod
    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

    Builder builder = TestProto.newBuilder();
    Message defaultInstance = TestProto.getDefaultInstance();
    Descriptor descriptor = TestProto.getDescriptor();

    // Mock method invocation
    doReturn(builder).when(newBuilderMethod).invoke(null);
    doReturn(defaultInstance).when(getDefaultInstanceMethod).invoke(null);
    doReturn(descriptor).when(getDescriptorMethod).invoke(null);

    // Mock Descriptor.getFields to return one field descriptor
    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    when(descriptor.getFields()).thenReturn(Collections.singletonList(fieldDescriptor));
    when(fieldDescriptor.getName()).thenReturn("field_name");
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.INT32);
    when(fieldDescriptor.isRepeated()).thenReturn(false);

    // Mock defaultInstance.getField to return Integer.class instance
    when(defaultInstance.getField(fieldDescriptor)).thenReturn(0);

    // Mock context.deserialize to return 42 for the field
    JsonDeserializationContext context = mock(JsonDeserializationContext.class);
    when(context.deserialize(jsonElement, Integer.class)).thenReturn(42);

    // Execute deserialize
    Message result = spyAdapter.deserialize(json, protoClass, context);

    assertNotNull(result);
    assertTrue(result instanceof TestProto);
    assertEquals(42, ((TestProto) result).getFieldName());
  }

  @Test
    @Timeout(8000)
  void deserialize_repeatedField_deserializesArray() throws Exception {
    JsonObject jsonObject = mock(JsonObject.class);
    JsonArray jsonArray = mock(JsonArray.class);
    JsonElement jsonElement = mock(JsonElement.class);

    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonElement.isJsonArray()).thenReturn(true);
    when(jsonElement.getAsJsonArray()).thenReturn(jsonArray);

    when(jsonObject.get(anyString())).thenReturn(jsonElement);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    @SuppressWarnings("unchecked")
    Class<? extends Message> protoClass = (Class<? extends Message>) TestProto.class;

    Method newBuilderMethod = TestProto.class.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = TestProto.class.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = TestProto.class.getDeclaredMethod("getDescriptor");

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

    Builder builder = TestProto.newBuilder();
    Message defaultInstance = TestProto.getDefaultInstance();
    Descriptor descriptor = TestProto.getDescriptor();

    doReturn(builder).when(newBuilderMethod).invoke(null);
    doReturn(defaultInstance).when(getDefaultInstanceMethod).invoke(null);
    doReturn(descriptor).when(getDescriptorMethod).invoke(null);

    FieldDescriptor repeatedFieldDescriptor = mock(FieldDescriptor.class);
    when(descriptor.getFields()).thenReturn(Collections.singletonList(repeatedFieldDescriptor));
    when(repeatedFieldDescriptor.getName()).thenReturn("repeated_field");
    when(repeatedFieldDescriptor.getOptions()).thenReturn(null);
    when(repeatedFieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.INT32);
    when(repeatedFieldDescriptor.isRepeated()).thenReturn(true);

    // The proto array field name is protoFormat.to(CaseFormat.LOWER_CAMEL, name) + "_"
    String protoArrayFieldName = protoFormat.to(CaseFormat.LOWER_CAMEL, "repeated_field") + "_";

    Field protoArrayField = TestProto.class.getDeclaredField(protoArrayFieldName);
    when(spyAdapter.protoFormat).thenReturn(protoFormat);

    // Mock protoClass.getDeclaredField
    ProtoTypeAdapter finalSpyAdapter = spyAdapter;
    doReturn(protoArrayField).when(finalSpyAdapter).getDeclaredField(protoClass, protoArrayFieldName);

    Type protoArrayFieldType = protoArrayField.getGenericType();

    JsonDeserializationContext context = mock(JsonDeserializationContext.class);
    Object deserializedValue = new ArrayList<Integer>();
    when(context.deserialize(jsonElement, protoArrayFieldType)).thenReturn(deserializedValue);

    // Mock protoBuilder.setField
    doNothing().when(builder).setField(repeatedFieldDescriptor, deserializedValue);

    Message result = finalSpyAdapter.deserialize(json, protoClass, context);

    assertNotNull(result);
    assertTrue(result instanceof TestProto);
  }

  @Test
    @Timeout(8000)
  void deserialize_enumField_deserializesEnum() throws Exception {
    JsonObject jsonObject = mock(JsonObject.class);
    JsonElement jsonElement = mock(JsonElement.class);

    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonElement.isJsonArray()).thenReturn(false);

    when(jsonObject.get(anyString())).thenReturn(jsonElement);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    @SuppressWarnings("unchecked")
    Class<? extends Message> protoClass = (Class<? extends Message>) TestProto.class;

    Method newBuilderMethod = TestProto.class.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = TestProto.class.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = TestProto.class.getDeclaredMethod("getDescriptor");

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

    Builder builder = TestProto.newBuilder();
    Message defaultInstance = TestProto.getDefaultInstance();
    Descriptor descriptor = TestProto.getDescriptor();

    doReturn(builder).when(newBuilderMethod).invoke(null);
    doReturn(defaultInstance).when(getDefaultInstanceMethod).invoke(null);
    doReturn(descriptor).when(getDescriptorMethod).invoke(null);

    FieldDescriptor enumFieldDescriptor = mock(FieldDescriptor.class);
    when(descriptor.getFields()).thenReturn(Collections.singletonList(enumFieldDescriptor));
    when(enumFieldDescriptor.getName()).thenReturn("enum_field");
    when(enumFieldDescriptor.getOptions()).thenReturn(null);
    when(enumFieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.ENUM);
    when(enumFieldDescriptor.isRepeated()).thenReturn(false);

    EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
    when(enumFieldDescriptor.getEnumType()).thenReturn(enumDescriptor);

    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);

    // Mock findValueByNameAndExtension to return enumValueDescriptor
    doReturn(enumValueDescriptor).when(spyAdapter).findValueByNameAndExtension(enumDescriptor, jsonElement);

    doNothing().when(builder).setField(enumFieldDescriptor, enumValueDescriptor);

    JsonDeserializationContext context = mock(JsonDeserializationContext.class);

    Message result = spyAdapter.deserialize(json, protoClass, context);

    assertNotNull(result);
    assertTrue(result instanceof TestProto);
  }

  @Test
    @Timeout(8000)
  void deserialize_throwsJsonParseExceptionOnInvocationTargetException() throws Exception {
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(new JsonObject());

    @SuppressWarnings("unchecked")
    Class<? extends Message> protoClass = (Class<? extends Message>) TestProto.class;

    JsonDeserializationContext context = mock(JsonDeserializationContext.class);

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);

    Method newBuilderMethod = TestProto.class.getDeclaredMethod("newBuilder");
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");

    // Make newBuilderMethod.invoke throw InvocationTargetException
    doThrow(new java.lang.reflect.InvocationTargetException(new Exception()))
        .when(newBuilderMethod).invoke(null);

    JsonParseException ex = assertThrows(JsonParseException.class,
        () -> spyAdapter.deserialize(json, protoClass, context));

    assertNotNull(ex.getCause());
  }

  // Helper test proto class with minimal implementation
  public static class TestProto extends Message {

    private int fieldName;
    private ArrayList<Integer> repeatedField_ = new ArrayList<>();
    private EnumValueDescriptor enumField;

    public int getFieldName() {
      return fieldName;
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public static TestProto getDefaultInstance() {
      return new TestProto();
    }

    public static Descriptor getDescriptor() {
      Descriptor descriptor = mock(Descriptor.class);
      return descriptor;
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
      if ("field_name".equals(field.getName())) {
        return fieldName;
      }
      return null;
    }

    public static class Builder extends Message.Builder<Builder> {

      private final TestProto instance = new TestProto();

      public Builder setField(FieldDescriptor field, Object value) {
        if ("field_name".equals(field.getName())) {
          instance.fieldName = (Integer) value;
        }
        if ("repeated_field".equals(field.getName())) {
          instance.repeatedField_ = (ArrayList<Integer>) value;
        }
        if ("enum_field".equals(field.getName())) {
          instance.enumField = (EnumValueDescriptor) value;
        }
        return this;
      }

      @Override
      public TestProto build() {
        return instance;
      }

      @Override
      public Builder clone() {
        return this;
      }

      @Override
      public Builder clear() {
        return this;
      }

      @Override
      public Descriptor getDescriptorForType() {
        return TestProto.getDescriptor();
      }

      @Override
      public TestProto getDefaultInstanceForType() {
        return TestProto.getDefaultInstance();
      }

      @Override
      public boolean isInitialized() {
        return true;
      }

      @Override
      public Builder mergeFrom(Message other) {
        return this;
      }
    }
  }
}