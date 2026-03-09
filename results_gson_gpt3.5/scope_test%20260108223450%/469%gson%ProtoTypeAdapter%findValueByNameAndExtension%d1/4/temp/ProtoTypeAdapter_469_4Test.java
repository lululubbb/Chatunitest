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
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.FieldDescriptor;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.Extension;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Set;

public class ProtoTypeAdapter_469_4Test {

  private ProtoTypeAdapter protoTypeAdapterName;
  private ProtoTypeAdapter protoTypeAdapterValue;

  private EnumDescriptor enumDescriptorMock;
  private EnumValueDescriptor enumValueDescriptorMock1;
  private EnumValueDescriptor enumValueDescriptorMock2;

  private JsonElement jsonElementMock;

  private static Class<?> enumSerializationClass;

  @BeforeEach
  public void setUp() throws Exception {
    // Load EnumSerialization class by reflection
    enumSerializationClass = Class.forName("com.google.gson.protobuf.EnumSerialization");

    // Obtain enum constants NAME and VALUE
    Object enumSerializationName = Enum.valueOf((Class<Enum>) enumSerializationClass, "NAME");
    Object enumSerializationValue = Enum.valueOf((Class<Enum>) enumSerializationClass, "VALUE");

    // Use reflection to invoke the private constructor
    Constructor<ProtoTypeAdapter> constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
        enumSerializationClass,
        com.google.common.base.CaseFormat.class,
        com.google.common.base.CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);

    protoTypeAdapterName = constructor.newInstance(
        enumSerializationName,
        com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        Collections.emptySet());

    protoTypeAdapterValue = constructor.newInstance(
        enumSerializationValue,
        com.google.common.base.CaseFormat.LOWER_UNDERSCORE,
        com.google.common.base.CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        Collections.emptySet());

    // Mock EnumDescriptor and EnumValueDescriptors
    enumDescriptorMock = mock(EnumDescriptor.class);
    enumValueDescriptorMock1 = mock(EnumValueDescriptor.class);
    enumValueDescriptorMock2 = mock(EnumValueDescriptor.class);

    // Mock JsonElement
    jsonElementMock = mock(JsonElement.class);
  }

  @Test
    @Timeout(8000)
  public void testFindValueByNameAndExtension_enumSerializationName_matchFound() throws Exception {
    // Setup enumValueDescriptorMock1.getOptions() and getName()
    var optionsMock1 = mock(EnumValueOptions.class);
    var optionsMock2 = mock(EnumValueOptions.class);
    when(enumValueDescriptorMock1.getOptions()).thenReturn(optionsMock1);
    when(enumValueDescriptorMock1.getName()).thenReturn("ENUM_ONE");

    when(enumValueDescriptorMock2.getOptions()).thenReturn(optionsMock2);
    when(enumValueDescriptorMock2.getName()).thenReturn("ENUM_TWO");

    // Setup enumDescriptor.getValues()
    when(enumDescriptorMock.getValues())
        .thenReturn(java.util.List.of(enumValueDescriptorMock1, enumValueDescriptorMock2));

    when(jsonElementMock.getAsString()).thenReturn("custom_enum_one");

    // Spy protoTypeAdapterName
    var spyAdapter = org.mockito.Mockito.spy(protoTypeAdapterName);

    // Mock private method getCustSerializedEnumValue using reflection and doAnswer
    Method getCustSerializedEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod(
        "getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    getCustSerializedEnumValueMethod.setAccessible(true);

    doAnswer(invocation -> {
      EnumValueOptions options = invocation.getArgument(0);
      String defaultName = invocation.getArgument(1);
      if ("ENUM_ONE".equals(defaultName)) {
        return "custom_enum_one";
      } else if ("ENUM_TWO".equals(defaultName)) {
        return "custom_enum_two";
      }
      return defaultName;
    }).when(spyAdapter).getClass()
      .getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class)
      .invoke(any(), any(), any());

    // Because direct mocking private method with Mockito is not allowed, use doAnswer on spyAdapter like this:
    // Override spyAdapter.getCustSerializedEnumValue by reflection proxy:
    // Instead, use a small helper proxy class to override the method:
    // But here simpler approach: replace spyAdapter with a subclass that overrides getCustSerializedEnumValue

    // Create subclass to override private method via reflection
    ProtoTypeAdapter spyAdapterWithOverride = new ProtoTypeAdapterSubclass(spyAdapter);

    EnumValueDescriptor result = invokeFindValueByNameAndExtension(spyAdapterWithOverride, enumDescriptorMock, jsonElementMock);

    assertSame(enumValueDescriptorMock1, result);
  }

  @Test
    @Timeout(8000)
  public void testFindValueByNameAndExtension_enumSerializationName_noMatch_throws() throws Exception {
    when(enumDescriptorMock.getValues()).thenReturn(java.util.List.of());

    when(jsonElementMock.getAsString()).thenReturn("unknown_enum");

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      invokeFindValueByNameAndExtension(protoTypeAdapterName, enumDescriptorMock, jsonElementMock);
    });

    assertTrue(thrown.getMessage().contains("Unrecognized enum name"));
  }

  @Test
    @Timeout(8000)
  public void testFindValueByNameAndExtension_enumSerializationValue_matchFound() throws Exception {
    when(jsonElementMock.getAsInt()).thenReturn(1);

    when(enumDescriptorMock.findValueByNumber(1)).thenReturn(enumValueDescriptorMock1);

    EnumValueDescriptor result = invokeFindValueByNameAndExtension(protoTypeAdapterValue, enumDescriptorMock, jsonElementMock);

    assertSame(enumValueDescriptorMock1, result);
  }

  @Test
    @Timeout(8000)
  public void testFindValueByNameAndExtension_enumSerializationValue_noMatch_throws() throws Exception {
    when(jsonElementMock.getAsInt()).thenReturn(999);

    when(enumDescriptorMock.findValueByNumber(999)).thenReturn(null);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      invokeFindValueByNameAndExtension(protoTypeAdapterValue, enumDescriptorMock, jsonElementMock);
    });

    assertTrue(thrown.getMessage().contains("Unrecognized enum value"));
  }

  private EnumValueDescriptor invokeFindValueByNameAndExtension(ProtoTypeAdapter adapter,
                                                               EnumDescriptor enumDescriptor,
                                                               JsonElement jsonElement) throws Exception {
    Method method = ProtoTypeAdapter.class.getDeclaredMethod(
        "findValueByNameAndExtension", EnumDescriptor.class, JsonElement.class);
    method.setAccessible(true);
    try {
      return (EnumValueDescriptor) method.invoke(adapter, enumDescriptor, jsonElement);
    } catch (InvocationTargetException e) {
      // unwrap
      Throwable cause = e.getCause();
      if (cause instanceof RuntimeException) {
        throw (RuntimeException) cause;
      } else if (cause instanceof Exception) {
        throw (Exception) cause;
      } else {
        throw e;
      }
    }
  }

  // Subclass to override private getCustSerializedEnumValue method
  private static class ProtoTypeAdapterSubclass extends ProtoTypeAdapter {
    private final ProtoTypeAdapter delegate;

    ProtoTypeAdapterSubclass(ProtoTypeAdapter delegate) throws Exception {
      super(getEnumSerialization(delegate),
          getProtoFormat(delegate),
          getJsonFormat(delegate),
          getSerializedNameExtensions(delegate),
          getSerializedEnumValueExtensions(delegate));
      this.delegate = delegate;
    }

    @Override
    protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
      if ("ENUM_ONE".equals(defaultValue)) {
        return "custom_enum_one";
      } else if ("ENUM_TWO".equals(defaultValue)) {
        return "custom_enum_two";
      }
      return defaultValue;
    }

    // Helper methods to get private fields from delegate via reflection
    private static Object getEnumSerialization(ProtoTypeAdapter adapter) throws Exception {
      var f = ProtoTypeAdapter.class.getDeclaredField("enumSerialization");
      f.setAccessible(true);
      return f.get(adapter);
    }

    private static com.google.common.base.CaseFormat getProtoFormat(ProtoTypeAdapter adapter) throws Exception {
      var f = ProtoTypeAdapter.class.getDeclaredField("protoFormat");
      f.setAccessible(true);
      return (com.google.common.base.CaseFormat) f.get(adapter);
    }

    private static com.google.common.base.CaseFormat getJsonFormat(ProtoTypeAdapter adapter) throws Exception {
      var f = ProtoTypeAdapter.class.getDeclaredField("jsonFormat");
      f.setAccessible(true);
      return (com.google.common.base.CaseFormat) f.get(adapter);
    }

    private static Set<?> getSerializedNameExtensions(ProtoTypeAdapter adapter) throws Exception {
      var f = ProtoTypeAdapter.class.getDeclaredField("serializedNameExtensions");
      f.setAccessible(true);
      return (Set<?>) f.get(adapter);
    }

    private static Set<?> getSerializedEnumValueExtensions(ProtoTypeAdapter adapter) throws Exception {
      var f = ProtoTypeAdapter.class.getDeclaredField("serializedEnumValueExtensions");
      f.setAccessible(true);
      return (Set<?>) f.get(adapter);
    }
  }
}