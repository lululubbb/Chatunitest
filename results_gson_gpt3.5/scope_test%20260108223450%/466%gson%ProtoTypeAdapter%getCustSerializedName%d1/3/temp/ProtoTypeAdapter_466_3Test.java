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
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Extension;
import com.google.common.base.CaseFormat;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProtoTypeAdapter_466_3Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private CaseFormat protoFormat;
  private CaseFormat jsonFormat;
  private ProtoTypeAdapter.EnumSerialization enumSerialization;
  private Set<Extension<FieldOptions, String>> serializedNameExtensions;

  @BeforeEach
  void setUp() throws Exception {
    enumSerialization = mock(ProtoTypeAdapter.EnumSerialization.class);
    protoFormat = mock(CaseFormat.class);
    jsonFormat = mock(CaseFormat.class);
    serializedNameExtensions = Collections.emptySet();

    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    protoTypeAdapter = constructor.newInstance(enumSerialization, protoFormat, jsonFormat,
        serializedNameExtensions, Collections.emptySet());
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsExtensionValue_whenExtensionPresent() throws Exception {
    @SuppressWarnings("unchecked")
    Extension<FieldOptions, String> extension = mock(Extension.class);
    serializedNameExtensions = Collections.singleton(extension);

    FieldOptions options = mock(FieldOptions.class);
    when(options.hasExtension(extension)).thenReturn(true);
    when(options.getExtension(extension)).thenReturn("customName");

    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    protoTypeAdapter = constructor.newInstance(enumSerialization, protoFormat, jsonFormat,
        serializedNameExtensions, Collections.emptySet());

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, "defaultName");

    assertEquals("customName", result);

    verify(options).hasExtension(extension);
    verify(options).getExtension(extension);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsFormattedName_whenNoExtensionPresent() throws Exception {
    FieldOptions options = mock(FieldOptions.class);

    // Setup serializedNameExtensions empty to simulate no extension present
    serializedNameExtensions = Collections.emptySet();

    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    protoTypeAdapter = constructor.newInstance(enumSerialization, protoFormat, jsonFormat,
        serializedNameExtensions, Collections.emptySet());

    when(protoFormat.to(jsonFormat, "defaultName")).thenReturn("formattedName");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, "defaultName");

    assertEquals("formattedName", result);

    verify(protoFormat).to(jsonFormat, "defaultName");
  }
}