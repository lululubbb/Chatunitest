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
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;

class ProtoTypeAdapterSerializeTest {

  private ProtoTypeAdapter protoTypeAdapter;
  private JsonSerializationContext context;

  @BeforeEach
  void setUp() throws Exception {
    // Use reflection to create Builder and set fields since enumSerialization() method does not exist
    Class<?> builderClass = Class.forName("com.google.gson.protobuf.ProtoTypeAdapter$Builder");
    Method newBuilderMethod = ProtoTypeAdapter.class.getDeclaredMethod("newBuilder");
    newBuilderMethod.setAccessible(true);
    Object builder = newBuilderMethod.invoke(null);

    // Use reflection to set fields on builder
    // The builder likely has public methods: protoFormat(), jsonFormat(), serializedNameExtensions(), serializedEnumValueExtensions(), build()
    // We call these with null or empty sets as needed.

    // protoFormat(null)
    Method protoFormatMethod = builderClass.getDeclaredMethod("protoFormat", CaseFormat.class);
    protoFormatMethod.setAccessible(true);
    protoFormatMethod.invoke(builder, (CaseFormat) null);

    // jsonFormat(null)
    Method jsonFormatMethod = builderClass.getDeclaredMethod("jsonFormat", CaseFormat.class);
    jsonFormatMethod.setAccessible(true);
    jsonFormatMethod.invoke(builder, (CaseFormat) null);

    // serializedNameExtensions(Collections.emptySet())
    Method serializedNameExtensionsMethod =
        builderClass.getDeclaredMethod("serializedNameExtensions", Set.class);
    serializedNameExtensionsMethod.setAccessible(true);
    serializedNameExtensionsMethod.invoke(builder, Collections.emptySet());

    // serializedEnumValueExtensions(Collections.emptySet())
    Method serializedEnumValueExtensionsMethod =
        builderClass.getDeclaredMethod("serializedEnumValueExtensions", Set.class);
    serializedEnumValueExtensionsMethod.setAccessible(true);
    serializedEnumValueExtensionsMethod.invoke(builder, Collections.emptySet());

    // build()
    Method buildMethod = builderClass.getDeclaredMethod("build");
    buildMethod.setAccessible(true);
    protoTypeAdapter = (ProtoTypeAdapter) buildMethod.invoke(builder);

    context = mock(JsonSerializationContext.class);
  }

  @Test
    @Timeout(8000)
  void testSerialize_emptyFields_returnsEmptyJsonObject() {
    Message src = mock(Message.class);
    when(src.getAllFields()).thenReturn(Collections.emptyMap());

    JsonElement result = protoTypeAdapter.serialize(src, (Type) Message.class, context);

    assertTrue(result.isJsonObject());
    assertEquals(0, result.getAsJsonObject().entrySet().size());
  }

  @Test
    @Timeout(8000)
  void testSerialize_nonEnumField_serializesValueDirectly() {
    Message src = mock(Message.class);
    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    Object fieldValue = "value";

    Map<FieldDescriptor, Object> fields = new HashMap<>();
    fields.put(fieldDescriptor, fieldValue);

    when(src.getAllFields()).thenReturn(fields);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.INT32);
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getName()).thenReturn("fieldName");

    when(context.serialize(fieldValue)).thenReturn(mock(JsonElement.class));

    JsonElement result = protoTypeAdapter.serialize(src, (Type) Message.class, context);

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("fieldName"));
    verify(context).serialize(fieldValue);
  }

  @Test
    @Timeout(8000)
  void testSerialize_enumField_singleEnumValue_serializesEnumValue() throws Exception {
    Message src = mock(Message.class);
    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    EnumValueDescriptor enumValueDescriptor = mock(EnumValueDescriptor.class);

    Map<FieldDescriptor, Object> fields = new HashMap<>();
    fields.put(fieldDescriptor, enumValueDescriptor);

    when(src.getAllFields()).thenReturn(fields);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.ENUM);
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getName()).thenReturn("enumField");

    // Spy protoTypeAdapter and override private getEnumValue using reflection and doAnswer
    ProtoTypeAdapter spyAdapter = spy(protoTypeAdapter);

    // Use reflection to get private getEnumValue method
    Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    getEnumValueMethod.setAccessible(true);

    Object enumValue = "enumValue";

    // Use doAnswer to intercept call to getEnumValue and return enumValue
    doAnswer(invocation -> {
      EnumValueDescriptor arg = invocation.getArgument(0);
      if (arg == enumValueDescriptor) {
        return enumValue;
      }
      return getEnumValueMethod.invoke(spyAdapter, arg);
    }).when(spyAdapter).serialize(any(), any(), any()); // We can't mock private method directly, so we intercept serialize call and replace getEnumValue by reflection

    // Instead of mocking getEnumValue directly, we create a subclass that overrides getEnumValue for the test
    ProtoTypeAdapter adapterWithOverride = new ProtoTypeAdapter(protoTypeAdapter.enumSerialization,
        protoTypeAdapter.protoFormat,
        protoTypeAdapter.jsonFormat,
        protoTypeAdapter.serializedNameExtensions,
        protoTypeAdapter.serializedEnumValueExtensions) {
      @Override
      protected Object getEnumValue(EnumValueDescriptor enumDesc) {
        if (enumDesc == enumValueDescriptor) {
          return enumValue;
        }
        return super.getEnumValue(enumDesc);
      }
    };

    JsonElement serializedEnum = mock(JsonElement.class);
    when(context.serialize(enumValue)).thenReturn(serializedEnum);

    JsonElement result = adapterWithOverride.serialize(src, (Type) Message.class, context);

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("enumField"));
    assertEquals(serializedEnum, jsonObject.get("enumField"));
    verify(context).serialize(enumValue);
  }

  @Test
    @Timeout(8000)
  void testSerialize_enumField_collectionEnumValue_serializesArray() throws Exception {
    Message src = mock(Message.class);
    FieldDescriptor fieldDescriptor = mock(FieldDescriptor.class);
    EnumValueDescriptor enumValueDescriptor1 = mock(EnumValueDescriptor.class);
    EnumValueDescriptor enumValueDescriptor2 = mock(EnumValueDescriptor.class);

    Collection<EnumValueDescriptor> enumCollection = Arrays.asList(enumValueDescriptor1, enumValueDescriptor2);

    Map<FieldDescriptor, Object> fields = new HashMap<>();
    fields.put(fieldDescriptor, enumCollection);

    when(src.getAllFields()).thenReturn(fields);
    when(fieldDescriptor.getType()).thenReturn(FieldDescriptor.Type.ENUM);
    when(fieldDescriptor.getOptions()).thenReturn(null);
    when(fieldDescriptor.getName()).thenReturn("enumCollectionField");

    Object enumValue1 = "enumValue1";
    Object enumValue2 = "enumValue2";

    // Create subclass to override getEnumValue
    ProtoTypeAdapter adapterWithOverride = new ProtoTypeAdapter(protoTypeAdapter.enumSerialization,
        protoTypeAdapter.protoFormat,
        protoTypeAdapter.jsonFormat,
        protoTypeAdapter.serializedNameExtensions,
        protoTypeAdapter.serializedEnumValueExtensions) {
      @Override
      protected Object getEnumValue(EnumValueDescriptor enumDesc) {
        if (enumDesc == enumValueDescriptor1) {
          return enumValue1;
        } else if (enumDesc == enumValueDescriptor2) {
          return enumValue2;
        }
        return super.getEnumValue(enumDesc);
      }
    };

    JsonElement serializedEnum1 = mock(JsonElement.class);
    JsonElement serializedEnum2 = mock(JsonElement.class);

    when(context.serialize(enumValue1)).thenReturn(serializedEnum1);
    when(context.serialize(enumValue2)).thenReturn(serializedEnum2);

    JsonElement result = adapterWithOverride.serialize(src, (Type) Message.class, context);

    JsonObject jsonObject = result.getAsJsonObject();
    assertTrue(jsonObject.has("enumCollectionField"));

    JsonElement element = jsonObject.get("enumCollectionField");
    assertTrue(element.isJsonArray());
    JsonArray jsonArray = element.getAsJsonArray();

    List<JsonElement> elements = new ArrayList<>();
    jsonArray.forEach(elements::add);

    assertEquals(2, elements.size());
    assertTrue(elements.contains(serializedEnum1));
    assertTrue(elements.contains(serializedEnum2));

    verify(context).serialize(enumValue1);
    verify(context).serialize(enumValue2);
  }
}