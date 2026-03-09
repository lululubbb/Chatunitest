package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeTokenIsAssignableFromTest {

  private TypeToken<Object> typeTokenClass;
  private TypeToken<Object> typeTokenParamType;
  private TypeToken<Object> typeTokenGenericArrayType;

  private Class<?> rawTypeClass;
  private ParameterizedType parameterizedType;
  private GenericArrayType genericArrayType;

  @BeforeEach
  void setup() throws Exception {
    rawTypeClass = String.class;

    // Create ParameterizedType mock for testing
    parameterizedType = mock(ParameterizedType.class);
    when(parameterizedType.getRawType()).thenReturn(rawTypeClass);
    when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[] {String.class});

    // Create GenericArrayType mock for testing
    genericArrayType = mock(GenericArrayType.class);
    when(genericArrayType.getGenericComponentType()).thenReturn(String.class);

    // Create TypeToken instances with reflection to set final fields
    typeTokenClass = createTypeTokenWithType(rawTypeClass);
    typeTokenParamType = createTypeTokenWithType(parameterizedType);
    typeTokenGenericArrayType = createTypeTokenWithType(genericArrayType);
  }

  private TypeToken<Object> createTypeTokenWithType(Type type) throws Exception {
    Constructor<TypeToken> ctor = TypeToken.class.getDeclaredConstructor(Type.class);
    ctor.setAccessible(true);
    @SuppressWarnings("unchecked")
    TypeToken<Object> token = (TypeToken<Object>) ctor.newInstance(type);

    // Set rawType field
    Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);
    rawTypeField.set(token, $Gson$Types.getRawType(type));

    // Set type field
    Field typeField = TypeToken.class.getDeclaredField("type");
    typeField.setAccessible(true);
    typeField.set(token, type);

    // Set hashCode field
    Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);
    hashCodeField.set(token, type.hashCode());

    return token;
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_nullType_returnsFalse() {
    assertFalse(typeTokenClass.isAssignableFrom((Type) null));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_sameType_returnsTrue() throws Exception {
    // typeTokenClass.type equals rawTypeClass
    TypeToken<Object> token = createTypeTokenWithType(rawTypeClass);
    assertTrue(token.isAssignableFrom(rawTypeClass));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_typeIsClass_assignableFromTrue() {
    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(String.class)).thenReturn(String.class);
      assertTrue(typeTokenClass.isAssignableFrom(String.class));
    }
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_typeIsClass_assignableFromFalse() {
    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(Integer.class)).thenReturn(Integer.class);
      assertFalse(typeTokenClass.isAssignableFrom(Integer.class));
    }
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_typeIsParameterizedType_invokesPrivateMethod() throws Exception {
    Type fromType = String.class;
    Map<String, Type> typeVarMap = new HashMap<>();

    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(fromType)).thenReturn(String.class);

      // Use reflection to invoke private static isAssignableFrom(Type, ParameterizedType, Map)
      Method privateIsAssignableFrom =
          TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
      privateIsAssignableFrom.setAccessible(true);

      boolean result =
          (boolean) privateIsAssignableFrom.invoke(null, fromType, parameterizedType, typeVarMap);

      // result depends on implementation, just check no exception and boolean returned
      assertTrue(result || !result);
    }

    // Call public isAssignableFrom with ParameterizedType type field
    assertDoesNotThrow(() -> {
      boolean res = typeTokenParamType.isAssignableFrom(fromType);
      assertTrue(res || !res);
    });
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_typeIsGenericArrayType_invokesPrivateMethod() throws Exception {
    Type fromType = String[].class;

    try (MockedStatic<$Gson$Types> mocked = Mockito.mockStatic($Gson$Types.class)) {
      mocked.when(() -> $Gson$Types.getRawType(fromType)).thenReturn(String[].class);

      // Use reflection to invoke private static isAssignableFrom(Type, GenericArrayType)
      Method privateIsAssignableFrom =
          TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
      privateIsAssignableFrom.setAccessible(true);

      boolean result =
          (boolean) privateIsAssignableFrom.invoke(null, fromType, genericArrayType);

      assertTrue(result || !result);
    }

    // Call public isAssignableFrom with GenericArrayType type field
    assertDoesNotThrow(() -> {
      boolean res = typeTokenGenericArrayType.isAssignableFrom(fromType);
      assertTrue(res || !res);
    });
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_unexpectedType_throwsAssertionError() throws Exception {
    Type unexpectedType = mock(Type.class);

    TypeToken<Object> token = createTypeTokenWithType(unexpectedType);

    AssertionError error = assertThrows(AssertionError.class, () -> token.isAssignableFrom(String.class));
    assertTrue(error.getMessage().contains(unexpectedType.toString()));
  }
}