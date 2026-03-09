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
import com.google.gson.protobuf.ProtoTypeAdapter.Builder;
import org.junit.jupiter.api.Test;

class ProtoTypeAdapter_462_6Test {

  @Test
    @Timeout(8000)
  void testNewBuilder_DefaultValues() {
    Builder builder = ProtoTypeAdapter.newBuilder();
    assertNotNull(builder);

    // Using reflection to verify internal fields of Builder
    try {
      var protoFormatField = Builder.class.getDeclaredField("protoFormat");
      protoFormatField.setAccessible(true);
      var protoFormatValue = protoFormatField.get(builder);
      assertEquals(CaseFormat.LOWER_UNDERSCORE, protoFormatValue);

      var jsonFormatField = Builder.class.getDeclaredField("jsonFormat");
      jsonFormatField.setAccessible(true);
      var jsonFormatValue = jsonFormatField.get(builder);
      assertEquals(CaseFormat.LOWER_CAMEL, jsonFormatValue);

      var enumSerializationField = Builder.class.getDeclaredField("enumSerialization");
      enumSerializationField.setAccessible(true);
      var enumSerializationValue = enumSerializationField.get(builder);

      // Access EnumSerialization via ProtoTypeAdapter.EnumSerialization
      Class<?> protoTypeAdapterClass = Class.forName("com.google.gson.protobuf.ProtoTypeAdapter");
      Class<?> enumSerializationClass = null;
      for (Class<?> innerClass : protoTypeAdapterClass.getDeclaredClasses()) {
        if ("EnumSerialization".equals(innerClass.getSimpleName())) {
          enumSerializationClass = innerClass;
          break;
        }
      }
      assertNotNull(enumSerializationClass, "EnumSerialization class not found");

      Object enumSerializationName = enumSerializationClass.getField("NAME").get(null);
      assertEquals(enumSerializationName, enumSerializationValue);

    } catch (NoSuchFieldException | IllegalAccessException | ClassNotFoundException e) {
      fail("Reflection failed: " + e.getMessage());
    }
  }
}