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

class TypeToken_122_5Test {

  TypeToken<?> typeToken;

  @BeforeEach
  void setup() {
    typeToken = new TypeToken<Object>() {};
  }

  @Test
    @Timeout(8000)
  void testProtectedConstructor_setsTypeAndRawTypeAndHashCode() throws Exception {
    TypeToken<String> token = new TypeToken<String>() {};
    // Using reflection to get private fields
    Field typeField = TypeToken.class.getDeclaredField("type");
    typeField.setAccessible(true);
    Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);
    Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);

    Type type = (Type) typeField.get(token);
    Class<?> rawType = (Class<?>) rawTypeField.get(token);
    int hashCode = hashCodeField.getInt(token);

    assertNotNull(type);
    assertNotNull(rawType);
    assertEquals(type.hashCode(), hashCode);
    assertEquals(rawType, $Gson$Types.getRawType(type));
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_setsFieldsCorrectly() throws Exception {
    Type someType = String.class;
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> token = constructor.newInstance(someType);

    Field typeField = TypeToken.class.getDeclaredField("type");
    typeField.setAccessible(true);
    Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);
    Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);

    Type type = (Type) typeField.get(token);
    Class<?> rawType = (Class<?>) rawTypeField.get(token);
    int hashCode = hashCodeField.getInt(token);

    assertEquals(someType, type);
    assertEquals(String.class, rawType);
    assertEquals(type.hashCode(), hashCode);
  }

  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument_returnsCorrectType() throws Exception {
    TypeToken<String> token = new TypeToken<String>() {};
    // private method
    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);
    Type type = (Type) method.invoke(token);
    assertNotNull(type);
    assertTrue(type instanceof ParameterizedType || type instanceof Class);
  }

  @Test
    @Timeout(8000)
  void testGetRawType_returnsRawType() throws Exception {
    Class<?> rawType = typeToken.getRawType();

    // Access private field rawType via reflection
    Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);
    Object rawTypeFieldValue = rawTypeField.get(typeToken);

    assertNotNull(rawType);
    assertEquals(rawTypeFieldValue, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetType_returnsType() throws Exception {
    Type type = typeToken.getType();

    // Access private field type via reflection
    Field typeField = TypeToken.class.getDeclaredField("type");
    typeField.setAccessible(true);
    Object typeFieldValue = typeField.get(typeToken);

    assertNotNull(type);
    assertEquals(typeFieldValue, type);
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFromClass_deprecated() {
    TypeToken<String> stringToken = new TypeToken<String>() {};
    assertTrue(stringToken.isAssignableFrom(String.class));
    assertFalse(stringToken.isAssignableFrom(Integer.class));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFromType_deprecated() {
    TypeToken<String> stringToken = new TypeToken<String>() {};
    assertTrue(stringToken.isAssignableFrom(String.class));
    assertFalse(stringToken.isAssignableFrom(Integer.class));
  }

  @Test
    @Timeout(8000)
  void testIsAssignableFromTypeToken_deprecated() {
    TypeToken<String> stringToken = new TypeToken<String>() {};
    TypeToken<Object> objectToken = new TypeToken<Object>() {};
    assertTrue(objectToken.isAssignableFrom(stringToken));
    assertFalse(stringToken.isAssignableFrom(objectToken));
  }

  @Test
    @Timeout(8000)
  void testStaticIsAssignableFrom_GenericArrayType() throws Exception {
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    Type from = String[].class;
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(null, from, genericArrayType);
    // No exception, boolean returned
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testStaticIsAssignableFrom_ParameterizedType() throws Exception {
    // Create a real ParameterizedType instance for 'from'
    ParameterizedType from = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    // Create a real ParameterizedType instance for 'to'
    ParameterizedType to = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {Object.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    Map<String, Type> typeVarMap = new HashMap<>();
    Method method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(null, from, to, typeVarMap);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testStaticTypeEquals() throws Exception {
    // Create a real ParameterizedType instance for 'from'
    ParameterizedType from = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    // Create a real ParameterizedType instance for 'to'
    ParameterizedType to = new ParameterizedType() {
      @Override
      public Type[] getActualTypeArguments() {
        return new Type[] {String.class};
      }
      @Override
      public Type getRawType() {
        return Map.class;
      }
      @Override
      public Type getOwnerType() {
        return null;
      }
    };

    Map<String, Type> typeVarMap = new HashMap<>();
    Method method = TypeToken.class.getDeclaredMethod("typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    method.setAccessible(true);
    boolean result = (boolean) method.invoke(null, from, to, typeVarMap);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testBuildUnexpectedTypeError() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    method.setAccessible(true);
    Type type = String.class;
    AssertionError error = (AssertionError) method.invoke(null, type, new Class<?>[]{Integer.class, Double.class});
    assertNotNull(error);
    assertTrue(error.getMessage().contains(String.class.getName()));
  }

  @Test
    @Timeout(8000)
  void testMatches() throws Exception {
    Method method = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    method.setAccessible(true);
    Type from = String.class;
    Type to = String.class;
    Map<String, Type> typeMap = new HashMap<>();
    boolean result = (boolean) method.invoke(null, from, to, typeMap);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void testHashCode_equals_toString() {
    TypeToken<String> token1 = new TypeToken<String>() {};
    TypeToken<String> token2 = new TypeToken<String>() {};
    assertEquals(token1.hashCode(), token2.hashCode());
    assertEquals(token1, token2);
    assertEquals(token1.toString(), token2.toString());
  }

  @Test
    @Timeout(8000)
  void testStaticGet_methods() {
    TypeToken<?> token1 = TypeToken.get(String.class);
    assertEquals(String.class, token1.getRawType());

    TypeToken<?> token2 = TypeToken.get(String.class);
    assertEquals(String.class, token2.getRawType());

    TypeToken<?> token3 = TypeToken.getParameterized(Map.class, String.class, Integer.class);
    assertNotNull(token3);
    assertEquals(Map.class, token3.getRawType());

    TypeToken<?> token4 = TypeToken.getArray(String.class);
    assertNotNull(token4);
    assertTrue(token4.getType() instanceof GenericArrayType || token4.getType() instanceof Class);
  }
}