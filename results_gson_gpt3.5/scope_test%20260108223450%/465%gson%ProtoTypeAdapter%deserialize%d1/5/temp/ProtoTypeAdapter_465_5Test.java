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
import org.mockito.AdditionalAnswers;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class ProtoTypeAdapterDeserializeTest {

  private ProtoTypeAdapter protoTypeAdapter;
  private JsonDeserializationContext context;

  // Dummy EnumSerialization and CaseFormat to satisfy constructor
  private enum DummyEnumSerialization {
    DEFAULT
  }

  private static class DummyCaseFormat extends com.google.common.base.CaseFormat {
    protected DummyCaseFormat(String name, int ordinal) {
      super(name, ordinal);
    }

    public String to(CaseFormat targetFormat, String input) {
      return input;
    }
  }

  private static final com.google.common.base.CaseFormat DUMMY_CASE_FORMAT =
      new DummyCaseFormat("DUMMY", 0);

  @BeforeEach
  public void setUp() throws Exception {
    // Create ProtoTypeAdapter instance via reflection because constructor is private
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
                    DUMMY_CASE_FORMAT,
                    DUMMY_CASE_FORMAT,
                    Collections.emptySet(),
                    Collections.emptySet());
    context = mock(JsonDeserializationContext.class);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withDynamicMessage_throwsIllegalStateException() {
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(new JsonObject());

    Type dynamicMessageType = com.google.protobuf.DynamicMessage.class;

    IllegalStateException thrown =
        assertThrows(
            IllegalStateException.class,
            () -> protoTypeAdapter.deserialize(json, dynamicMessageType, context));

    assertEquals("only generated messages are supported", thrown.getMessage());
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withValidProtoMessage_buildsMessage() throws Exception {
    // Mock JsonElement to return JsonObject
    JsonObject jsonObject = mock(JsonObject.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonElement.isJsonArray()).thenReturn(false);
    when(jsonObject.get(anyString())).thenReturn(jsonElement);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    // Mock protoClass
    Class<? extends Message> protoClass = TestProtoMessage.class;

    // Prepare mocks for static methods getCachedMethod
    Method newBuilderMethod = TestProtoMessage.class.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = TestProtoMessage.class.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = TestProtoMessage.class.getDeclaredMethod("getDescriptor");

    // Spy on ProtoTypeAdapter to mock getCachedMethod
    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

    // Mock Builder
    Builder builder = mock(Builder.class);
    when(newBuilderMethod.invoke(null)).thenReturn(builder);

    // Mock default instance
    Message defaultInstance = mock(Message.class);
    when(getDefaultInstanceMethod.invoke(null)).thenReturn(defaultInstance);

    // Mock Descriptor and FieldDescriptor
    Descriptor descriptor = mock(Descriptor.class);
    when(getDescriptorMethod.invoke(null)).thenReturn(descriptor);

    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    when(descriptor.getFields()).thenReturn(Collections.singletonList(fieldDescriptor));

    when(fieldDescriptor.getName()).thenReturn("testField");
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.INT32);
    when(fieldDescriptor.isRepeated()).thenReturn(false);

    when(jsonObject.get("testField")).thenReturn(jsonElement);

    // Mock defaultInstance.getField to return an Integer
    when(defaultInstance.getField(fieldDescriptor)).thenReturn(0);

    // Mock context.deserialize to return 123 when deserializing the field
    when(context.deserialize(jsonElement, Integer.class)).thenReturn(123);

    // Mock builder.setField to return builder for chaining
    when(builder.setField(fieldDescriptor, 123)).thenReturn(builder);

    // Mock builder.build() to return a test message instance
    Message builtMessage = mock(Message.class);
    when(builder.build()).thenReturn(builtMessage);

    Message result = spyAdapter.deserialize(json, protoClass, context);

    assertSame(builtMessage, result);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withEnumField_singleValue() throws Exception {
    JsonObject jsonObject = mock(JsonObject.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonElement.isJsonArray()).thenReturn(false);
    when(jsonObject.get(anyString())).thenReturn(jsonElement);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    Class<? extends Message> protoClass = TestProtoMessage.class;

    Method newBuilderMethod = TestProtoMessage.class.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = TestProtoMessage.class.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = TestProtoMessage.class.getDeclaredMethod("getDescriptor");

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

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

    when(jsonObject.get("enumField")).thenReturn(jsonElement);

    EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
    when(fieldDescriptor.getEnumType()).thenReturn(enumDescriptor);

    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);

    doReturn(enumValueDescriptor)
        .when(spyAdapter)
        .findValueByNameAndExtension(enumDescriptor, jsonElement);

    when(builder.setField(fieldDescriptor, enumValueDescriptor)).thenReturn(builder);

    Message builtMessage = mock(Message.class);
    when(builder.build()).thenReturn(builtMessage);

    Message result = spyAdapter.deserialize(json, protoClass, context);

    assertSame(builtMessage, result);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withEnumField_arrayValue() throws Exception {
    JsonObject jsonObject = mock(JsonObject.class);
    JsonArray jsonArray = mock(JsonArray.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonElement.isJsonArray()).thenReturn(true);
    when(jsonElement.getAsJsonArray()).thenReturn(jsonArray);
    when(jsonObject.get(anyString())).thenReturn(jsonElement);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    Class<? extends Message> protoClass = TestProtoMessage.class;

    Method newBuilderMethod = TestProtoMessage.class.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = TestProtoMessage.class.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = TestProtoMessage.class.getDeclaredMethod("getDescriptor");

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

    Builder builder = mock(Builder.class);
    when(newBuilderMethod.invoke(null)).thenReturn(builder);

    Message defaultInstance = mock(Message.class);
    when(getDefaultInstanceMethod.invoke(null)).thenReturn(defaultInstance);

    Descriptor descriptor = mock(Descriptor.class);
    when(getDescriptorMethod.invoke(null)).thenReturn(descriptor);

    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    when(descriptor.getFields()).thenReturn(Collections.singletonList(fieldDescriptor));

    when(fieldDescriptor.getName()).thenReturn("enumArrayField");
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.ENUM);
    when(fieldDescriptor.isRepeated()).thenReturn(false);

    when(jsonObject.get("enumArrayField")).thenReturn(jsonElement);

    EnumDescriptor enumDescriptor = mock(EnumDescriptor.class);
    when(fieldDescriptor.getEnumType()).thenReturn(enumDescriptor);

    // Setup JsonArray to have 2 elements
    JsonElement element1 = mock(JsonElement.class);
    JsonElement element2 = mock(JsonElement.class);
    when(jsonArray.size()).thenReturn(2);
    when(jsonArray.iterator()).thenReturn(java.util.Arrays.asList(element1, element2).iterator());

    EnumValueDescriptor enumValueDescriptor1 = mock(EnumValueDescriptor.class);
    EnumValueDescriptor enumValueDescriptor2 = mock(EnumValueDescriptor.class);

    doReturn(enumValueDescriptor1)
        .doReturn(enumValueDescriptor2)
        .when(spyAdapter)
        .findValueByNameAndExtension(eq(enumDescriptor), any(JsonElement.class));

    Collection<EnumValueDescriptor> expectedEnumCollection = new ArrayList<>();
    expectedEnumCollection.add(enumValueDescriptor1);
    expectedEnumCollection.add(enumValueDescriptor2);

    when(builder.setField(fieldDescriptor, expectedEnumCollection)).thenReturn(builder);

    Message builtMessage = mock(Message.class);
    when(builder.build()).thenReturn(builtMessage);

    Message result = spyAdapter.deserialize(json, protoClass, context);

    assertSame(builtMessage, result);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withRepeatedField_deserializesArray() throws Exception {
    JsonObject jsonObject = mock(JsonObject.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonObject.get(anyString())).thenReturn(jsonElement);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    Class<? extends Message> protoClass = TestProtoMessage.class;

    Method newBuilderMethod = TestProtoMessage.class.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = TestProtoMessage.class.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = TestProtoMessage.class.getDeclaredMethod("getDescriptor");

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

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

    when(jsonObject.get("repeatedField")).thenReturn(jsonElement);

    // Setup protoClass to have declared field for repeated field with _ suffix
    Field protoArrayField =
        TestProtoMessage.class.getDeclaredField("repeatedField_");
    when(protoClass.getDeclaredField("repeatedField_")).thenReturn(protoArrayField);

    Type protoArrayFieldType = protoArrayField.getGenericType();

    when(context.deserialize(jsonElement, protoArrayFieldType)).thenReturn(Collections.singletonList(1));

    when(builder.setField(fieldDescriptor, Collections.singletonList(1))).thenReturn(builder);

    Message builtMessage = mock(Message.class);
    when(builder.build()).thenReturn(builtMessage);

    Message result = spyAdapter.deserialize(json, protoClass, context);

    assertSame(builtMessage, result);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_withNonRepeatedField_deserializesSingleValue() throws Exception {
    JsonObject jsonObject = mock(JsonObject.class);
    JsonElement jsonElement = mock(JsonElement.class);
    when(jsonElement.isJsonNull()).thenReturn(false);
    when(jsonObject.get(anyString())).thenReturn(jsonElement);

    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(jsonObject);

    Class<? extends Message> protoClass = TestProtoMessage.class;

    Method newBuilderMethod = TestProtoMessage.class.getDeclaredMethod("newBuilder");
    Method getDefaultInstanceMethod = TestProtoMessage.class.getDeclaredMethod("getDefaultInstance");
    Method getDescriptorMethod = TestProtoMessage.class.getDeclaredMethod("getDescriptor");

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);
    doReturn(newBuilderMethod).when(spyAdapter).getCachedMethod(protoClass, "newBuilder");
    doReturn(getDefaultInstanceMethod).when(spyAdapter).getCachedMethod(protoClass, "getDefaultInstance");
    doReturn(getDescriptorMethod).when(spyAdapter).getCachedMethod(protoClass, "getDescriptor");

    Builder builder = mock(Builder.class);
    when(newBuilderMethod.invoke(null)).thenReturn(builder);

    Message defaultInstance = mock(Message.class);
    when(getDefaultInstanceMethod.invoke(null)).thenReturn(defaultInstance);

    Descriptor descriptor = mock(Descriptor.class);
    when(getDescriptorMethod.invoke(null)).thenReturn(descriptor);

    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    when(descriptor.getFields()).thenReturn(Collections.singletonList(fieldDescriptor));

    when(fieldDescriptor.getName()).thenReturn("singleField");
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.INT32);
    when(fieldDescriptor.isRepeated()).thenReturn(false);

    when(jsonObject.get("singleField")).thenReturn(jsonElement);

    when(defaultInstance.getField(fieldDescriptor)).thenReturn(0);

    when(context.deserialize(jsonElement, Integer.class)).thenReturn(42);

    when(builder.setField(fieldDescriptor, 42)).thenReturn(builder);

    Message builtMessage = mock(Message.class);
    when(builder.build()).thenReturn(builtMessage);

    Message result = spyAdapter.deserialize(json, protoClass, context);

    assertSame(builtMessage, result);
  }

  @Test
    @Timeout(8000)
  public void testDeserialize_throwsJsonParseExceptionOnReflectionExceptions() throws Exception {
    JsonElement json = mock(JsonElement.class);
    when(json.getAsJsonObject()).thenReturn(new JsonObject());

    Class<? extends Message> protoClass = TestProtoMessage.class;

    ProtoTypeAdapter spyAdapter = Mockito.spy(protoTypeAdapter);

    doThrow(new NoSuchMethodException("test exception"))
        .when(spyAdapter)
        .getCachedMethod(protoClass, "newBuilder");

    JsonParseException ex =
        assertThrows(JsonParseException.class, () -> spyAdapter.deserialize(json, protoClass, context));

    assertTrue(ex.getCause() instanceof NoSuchMethodException);
  }

  // Dummy TestProtoMessage to simulate proto generated class
  public static class TestProtoMessage extends Message {

    public static Builder newBuilder() {
      return new TestBuilder();
    }

    public static TestProtoMessage getDefaultInstance() {
      return new TestProtoMessage();
    }

    public static Descriptor getDescriptor() {
      Descriptor descriptor = mock(Descriptor.class);
      return descriptor;
    }

    @Override
    public Builder newBuilderForType() {
      return newBuilder();
    }

    @Override
    public Builder toBuilder() {
      return newBuilder();
    }

    @Override
    public void writeTo(com.google.protobuf.CodedOutputStream output) {}

    @Override
    public int getSerializedSize() {
      return 0;
    }

    @Override
    public boolean isInitialized() {
      return true;
    }

    @Override
    public com.google.protobuf.Parser<? extends Message> getParserForType() {
      return null;
    }

    public static class TestBuilder extends Builder<TestBuilder> {

      @Override
      public TestProtoMessage build() {
        return new TestProtoMessage();
      }

      @Override
      public TestBuilder clone() {
        return this;
      }

      @Override
      public TestBuilder clear() {
        return this;
      }

      @Override
      public TestBuilder mergeFrom(Message other) {
        return this;
      }

      @Override
      public boolean isInitialized() {
        return true;
      }

      @Override
      public TestProtoMessage getDefaultInstanceForType() {
        return new TestProtoMessage();
      }
    }
  }
}