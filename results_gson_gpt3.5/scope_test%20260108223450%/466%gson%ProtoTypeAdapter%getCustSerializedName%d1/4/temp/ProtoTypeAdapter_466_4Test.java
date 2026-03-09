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
import java.lang.reflect.Method;
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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ProtoTypeAdapter_466_4Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private CaseFormat protoFormatMock;
  private CaseFormat jsonFormatMock;
  private Extension<FieldOptions, String> extensionMock;
  private FieldOptions fieldOptionsMock;

  @BeforeEach
  void setUp() throws Exception {
    protoFormatMock = mock(CaseFormat.class);
    jsonFormatMock = mock(CaseFormat.class);
    extensionMock = mock(Extension.class);
    fieldOptionsMock = mock(FieldOptions.class);

    Set<Extension<FieldOptions, String>> serializedNameExtensions = new HashSet<>();
    serializedNameExtensions.add(extensionMock);

    // Other sets can be empty for this test
    Set serializedEnumValueExtensions = Collections.emptySet();

    // Use reflection to invoke private constructor
    var constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class
    );
    constructor.setAccessible(true);
    protoTypeAdapter = (ProtoTypeAdapter) constructor.newInstance(
        null,
        protoFormatMock,
        jsonFormatMock,
        serializedNameExtensions,
        serializedEnumValueExtensions
    );
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsExtensionValue_whenExtensionPresent() throws Exception {
    // Arrange
    when(fieldOptionsMock.hasExtension(extensionMock)).thenReturn(true);
    when(fieldOptionsMock.getExtension(extensionMock)).thenReturn("extName");

    // Use reflection to invoke private method
    var method = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCustSerializedName",
        FieldOptions.class,
        String.class
    );
    method.setAccessible(true);

    // Act
    String result = (String) method.invoke(protoTypeAdapter, fieldOptionsMock, "defaultName");

    // Assert
    assertEquals("extName", result);
    verify(fieldOptionsMock).hasExtension(extensionMock);
    verify(fieldOptionsMock).getExtension(extensionMock);
    verifyNoMoreInteractions(fieldOptionsMock);
    verifyNoInteractions(protoFormatMock, jsonFormatMock);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsProtoFormatConversion_whenNoExtensionPresent() throws Exception {
    // Arrange
    when(fieldOptionsMock.hasExtension(extensionMock)).thenReturn(false);
    when(protoFormatMock.to(jsonFormatMock, "defaultName")).thenReturn("convertedName");

    // Use reflection to invoke private method
    var method = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCustSerializedName",
        FieldOptions.class,
        String.class
    );
    method.setAccessible(true);

    // Act
    String result = (String) method.invoke(protoTypeAdapter, fieldOptionsMock, "defaultName");

    // Assert
    assertEquals("convertedName", result);
    verify(fieldOptionsMock).hasExtension(extensionMock);
    verify(protoFormatMock).to(jsonFormatMock, "defaultName");
    verifyNoMoreInteractions(fieldOptionsMock, protoFormatMock);
    verifyNoInteractions(jsonFormatMock);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsProtoFormatConversion_whenNoExtensions() throws Exception {
    // Arrange
    // Create adapter with empty serializedNameExtensions set
    var constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class
    );
    constructor.setAccessible(true);
    ProtoTypeAdapter adapterWithoutExtensions = (ProtoTypeAdapter) constructor.newInstance(
        null,
        protoFormatMock,
        jsonFormatMock,
        Collections.emptySet(),
        Collections.emptySet()
    );
    when(protoFormatMock.to(jsonFormatMock, "defaultName")).thenReturn("convertedName");

    var method = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCustSerializedName",
        FieldOptions.class,
        String.class
    );
    method.setAccessible(true);

    // Act
    String result = (String) method.invoke(adapterWithoutExtensions, fieldOptionsMock, "defaultName");

    // Assert
    assertEquals("convertedName", result);
    verify(protoFormatMock).to(jsonFormatMock, "defaultName");
    verifyNoInteractions(fieldOptionsMock);
  }
}