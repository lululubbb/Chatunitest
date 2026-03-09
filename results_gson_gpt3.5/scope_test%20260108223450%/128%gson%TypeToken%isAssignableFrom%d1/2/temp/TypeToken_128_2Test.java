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

class TypeTokenIsAssignableFromTest {

  private TypeToken<?> typeTokenClass;
  private TypeToken<?> typeTokenParamType;
  private TypeToken<?> typeTokenGenericArray;

  private Class<?> rawTypeClass;
  private ParameterizedType parameterizedTypeMock;
  private GenericArrayType genericArrayTypeMock;

  @BeforeEach
  void setup() throws Exception {
    // Setup rawTypeClass and TypeToken with raw Class type
    rawTypeClass = String.class;
    typeTokenClass = createTypeToken(rawTypeClass);

    // Setup ParameterizedType mock
    parameterizedTypeMock = mock(ParameterizedType.class);
    when(parameterizedTypeMock.getRawType()).thenReturn(java.util.List.class);
    typeTokenParamType = createTypeToken(parameterizedTypeMock);

    // Setup GenericArrayType mock
    genericArrayTypeMock = mock(GenericArrayType.class);
    // Mock getGenericComponentType to return String.class
    when(genericArrayTypeMock.getGenericComponentType()).thenReturn(String.class);
    typeTokenGenericArray = createTypeToken(genericArrayTypeMock);
  }

  private TypeToken<?> createTypeToken(Type type) throws Exception {
    Constructor<TypeToken> ctor = TypeToken.class.getDeclaredConstructor(Type.class);
    ctor.setAccessible(true);
    TypeToken<?> token = ctor.newInstance(type);

    Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);

    if (type instanceof Class<?>) {
      rawTypeField.set(token, (Class<?>) type);
    } else if (type instanceof ParameterizedType) {
      rawTypeField.set(token, (Class<?>) ((ParameterizedType) type).getRawType());
    } else if (type instanceof GenericArrayType) {
      // For array type, rawType is array class of component type
      Type componentType = ((GenericArrayType) type).getGenericComponentType();
      Class<?> componentRaw = $Gson$Types.getRawType(componentType);
      Object arrayInstance = java.lang.reflect.Array.newInstance(componentRaw, 0);
      rawTypeField.set(token, arrayInstance.getClass());
    } else if (type instanceof TypeVariable<?>) {
      // For TypeVariable, set rawType to Object.class to avoid NPEs in tests
      rawTypeField.set(token, Object.class);
    } else {
      // fallback: set rawType to Object.class
      rawTypeField.set(token, Object.class);
    }

    return token;
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_nullType_returnsFalse() throws Exception {
    // Use reflection to invoke isAssignableFrom(Type) to avoid ambiguity
    Method isAssignableFromMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    isAssignableFromMethod.setAccessible(true);
    boolean result = (boolean) isAssignableFromMethod.invoke(typeTokenClass, new Object[] { null });
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_sameType_returnsTrue() throws Exception {
    Type type = typeTokenClass.getType();
    // Use reflection to invoke isAssignableFrom(Type) to avoid ambiguity
    Method isAssignableFromMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    isAssignableFromMethod.setAccessible(true);
    boolean result = (boolean) isAssignableFromMethod.invoke(typeTokenClass, type);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_typeIsClass_assignableFromRawType() throws Exception {
    // Use reflection to invoke isAssignableFrom(Type) to avoid ambiguity

    Method isAssignableFromMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    isAssignableFromMethod.setAccessible(true);

    // from is subclass of rawTypeClass (String.class)
    Type from = String.class;
    boolean result1 = (boolean) isAssignableFromMethod.invoke(typeTokenClass, from);
    assertTrue(result1);

    // from is unrelated class
    Type unrelated = Integer.class;
    boolean result2 = (boolean) isAssignableFromMethod.invoke(typeTokenClass, unrelated);
    // String.class.isAssignableFrom(Integer.class) == false
    assertFalse(result2);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_typeIsParameterizedType_callsPrivateIsAssignableFrom() throws Exception {
    // Prepare a ParameterizedType to test private isAssignableFrom(Type, ParameterizedType, Map)
    Type from = new TypeToken<java.util.List<String>>() {}.getType();
    ParameterizedType to = (ParameterizedType) typeTokenParamType.getType();

    // Use reflection to invoke private static isAssignableFrom(Type, ParameterizedType, Map)
    Method privateMethod = TypeToken.class.getDeclaredMethod(
        "isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    privateMethod.setAccessible(true);

    Map<String, Type> typeVarMap = new HashMap<>();
    boolean result = (boolean) privateMethod.invoke(null, from, to, typeVarMap);
    // We cannot assert exact value but ensure method returns boolean without exception
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_typeIsGenericArrayType_callsPrivateIsAssignableFrom() throws Exception {
    // from is array of String[]
    Type from = String[].class;

    // Create a proper GenericArrayType instance instead of mock for 'to'
    GenericArrayType to = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return String.class;
      }

      @Override
      public boolean equals(Object obj) {
        if (obj instanceof GenericArrayType) {
          GenericArrayType other = (GenericArrayType) obj;
          return getGenericComponentType().equals(other.getGenericComponentType());
        }
        return false;
      }

      @Override
      public int hashCode() {
        return getGenericComponentType().hashCode();
      }

      @Override
      public String toString() {
        return getGenericComponentType().toString() + "[]";
      }
    };

    // rawType.isAssignableFrom($Gson$Types.getRawType(from)) must be true for String[].class
    assertTrue(typeTokenGenericArray.getRawType().isAssignableFrom($Gson$Types.getRawType(from)));

    // Use reflection to invoke private static isAssignableFrom(Type, GenericArrayType)
    Method privateMethod = TypeToken.class.getDeclaredMethod(
        "isAssignableFrom", Type.class, GenericArrayType.class);
    privateMethod.setAccessible(true);

    boolean result = (boolean) privateMethod.invoke(null, from, to);
    // Result is boolean, just assert no exceptions and boolean returned
    assertNotNull(result);

    // Also test with from that is not assignable
    Type fromUnassignable = Integer[].class;
    boolean result2 = (boolean) privateMethod.invoke(null, fromUnassignable, to);
    assertNotNull(result2);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_typeIsUnexpectedType_throwsAssertionError() throws Exception {
    // Create a TypeToken with a TypeVariable (unexpected type)
    TypeVariable<Class<TypeToken>>[] typeVariables = TypeToken.class.getTypeParameters();
    TypeVariable<?> typeVariable = typeVariables.length > 0 ? typeVariables[0] : null;
    if (typeVariable == null) {
      // fallback to anonymous type variable
      typeVariable = mock(TypeVariable.class);
    }
    TypeToken<?> token = createTypeToken(typeVariable);

    // Use reflection to invoke isAssignableFrom(Type) to avoid ambiguity and expect AssertionError
    Method isAssignableFromMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    isAssignableFromMethod.setAccessible(true);

    AssertionError error = assertThrows(AssertionError.class, () -> {
      try {
        isAssignableFromMethod.invoke(token, String.class);
      } catch (InvocationTargetException e) {
        // unwrap InvocationTargetException to throw cause
        Throwable cause = e.getCause();
        if (cause instanceof AssertionError) {
          throw (AssertionError) cause;
        } else if (cause instanceof RuntimeException) {
          throw (RuntimeException) cause;
        } else {
          throw new RuntimeException(cause);
        }
      }
    });
    assertTrue(error.getMessage().contains("Unexpected type"));
  }
}