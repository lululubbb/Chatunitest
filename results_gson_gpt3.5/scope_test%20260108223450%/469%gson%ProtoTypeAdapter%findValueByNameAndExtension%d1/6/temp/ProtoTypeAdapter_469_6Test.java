package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.base.CaseFormat;
import com.google.common.collect.MapMaker;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Message;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.JsonElement;
import com.google.protobuf.Descriptors.EnumDescriptor;
import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import com.google.protobuf.Extension;
import com.google.protobuf.DescriptorProtos.FieldOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public class ProtoTypeAdapter_469_6Test {

  private ProtoTypeAdapter protoTypeAdapterName;
  private ProtoTypeAdapter protoTypeAdapterValue;

  private EnumDescriptor enumDescriptorMock;
  private EnumValueDescriptor enumValueDescriptorMock1;
  private EnumValueDescriptor enumValueDescriptorMock2;
  private JsonElement jsonElementMock;

  private Set<Extension<FieldOptions, String>> serializedNameExtensions = Collections.emptySet();
  private Set<Extension<com.google.protobuf.DescriptorProtos.EnumValueOptions, String>> serializedEnumValueExtensions = Collections.emptySet();

  @BeforeEach
  public void setUp() throws Exception {
    // Create instances of ProtoTypeAdapter with different EnumSerialization values
    protoTypeAdapterName = createProtoTypeAdapter(getEnumSerializationByName("NAME"));
    protoTypeAdapterValue = createProtoTypeAdapter(getEnumSerializationByName("VALUE"));

    enumDescriptorMock = mock(EnumDescriptor.class);
    enumValueDescriptorMock1 = mock(EnumValueDescriptor.class);
    enumValueDescriptorMock2 = mock(EnumValueDescriptor.class);
    jsonElementMock = mock(JsonElement.class);
  }

  private ProtoTypeAdapter.EnumSerialization getEnumSerializationByName(String name) throws Exception {
    Class<?> enumClass = Class.forName("com.google.gson.protobuf.ProtoTypeAdapter$EnumSerialization");
    Object[] enumConstants = (Object[]) enumClass.getMethod("values").invoke(null);
    for (Object enumConstant : enumConstants) {
      if (enumConstant.toString().equals(name)) {
        return (ProtoTypeAdapter.EnumSerialization) enumConstant;
      }
    }
    throw new IllegalArgumentException("EnumSerialization value not found: " + name);
  }

  private ProtoTypeAdapter createProtoTypeAdapter(ProtoTypeAdapter.EnumSerialization enumSerialization) throws Exception {
    Class<?> clazz = ProtoTypeAdapter.class;
    java.lang.reflect.Constructor<?> constructor = clazz.getDeclaredConstructor(
        ProtoTypeAdapter.EnumSerialization.class,
        com.google.common.base.CaseFormat.class,
        com.google.common.base.CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);
    return (ProtoTypeAdapter) constructor.newInstance(
        enumSerialization,
        com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        serializedNameExtensions,
        serializedEnumValueExtensions);
  }

  @Test
    @Timeout(8000)
  public void testFindValueByNameAndExtension_enumSerializationName_matchFound() throws Throwable {
    // Setup enumValueDescriptorMock1 to return name and options
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");
    EnumValueOptions optionsMock1 = mock(EnumValueOptions.class);
    when(enumValueDescriptorMock1.getOptions()).thenReturn(optionsMock1);

    when(enumValueDescriptorMock2.getName()).thenReturn("ENUM_TWO");
    EnumValueOptions optionsMock2 = mock(EnumValueOptions.class);
    when(enumValueDescriptorMock2.getOptions()).thenReturn(optionsMock2);

    // Setup enumDescriptorMock.getValues() to return two enum value descriptors
    when(enumDescriptorMock.getValues()).thenReturn(java.util.Arrays.asList(enumValueDescriptorMock1, enumValueDescriptorMock2));

    // Setup jsonElementMock to return "ENUM_ONE"
    when(jsonElementMock.getAsString()).thenReturn("ENUM_ONE");

    // Create a subclass to override the private method getCustSerializedEnumValue via reflection
    ProtoTypeAdapter testAdapter = createProtoTypeAdapter(getEnumSerializationByName("NAME"));

    ProtoTypeAdapter spyAdapter = spy(testAdapter);

    // Use reflection to mock private method getCustSerializedEnumValue
    Method getCustSerializedEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    getCustSerializedEnumValueMethod.setAccessible(true);

    doAnswer((Answer<String>) invocation -> {
      EnumValueOptions options = invocation.getArgument(0);
      String defaultValue = invocation.getArgument(1);
      if (options == optionsMock1) {
        return "ENUM_ONE";
      } else if (options == optionsMock2) {
        return "ENUM_TWO";
      }
      return defaultValue;
    }).when(spyAdapter).getClass()
        .getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class)
        .invoke(spyAdapter, any(EnumValueOptions.class), anyString());

    // The above will not work directly; instead, use Mockito's doAnswer combined with reflection invoke

    // Correct way to mock private method with Mockito and reflection:
    // Use Mockito's doAnswer with Mockito's spy and reflection invoke as follows:

    // Instead of above, do:
    doAnswer((Answer<String>) invocation -> {
      EnumValueOptions options = invocation.getArgument(0);
      String defaultValue = invocation.getArgument(1);
      if (options == optionsMock1) {
        return "ENUM_ONE";
      } else if (options == optionsMock2) {
        return "ENUM_TWO";
      }
      return defaultValue;
    }).when(spyAdapter, "getCustSerializedEnumValue", any(EnumValueOptions.class), anyString());

    // But this is the original error, so we replace with this approach:

    // Use reflection to replace the private method for the spy using org.mockito.internal.util.reflection.Whitebox
    // Since we cannot do that, we use a helper method to invoke the method instead of mocking

    // So we create a subclass that overrides getCustSerializedEnumValue by reflection:

    ProtoTypeAdapter adapterWithOverride = new ProtoTypeAdapter(getEnumSerializationByName("NAME"),
        com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        serializedNameExtensions,
        serializedEnumValueExtensions) {
      @Override
      @SuppressWarnings("unchecked")
      protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
        if (options == optionsMock1) {
          return "ENUM_ONE";
        } else if (options == optionsMock2) {
          return "ENUM_TWO";
        }
        return defaultValue;
      }
    };

    EnumValueDescriptor result = invokeFindValueByNameAndExtension(adapterWithOverride, enumDescriptorMock, jsonElementMock);

    assertSame(enumValueDescriptorMock1, result);
  }

  @Test
    @Timeout(8000)
  public void testFindValueByNameAndExtension_enumSerializationName_noMatch_throws() throws Throwable {
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");
    EnumValueOptions optionsMock1 = mock(EnumValueOptions.class);
    when(enumValueDescriptorMock1.getOptions()).thenReturn(optionsMock1);

    when(enumDescriptorMock.getValues()).thenReturn(java.util.Collections.singletonList(enumValueDescriptorMock1));

    when(jsonElementMock.getAsString()).thenReturn("UNKNOWN_ENUM");

    ProtoTypeAdapter adapterWithOverride = new ProtoTypeAdapter(getEnumSerializationByName("NAME"),
        com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        serializedNameExtensions,
        serializedEnumValueExtensions) {
      @Override
      @SuppressWarnings("unchecked")
      protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
        if (options == optionsMock1) {
          return "ENUM_ONE";
        }
        return defaultValue;
      }
    };

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> invokeFindValueByNameAndExtension(adapterWithOverride, enumDescriptorMock, jsonElementMock));

    assertTrue(thrown.getMessage().contains("Unrecognized enum name"));
    assertTrue(thrown.getMessage().contains("UNKNOWN_ENUM"));
  }

  @Test
    @Timeout(8000)
  public void testFindValueByNameAndExtension_enumSerializationValue_matchFound() throws Throwable {
    when(jsonElementMock.getAsInt()).thenReturn(5);
    when(enumDescriptorMock.findValueByNumber(5)).thenReturn(enumValueDescriptorMock1);

    EnumValueDescriptor result = invokeFindValueByNameAndExtension(protoTypeAdapterValue, enumDescriptorMock, jsonElementMock);

    assertSame(enumValueDescriptorMock1, result);
  }

  @Test
    @Timeout(8000)
  public void testFindValueByNameAndExtension_enumSerializationValue_noMatch_throws() throws Throwable {
    when(jsonElementMock.getAsInt()).thenReturn(10);
    when(enumDescriptorMock.findValueByNumber(10)).thenReturn(null);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class,
        () -> invokeFindValueByNameAndExtension(protoTypeAdapterValue, enumDescriptorMock, jsonElementMock));

    assertTrue(thrown.getMessage().contains("Unrecognized enum value"));
    assertTrue(thrown.getMessage().contains("10"));
  }

  private EnumValueDescriptor invokeFindValueByNameAndExtension(ProtoTypeAdapter adapter,
      EnumDescriptor desc,
      JsonElement jsonElement) throws Throwable {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod("findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);
    try {
      return (EnumValueDescriptor) method.invoke(adapter, desc, jsonElement);
    } catch (InvocationTargetException e) {
      throw e.getCause();
    }
  }
}