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

class ProtoTypeAdapter_462_4Test {

  @Test
    @Timeout(8000)
  void testNewBuilder_DefaultValues() {
    ProtoTypeAdapter.Builder builder = ProtoTypeAdapter.newBuilder();
    assertNotNull(builder);

    // Use reflection to verify the fields inside the Builder instance
    try {
      var protoFormatField = ProtoTypeAdapter.Builder.class.getDeclaredField("protoFormat");
      protoFormatField.setAccessible(true);
      Object protoFormatValue = protoFormatField.get(builder);
      assertEquals(CaseFormat.LOWER_UNDERSCORE, protoFormatValue);

      var jsonFormatField = ProtoTypeAdapter.Builder.class.getDeclaredField("jsonFormat");
      jsonFormatField.setAccessible(true);
      Object jsonFormatValue = jsonFormatField.get(builder);
      assertEquals(CaseFormat.LOWER_CAMEL, jsonFormatValue);

      var enumSerializationField = ProtoTypeAdapter.Builder.class.getDeclaredField("enumSerialization");
      enumSerializationField.setAccessible(true);
      Object enumSerializationValue = enumSerializationField.get(builder);
      assertEquals(ProtoTypeAdapter.EnumSerialization.NAME, enumSerializationValue);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}