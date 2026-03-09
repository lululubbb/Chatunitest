package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.isNull;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeToken_141_1Test {

  @Test
    @Timeout(8000)
  void testGetArray_withRegularType() {
    Type componentType = String.class;
    GenericArrayType arrayType = Mockito.mock(GenericArrayType.class);

    try (MockedStatic<$Gson$Types> mockedStatic = Mockito.mockStatic($Gson$Types.class)) {
      // Fix: match exact argument, not any(Type.class)
      mockedStatic.when(() -> $Gson$Types.arrayOf(componentType)).thenReturn(arrayType);

      TypeToken<?> result = TypeToken.getArray(componentType);

      assertNotNull(result);
      assertEquals(arrayType, result.getType());
      mockedStatic.verify(() -> $Gson$Types.arrayOf(componentType), Mockito.times(1));
    }
  }

  @Test
    @Timeout(8000)
  void testGetArray_withNullType() {
    Type componentType = null;
    GenericArrayType arrayType = Mockito.mock(GenericArrayType.class);

    try (MockedStatic<$Gson$Types> mockedStatic = Mockito.mockStatic($Gson$Types.class)) {
      // Fix: match exact argument null, not isNull() matcher (which doesn't work with static mocks)
      mockedStatic.when(() -> $Gson$Types.arrayOf((Type) null)).thenReturn(arrayType);

      TypeToken<?> result = TypeToken.getArray(componentType);

      assertNotNull(result);
      assertEquals(arrayType, result.getType());
      mockedStatic.verify(() -> $Gson$Types.arrayOf(componentType), Mockito.times(1));
    }
  }
}