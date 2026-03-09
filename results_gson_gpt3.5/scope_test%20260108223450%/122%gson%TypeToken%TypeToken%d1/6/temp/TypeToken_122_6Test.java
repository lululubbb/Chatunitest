package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class TypeToken_122_6Test {

  @Test
    @Timeout(8000)
  void testProtectedConstructor_setsTypeRawTypeAndHashCode() throws Exception {
    // Create anonymous subclass to capture generic type argument
    TypeToken<String> token = new TypeToken<String>() {};

    // Use reflection to access private fields
    var typeField = TypeToken.class.getDeclaredField("type");
    typeField.setAccessible(true);
    Type type = (Type) typeField.get(token);
    assertNotNull(type);

    var rawTypeField = TypeToken.class.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);
    Class<?> rawType = (Class<?>) rawTypeField.get(token);
    assertEquals(String.class, rawType);

    var hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);
    int hashCode = hashCodeField.getInt(token);
    assertEquals(type.hashCode(), hashCode);
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor_setsFieldsCorrectly() throws Exception {
    Type stringType = String.class;
    var ctor = TypeToken.class.getDeclaredConstructor(Type.class);
    ctor.setAccessible(true);
    TypeToken<?> token = (TypeToken<?>) ctor.newInstance(stringType);

    var typeField = TypeToken.class.getDeclaredField("type");
    typeField.setAccessible(true);
    Type type = (Type) typeField.get(token);
    assertEquals(stringType, type);

    var rawTypeField = TypeToken.class.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);
    Class<?> rawType = (Class<?>) rawTypeField.get(token);
    assertEquals(String.class, rawType);

    var hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);
    int expectedHashCode = stringType.hashCode();
    assertEquals(expectedHashCode, hashCodeField.getInt(token));
  }

  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument_returnsType() throws Exception {
    TypeToken<String> token = new TypeToken<String>() {};
    var method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);
    Type result = (Type) method.invoke(token);
    assertNotNull(result);
    assertEquals(String.class, $Gson$Types.getRawType(result));
  }

  @Test
    @Timeout(8000)
  void testGetRawType_returnsRawType() throws Exception {
    TypeToken<Integer> token = new TypeToken<Integer>() {};
    Class<?> rawType = token.getRawType();
    assertEquals(Integer.class, rawType);
  }

  @Test
    @Timeout(8000)
  void testGetType_returnsType() throws Exception {
    TypeToken<Double> token = new TypeToken<Double>() {};
    Type type = token.getType();
    assertEquals(Double.class, $Gson$Types.getRawType(type));
  }

  @Test
    @Timeout(8000)
  void testEqualsAndHashCode() {
    TypeToken<String> token1 = new TypeToken<String>() {};
    TypeToken<String> token2 = new TypeToken<String>() {};
    TypeToken<Integer> token3 = new TypeToken<Integer>() {};

    assertEquals(token1, token2);
    assertEquals(token1.hashCode(), token2.hashCode());
    assertNotEquals(token1, token3);
    assertNotEquals(token1.hashCode(), token3.hashCode());
  }

  @Test
    @Timeout(8000)
  void testToString_containsTypeName() {
    TypeToken<String> token = new TypeToken<String>() {};
    String str = token.toString();
    assertTrue(str.contains("java.lang.String"));
  }

  @Test
    @Timeout(8000)
  void testStaticGet_withType() {
    TypeToken<?> token = TypeToken.get(String.class);
    assertEquals(String.class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  void testStaticGet_withClass() {
    TypeToken<String> token = TypeToken.get(String.class);
    assertEquals(String.class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  void testStaticGetParameterized() {
    TypeToken<?> token = TypeToken.getParameterized(Map.class, String.class, Integer.class);
    assertEquals(Map.class, token.getRawType());
  }

  @Test
    @Timeout(8000)
  void testStaticGetArray() {
    TypeToken<?> token = TypeToken.getArray(String.class);
    assertTrue(token.getType().getTypeName().contains("[]"));
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFrom_withClass() {
    TypeToken<Number> token = new TypeToken<Number>() {};
    assertTrue(token.isAssignableFrom(Integer.class));
    assertFalse(token.isAssignableFrom(String.class));
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFrom_withType() {
    TypeToken<Number> token = new TypeToken<Number>() {};
    assertTrue(token.isAssignableFrom(Integer.class));
    assertFalse(token.isAssignableFrom(String.class));
  }

  @Test
    @Timeout(8000)
  void testDeprecatedIsAssignableFrom_withToken() {
    TypeToken<Number> token = new TypeToken<Number>() {};
    TypeToken<Integer> intToken = new TypeToken<Integer>() {};
    TypeToken<String> strToken = new TypeToken<String>() {};
    assertTrue(token.isAssignableFrom(intToken));
    assertFalse(token.isAssignableFrom(strToken));
  }
}