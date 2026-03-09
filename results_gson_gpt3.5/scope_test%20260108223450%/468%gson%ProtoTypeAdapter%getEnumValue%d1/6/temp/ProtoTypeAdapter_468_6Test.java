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
import com.google.protobuf.DescriptorProtos.FieldOptions;
import com.google.protobuf.Descriptors.Descriptor;
import com.google.protobuf.Descriptors.EnumDescriptor;
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

import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.base.CaseFormat;
import com.google.protobuf.Extension;

class ProtoTypeAdapter_468_6Test {

  private ProtoTypeAdapter protoTypeAdapterName;
  private ProtoTypeAdapter protoTypeAdapterNumber;

  private EnumValueDescriptor enumValueDescriptorMock;
  private EnumValueOptions enumValueOptionsMock;

  private Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions;

  // Use the EnumSerialization from ProtoTypeAdapter class via reflection
  private Object enumSerializationName;
  private Object enumSerializationNumber;

  @BeforeEach
  void setUp() throws Exception {
    serializedEnumValueExtensions = new HashSet<>();

    // Get EnumSerialization enum class inside ProtoTypeAdapter
    Class<?> protoTypeAdapterClass = ProtoTypeAdapter.class;
    Class<?> enumSerializationClass = null;
    for (Class<?> innerClass : protoTypeAdapterClass.getDeclaredClasses()) {
      if ("EnumSerialization".equals(innerClass.getSimpleName())) {
        enumSerializationClass = innerClass;
        break;
      }
    }
    assertNotNull(enumSerializationClass, "EnumSerialization class not found");

    // Get enum constants NAME and NUMBER from ProtoTypeAdapter.EnumSerialization
    Object[] enumConstants = enumSerializationClass.getEnumConstants();
    Object nameConst = null;
    Object numberConst = null;
    for (Object constant : enumConstants) {
      if ("NAME".equals(constant.toString())) {
        nameConst = constant;
      } else if ("NUMBER".equals(constant.toString())) {
        numberConst = constant;
      }
    }
    assertNotNull(nameConst, "EnumSerialization.NAME not found");
    assertNotNull(numberConst, "EnumSerialization.NUMBER not found");
    enumSerializationName = nameConst;
    enumSerializationNumber = numberConst;

    // Use reflection to get the private constructor of ProtoTypeAdapter
    var constructor = protoTypeAdapterClass.getDeclaredConstructor(
        enumSerializationClass,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);

    // Create ProtoTypeAdapter instances with EnumSerialization.NAME and NUMBER
    protoTypeAdapterName = (ProtoTypeAdapter) constructor.newInstance(
        enumSerializationName,
        CaseFormat.LOWER_UNDERSCORE,
        CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        serializedEnumValueExtensions);

    protoTypeAdapterNumber = (ProtoTypeAdapter) constructor.newInstance(
        enumSerializationNumber,
        CaseFormat.LOWER_UNDERSCORE,
        CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        serializedEnumValueExtensions);

    enumValueDescriptorMock = mock(EnumValueDescriptor.class);
    enumValueOptionsMock = mock(EnumValueOptions.class);
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_withEnumSerializationName_returnsCustomSerializedEnumValue() throws Exception {
    // Arrange
    when(enumValueDescriptorMock.getOptions()).thenReturn(enumValueOptionsMock);
    when(enumValueDescriptorMock.getName()).thenReturn("ENUM_NAME");

    // Create a dynamic proxy subclass of ProtoTypeAdapter to override the private method
    ProtoTypeAdapter protoTypeAdapterNameWithOverride = createProtoTypeAdapterWithOverriddenGetCustSerializedEnumValue();

    // Act
    Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    getEnumValueMethod.setAccessible(true);
    Object result = getEnumValueMethod.invoke(protoTypeAdapterNameWithOverride, enumValueDescriptorMock);

    // Assert
    assertEquals("custom_serialized_value", result);
    verify(enumValueDescriptorMock).getOptions();
    verify(enumValueDescriptorMock).getName();
  }

  @Test
    @Timeout(8000)
  void testGetEnumValue_withEnumSerializationNumber_returnsNumber() throws Exception {
    // Arrange
    when(enumValueDescriptorMock.getNumber()).thenReturn(42);

    // Act
    Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
    getEnumValueMethod.setAccessible(true);
    Object result = getEnumValueMethod.invoke(protoTypeAdapterNumber, enumValueDescriptorMock);

    // Assert
    assertEquals(42, result);
    verify(enumValueDescriptorMock).getNumber();
  }

  /**
   * Create a ProtoTypeAdapter instance that overrides the private method getCustSerializedEnumValue
   * by using a proxy subclass with reflection to call the original constructor.
   */
  private ProtoTypeAdapter createProtoTypeAdapterWithOverriddenGetCustSerializedEnumValue() throws Exception {
    // Get ProtoTypeAdapter class and EnumSerialization class
    Class<?> protoTypeAdapterClass = ProtoTypeAdapter.class;
    Class<?> enumSerializationClass = null;
    for (Class<?> innerClass : protoTypeAdapterClass.getDeclaredClasses()) {
      if ("EnumSerialization".equals(innerClass.getSimpleName())) {
        enumSerializationClass = innerClass;
        break;
      }
    }
    assertNotNull(enumSerializationClass, "EnumSerialization class not found");

    // Define a subclass dynamically via anonymous class extending ProtoTypeAdapter
    // Because constructor is private, use reflection to create instance and set a delegate for getCustSerializedEnumValue

    // Use reflection to get the constructor
    var constructor = protoTypeAdapterClass.getDeclaredConstructor(
        enumSerializationClass,
        CaseFormat.class,
        CaseFormat.class,
        Set.class,
        Set.class);
    constructor.setAccessible(true);

    // Create instance with EnumSerialization.NAME
    Object instance = constructor.newInstance(
        enumSerializationName,
        CaseFormat.LOWER_UNDERSCORE,
        CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        serializedEnumValueExtensions);

    // Create a proxy subclass that overrides getCustSerializedEnumValue using java.lang.reflect.Proxy is not possible
    // Instead, create a dynamic subclass by using a small anonymous subclass with reflection

    // Create a subclass via reflection with overridden method using java.lang.reflect.Proxy is impossible for classes.
    // So, use java.lang.reflect.Proxy is not applicable here.
    // Instead, use a wrapper that intercepts calls via reflection.

    // Use a dynamic proxy is not possible for classes, so we create a subclass via bytecode or use reflection to override method.

    // Alternative: use a helper subclass defined here as a static nested class with package-private constructor
    // But constructor is private, so we cannot extend directly.

    // So, use reflection to create a proxy object that intercepts getCustSerializedEnumValue calls via method interception
    // Since this is complex, use reflection to replace the private method temporarily

    // Use reflection to replace getCustSerializedEnumValue method temporarily with a method that returns "custom_serialized_value"
    // This is complex; instead, use a MethodHandle or setAccessible hack:

    // We can use a small helper class to invoke getEnumValue and intercept getCustSerializedEnumValue by reflection

    // Instead, create a subclass via Unsafe or similar is complex; so we will use a simple approach:
    // Use a Mockito spy on the instance and stub the private method getCustSerializedEnumValue via doAnswer with reflection invoke

    ProtoTypeAdapter spy = Mockito.spy((ProtoTypeAdapter) instance);

    Method privateMethod = protoTypeAdapterClass.getDeclaredMethod("getCustSerializedEnumValue", EnumValueOptions.class, String.class);
    privateMethod.setAccessible(true);

    // Stub private method call by using doAnswer on spy and reflection invoke
    // Since Mockito cannot stub private methods directly, use doAnswer on spy and invoke private method manually

    // Use doAnswer to intercept calls to getCustSerializedEnumValue by using reflection invoke
    // But Mockito cannot intercept private methods directly.
    // So we use a workaround: override getEnumValue to call our own getCustSerializedEnumValue

    // Use doAnswer on getEnumValue to replace getCustSerializedEnumValue call:
    // But getEnumValue is private, so also cannot mock directly.

    // Instead, use Mockito's doReturn on spy with method invocation using reflection invoke:

    // Use a helper class to call getEnumValue on spy and return "custom_serialized_value" when getCustSerializedEnumValue is called.

    // Use a Mockito Answer on getCustSerializedEnumValue method call via reflection:
    // But Mockito cannot mock private methods.

    // Alternative: Use reflection to replace the method implementation temporarily is not trivial.

    // So, create a subclass via reflection proxy that overrides getCustSerializedEnumValue by reflection invoke:

    // Because of complexity, simplest solution: create a subclass with package-private constructor in same package as ProtoTypeAdapterTest
    // But constructor is private, so subclassing is impossible.

    // Final approach: create a new class in same package with different name that has the same private constructor accessible via reflection and overrides the method.
    // Since this is a test, we can define a new class in this test file.

    // So, we define a small helper class inside this test class that uses reflection to call the private constructor and overrides getCustSerializedEnumValue by reflection invoke.

    return new ProtoTypeAdapterNameOverride(
        enumSerializationName,
        CaseFormat.LOWER_UNDERSCORE,
        CaseFormat.LOWER_CAMEL,
        Collections.emptySet(),
        serializedEnumValueExtensions);
  }

  /**
   * Helper subclass of ProtoTypeAdapter that overrides getCustSerializedEnumValue method.
   * Because ProtoTypeAdapter constructor is private, use reflection to call the constructor.
   */
  private static class ProtoTypeAdapterNameOverride extends ProtoTypeAdapter {
    private ProtoTypeAdapterNameOverride(Object enumSerialization,
                                         CaseFormat protoFormat,
                                         CaseFormat jsonFormat,
                                         Set<Extension<com.google.protobuf.DescriptorProtos.FieldOptions, String>> serializedNameExtensions,
                                         Set<Extension<EnumValueOptions, String>> serializedEnumValueExtensions) throws Exception {
      // Call private constructor via reflection
      var constructor = ProtoTypeAdapter.class.getDeclaredConstructor(
          enumSerialization.getClass(),
          CaseFormat.class,
          CaseFormat.class,
          Set.class,
          Set.class);
      constructor.setAccessible(true);
      constructor.newInstance(enumSerialization, protoFormat, jsonFormat, serializedNameExtensions, serializedEnumValueExtensions);

      // Because super() call must be first, we cannot call constructor.newInstance here.
      // So workaround: create a dummy instance and copy fields by reflection

      // Instead, use Unsafe to allocate instance without constructor
      // But for simplicity, call super constructor by reflection then copy fields.

      // Since super constructor is private, we cannot call super(...) directly.
      // So, create instance by Unsafe.allocateInstance and initialize fields by reflection.

      // Use Unsafe to allocate instance:
      sun.misc.Unsafe unsafe = getUnsafe();
      Object instance = unsafe.allocateInstance(ProtoTypeAdapter.class);

      // Set fields by reflection:
      setField(instance, "enumSerialization", enumSerialization);
      setField(instance, "protoFormat", protoFormat);
      setField(instance, "jsonFormat", jsonFormat);
      setField(instance, "serializedNameExtensions", serializedNameExtensions);
      setField(instance, "serializedEnumValueExtensions", serializedEnumValueExtensions);

      // Cast instance to ProtoTypeAdapter and assign to this
      // But Java does not allow assigning to 'this', so this approach is invalid.

      // Therefore, this approach is not possible in Java.

      // Alternative: Use a delegation pattern: keep a ProtoTypeAdapter instance internally and override getCustSerializedEnumValue by reflection.

      throw new UnsupportedOperationException("Cannot instantiate subclass due to private constructor");
    }

    @Override
    protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
      return "custom_serialized_value";
    }

    // Helper methods to get Unsafe and set fields
    private static sun.misc.Unsafe getUnsafe() throws Exception {
      java.lang.reflect.Field f = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
      f.setAccessible(true);
      return (sun.misc.Unsafe) f.get(null);
    }

    private static void setField(Object instance, String fieldName, Object value) throws Exception {
      java.lang.reflect.Field field = ProtoTypeAdapter.class.getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(instance, value);
    }
  }
}