package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

class TypeToken_141_5Test {

  private static GenericArrayType createGenericArrayType(Type componentType) {
    return new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return componentType;
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericArrayType)) return false;
        GenericArrayType that = (GenericArrayType) o;
        return (componentType == null ? that.getGenericComponentType() == null : componentType.equals(that.getGenericComponentType()));
      }
      @Override
      public int hashCode() {
        return componentType == null ? 0 : componentType.hashCode();
      }
      @Override
      public String toString() {
        return componentType + "[]";
      }
    };
  }

  @Test
    @Timeout(8000)
  void testGetArray_withRegularType() {
    Type componentType = String.class;
    GenericArrayType arrayType = createGenericArrayType(componentType);

    try (MockedStatic<$Gson$Types> mocked = mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.arrayOf(eq(componentType))).thenReturn(arrayType);

      TypeToken<?> result = null;
      // Call inside mock scope to ensure mock is active during constructor
      result = TypeToken.getArray(componentType);

      assertNotNull(result);
      assertEquals(arrayType, result.getType());
      mocked.verify(() -> $Gson$Types.arrayOf(eq(componentType)));
    }
  }

  @Test
    @Timeout(8000)
  void testGetArray_withParameterizedType() {
    Type componentType = mock(Type.class);
    GenericArrayType arrayType = createGenericArrayType(componentType);

    try (MockedStatic<$Gson$Types> mocked = mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.arrayOf(eq(componentType))).thenReturn(arrayType);

      TypeToken<?> result = null;
      result = TypeToken.getArray(componentType);

      assertNotNull(result);
      assertEquals(arrayType, result.getType());
      mocked.verify(() -> $Gson$Types.arrayOf(eq(componentType)));
    }
  }

  @Test
    @Timeout(8000)
  void testGetArray_withNullType() {
    Type componentType = null;
    GenericArrayType arrayType = createGenericArrayType(componentType);

    try (MockedStatic<$Gson$Types> mocked = mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.arrayOf(isNull())).thenReturn(arrayType);

      TypeToken<?> result = null;
      result = TypeToken.getArray(componentType);

      assertNotNull(result);
      assertEquals(arrayType, result.getType());
      mocked.verify(() -> $Gson$Types.arrayOf(isNull()));
    }
  }
}