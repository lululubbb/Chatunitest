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
import org.mockito.InOrder;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class ProtoTypeAdapterDeserializeTest {

  private ProtoTypeAdapter protoTypeAdapter;
  private JsonDeserializationContext context;

  // Dummy EnumSerialization and CaseFormat enums/classes for constructor
  enum EnumSerialization { NAME }
  enum CaseFormat {
    LOWER_CAMEL,
    ;

    public String to(CaseFormat format, String input) {
      return input;
    }
  }

  // Dummy Extension class for constructor
  static class Extension<T, V> {}

  @BeforeEach
  public void setUp() throws Exception {
    protoTypeAdapter = createProtoTypeAdapter();
    context = mock(JsonDeserializationContext.class);
  }

  private ProtoTypeAdapter createProtoTypeAdapter() throws Exception {
    // Use reflection to call private constructor
    Class<ProtoTypeAdapter> clazz = ProtoTypeAdapter.class;
    java.lang.reflect.Constructor<ProtoTypeAdapter> ctor =
        clazz.getDeclaredConstructor(
            EnumSerialization.class,
            CaseFormat.class,
            CaseFormat.class,
            Set.class,
            Set.class);
    ctor.setAccessible(true);
    return ctor.newInstance(
        EnumSerialization.NAME,
        CaseFormat.LOWER_CAMEL,
        CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        Collections.emptySet());
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_dynamicMessage_throwsIllegalStateException() {
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(new JsonObject());

    Class<? extends Message> dynamicMessageClass = com.google.protobuf.DynamicMessage.class;

    Type type = dynamicMessageClass;

    IllegalStateException thrown =
        assertThrows(
            IllegalStateException.class,
            () -> protoTypeAdapter.deserialize(json, type, context));
    assertEquals("only generated messages are supported", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_success_singleField_nonEnum_nonRepeated() throws Exception {
    JsonObject jsonObject = mock(JsonObject.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonElement.isJsonArray()).thenReturn(false);

    when(jsonObject.get(anyString())).thenReturn(jsonElement);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    // Mock protoClass with generated message class
    Class<? extends Message> protoClass = DummyMessage.class;

    Type type = protoClass;

    // Setup mocks for static methods: newBuilder, getDefaultInstance, getDescriptor
    Method newBuilderMethod = protoClass.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = protoClass.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = protoClass.getDeclaredMethod("getDescriptor");

    // Spy on protoTypeAdapter to mock getCachedMethod
    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);

    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

    Builder builder = DummyMessage.newBuilder();
    when(newBuilderMethod.invoke(null)).thenReturn(builder);

    Message defaultInstance = DummyMessage.getDefaultInstance();
    when(getDefaultInstanceMethod.invoke(null)).thenReturn(defaultInstance);

    Descriptor descriptor = DummyMessage.getDescriptor();
    when(getDescriptorMethod.invoke(null)).thenReturn(descriptor);

    // Mock context.deserialize for the field type
    when(context.deserialize(eq(jsonElement), eq(String.class))).thenReturn("value");

    Message result = spyAdapter.deserialize(json, type, context);

    assertNotNull(result);
    assertEquals("value", ((DummyMessage) result).getFieldValue());
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_success_enumField_singleValue() throws Exception {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("enumField", "ENUM_VALUE_1");
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    Class<? extends Message> protoClass = DummyEnumMessage.class;

    Type type = protoClass;

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);

    Method newBuilderMethod = protoClass.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = protoClass.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = protoClass.getDeclaredMethod("getDescriptor");

    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

    Builder builder = DummyEnumMessage.newBuilder();
    when(newBuilderMethod.invoke(null)).thenReturn(builder);

    Message defaultInstance = DummyEnumMessage.getDefaultInstance();
    when(getDefaultInstanceMethod.invoke(null)).thenReturn(defaultInstance);

    Descriptor descriptor = DummyEnumMessage.getDescriptor();
    when(getDescriptorMethod.invoke(null)).thenReturn(descriptor);

    // Spy findValueByNameAndExtension to return dummy enum value descriptor
    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);
    doReturn(enumValueDescriptor)
        .when(spyAdapter)
        .findValueByNameAndExtension(any(EnumDescriptor.class), any(JsonElement.class));

    Message result = spyAdapter.deserialize(json, type, context);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_success_enumField_arrayValue() throws Exception {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("ENUM_VALUE_1");
    jsonArray.add("ENUM_VALUE_2");

    JsonObject jsonObject = new JsonObject();
    jsonObject.add("enumField", jsonArray);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    Class<? extends Message> protoClass = DummyEnumMessage.class;

    Type type = protoClass;

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);

    Method newBuilderMethod = protoClass.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = protoClass.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = protoClass.getDeclaredMethod("getDescriptor");

    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

    Builder builder = DummyEnumMessage.newBuilder();
    when(newBuilderMethod.invoke(null)).thenReturn(builder);

    Message defaultInstance = DummyEnumMessage.getDefaultInstance();
    when(getDefaultInstanceMethod.invoke(null)).thenReturn(defaultInstance);

    Descriptor descriptor = DummyEnumMessage.getDescriptor();
    when(getDescriptorMethod.invoke(null)).thenReturn(descriptor);

    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);
    doReturn(enumValueDescriptor)
        .when(spyAdapter)
        .findValueByNameAndExtension(any(EnumDescriptor.class), any(JsonElement.class));

    Message result = spyAdapter.deserialize(json, type, context);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_success_repeatedField() throws Exception {
    JsonArray jsonArray = new JsonArray();
    jsonArray.add("value1");
    jsonArray.add("value2");

    JsonObject jsonObject = new JsonObject();
    jsonObject.add("repeatedField_", jsonArray);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    Class<? extends Message> protoClass = DummyRepeatedMessage.class;

    Type type = protoClass;

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);

    Method newBuilderMethod = protoClass.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = protoClass.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = protoClass.getDeclaredMethod("getDescriptor");

    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

    Builder builder = DummyRepeatedMessage.newBuilder();
    when(newBuilderMethod.invoke(null)).thenReturn(builder);

    Message defaultInstance = DummyRepeatedMessage.getDefaultInstance();
    when(getDefaultInstanceMethod.invoke(null)).thenReturn(defaultInstance);

    Descriptor descriptor = DummyRepeatedMessage.getDescriptor();
    when(getDescriptorMethod.invoke(null)).thenReturn(descriptor);

    // Setup reflection for repeated field
    Field protoArrayField = protoClass.getDeclaredField("repeatedField_");
    when(spyAdapter.protoFormat.to(CaseFormat.LOWER_CAMEL, "repeatedField")).thenReturn("repeatedField_");

    when(context.deserialize(any(JsonElement.class), eq(protoArrayField.getGenericType())))
        .thenReturn(new ArrayList<>());

    Message result = spyAdapter.deserialize(json, type, context);

    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_throwsJsonParseException_onReflectionException() throws Exception {
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(new JsonObject());

    Class<? extends Message> protoClass = DummyMessage.class;

    Type type = protoClass;

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);

    doThrow(new NoSuchMethodException("test exception"))
        .when(spyAdapter)
        .getCachedMethod(protoClass, "newBuilder");

    JsonParseException thrown =
        assertThrows(
            JsonParseException.class,
            () -> spyAdapter.deserialize(json, type, context));
    assertTrue(thrown.getMessage().contains("test exception"));
  }

  // Dummy message classes for testing

  public static class DummyMessage extends Message {
    private String fieldValue;

    public String getFieldValue() {
      return fieldValue;
    }

    public static Builder newBuilder() {
      return new Builder();
    }

    public static DummyMessage getDefaultInstance() {
      return new DummyMessage();
    }

    public static Descriptor getDescriptor() {
      Descriptor descriptor = mock(Descriptor.class);
      FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
      when(fieldDescriptor.getName()).thenReturn("fieldValue");
      when(fieldDescriptor.getOptions()).thenReturn(null);
      when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.STRING);
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
      return "";
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

    public static class Builder extends Message.Builder<Builder> {
      private final DummyMessage instance = new DummyMessage();

      @Override
      public DummyMessage build() {
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
      public Builder mergeFrom(Message other) {
        return this;
      }

      @Override
      public boolean isInitialized() {
        return true;
      }

      @Override
      public Builder setField(FieldDescriptor field, Object value) {
        instance.fieldValue = (String) value;
        return this;
      }
    }
  }

  public static class DummyEnumMessage extends Message {
    public static Builder newBuilder() {
      return new Builder();
    }

    public static DummyEnumMessage getDefaultInstance() {
      return new DummyEnumMessage();
    }

    public static Descriptor getDescriptor() {
      Descriptor descriptor = mock(Descriptor.class);
      FieldDescriptor enumField = mock(FieldDescriptor.class);
      when(enumField.getName()).thenReturn("enumField");
      when(enumField.getOptions()).thenReturn(null);
      when(enumField.getType()).thenReturn(FieldDescriptor.Type.ENUM);
      when(enumField.isRepeated()).thenReturn(false);
      EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
      when(enumField.getEnumType()).thenReturn(enumDescriptor);
      when(descriptor.getFields()).thenReturn(Collections.singletonList(enumField));
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

    public static class Builder extends Message.Builder<Builder> {
      @Override
      public DummyEnumMessage build() {
        return new DummyEnumMessage();
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
      public Builder mergeFrom(Message other) {
        return this;
      }

      @Override
      public boolean isInitialized() {
        return true;
      }

      @Override
      public Builder setField(FieldDescriptor field, Object value) {
        return this;
      }
    }
  }

  public static class DummyRepeatedMessage extends Message {
    public static Builder newBuilder() {
      return new Builder();
    }

    public static DummyRepeatedMessage getDefaultInstance() {
      return new DummyRepeatedMessage();
    }

    public static Descriptor getDescriptor() {
      Descriptor descriptor = mock(Descriptor.class);
      FieldDescriptor repeatedField = mock(FieldDescriptor.class);
      when(repeatedField.getName()).thenReturn("repeatedField");
      when(repeatedField.getOptions()).thenReturn(null);
      when(repeatedField.getType()).thenReturn(FieldDescriptor.Type.STRING);
      when(repeatedField.isRepeated()).thenReturn(true);
      when(descriptor.getFields()).thenReturn(Collections.singletonList(repeatedField));
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

    public static class Builder extends Message.Builder<Builder> {
      @Override
      public DummyRepeatedMessage build() {
        return new DummyRepeatedMessage();
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
      public Builder mergeFrom(Message other) {
        return this;
      }

      @Override
      public boolean isInitialized() {
        return true;
      }

      @Override
      public Builder setField(FieldDescriptor field, Object value) {
        return this;
      }
    }
  }
}