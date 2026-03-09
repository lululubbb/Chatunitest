package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import java.util.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TypeToken_122_4Test {

  // Test the protected no-arg constructor and its behavior
  @Test
    @Timeout(8000)
  void testNoArgConstructor_andGetters() throws Exception {
    // Create an anonymous subclass of TypeToken to capture generic type
    TypeToken<List<String>> token = new TypeToken<List<String>>() {};

    // Verify that getType returns a ParameterizedType representing List<String>
    Type type = token.getType();
    assertNotNull(type);
    assertTrue(type instanceof ParameterizedType);
    ParameterizedType pType = (ParameterizedType) type;
    assertEquals(List.class, pType.getRawType());
    assertEquals(String.class, pType.getActualTypeArguments()[0]);

    // Verify rawType is List.class or supertype
    Class<?> rawType = token.getRawType();
    assertNotNull(rawType);
    assertTrue(List.class.isAssignableFrom(rawType));

    // hashCode equals type.hashCode()
    assertEquals(type.hashCode(), token.hashCode());
  }

  // Test the private constructor TypeToken(Type)
  @Test
    @Timeout(8000)
  void testPrivateConstructor_withType() throws Exception {
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);

    Type paramType = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() { return new Type[] {String.class}; }
      @Override public Type getRawType() { return List.class; }
      @Override public Type getOwnerType() { return null; }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return Objects.equals(getRawType(), that.getRawType()) &&
               Arrays.equals(getActualTypeArguments(), that.getActualTypeArguments()) &&
               Objects.equals(getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return Arrays.hashCode(getActualTypeArguments()) ^
               Objects.hashCode(getRawType()) ^
               Objects.hashCode(getOwnerType());
      }
      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(((Class<?>)getRawType()).getName());
        if (getActualTypeArguments() != null && getActualTypeArguments().length > 0) {
          sb.append("<");
          boolean first = true;
          for (Type t : getActualTypeArguments()) {
            if (!first) sb.append(", ");
            sb.append(t.getTypeName());
            first = false;
          }
          sb.append(">");
        }
        return sb.toString();
      }
    };

    TypeToken<?> token = constructor.newInstance(paramType);

    assertNotNull(token);
    assertEquals(paramType, token.getType());
    assertEquals(List.class, token.getRawType());
    assertEquals(paramType.hashCode(), token.hashCode());
  }

  // Test private method getTypeTokenTypeArgument() via reflection
  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument() throws Exception {
    TypeToken<List<String>> token = new TypeToken<List<String>>() {};
    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);
    Type result = (Type) method.invoke(token);
    assertNotNull(result);
    assertTrue(result instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) result;
    assertEquals(List.class, pt.getRawType());
    assertEquals(String.class, pt.getActualTypeArguments()[0]);
  }

  // Test deprecated isAssignableFrom(Class<?>)
  @Test
    @Timeout(8000)
  void testIsAssignableFromClass() {
    TypeToken<List> token = new TypeToken<List>() {};
    assertTrue(token.isAssignableFrom(ArrayList.class));
    assertFalse(token.isAssignableFrom(String.class));
  }

  // Test deprecated isAssignableFrom(Type)
  @Test
    @Timeout(8000)
  void testIsAssignableFromType() throws Exception {
    TypeToken<List> token = new TypeToken<List>() {};
    assertTrue(token.isAssignableFrom(new TypeToken<ArrayList>() {}.getType()));
    assertFalse(token.isAssignableFrom(String.class));

    // Test with GenericArrayType
    // Create a proper GenericArrayType for testing
    GenericArrayType arrayType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return String.class;
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericArrayType)) return false;
        GenericArrayType that = (GenericArrayType) o;
        return Objects.equals(getGenericComponentType(), that.getGenericComponentType());
      }
      @Override
      public int hashCode() {
        return Objects.hashCode(getGenericComponentType());
      }
      @Override
      public String toString() {
        return getGenericComponentType().getTypeName() + "[]";
      }
    };

    // Use reflection to invoke private static isAssignableFrom(Type, GenericArrayType)
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);

    // Test isAssignableFrom with ArrayList.class (which is not a generic array)
    boolean result1 = (boolean) method.invoke(null, ArrayList.class, arrayType);
    assertFalse(result1);

    // Test isAssignableFrom with String[].class (which is an array class)
    boolean result2 = (boolean) method.invoke(null, String[].class, arrayType);
    assertTrue(result2);
  }

  // Test deprecated isAssignableFrom(TypeToken<?>)
  @Test
    @Timeout(8000)
  void testIsAssignableFromTypeToken() {
    TypeToken<List> token = new TypeToken<List>() {};
    TypeToken<ArrayList> arrayListToken = new TypeToken<ArrayList>() {};
    TypeToken<String> stringToken = new TypeToken<String>() {};
    assertTrue(token.isAssignableFrom(arrayListToken));
    assertFalse(token.isAssignableFrom(stringToken));
  }

  // Test equals and hashCode consistency
  @Test
    @Timeout(8000)
  void testEqualsAndHashCode() {
    TypeToken<List<String>> token1 = new TypeToken<List<String>>() {};
    TypeToken<List<String>> token2 = new TypeToken<List<String>>() {};
    TypeToken<List<Integer>> token3 = new TypeToken<List<Integer>>() {};
    TypeToken<String> token4 = new TypeToken<String>() {};

    assertEquals(token1, token2);
    assertEquals(token1.hashCode(), token2.hashCode());
    assertNotEquals(token1, token3);
    assertNotEquals(token1, token4);
    assertNotEquals(token1, null);
    assertNotEquals(token1, "some string");
  }

  // Test toString returns type.toString()
  @Test
    @Timeout(8000)
  void testToString() {
    TypeToken<List<String>> token = new TypeToken<List<String>>() {};
    assertEquals(token.getType().toString(), token.toString());
  }

  // Test static method get(Type)
  @Test
    @Timeout(8000)
  void testStaticGetWithType() {
    Type type = new TypeToken<Map<String, Integer>>() {}.getType();
    TypeToken<?> token = TypeToken.get(type);
    assertNotNull(token);
    assertEquals(type, token.getType());
  }

  // Test static method get(Class<T>)
  @Test
    @Timeout(8000)
  void testStaticGetWithClass() {
    TypeToken<String> token = TypeToken.get(String.class);
    assertNotNull(token);
    assertEquals(String.class, token.getType());
    assertEquals(String.class, token.getRawType());
  }

  // Test static method getParameterized(Type, Type...)
  @Test
    @Timeout(8000)
  void testStaticGetParameterized() {
    TypeToken<?> token = TypeToken.getParameterized(Map.class, String.class, Integer.class);
    assertNotNull(token);
    Type type = token.getType();
    assertTrue(type instanceof ParameterizedType);
    ParameterizedType pt = (ParameterizedType) type;
    assertEquals(Map.class, pt.getRawType());
    assertArrayEquals(new Type[] {String.class, Integer.class}, pt.getActualTypeArguments());
  }

  // Test static method getArray(Type)
  @Test
    @Timeout(8000)
  void testStaticGetArray() {
    TypeToken<?> token = TypeToken.getArray(String.class);
    assertNotNull(token);
    Type type = token.getType();
    assertTrue(type instanceof GenericArrayType || type instanceof Class<?>);
    if (type instanceof Class<?>) {
      assertTrue(((Class<?>) type).isArray());
      assertEquals(String.class, ((Class<?>) type).getComponentType());
    }
  }
}