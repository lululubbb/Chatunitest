package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.internal.$Gson$Types;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class TypeTokenToStringTest {

  @Test
    @Timeout(8000)
  void toString_returnsTypeToStringResult() {
    // Arrange
    Type mockType = new Type() {};
    // Use reflection to instantiate private constructor TypeToken(Type)
    TypeToken<?> typeToken;
    try {
      var constructor = TypeToken.class.getDeclaredConstructor(Type.class);
      constructor.setAccessible(true);
      typeToken = (TypeToken<?>) constructor.newInstance(mockType);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    String expectedString = "mockedTypeString";

    try (MockedStatic<$Gson$Types> mockedGsonTypes = mockStatic($Gson$Types.class)) {
      mockedGsonTypes.when(() -> $Gson$Types.typeToString(mockType)).thenReturn(expectedString);

      // Act
      String result = typeToken.toString();

      // Assert
      assertEquals(expectedString, result);
    }
  }
}