package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class TypeToken_141_4Test {

  @Test
    @Timeout(8000)
  void testGetArray_withNormalType_invokesArrayOfAndReturnsTypeToken() {
    Type componentType = String.class;

    try (MockedStatic<$Gson$Types> mockedStatic = mockStatic($Gson$Types.class)) {
      GenericArrayType arrayType = mock(GenericArrayType.class);
      mockedStatic.when(() -> $Gson$Types.arrayOf(componentType)).thenReturn(arrayType);

      TypeToken<?> result = new TypeToken<>(arrayType);

      mockedStatic.verify(() -> $Gson$Types.arrayOf(componentType));
      assertNotNull(result);
      assertEquals(arrayType, result.getType());
    }
  }

  @Test
    @Timeout(8000)
  void testGetArray_withNullType_invokesArrayOfAndReturnsTypeToken() {
    Type componentType = null;

    try (MockedStatic<$Gson$Types> mockedStatic = mockStatic($Gson$Types.class)) {
      GenericArrayType arrayType = mock(GenericArrayType.class);
      mockedStatic.when(() -> $Gson$Types.arrayOf(componentType)).thenReturn(arrayType);

      TypeToken<?> result = new TypeToken<>(arrayType);

      mockedStatic.verify(() -> $Gson$Types.arrayOf(componentType));
      assertNotNull(result);
      assertEquals(arrayType, result.getType());
    }
  }
}