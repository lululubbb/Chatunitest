package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.google.common.base.CaseFormat;

class ProtoTypeAdapter_462_2Test {

  @Test
    @Timeout(8000)
  void newBuilder_shouldReturnBuilderWithExpectedDefaults() throws Exception {
    // Act
    ProtoTypeAdapter.Builder builder = ProtoTypeAdapter.newBuilder();

    // Assert
    assertNotNull(builder);

    // Using reflection to verify private fields inside Builder if any
    // Since Builder class details are not given, we verify the enumSerialization and formats via reflection if accessible

    // Check enumSerialization field
    var enumSerializationField = builder.getClass().getDeclaredField("enumSerialization");
    enumSerializationField.setAccessible(true);
    Object enumSerializationValue = enumSerializationField.get(builder);
    assertEquals(ProtoTypeAdapter.EnumSerialization.NAME, enumSerializationValue);

    // Check protoFormat field
    var protoFormatField = builder.getClass().getDeclaredField("protoFormat");
    protoFormatField.setAccessible(true);
    Object protoFormatValue = protoFormatField.get(builder);
    assertEquals(CaseFormat.LOWER_UNDERSCORE, protoFormatValue);

    // Check jsonFormat field
    var jsonFormatField = builder.getClass().getDeclaredField("jsonFormat");
    jsonFormatField.setAccessible(true);
    Object jsonFormatValue = jsonFormatField.get(builder);
    assertEquals(CaseFormat.LOWER_CAMEL, jsonFormatValue);
  }
}