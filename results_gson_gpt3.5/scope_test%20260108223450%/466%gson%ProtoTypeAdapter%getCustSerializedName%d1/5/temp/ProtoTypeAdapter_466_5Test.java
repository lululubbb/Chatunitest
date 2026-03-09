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
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

class ProtoTypeAdapter_466_5Test {

  private ProtoTypeAdapter protoTypeAdapter;
  private CaseFormat protoFormat;
  private CaseFormat jsonFormat;
  private Set<Extension<FieldOptions, String>> serializedNameExtensions;
  private Set<Extension<?, String>> emptyEnumExtensions = Collections.emptySet();

  @BeforeEach
  void setUp() throws Exception {
    protoFormat = CaseFormat.LOWER_UNDERSCORE;
    jsonFormat = CaseFormat.LOWER_CAMEL;
    serializedNameExtensions = new HashSet<>();
    // Use reflection to access the private constructor with correct parameter types
    Constructor<ProtoTypeAdapter> ctor = ProtoTypeAdapter.class.getDeclaredConstructor(
        Class.forName("com.google.gson.protobuf.EnumSerialization"),
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    ctor.setAccessible(true);
    // Pass null for EnumSerialization since it is not used in tested method
    protoTypeAdapter = ctor.newInstance(null, protoFormat, jsonFormat,
        serializedNameExtensions, emptyEnumExtensions);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsExtensionValue_whenExtensionPresent() throws Exception {
    @SuppressWarnings("unchecked")
    Extension<FieldOptions, String> extension = mock(Extension.class);
    serializedNameExtensions.add(extension);

    FieldOptions options = mock(FieldOptions.class);

    when(options.hasExtension(extension)).thenReturn(true);
    when(options.getExtension(extension)).thenReturn("customName");

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    String result = (String) method.invoke(protoTypeAdapter, options, "defaultName");

    assertEquals("customName", result);

    verify(options).hasExtension(extension);
    verify(options).getExtension(extension);
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsConvertedName_whenNoExtensionPresent() throws Exception {
    FieldOptions options = mock(FieldOptions.class);

    // Add one extension that returns false for hasExtension
    @SuppressWarnings("unchecked")
    Extension<FieldOptions, String> extension = mock(Extension.class);
    serializedNameExtensions.add(extension);

    when(options.hasExtension(extension)).thenReturn(false);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    // Mock CaseFormat instance method to() on protoFormat
    try (MockedStatic<CaseFormat> mockedCaseFormat = Mockito.mockStatic(CaseFormat.class, Mockito.CALLS_REAL_METHODS)) {
      // Since protoFormat.to() is an instance method, mock the protoFormat instance itself
      CaseFormat protoFormatSpy = Mockito.spy(protoFormat);
      // Replace protoFormat field in protoTypeAdapter with spy
      java.lang.reflect.Field protoFormatField = ProtoTypeAdapter.class.getDeclaredField("protoFormat");
      protoFormatField.setAccessible(true);
      protoFormatField.set(protoTypeAdapter, protoFormatSpy);

      when(protoFormatSpy.to(jsonFormat, "defaultName")).thenReturn("defaultNameConverted");

      String result = (String) method.invoke(protoTypeAdapter, options, "defaultName");

      assertEquals("defaultNameConverted", result);

      verify(options).hasExtension(extension);
      verify(protoFormatSpy).to(jsonFormat, "defaultName");
    }
  }

  @Test
    @Timeout(8000)
  void getCustSerializedName_returnsConvertedName_whenNoExtensionsConfigured() throws Exception {
    // Create adapter with empty extensions using reflection
    Constructor<ProtoTypeAdapter> ctor = ProtoTypeAdapter.class.getDeclaredConstructor(
        Class.forName("com.google.gson.protobuf.EnumSerialization"),
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    ctor.setAccessible(true);
    protoTypeAdapter = ctor.newInstance(null, protoFormat, jsonFormat,
        Collections.emptySet(), Collections.emptySet());

    FieldOptions options = mock(FieldOptions.class);

    Method method = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedName", FieldOptions.class, String.class);
    method.setAccessible(true);

    try (MockedStatic<CaseFormat> mockedCaseFormat = Mockito.mockStatic(CaseFormat.class, Mockito.CALLS_REAL_METHODS)) {
      CaseFormat protoFormatSpy = Mockito.spy(protoFormat);
      java.lang.reflect.Field protoFormatField = ProtoTypeAdapter.class.getDeclaredField("protoFormat");
      protoFormatField.setAccessible(true);
      protoFormatField.set(protoTypeAdapter, protoFormatSpy);

      when(protoFormatSpy.to(jsonFormat, "defaultName")).thenReturn("defaultNameConverted");

      String result = (String) method.invoke(protoTypeAdapter, options, "defaultName");

      assertEquals("defaultNameConverted", result);

      verify(protoFormatSpy).to(jsonFormat, "defaultName");
    }
  }
}