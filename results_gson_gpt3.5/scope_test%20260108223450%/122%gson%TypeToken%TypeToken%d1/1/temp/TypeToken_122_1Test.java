package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class TypeToken_122_1Test {

  // Test the protected no-arg constructor and its initialization
  @Test
    @Timeout(8000)
  void testNoArgConstructor() {
    TypeToken<List<String>> typeToken = new TypeToken<List<String>>() {};
    assertNotNull(typeToken.getType());
    assertNotNull(typeToken.getRawType());
    assertEquals(typeToken.getRawType(), $Gson$Types.getRawType(typeToken.getType()));
    assertEquals(typeToken.hashCode(), typeToken.getType().hashCode());
  }

  // Test the private constructor with a Type argument via reflection
  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    Type type = String.class;
    var constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> token = (TypeToken<?>) constructor.newInstance(type);
    assertEquals(type, token.getType());
    assertEquals(String.class, token.getRawType());
    assertEquals(type.hashCode(), token.hashCode());
  }

  // Test getRawType returns correct raw type
  @Test
    @Timeout(8000)
  void testGetRawType() {
    TypeToken<String> token = new TypeToken<String>() {};
    assertEquals(String.class, token.getRawType());
  }

  // Test getType returns the correct type
  @Test
    @Timeout(8000)
  void testGetType() {
    TypeToken<List<String>> token = new TypeToken<List<String>>() {};
    Type type = token.getType();
    assertNotNull(type);
    assertTrue(type instanceof ParameterizedType);
  }

  // Test equals and hashCode consistency
  @Test
    @Timeout(8000)
  void testEqualsAndHashCode() {
    TypeToken<List<String>> token1 = new TypeToken<List<String>>() {};
    TypeToken<List<String>> token2 = new TypeToken<List<String>>() {};
    TypeToken<List<Integer>> token3 = new TypeToken<List<Integer>>() {};

    assertEquals(token1, token2);
    assertEquals(token1.hashCode(), token2.hashCode());
    assertNotEquals(token1, token3);
  }

  // Test toString returns type's toString
  @Test
    @Timeout(8000)
  void testToString() {
    TypeToken<String> token = new TypeToken<String>() {};
    assertEquals(token.getType().toString(), token.toString());
  }

  // Test deprecated isAssignableFrom(Class<?>)
  @Test
    @Timeout(8000)
  void testIsAssignableFromClass() {
    TypeToken<Number> numberToken = new TypeToken<Number>() {};
    assertTrue(numberToken.isAssignableFrom(Integer.class));
    assertFalse(numberToken.isAssignableFrom(String.class));
  }

  // Test deprecated isAssignableFrom(Type)
  @Test
    @Timeout(8000)
  void testIsAssignableFromType() {
    TypeToken<Number> numberToken = new TypeToken<Number>() {};
    assertTrue(numberToken.isAssignableFrom(Integer.class));
    assertFalse(numberToken.isAssignableFrom(String.class));
  }

  // Test deprecated isAssignableFrom(TypeToken<?>)
  @Test
    @Timeout(8000)
  void testIsAssignableFromTypeToken() {
    TypeToken<Number> numberToken = new TypeToken<Number>() {};
    TypeToken<Integer> integerToken = new TypeToken<Integer>() {};
    TypeToken<String> stringToken = new TypeToken<String>() {};

    assertTrue(numberToken.isAssignableFrom(integerToken));
    assertFalse(numberToken.isAssignableFrom(stringToken));
  }

  // Test static get(Type) method
  @Test
    @Timeout(8000)
  void testStaticGetType() {
    TypeToken<?> token = TypeToken.get(String.class);
    assertEquals(String.class, token.getRawType());
  }

  // Test static get(Class<T>) method
  @Test
    @Timeout(8000)
  void testStaticGetClass() {
    TypeToken<String> token = TypeToken.get(String.class);
    assertEquals(String.class, token.getRawType());
  }

  // Test static getParameterized method
  @Test
    @Timeout(8000)
  void testGetParameterized() {
    Type rawType = List.class;
    Type[] typeArgs = new Type[] {String.class};
    TypeToken<?> token = TypeToken.getParameterized(rawType, typeArgs);
    assertNotNull(token);
    assertTrue(token.getType() instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) token.getType();
    assertEquals(rawType, pt.getRawType());
    assertArrayEquals(typeArgs, pt.getActualTypeArguments());
  }

  // Test static getArray method
  @Test
    @Timeout(8000)
  void testGetArray() {
    TypeToken<?> token = TypeToken.getArray(String.class);
    assertNotNull(token);
    assertTrue(token.getType() instanceof GenericArrayType || token.getType() instanceof Class);
  }
}