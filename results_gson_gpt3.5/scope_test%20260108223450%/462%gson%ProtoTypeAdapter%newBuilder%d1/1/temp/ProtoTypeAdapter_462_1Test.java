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

class ProtoTypeAdapter_462_1Test {

  @Test
    @Timeout(8000)
  void testNewBuilder() {
    ProtoTypeAdapter.Builder builder = ProtoTypeAdapter.newBuilder();
    assertNotNull(builder);
    // Check that the builder has the expected enumSerialization
    assertEquals(ProtoTypeAdapter.EnumSerialization.NAME, 
        getFieldValue(builder, "enumSerialization"));
    // Check that the builder has the expected protoFormat
    assertEquals(CaseFormat.LOWER_UNDERSCORE, 
        getFieldValue(builder, "protoFormat"));
    // Check that the builder has the expected jsonFormat
    assertEquals(CaseFormat.LOWER_CAMEL, 
        getFieldValue(builder, "jsonFormat"));
  }

  private static Object getFieldValue(Object obj, String fieldName) {
    try {
      java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      return field.get(obj);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
  }
}