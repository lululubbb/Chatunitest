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
  private TypeToken<?> typeTokenParameterized;
  private TypeToken<?> typeTokenGenericArray;
  private Class<?> rawTypeClass;
  private ParameterizedType parameterizedTypeMock;
  private GenericArrayType genericArrayTypeMock;

  @BeforeEach
  void setUp() throws Exception {
    // Create TypeToken with raw class type
    typeTokenClass = new TypeToken<String>() {};
    rawTypeClass = typeTokenClass.getRawType();

    // Create ParameterizedType mock for testing
    parameterizedTypeMock = mock(ParameterizedType.class);
    when(parameterizedTypeMock.getRawType()).thenReturn(String.class);
    when(parameterizedTypeMock.getActualTypeArguments()).thenReturn(new Type[] {String.class});

    // Create TypeToken with ParameterizedType via reflection
    typeTokenParameterized = createTypeTokenWithType(parameterizedTypeMock);

    // Create GenericArrayType mock for testing
    genericArrayTypeMock = mock(GenericArrayType.class);
    when(genericArrayTypeMock.getGenericComponentType()).thenReturn(String[].class);

    // Create TypeToken with GenericArrayType via reflection
    typeTokenGenericArray = createTypeTokenWithType(genericArrayTypeMock);
  }

  private TypeToken<?> createTypeTokenWithType(Type type) throws Exception {
    // Use private constructor TypeToken(Type) via reflection
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    return constructor.newInstance(type);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_nullType_returnsFalse() {
    // Disambiguate call by casting null to Type
    assertFalse(typeTokenClass.isAssignableFrom((Type) null));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_sameType_returnsTrue() {
    // type equals from
    Type type = typeTokenClass.getType();
    assertTrue(typeTokenClass.isAssignableFrom(type));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_rawTypeAssignableFrom_returnsTrue() {
    Type from = Integer.class;
    TypeToken<Number> numberToken = new TypeToken<Number>() {};
    // Number.class.isAssignableFrom(Integer.class) == true
    assertTrue(numberToken.isAssignableFrom(from));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_rawTypeAssignableFrom_returnsFalse() {
    Type from = Number.class;
    TypeToken<Integer> integerToken = new TypeToken<Integer>() {};
    // Integer.class.isAssignableFrom(Number.class) == false
    assertFalse(integerToken.isAssignableFrom(from));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_parameterizedType_callsPrivateIsAssignableFrom() throws Exception {
    // Use reflection to invoke private static isAssignableFrom(Type, ParameterizedType, Map)
    Method privateMethod = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    privateMethod.setAccessible(true);

    Map<String, Type> typeVarMap = new HashMap<>();

    // from is String.class, to is parameterizedTypeMock (rawType String.class)
    boolean result = (boolean) privateMethod.invoke(null, String.class, parameterizedTypeMock, typeVarMap);
    assertTrue(result);

    // from is Integer.class, to is parameterizedTypeMock (rawType String.class) - should be false
    result = (boolean) privateMethod.invoke(null, Integer.class, parameterizedTypeMock, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_genericArrayType_returnsTrue() {
    // rawType.isAssignableFrom($Gson$Types.getRawType(from)) must be true and private isAssignableFrom(from, GenericArrayType) true
    // from is String[].class, type is genericArrayTypeMock with component String[]
    Type from = String[].class;
    assertTrue(typeTokenGenericArray.isAssignableFrom(from));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_genericArrayType_returnsFalseDueToRawType() throws Exception {
    // Create TypeToken with GenericArrayType with rawType that does not assign from the from type raw type
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    when(genericArrayType.getGenericComponentType()).thenReturn(String.class);

    TypeToken<?> token = createTypeTokenWithType(genericArrayType);

    // from raw type is Integer[].class which raw type is Integer[].class
    Type from = Integer[].class;

    // rawType.isAssignableFrom(String[].class) false => should return false
    assertFalse(token.isAssignableFrom(from));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_throwsOnUnexpectedType() throws Exception {
    // Create a TypeToken with a Type that is not Class, ParameterizedType or GenericArrayType
    Type weirdType = new Type() {};
    TypeToken<?> token = createTypeTokenWithType(weirdType);

    AssertionError error = assertThrows(AssertionError.class, () -> token.isAssignableFrom(String.class));
    String message = error.getMessage();
    assertTrue(message.contains("Expected one of"));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_deprecatedClassOverload_consistent() {
    TypeToken<Number> token = new TypeToken<Number>() {};
    assertTrue(token.isAssignableFrom((Class<?>) Integer.class));
    assertFalse(token.isAssignableFrom((Class<?>) String.class));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_deprecatedTokenOverload_consistent() {
    TypeToken<Number> token = new TypeToken<Number>() {};
    TypeToken<Integer> intToken = new TypeToken<Integer>() {};
    TypeToken<String> stringToken = new TypeToken<String>() {};
    assertTrue(token.isAssignableFrom(intToken));
    assertFalse(token.isAssignableFrom(stringToken));
  }
}