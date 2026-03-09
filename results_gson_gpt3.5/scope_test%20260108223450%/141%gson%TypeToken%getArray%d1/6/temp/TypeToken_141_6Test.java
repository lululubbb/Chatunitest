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
import org.mockito.Mockito;

class TypeToken_141_6Test {

  @Test
    @Timeout(8000)
  void testGetArray_withRegularType() {
    Type componentType = String.class;

    GenericArrayType arrayType = $Gson$Types.arrayOf(componentType);

    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.arrayOf(componentType)).thenReturn(arrayType);

      // Call getArray inside the mocked block to ensure mocking works properly
      TypeToken<?> result = TypeToken.getArray(componentType);

      assertNotNull(result);
      assertEquals(arrayType, result.getType());

      mocked.verify(() -> $Gson$Types.arrayOf(componentType), times(1));
    }
  }

  @Test
    @Timeout(8000)
  void testGetArray_withParameterizedType() {
    Type componentType = mock(Type.class);

    GenericArrayType arrayType = mock(GenericArrayType.class);

    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.arrayOf(componentType)).thenReturn(arrayType);

      TypeToken<?> result = TypeToken.getArray(componentType);

      assertNotNull(result);
      assertEquals(arrayType, result.getType());

      mocked.verify(() -> $Gson$Types.arrayOf(componentType), times(1));
    }
  }

  @Test
    @Timeout(8000)
  void testGetArray_withNullType() throws Exception {
    Type componentType = null;

    GenericArrayType arrayType = mock(GenericArrayType.class);

    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.arrayOf(componentType)).thenReturn(arrayType);

      // Use reflection to invoke the private constructor
      java.lang.reflect.Constructor<TypeToken> constructor =
          TypeToken.class.getDeclaredConstructor(Type.class);
      constructor.setAccessible(true);
      TypeToken<?> result = constructor.newInstance(arrayType);

      assertNotNull(result);
      assertEquals(arrayType, result.getType());

      mocked.verify(() -> $Gson$Types.arrayOf(componentType), times(1));
    }
  }
}