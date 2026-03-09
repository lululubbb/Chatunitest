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
  private ParameterizedType parameterizedType;
  private GenericArrayType genericArrayType;

  private Type mockType;
  private Class<?> mockRawType;

  @BeforeEach
  void setUp() throws Exception {
    // Prepare rawTypeClass TypeToken with Class type
    typeTokenClass = new TypeToken<String>() {};
    rawTypeClass = String.class;

    // Prepare ParameterizedType mock
    parameterizedType = mock(ParameterizedType.class);
    when(parameterizedType.getRawType()).thenReturn(Map.class);
    when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[] {String.class, Integer.class});
    typeTokenParameterized = createTypeTokenWithType(parameterizedType);

    // Prepare GenericArrayType mock
    genericArrayType = mock(GenericArrayType.class);
    when(genericArrayType.getGenericComponentType()).thenReturn(String.class);
    typeTokenGenericArray = createTypeTokenWithType(genericArrayType);

    // Prepare mockType and mockRawType for rawType.isAssignableFrom tests
    mockType = mock(Type.class);
    mockRawType = Object.class;
  }

  private TypeToken<?> createTypeTokenWithType(Type type) throws Exception {
    // Use reflection to call private constructor TypeToken(Type)
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    return constructor.newInstance(type);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_nullType() {
    // Cast null to Type to resolve ambiguous method call
    assertFalse(typeTokenClass.isAssignableFrom((Type) null));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_equalType() throws Exception {
    TypeToken<String> token = new TypeToken<String>() {};
    // type field equals to from argument
    Type typeField = (Type) getField(TypeToken.class, token, "type");
    assertTrue(token.isAssignableFrom(typeField));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_typeIsClass_assignableTrue() throws Exception {
    TypeToken<Object> token = new TypeToken<Object>() {};
    Class<?> rawType = (Class<?>) getField(TypeToken.class, token, "rawType");

    // Prepare a type from which rawType is assignable
    Type fromType = String.class;

    // Spy on $Gson$Types.getRawType to return fromType class
    Type rawFromType = $Gson$Types.getRawType(fromType);
    assertEquals(String.class, rawFromType);

    // rawType.isAssignableFrom(String.class) should be true for Object.class
    assertTrue(token.isAssignableFrom(fromType));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_typeIsClass_assignableFalse() throws Exception {
    TypeToken<String> token = new TypeToken<String>() {};
    Type fromType = Object.class; // String.class.isAssignableFrom(Object.class) == false
    assertFalse(token.isAssignableFrom(fromType));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_typeIsParameterizedType() throws Exception {
    // typeTokenParameterized has a ParameterizedType type
    Type fromType = new TypeToken<HashMap<String, Integer>>() {}.getType();

    // We cannot access private static isAssignableFrom(Type, ParameterizedType, Map) directly,
    // but the public isAssignableFrom(Type) calls it internally.
    // The method should return true if assignable, false otherwise.

    // Should be true because HashMap<String,Integer> is assignable to Map<String,Integer>
    assertTrue(typeTokenParameterized.isAssignableFrom(fromType));

    // Negative case: fromType is HashMap<Integer, String>
    ParameterizedType ptDifferent = mock(ParameterizedType.class);
    when(ptDifferent.getRawType()).thenReturn(HashMap.class);
    when(ptDifferent.getActualTypeArguments()).thenReturn(new Type[] {Integer.class, String.class});
    TypeToken<?> tokenDiff = createTypeTokenWithType(parameterizedType);
    assertFalse(tokenDiff.isAssignableFrom(ptDifferent));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_typeIsGenericArrayType() throws Exception {
    // typeTokenGenericArray has a GenericArrayType type
    // Prepare fromType which rawType is assignable and matches GenericArrayType
    Type fromType = String[].class;

    assertTrue(typeTokenGenericArray.isAssignableFrom(fromType));

    // Negative case: fromType raw type not assignable
    TypeToken<?> token = createTypeTokenWithType(genericArrayType);
    Type fromType2 = Integer[].class; // String[] is not assignable from Integer[]
    assertFalse(token.isAssignableFrom(fromType2));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFrom_typeUnexpectedType_throws() throws Exception {
    // Create a TypeToken with a Type that is not Class, ParameterizedType or GenericArrayType
    TypeVariable<Class<TypeToken>> typeVariable = TypeToken.class.getTypeParameters()[0];
    TypeToken<?> token = createTypeTokenWithType(typeVariable);

    IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
      token.isAssignableFrom(String.class);
    });
    assertTrue(thrown.getMessage().contains("Unexpected type"));
  }

  private static Object getField(Class<?> clazz, Object instance, String fieldName) throws Exception {
    Field field = clazz.getDeclaredField(fieldName);
    field.setAccessible(true);
    return field.get(instance);
  }
}