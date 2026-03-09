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
import static org.mockito.Mockito.*;

import com.google.common.base.CaseFormat;
import org.junit.jupiter.api.Test;

class ProtoTypeAdapter_462_3Test {

  @Test
    @Timeout(8000)
  void testNewBuilder_DefaultValues() {
    ProtoTypeAdapter.Builder builder = ProtoTypeAdapter.newBuilder();
    assertNotNull(builder);

    // Using reflection to verify the internal fields of Builder
    try {
      var protoFormatField = ProtoTypeAdapter.Builder.class.getDeclaredField("protoFormat");
      var jsonFormatField = ProtoTypeAdapter.Builder.class.getDeclaredField("jsonFormat");
      var enumSerializationField = ProtoTypeAdapter.Builder.class.getDeclaredField("enumSerialization");
      protoFormatField.setAccessible(true);
      jsonFormatField.setAccessible(true);
      enumSerializationField.setAccessible(true);

      CaseFormat protoFormat = (CaseFormat) protoFormatField.get(builder);
      CaseFormat jsonFormat = (CaseFormat) jsonFormatField.get(builder);

      // Reflectively get EnumSerialization class from ProtoTypeAdapter.Builder
      Class<?> enumSerializationClass = enumSerializationField.getType();
      Object enumSerialization = enumSerializationField.get(builder);

      // Get the Enum constant NAME from EnumSerialization class
      Object enumSerializationName = enumSerializationClass.getField("NAME").get(null);

      assertEquals(CaseFormat.LOWER_UNDERSCORE, protoFormat);
      assertEquals(CaseFormat.LOWER_CAMEL, jsonFormat);
      assertEquals(enumSerializationName, enumSerialization);

    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}