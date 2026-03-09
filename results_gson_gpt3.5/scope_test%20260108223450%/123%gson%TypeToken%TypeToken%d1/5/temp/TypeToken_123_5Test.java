package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import java.util.Map;
import java.util.HashMap;
import java.util.Objects;
import org.junit.jupiter.api.Test;

class TypeToken_123_5Test {

  @Test
    @Timeout(8000)
  void testPrivateConstructorWithValidType() throws Exception {
    Type type = String.class;
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> instance = constructor.newInstance(type);

    // Verify fields
    Field typeField = TypeToken.class.getDeclaredField("type");
    typeField.setAccessible(true);
    Type actualType = (Type) typeField.get(instance);
    assertEquals($Gson$Types.canonicalize(type), actualType);

    Field rawTypeField = TypeToken.class.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);
    Class<?> rawType = (Class<?>) rawTypeField.get(instance);
    assertEquals($Gson$Types.getRawType(actualType), rawType);

    Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);
    int hashCode = hashCodeField.getInt(instance);
    assertEquals(actualType.hashCode(), hashCode);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructorWithNullTypeThrowsNPE() throws Exception {
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    Throwable exception = assertThrows(InvocationTargetException.class, () -> {
      constructor.newInstance((Object) null);
    });
    assertNotNull(exception);
    // Check cause is NullPointerException
    assertTrue(exception.getCause() instanceof NullPointerException);
  }

  @Test
    @Timeout(8000)
  void testGetRawTypeAndGetType() throws Exception {
    TypeToken<String> token = TypeToken.get(String.class);
    assertEquals(String.class, token.getRawType());
    assertEquals(String.class, token.getType());
  }

  @Test
    @Timeout(8000)
  void testEqualsAndHashCode() throws Exception {
    TypeToken<String> token1 = TypeToken.get(String.class);
    TypeToken<String> token2 = TypeToken.get(String.class);
    TypeToken<Integer> token3 = TypeToken.get(Integer.class);

    assertEquals(token1, token2);
    assertEquals(token1.hashCode(), token2.hashCode());

    assertNotEquals(token1, token3);
    assertNotEquals(token1.hashCode(), token3.hashCode());

    assertNotEquals(token1, null);
    assertNotEquals(token1, "some string");
  }

  @Test
    @Timeout(8000)
  void testToString() {
    TypeToken<String> token = TypeToken.get(String.class);
    String toString = token.toString();
    assertTrue(toString.contains("java.lang.String"));
  }

  @Test
    @Timeout(8000)
  void testGetStaticMethods() {
    TypeToken<?> token = TypeToken.get(String.class);
    assertNotNull(token);

    TypeToken<?> parameterized = TypeToken.getParameterized(Map.class, String.class, Integer.class);
    assertNotNull(parameterized);
    assertTrue(parameterized.getType() instanceof ParameterizedType);

    TypeToken<?> arrayToken = TypeToken.getArray(String.class);
    assertNotNull(arrayToken);
    assertTrue(arrayToken.getType() instanceof GenericArrayType || arrayToken.getType() instanceof Class);
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFromMethods() {
    TypeToken<String> token = TypeToken.get(String.class);

    assertTrue(token.isAssignableFrom(String.class));
    assertTrue(token.isAssignableFrom((Type) String.class));
    assertTrue(token.isAssignableFrom(TypeToken.get(String.class)));

    assertFalse(token.isAssignableFrom(Integer.class));
    assertFalse(token.isAssignableFrom((Type) Integer.class));
    assertFalse(token.isAssignableFrom(TypeToken.get(Integer.class)));
  }

  @Test
    @Timeout(8000)
  void testPrivateGetTypeTokenTypeArgument() throws Exception {
    // Create anonymous direct subclass to satisfy "Must only create direct subclasses of TypeToken"
    TypeToken<String> token = new TypeToken<String>(){};
    var method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);
    Type result = (Type) method.invoke(token);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testPrivateStaticIsAssignableFromMethods() throws Exception {
    Method isAssignableFromGenericArrayType = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, GenericArrayType.class);
    isAssignableFromGenericArrayType.setAccessible(true);

    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    when(genericArrayType.getGenericComponentType()).thenReturn(String.class);

    Boolean result = (Boolean) isAssignableFromGenericArrayType.invoke(null, String[].class, genericArrayType);
    assertNotNull(result);

    Method isAssignableFromParameterizedType = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class, ParameterizedType.class, Map.class);
    isAssignableFromParameterizedType.setAccessible(true);

    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    Map<String, Type> typeVarMap = new HashMap<>();
    Boolean paramResult = (Boolean) isAssignableFromParameterizedType.invoke(null, String.class, parameterizedType, typeVarMap);
    assertNotNull(paramResult);
  }

  @Test
    @Timeout(8000)
  void testPrivateStaticTypeEquals() throws Exception {
    Method typeEquals = TypeToken.class.getDeclaredMethod("typeEquals", ParameterizedType.class, ParameterizedType.class, Map.class);
    typeEquals.setAccessible(true);

    ParameterizedType pt1 = mock(ParameterizedType.class);
    ParameterizedType pt2 = mock(ParameterizedType.class);
    Map<String, Type> map = new HashMap<>();

    // Setup mocks to avoid NullPointerException inside typeEquals
    when(pt1.getRawType()).thenReturn(String.class);
    when(pt2.getRawType()).thenReturn(String.class);
    when(pt1.getOwnerType()).thenReturn(null);
    when(pt2.getOwnerType()).thenReturn(null);
    when(pt1.getActualTypeArguments()).thenReturn(new Type[0]);
    when(pt2.getActualTypeArguments()).thenReturn(new Type[0]);

    Boolean result = (Boolean) typeEquals.invoke(null, pt1, pt2, map);
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void testPrivateStaticBuildUnexpectedTypeError() throws Exception {
    Method buildUnexpectedTypeError = TypeToken.class.getDeclaredMethod("buildUnexpectedTypeError", Type.class, Class[].class);
    buildUnexpectedTypeError.setAccessible(true);

    AssertionError error = (AssertionError) buildUnexpectedTypeError.invoke(null, String.class, new Class[]{Integer.class});
    assertTrue(error.getMessage().contains("Expected one of"));
  }

  @Test
    @Timeout(8000)
  void testPrivateStaticMatches() throws Exception {
    Method matches = TypeToken.class.getDeclaredMethod("matches", Type.class, Type.class, Map.class);
    matches.setAccessible(true);

    Map<String, Type> map = new HashMap<>();
    Boolean result = (Boolean) matches.invoke(null, String.class, String.class, map);
    assertTrue(result);

    result = (Boolean) matches.invoke(null, String.class, Integer.class, map);
    assertFalse(result);
  }
}