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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Extension;
import com.google.common.base.CaseFormat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ProtoTypeAdapter_466_1Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private CaseFormat protoFormatMock;
  private CaseFormat jsonFormatMock;
  private Extension<FieldOptions, String> extensionMock1;
  private Extension<FieldOptions, String> extensionMock2;
  private FieldOptions fieldOptionsMock;

  @BeforeEach
  void setUp() throws Exception {
    protoFormatMock = mock(CaseFormat.class);
    jsonFormatMock = mock(CaseFormat.class);
    extensionMock1 = mock(Extension.class);
    extensionMock2 = mock(Extension.class);
    fieldOptionsMock = mock(FieldOptions.class);
    Set<Extension<FieldOptions, String>> serializedNameExtensions = new HashSet<>();
    serializedNameExtensions.add(extensionMock1);
    serializedNameExtensions.add(extensionMock2);

    // Use reflection to invoke the private constructor
    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    protoTypeAdapter = constructor.newInstance(
        null,
        protoFormatMock,
        jsonFormatMock,
        serializedNameExtensions,
        Collections.emptySet());
  }

  @SuppressWarnings("unchecked")
  private Set<Extension<FieldOptions, String>> getSerializedNameExtensions() throws Exception {
    Field field = ProtoTypeAdapter.class.getDeclaredField("serializedNameExtensions");
    field.setAccessible(true);
    return (Set<Extension<FieldOptions, String>>) field.get(protoTypeAdapter);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_extensionPresentReturnsExtensionValue() throws Exception {
    // Setup: first extension does not exist, second extension exists and returns "customName"
    when(fieldOptionsMock.hasExtension(extensionMock1)).thenReturn(false);
    when(fieldOptionsMock.hasExtension(extensionMock2)).thenReturn(true);
    when(fieldOptionsMock.getExtension(extensionMock2)).thenReturn("customName");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, fieldOptionsMock, "defaultName");

    assertEquals("customName", result);
    verify(fieldOptionsMock).hasExtension(extensionMock1);
    verify(fieldOptionsMock).hasExtension(extensionMock2);
    verify(fieldOptionsMock).getExtension(extensionMock2);
    verifyNoMoreInteractions(fieldOptionsMock);
    verifyNoInteractions(protoFormatMock, jsonFormatMock);
  }

  @Test
    @Timeout(8000)
  void testGetCustSerializedName_noExtensionReturnsConvertedName() throws Exception {
    Set<Extension<FieldOptions, String>> serializedNameExtensions = getSerializedNameExtensions();

    // Setup: no extensions present, protoFormat.to(jsonFormat, defaultName) returns "convertedName"
    for (Extension<FieldOptions, String> ext : serializedNameExtensions) {
      when(fieldOptionsMock.hasExtension(ext)).thenReturn(false);
    }
    when(protoFormatMock.to(jsonFormatMock, "defaultName")).thenReturn("convertedName");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, fieldOptionsMock, "defaultName");

    assertEquals("convertedName", result);

    for (Extension<FieldOptions, String> ext : serializedNameExtensions) {
      verify(fieldOptionsMock).hasExtension(ext);
    }
    verify(protoFormatMock).to(jsonFormatMock, "defaultName");
    verifyNoMoreInteractions(fieldOptionsMock, protoFormatMock, jsonFormatMock);
  }
}