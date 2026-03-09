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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.*;
import java.util.HashMap;
import java.util.Map;

class TypeToken_123_6Test {

  private TypeToken<?> typeTokenString;
  private TypeToken<?> typeTokenListString;
  private Type genericArrayType;
  private ParameterizedType parameterizedType;

  @BeforeEach
  void setUp() throws Exception {
    typeTokenString = TypeToken.get(String.class);

    // Create ParameterizedType for java.util.Map<String, Integer>
    parameterizedType = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{String.class, Integer.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return getRawType().equals(that.getRawType()) &&
               java.util.Arrays.equals(getActualTypeArguments(), that.getActualTypeArguments()) &&
               Objects.equals(getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getActualTypeArguments()) ^
               getRawType().hashCode() ^
               (getOwnerType() == null ? 0 : getOwnerType().hashCode());
      }
      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(((Class<?>) getRawType()).getName());
        sb.append("<");
        boolean first = true;
        for (Type t : getActualTypeArguments()) {
          if (!first) sb.append(", ");
          sb.append(t.getTypeName());
          first = false;
        }
        sb.append(">");
        return sb.toString();
      }
    };
    typeTokenListString = TypeToken.getParameterized(Map.class, String.class, Integer.class);

    // Create GenericArrayType String[]
    genericArrayType = new GenericArrayType() {
      @Override
      public Type getGenericComponentType() {
        return String.class;
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GenericArrayType)) return false;
        GenericArrayType that = (GenericArrayType) o;
        return getGenericComponentType().equals(that.getGenericComponentType());
      }
      @Override
      public int hashCode() {
        return getGenericComponentType().hashCode();
      }
      @Override
      public String toString() {
        return getGenericComponentType().getTypeName() + "[]";
      }
    };
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructorWithType() throws Exception {
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> token = constructor.newInstance(String.class);
    assertEquals(String.class, token.getRawType());
    assertEquals($Gson$Types.canonicalize(String.class), token.getType());
    assertEquals(token.getType().hashCode(), token.hashCode());
  }

  @Test
    @Timeout(8000)
  void testGetRawType() {
    assertEquals(String.class, typeTokenString.getRawType());
    assertEquals(Map.class, typeTokenListString.getRawType());
  }

  @Test
    @Timeout(8000)
  void testGetType() {
    assertEquals($Gson$Types.canonicalize(parameterizedType), $Gson$Types.canonicalize(typeTokenListString.getType()));
    assertEquals(String.class, typeTokenString.getType());
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFromClass() {
    assertTrue(typeTokenString.isAssignableFrom(String.class));
    assertFalse(typeTokenString.isAssignableFrom(Integer.class));
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFromType() {
    assertTrue(typeTokenString.isAssignableFrom(String.class));
    assertFalse(typeTokenString.isAssignableFrom(Integer.class));
    assertTrue(typeTokenListString.isAssignableFrom(parameterizedType));
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFromTypeToken() {
    TypeToken<String> stringToken = TypeToken.get(String.class);
    TypeToken<Integer> intToken = TypeToken.get(Integer.class);
    assertTrue(typeTokenString.isAssignableFrom(stringToken));
    assertFalse(typeTokenString.isAssignableFrom(intToken));
    assertTrue(typeTokenListString.isAssignableFrom(TypeToken.getParameterized(Map.class, String.class, Integer.class)));
  }

  @Test
    @Timeout(8000)
  void testPrivateIsAssignableFromGenericArrayType() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);

    // from is String[].class, to is genericArrayType (String[])
    Type from = Class.forName("[Ljava.lang.String;");
    Boolean result = (Boolean) method.invoke(null, from, genericArrayType);
    assertTrue(result);

    // from is Integer[].class, to is genericArrayType (String[])
    from = Class.forName("[Ljava.lang.Integer;");
    result = (Boolean) method.invoke(null, from, genericArrayType);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testPrivateIsAssignableFromParameterizedType() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);

    Map<String, Type> emptyMap = new HashMap<>();
    // from: Map<String, Integer>, to: parameterizedType Map<String, Integer>
    Boolean result = (Boolean) method.invoke(null, parameterizedType, parameterizedType, emptyMap);
    assertTrue(result);

    // from: Map<String, Integer>, to: Map<String, String>
    ParameterizedType toDifferent = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{String.class, String.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return getRawType().equals(that.getRawType()) &&
               java.util.Arrays.equals(getActualTypeArguments(), that.getActualTypeArguments()) &&
               Objects.equals(getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getActualTypeArguments()) ^
               getRawType().hashCode() ^
               (getOwnerType() == null ? 0 : getOwnerType().hashCode());
      }
      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(((Class<?>) getRawType()).getName());
        sb.append("<");
        boolean first = true;
        for (Type t : getActualTypeArguments()) {
          if (!first) sb.append(", ");
          sb.append(t.getTypeName());
          first = false;
        }
        sb.append(">");
        return sb.toString();
      }
    };
    result = (Boolean) method.invoke(null, parameterizedType, toDifferent, emptyMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testPrivateTypeEquals() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);

    Map<String, Type> emptyMap = new HashMap<>();

    ParameterizedType pt1 = parameterizedType;

    ParameterizedType pt2 = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{String.class, Integer.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return getRawType().equals(that.getRawType()) &&
               java.util.Arrays.equals(getActualTypeArguments(), that.getActualTypeArguments()) &&
               Objects.equals(getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getActualTypeArguments()) ^
               getRawType().hashCode() ^
               (getOwnerType() == null ? 0 : getOwnerType().hashCode());
      }
      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(((Class<?>) getRawType()).getName());
        sb.append("<");
        boolean first = true;
        for (Type t : getActualTypeArguments()) {
          if (!first) sb.append(", ");
          sb.append(t.getTypeName());
          first = false;
        }
        sb.append(">");
        return sb.toString();
      }
    };

    Boolean result = (Boolean) method.invoke(null, pt1, pt2, emptyMap);
    assertTrue(result);

    ParameterizedType pt3 = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[]{String.class, String.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
      @Override
      public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ParameterizedType)) return false;
        ParameterizedType that = (ParameterizedType) o;
        return getRawType().equals(that.getRawType()) &&
               java.util.Arrays.equals(getActualTypeArguments(), that.getActualTypeArguments()) &&
               Objects.equals(getOwnerType(), that.getOwnerType());
      }
      @Override
      public int hashCode() {
        return java.util.Arrays.hashCode(getActualTypeArguments()) ^
               getRawType().hashCode() ^
               (getOwnerType() == null ? 0 : getOwnerType().hashCode());
      }
      @Override
      public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(((Class<?>) getRawType()).getName());
        sb.append("<");
        boolean first = true;
        for (Type t : getActualTypeArguments()) {
          if (!first) sb.append(", ");
          sb.append(t.getTypeName());
          first = false;
        }
        sb.append(">");
        return sb.toString();
      }
    };

    result = (Boolean) method.invoke(null, pt1, pt3, emptyMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testPrivateBuildUnexpectedTypeError() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    Type type = String.class;
    Class<?>[] expected = new Class[]{Integer.class, Double.class};

    AssertionError error = (AssertionError) method.invoke(null, type, (Object) expected);
    assertTrue(error.getMessage().contains("Expected one of"));
    assertTrue(error.getMessage().contains("java.lang.String"));
  }

  @Test
    @Timeout(8000)
  void testPrivateMatches() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    Map<String, Type> emptyMap = new HashMap<>();

    Boolean result = (Boolean) method.invoke(null, String.class, String.class, emptyMap);
    assertTrue(result);

    result = (Boolean) method.invoke(null, Integer.class, String.class, emptyMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testHashCodeAndEquals() {
    TypeToken<String> token1 = TypeToken.get(String.class);
    TypeToken<String> token2 = TypeToken.get(String.class);
    TypeToken<Integer> token3 = TypeToken.get(Integer.class);

    assertEquals(token1.hashCode(), token2.hashCode());
    assertEquals(token1, token2);
    assertNotEquals(token1, token3);
    assertNotEquals(token1.hashCode(), token3.hashCode());
    assertNotEquals(token1, null);
    assertNotEquals(token1, new Object());
  }

  @Test
    @Timeout(8000)
  void testToString() {
    TypeToken<String> token = TypeToken.get(String.class);
    String str = token.toString();
    assertTrue(str.contains("java.lang.String"));
  }

  @Test
    @Timeout(8000)
  void testStaticGetMethods() throws Exception {
    TypeToken<?> token1 = TypeToken.get(String.class);
    assertEquals(String.class, token1.getRawType());

    TypeToken<?> token2 = TypeToken.get(parameterizedType);
    assertEquals(Map.class, token2.getRawType());

    TypeToken<?> token3 = TypeToken.getParameterized(Map.class, String.class, Integer.class);
    assertEquals(Map.class, token3.getRawType());

    TypeToken<?> token4 = TypeToken.getArray(String.class);
    assertEquals(Class.forName("[Ljava.lang.String;"), token4.getRawType());
  }
}