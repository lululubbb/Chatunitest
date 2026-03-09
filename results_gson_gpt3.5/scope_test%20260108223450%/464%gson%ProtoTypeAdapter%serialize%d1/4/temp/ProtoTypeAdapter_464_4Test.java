package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.base.CaseFormat;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializer;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Extension;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

class ProtoTypeAdapterSerializeTest {

  private ProtoTypeAdapter adapter;
  private JsonSerializationContext context;

  @BeforeEach
  void setUp() {
    // Create an instance of ProtoTypeAdapter using reflection because constructor is private
    try {
      Class<?> clazz = ProtoTypeAdapter.class;
      Field enumSerializationField = clazz.getDeclaredField("enumSerialization");
      Field protoFormatField = clazz.getDeclaredField("protoFormat");
      Field jsonFormatField = clazz.getDeclaredField("jsonFormat");
      Field serializedNameExtensionsField = clazz.getDeclaredField("serializedNameExtensions");
      Field serializedEnumValueExtensionsField = clazz.getDeclaredField("serializedEnumValueExtensions");

      enumSerializationField.setAccessible(true);
      protoFormatField.setAccessible(true);
      jsonFormatField.setAccessible(true);
      serializedNameExtensionsField.setAccessible(true);
      serializedEnumValueExtensionsField.setAccessible(true);

      // Use newBuilder() to create builder and build adapter, but newBuilder() signature is unknown,
      // so instantiate via reflection with dummy values for fields.
      // Instead, use reflection to call private constructor.

      // Prepare dummy sets
      Set dummyFieldOptionsExtensions = Collections.emptySet();
      Set dummyEnumValueOptionsExtensions = Collections.emptySet();

      // EnumSerialization and CaseFormat are likely enums or classes, use null or mocks
      Object enumSerializationMock = null;
      Object protoFormatMock = null;
      Object jsonFormatMock = null;

      // Find constructor with matching parameters
      java.lang.reflect.Constructor<?> ctor = clazz.getDeclaredConstructor(
          EnumSerialization.class,
          com.google.common.base.CaseFormat.class,
          com.google.common.base.CaseFormat.class,
          Set.class,
          Set.class);
      ctor.setAccessible(true);

      adapter = (ProtoTypeAdapter) ctor.newInstance(
          enumSerializationMock,
          protoFormatMock,
          jsonFormatMock,
          dummyFieldOptionsExtensions,
          dummyEnumValueOptionsExtensions);

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    context = mock(JsonSerializationContext.class);
  }

  @Test
    @Timeout(8000)
  void testSerialize_withEmptyFields_returnsEmptyJsonObject() {
    Message src = mock(Message.class);
    when(src.getAllFields()).thenReturn(Collections.emptyMap());

    JsonElement result = adapter.serialize(src, (Type) null, context);

    assertTrue(result.isJsonObject());
    assertEquals(0, result.getAsJsonObject().entrySet().size());
  }

  @Test
    @Timeout(8000)
  void testSerialize_withNonEnumField_serializesFieldValue() {
    Message src = mock(Message.class);
    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.INT32);
    when(fieldDescriptor.getName()).thenReturn("fieldName");
    // Mock getOptions to return empty FieldOptions
    try {
      Method getOptionsMethod = FieldDescriptor.class.getDeclaredMethod("getOptions");
      getOptionsMethod.setAccessible(true);
      when(fieldDescriptor.getOptions()).thenReturn(null);
    } catch (NoSuchMethodException ignored) {
    }
    Map<FieldDescriptor, Object> fields = new HashMap<>();
    fields.put(fieldDescriptor, 123);
    when(src.getAllFields()).thenReturn(fields);

    // Mock getCustSerializedName to return original field name
    try {
      Method getCustSerializedName = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", 
          com.google.protobuf.DescriptorProtos.FieldOptions.class, String.class);
      getCustSerializedName.setAccessible(true);
      ProtoTypeAdapter spyAdapter = spy(adapter);
      doReturn("fieldName").when(spyAdapter).getCustSerializedName(null, "fieldName");

      when(context.serialize(123)).thenReturn(mock(JsonElement.class));

      JsonElement result = spyAdapter.serialize(src, (Type) null, context);

      assertTrue(result.isJsonObject());
      JsonObject obj = result.getAsJsonObject();
      assertTrue(obj.has("fieldName"));
      verify(context).serialize(123);
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
    @Timeout(8000)
  void testSerialize_withEnumField_serializesEnumValue() {
    Message src = mock(Message.class);
    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.ENUM);
    when(fieldDescriptor.getName()).thenReturn("enumField");
    // Mock getOptions to return null
    try {
      when(fieldDescriptor.getOptions()).thenReturn(null);
    } catch (Exception ignored) {
    }

    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);

    Map<FieldDescriptor, Object> fields = new HashMap<>();
    fields.put(fieldDescriptor, enumValueDescriptor);
    when(src.getAllFields()).thenReturn(fields);

    try {
      ProtoTypeAdapter spyAdapter = spy(adapter);

      // Stub getCustSerializedName to return field name
      doReturn("enumField").when(spyAdapter).getCustSerializedName(null, "enumField");

      // Stub getEnumValue to return a dummy enum value
      doReturn("ENUM_VALUE").when(spyAdapter).getEnumValue(enumValueDescriptor);

      JsonElement serializedEnumValue = mock(JsonElement.class);
      when(context.serialize("ENUM_VALUE")).thenReturn(serializedEnumValue);

      JsonElement result = spyAdapter.serialize(src, (Type) null, context);

      assertTrue(result.isJsonObject());
      JsonObject obj = result.getAsJsonObject();
      assertTrue(obj.has("enumField"));
      assertEquals(serializedEnumValue, obj.get("enumField"));
      verify(context).serialize("ENUM_VALUE");
    } catch (Exception e) {
      fail(e);
    }
  }

  @Test
    @Timeout(8000)
  void testSerialize_withEnumCollectionField_serializesEnumValuesArray() {
    Message src = mock(Message.class);
    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.ENUM);
    when(fieldDescriptor.getName()).thenReturn("enumCollectionField");
    try {
      when(fieldDescriptor.getOptions()).thenReturn(null);
    } catch (Exception ignored) {
    }

    EnumValueDescriptor enumValueDescriptor1 = mock(EnumValueDescriptor.class);
    EnumValueDescriptor enumValueDescriptor2 = mock(EnumValueDescriptor.class);

    Collection<EnumValueDescriptor> enumCollection = Arrays.asList(enumValueDescriptor1, enumValueDescriptor2);

    Map<FieldDescriptor, Object> fields = new HashMap<>();
    fields.put(fieldDescriptor, enumCollection);
    when(src.getAllFields()).thenReturn(fields);

    try {
      ProtoTypeAdapter spyAdapter = spy(adapter);

      doReturn("enumCollectionField").when(spyAdapter).getCustSerializedName(null, "enumCollectionField");

      doReturn("ENUM1").when(spyAdapter).getEnumValue(enumValueDescriptor1);
      doReturn("ENUM2").when(spyAdapter).getEnumValue(enumValueDescriptor2);

      JsonElement serializedEnum1 = mock(JsonElement.class);
      JsonElement serializedEnum2 = mock(JsonElement.class);

      when(context.serialize("ENUM1")).thenReturn(serializedEnum1);
      when(context.serialize("ENUM2")).thenReturn(serializedEnum2);

      JsonElement result = spyAdapter.serialize(src, (Type) null, context);

      assertTrue(result.isJsonObject());
      JsonObject obj = result.getAsJsonObject();
      assertTrue(obj.has("enumCollectionField"));
      JsonElement jsonElement = obj.get("enumCollectionField");
      assertTrue(jsonElement.isJsonArray());
      JsonArray array = jsonElement.getAsJsonArray();
      assertEquals(2, array.size());
      assertEquals(serializedEnum1, array.get(0));
      assertEquals(serializedEnum2, array.get(1));

      verify(context).serialize("ENUM1");
      verify(context).serialize("ENUM2");
    } catch (Exception e) {
      fail(e);
    }
  }
}