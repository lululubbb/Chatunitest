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
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ProtoTypeAdapter_466_6Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private CaseFormat protoFormatMock;
  private CaseFormat jsonFormatMock;
  private Set<Extension<FieldOptions, String>> serializedNameExtensionsMock;

  @BeforeEach
  void setUp() throws Exception {
    protoFormatMock = mock(CaseFormat.class);
    jsonFormatMock = mock(CaseFormat.class);
    serializedNameExtensionsMock = new HashSet<>();

    // Use reflection to access the private constructor
    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);

    protoTypeAdapter = constructor.newInstance(
        null, // enumSerialization not needed for getCustSerializedName
        protoFormatMock,
        jsonFormatMock,
        serializedNameExtensionsMock,
        Collections.emptySet()
    );
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsExtensionValue_whenExtensionPresent() throws Exception {
    @SuppressWarnings("unchecked")
    Extension<FieldOptions, String> extensionMock = mock(Extension.class);
    serializedNameExtensionsMock.add(extensionMock);

    FieldOptions optionsMock = mock(FieldOptions.class);

    when(optionsMock.hasExtension(extensionMock)).thenReturn(true);
    when(optionsMock.getExtension(extensionMock)).thenReturn("customName");

    // Use reflection to invoke private method
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, optionsMock, "defaultName");

    assertEquals("customName", result);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsConvertedName_whenNoExtensionPresent() throws Exception {
    FieldOptions optionsMock = mock(FieldOptions.class);

    // No extensions added, so no extension present
    String defaultName = "defaultName";

    // Mock protoFormat.to(jsonFormat, defaultName) call
    when(protoFormatMock.to(jsonFormatMock, defaultName)).thenReturn("convertedName");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, optionsMock, defaultName);

    assertEquals("convertedName", result);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_skipsExtensionsWithoutExtension() throws Exception {
    @SuppressWarnings("unchecked")
    Extension<FieldOptions, String> extensionMock1 = mock(Extension.class);
    @SuppressWarnings("unchecked")
    Extension<FieldOptions, String> extensionMock2 = mock(Extension.class);
    serializedNameExtensionsMock.add(extensionMock1);
    serializedNameExtensionsMock.add(extensionMock2);

    FieldOptions optionsMock = mock(FieldOptions.class);

    when(optionsMock.hasExtension(extensionMock1)).thenReturn(false);
    when(optionsMock.hasExtension(extensionMock2)).thenReturn(false);

    String defaultName = "defaultName";

    when(protoFormatMock.to(jsonFormatMock, defaultName)).thenReturn("convertedName");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, optionsMock, defaultName);

    assertEquals("convertedName", result);

    verify(optionsMock, times(1)).hasExtension(extensionMock1);
    verify(optionsMock, times(1)).hasExtension(extensionMock2);
  }
}