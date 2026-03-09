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
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

class TypeToken_122_3Test {

  @Test
    @Timeout(8000)
  void testProtectedConstructor_setsTypeRawTypeAndHashCode() throws Exception {
    // Use anonymous subclass to call protected constructor
    TypeToken<String> token = new TypeToken<String>() {};
    // Access private fields via reflection
    Field typeField = TypeToken.class.getDeclaredField("type");
    typeField.setAccessible(true);
    Type type = (Type) typeField.get(token);

    Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);
    Class<?> rawType = (Class<?>) rawTypeField.get(token);

    Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);
    int hashCode = hashCodeField.getInt(token);

    // The type should be String.class or a ParameterizedType representing String
    assertNotNull(type);
    assertEquals(String.class, rawType);
    assertEquals(type.hashCode(), hashCode);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_setsFields() throws Exception {
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    Type type = String.class;
    try (MockedStatic<$Gson$Types> mockedStatic = Mockito.mockStatic($Gson$Types.class)) {
      mockedStatic.when(() -> $Gson$Types.getRawType(type)).thenReturn((Class) String.class);
      TypeToken<String> token = constructor.newInstance(type);
      // Validate fields
      Field typeField = TypeToken.class.getDeclaredField("type");
      typeField.setAccessible(true);
      assertEquals(type, typeField.get(token));

      Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
      rawTypeField.setAccessible(true);
      assertEquals(String.class, rawTypeField.get(token));

      Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
      hashCodeField.setAccessible(true);
      assertEquals(type.hashCode(), hashCodeField.getInt(token));
    }
  }

  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument_returnsCorrectType() throws Exception {
    TypeToken<String> token = new TypeToken<String>() {};
    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);
    Type type = (Type) method.invoke(token);
    assertNotNull(type);
    assertEquals(String.class, $Gson$Types.getRawType(type));
  }

  @Test
    @Timeout(8000)
  void testGetRawTypeAndGetType() {
    TypeToken<String> token = new TypeToken<String>() {};
    assertEquals(String.class, token.getRawType());
    assertEquals(String.class, token.getType());
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFromClass() {
    TypeToken<String> token = new TypeToken<String>() {};
    assertTrue(token.isAssignableFrom(String.class));
    assertFalse(token.isAssignableFrom(Integer.class));
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFromType() {
    TypeToken<String> token = new TypeToken<String>() {};
    assertTrue(token.isAssignableFrom(String.class));
    assertFalse(token.isAssignableFrom(Integer.class));
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFromTypeToken() {
    TypeToken<String> token = new TypeToken<String>() {};
    TypeToken<String> token2 = new TypeToken<String>() {};
    TypeToken<Integer> token3 = new TypeToken<Integer>() {};
    assertTrue(token.isAssignableFrom(token2));
    assertFalse(token.isAssignableFrom(token3));
  }

  @Test
    @Timeout(8000)
  void testStaticIsAssignableFrom_GenericArrayType() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);

    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    when(genericArrayType.getGenericComponentType()).thenReturn(String.class);

    // from is a Class array type assignable to genericArrayType
    Type from = String[].class;

    boolean result = (boolean) method.invoke(null, from, genericArrayType);
    assertTrue(result);

    // from is not assignable
    from = Integer.class;
    result = (boolean) method.invoke(null, from, genericArrayType);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testStaticIsAssignableFrom_ParameterizedType() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);

    ParameterizedType to = mock(ParameterizedType.class);
    when(to.getRawType()).thenReturn(Map.class);
    when(to.getActualTypeArguments()).thenReturn(new Type[] {String.class, Integer.class});

    Map<String, Type> typeVarMap = new HashMap<>();

    // from is assignable parameterized type Map<String,Integer>
    ParameterizedType from = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return new Type[] {String.class, Integer.class};
      }
      @Override public Type getRawType() {
        return Map.class;
      }
      @Override public Type getOwnerType() {
        return null;
      }
    };

    boolean result = (boolean) method.invoke(null, from, to, typeVarMap);
    assertTrue(result);

    // from is not assignable: different raw type
    ParameterizedType from2 = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {
        return new Type[] {String.class, Integer.class};
      }
      @Override public Type getRawType() {
        return String.class;
      }
      @Override public Type getOwnerType() {
        return null;
      }
    };
    result = (boolean) method.invoke(null, from2, to, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testStaticTypeEquals() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);

    ParameterizedType p1 = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {return new Type[] {String.class};}
      @Override public Type getRawType() {return Map.class;}
      @Override public Type getOwnerType() {return null;}
    };

    ParameterizedType p2 = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {return new Type[] {String.class};}
      @Override public Type getRawType() {return Map.class;}
      @Override public Type getOwnerType() {return null;}
    };

    Map<String, Type> typeVarMap = new HashMap<>();
    boolean result = (boolean) method.invoke(null, p1, p2, typeVarMap);
    assertTrue(result);

    ParameterizedType p3 = new ParameterizedType() {
      @Override public Type[] getActualTypeArguments() {return new Type[] {Integer.class};}
      @Override public Type getRawType() {return Map.class;}
      @Override public Type getOwnerType() {return null;}
    };
    result = (boolean) method.invoke(null, p1, p3, typeVarMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testStaticBuildUnexpectedTypeError() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);

    Type type = String.class;
    AssertionError error = (AssertionError) method.invoke(null, type, new Class[] {Integer.class, Double.class});
    assertTrue(error.getMessage().contains("Expected one of"));
    assertTrue(error.getMessage().contains("java.lang.String"));
  }

  @Test
    @Timeout(8000)
  void testStaticMatches() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);

    Map<String, Type> typeMap = new HashMap<>();
    Type from = String.class;
    Type to = String.class;
    boolean result = (boolean) method.invoke(null, from, to, typeMap);
    assertTrue(result);

    to = Object.class;
    result = (boolean) method.invoke(null, from, to, typeMap);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void testHashCodeAndEquals() {
    TypeToken<String> token1 = new TypeToken<String>() {};
    TypeToken<String> token2 = new TypeToken<String>() {};
    TypeToken<Integer> token3 = new TypeToken<Integer>() {};

    assertEquals(token1.hashCode(), token2.hashCode());
    assertEquals(token1, token2);
    assertNotEquals(token1, token3);
    assertNotEquals(token1, null);
    assertNotEquals(token1, "string");
  }

  @Test
    @Timeout(8000)
  void testToString() {
    TypeToken<String> token = new TypeToken<String>() {};
    String str = token.toString();
    assertNotNull(str);
    assertTrue(str.contains("java.lang.String"));
  }

  @Test
    @Timeout(8000)
  void testStaticGetTypeToken() {
    TypeToken<?> token = TypeToken.get(String.class);
    assertNotNull(token);
    assertEquals(String.class, token.getRawType());

    TypeToken<?> token2 = TypeToken.get(String.class);
    assertEquals(token2, token);
  }

  @Test
    @Timeout(8000)
  void testStaticGetParameterized() {
    TypeToken<?> token = TypeToken.getParameterized(Map.class, String.class, Integer.class);
    assertNotNull(token);
    assertEquals(Map.class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  void testStaticGetArray() {
    TypeToken<?> token = TypeToken.getArray(String.class);
    assertNotNull(token);
    assertTrue(token.getType() instanceof GenericArrayType || token.getType() instanceof Class);
  }
}