package com.google.gson.protobuf;
import org.junit.jupiter.api.Timeout;
import static java.util.Objects.requireNonNull;
import com.google.common.base.CaseFormat;
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
import com.google.protobuf.Extension;
import com.google.protobuf.Message;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.protobuf.Descriptors.EnumValueDescriptor;
import com.google.protobuf.DescriptorProtos.EnumValueOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Collections;

public class ProtoTypeAdapter_468_4Test {

    private ProtoTypeAdapter adapterNameSerialization;
    private ProtoTypeAdapter adapterNumberSerialization;

    private EnumValueDescriptor enumValueDescriptorMock;
    private EnumValueOptions enumValueOptionsMock;

    enum EnumSerialization {
        NAME,
        NUMBER
    }

    @BeforeEach
    public void setUp() throws Exception {
        // Create instance of ProtoTypeAdapter with enumSerialization = NAME
        adapterNameSerialization = createProtoTypeAdapter(EnumSerialization.NAME);
        // Create instance of ProtoTypeAdapter with enumSerialization = NUMBER
        adapterNumberSerialization = createProtoTypeAdapter(EnumSerialization.NUMBER);

        // Mock EnumValueDescriptor
        enumValueDescriptorMock = mock(EnumValueDescriptor.class);
        enumValueOptionsMock = mock(EnumValueOptions.class);

        // Mock getOptions() to return enumValueOptionsMock
        when(enumValueDescriptorMock.getOptions()).thenReturn(enumValueOptionsMock);
    }

    private ProtoTypeAdapter createProtoTypeAdapter(EnumSerialization enumSerialization) throws Exception {
        // Use reflection to get ProtoTypeAdapter.newBuilder() and build instance with enumSerialization
        Class<?> clazz = ProtoTypeAdapter.class;
        Method newBuilderMethod = clazz.getDeclaredMethod("newBuilder");
        newBuilderMethod.setAccessible(true);
        Object builder = newBuilderMethod.invoke(null);

        // The builder class is a static nested class inside ProtoTypeAdapter
        Class<?> builderClass = builder.getClass();

        // Set enumSerialization via reflection on builder
        // Assuming builder has a method enumSerialization(EnumSerialization)
        Method enumSerializationSetter = null;
        for (Method m : builderClass.getDeclaredMethods()) {
            if (m.getParameterCount() == 1 && m.getName().toLowerCase().contains("enumserialization")) {
                enumSerializationSetter = m;
                break;
            }
        }
        if (enumSerializationSetter == null) {
            throw new IllegalStateException("Cannot find enumSerialization setter on builder");
        }
        enumSerializationSetter.setAccessible(true);
        // Pass ProtoTypeAdapter.EnumSerialization enum constant corresponding to enumSerialization.name()
        Object protoEnumSerialization = Enum.valueOf(
                (Class<Enum>) Class.forName("com.google.gson.protobuf.ProtoTypeAdapter$EnumSerialization"),
                enumSerialization.name());
        enumSerializationSetter.invoke(builder, protoEnumSerialization);

        // Assuming builder has methods for protoFormat, jsonFormat, serializedNameExtensions, serializedEnumValueExtensions
        // Set protoFormat to LOWER_UNDERSCORE
        Class<?> caseFormatClass = Class.forName("com.google.common.base.CaseFormat");
        Object lowerUnderscore = Enum.valueOf((Class<Enum>) caseFormatClass, "LOWER_UNDERSCORE");
        Object lowerCamel = Enum.valueOf((Class<Enum>) caseFormatClass, "LOWER_CAMEL");

        Method protoFormatSetter = null;
        Method jsonFormatSetter = null;
        Method serializedNameExtensionsSetter = null;
        Method serializedEnumValueExtensionsSetter = null;
        Method buildMethod = null;

        for (Method m : builderClass.getDeclaredMethods()) {
            String name = m.getName().toLowerCase();
            if (m.getParameterCount() == 1) {
                if (name.contains("protoformat")) {
                    protoFormatSetter = m;
                } else if (name.contains("jsonformat")) {
                    jsonFormatSetter = m;
                } else if (name.contains("serializednameextensions")) {
                    serializedNameExtensionsSetter = m;
                } else if (name.contains("serializedenumvalueextensions")) {
                    serializedEnumValueExtensionsSetter = m;
                }
            } else if (m.getParameterCount() == 0 && name.equals("build")) {
                buildMethod = m;
            }
        }

        if (protoFormatSetter == null || jsonFormatSetter == null
                || serializedNameExtensionsSetter == null || serializedEnumValueExtensionsSetter == null
                || buildMethod == null) {
            throw new IllegalStateException("Builder missing required methods");
        }

        protoFormatSetter.setAccessible(true);
        jsonFormatSetter.setAccessible(true);
        serializedNameExtensionsSetter.setAccessible(true);
        serializedEnumValueExtensionsSetter.setAccessible(true);
        buildMethod.setAccessible(true);

        serializedNameExtensionsSetter.invoke(builder, Collections.emptySet());
        serializedEnumValueExtensionsSetter.invoke(builder, Collections.emptySet());
        protoFormatSetter.invoke(builder, lowerUnderscore);
        jsonFormatSetter.invoke(builder, lowerCamel);

        return (ProtoTypeAdapter) buildMethod.invoke(builder);
    }

    @Test
    @Timeout(8000)
    public void testGetEnumValue_NameSerialization_CustomSerializedValue() throws Exception {
        // Arrange
        String defaultName = "ENUM_NAME";
        when(enumValueDescriptorMock.getName()).thenReturn(defaultName);

        String customValue = "custom_serialized_value";

        // Create a dynamic subclass of ProtoTypeAdapter via reflection proxy to override getCustSerializedEnumValue
        // Since constructor is private, create instance via builder, then create a proxy that overrides the method

        ProtoTypeAdapter baseAdapter = adapterNameSerialization;

        // Create a proxy subclass that overrides getCustSerializedEnumValue by reflection
        ProtoTypeAdapter adapterProxy = new ProtoTypeAdapter(baseAdapter) {
            @Override
            protected String getCustSerializedEnumValue(EnumValueOptions options, String defaultValue) {
                return customValue;
            }
        };

        // Use reflection to get the getEnumValue method
        Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
        getEnumValueMethod.setAccessible(true);

        // Act
        Object result = getEnumValueMethod.invoke(adapterProxy, enumValueDescriptorMock);

        // Assert
        assertEquals(customValue, result);
    }

    @Test
    @Timeout(8000)
    public void testGetEnumValue_NumberSerialization_ReturnsNumber() throws Exception {
        // Arrange
        int number = 123;
        when(enumValueDescriptorMock.getNumber()).thenReturn(number);

        // Act
        Method getEnumValueMethod = ProtoTypeAdapter.class.getDeclaredMethod("getEnumValue", EnumValueDescriptor.class);
        getEnumValueMethod.setAccessible(true);
        Object result = getEnumValueMethod.invoke(adapterNumberSerialization, enumValueDescriptorMock);

        // Assert
        assertEquals(number, result);
    }
}