package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeToken_isAssignableFrom_Test {

  private TypeToken<Object> typeToken;

  @BeforeEach
  void setUp() {
    typeToken = new TypeToken<Object>() {};
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withClassAssignable() {
    // Object is assignable from String.class? Yes
    TypeToken<Object> objectToken = new TypeToken<Object>() {};
    assertTrue(objectToken.isAssignableFrom(String.class));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withClassNotAssignable() {
    TypeToken<String> stringToken = new TypeToken<String>() {};
    assertFalse(stringToken.isAssignableFrom(Object.class));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withNullClass() {
    assertThrows(NullPointerException.class, () -> typeToken.isAssignableFrom((Class<?>) null));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withTypeAssignable() throws Exception {
    Type stringType = String.class;
    // Use reflection to invoke private isAssignableFrom(Type)
    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    method.setAccessible(true);
    Boolean result = (Boolean) method.invoke(typeToken, stringType);
    assertTrue(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withTypeNotAssignable() throws Exception {
    Type objectType = Object.class;
    TypeToken<String> stringToken = new TypeToken<String>() {};
    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    method.setAccessible(true);
    Boolean result = (Boolean) method.invoke(stringToken, objectType);
    assertFalse(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withTypeTokenAssignable() {
    TypeToken<Object> objectToken = new TypeToken<Object>() {};
    TypeToken<String> stringToken = new TypeToken<String>() {};
    assertTrue(objectToken.isAssignableFrom(stringToken));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withTypeTokenNotAssignable() {
    TypeToken<String> stringToken = new TypeToken<String>() {};
    TypeToken<Object> objectToken = new TypeToken<Object>() {};
    assertFalse(stringToken.isAssignableFrom(objectToken));
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withGenericArrayType() throws Exception {
    // Create a GenericArrayType mock
    GenericArrayType genericArrayType = mock(GenericArrayType.class);
    when(genericArrayType.getGenericComponentType()).thenReturn(String.class);

    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    method.setAccessible(true);
    Boolean result = (Boolean) method.invoke(typeToken, genericArrayType);
    // We can't be sure, but the method should not throw and return boolean
    assertNotNull(result);
  }

  @Test
    @Timeout(8000)
  void isAssignableFrom_withParameterizedType() throws Exception {
    ParameterizedType parameterizedType = mock(ParameterizedType.class);
    when(parameterizedType.getRawType()).thenReturn(Object.class);
    when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[] {String.class});

    var method = TypeToken.class.getDeclaredMethod("isAssignableFrom", Type.class);
    method.setAccessible(true);
    Boolean result = (Boolean) method.invoke(typeToken, parameterizedType);
    assertNotNull(result);
  }
}