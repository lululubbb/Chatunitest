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

class ProtoTypeAdapter_466_2Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private CaseFormat protoFormatMock;
  private CaseFormat jsonFormatMock;
  private Set<Extension<FieldOptions, String>> serializedNameExtensions;

  @BeforeEach
  void setUp() throws Exception {
    protoFormatMock = mock(CaseFormat.class);
    jsonFormatMock = mock(CaseFormat.class);
    serializedNameExtensions = new HashSet<>();

    // Use reflection to access the private constructor with correct parameter types
    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        EnumSerialization.class,
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
        Collections.emptySet()
    );
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsExtensionValue_whenExtensionPresent() throws Exception {
    // Arrange
    @SuppressWarnings("unchecked")
    Extension<FieldOptions, String> extension = mock(Extension.class);
    serializedNameExtensions.add(extension);

    FieldOptions optionsMock = mock(FieldOptions.class);
    when(optionsMock.hasExtension(extension)).thenReturn(true);
    when(optionsMock.getExtension(extension)).thenReturn("extensionValue");

    // Use reflection to access private method
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    // Act
    String result = (String) method.invoke(protoTypeAdapter, optionsMock, "defaultName");

    // Assert
    assertEquals("extensionValue", result);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsFormattedName_whenNoExtensionPresent() throws Exception {
    // Arrange
    FieldOptions optionsMock = mock(FieldOptions.class);

    // Clear extensions to test no extension case
    serializedNameExtensions.clear();

    String defaultName = "default_name";

    when(protoFormatMock.to(jsonFormatMock, defaultName)).thenReturn("formattedName");

    // Use reflection to access private method
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    // Act
    String result = (String) method.invoke(protoTypeAdapter, optionsMock, defaultName);

    // Assert
    assertEquals("formattedName", result);
  }
}