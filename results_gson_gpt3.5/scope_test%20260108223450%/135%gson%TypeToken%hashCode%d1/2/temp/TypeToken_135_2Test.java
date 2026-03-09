package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class TypeToken_hashCode_Test {

  @Test
    @Timeout(8000)
  void hashCode_returnsPrecomputedHashCode() throws Exception {
    // Create instance using anonymous subclass to capture generic type
    TypeToken<String> instance = new TypeToken<String>() {};

    // Use reflection to get private final field 'hashCode'
    Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);

    // Remove final modifier from the field
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(hashCodeField, hashCodeField.getModifiers() & ~java.lang.reflect.Modifier.FINAL);

    // Set a known hashCode value
    int expectedHashCode = 123456789;
    hashCodeField.setInt(instance, expectedHashCode);

    // Act
    int actualHashCode = instance.hashCode();

    // Assert
    assertEquals(expectedHashCode, actualHashCode);
  }
}