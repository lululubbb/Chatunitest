package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeTokenToStringTest {

  @Test
    @Timeout(8000)
  void toString_shouldReturnTypeToStringResult() throws Exception {
    Type mockType = Mockito.mock(Type.class);

    // Create a TypeToken instance via the private constructor with mockType
    TypeToken<?> typeToken = createTypeTokenWithType(mockType);

    try (MockedStatic<$Gson$Types> mockedStatic = Mockito.mockStatic($Gson$Types.class)) {
      mockedStatic.when(() -> $Gson$Types.typeToString(mockType)).thenReturn("mockTypeString");

      String result = typeToken.toString();

      assertEquals("mockTypeString", result);
      mockedStatic.verify(() -> $Gson$Types.typeToString(mockType));
    }
  }

  private static TypeToken<?> createTypeTokenWithType(Type type) throws Exception {
    // Use the private constructor TypeToken(Type)
    var constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> token = (TypeToken<?>) constructor.newInstance(type);

    // Set the private final field 'type' to the provided type explicitly
    setFinalField(token, "type", type);

    // Calculate rawType using $Gson$Types.getRawType(type)
    Class<?> rawType = $Gson$Types.getRawType(type);

    // Set the private final field 'rawType' explicitly
    setFinalField(token, "rawType", rawType);

    // Set the private final field 'hashCode' as type.hashCode() ^ rawType.hashCode()
    int hashCode = type.hashCode() ^ rawType.hashCode();
    setFinalField(token, "hashCode", hashCode);

    return token;
  }

  private static void setFinalField(Object target, String fieldName, Object value) throws Exception {
    Field field = TypeToken.class.getDeclaredField(fieldName);
    field.setAccessible(true);

    // Remove final modifier if present
    Field modifiersField = Field.class.getDeclaredField("modifiers");
    modifiersField.setAccessible(true);
    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

    field.set(target, value);
  }
}