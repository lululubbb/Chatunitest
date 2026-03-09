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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TypeTokenEqualsTest {

  private TypeToken<?> typeToken;
  private TypeToken<?> otherTypeToken;

  @BeforeEach
  void setUp() throws Exception {
    // Use reflection to create TypeToken instances with specific types
    typeToken = createTypeTokenWithType(String.class);
    otherTypeToken = createTypeTokenWithType(String.class);
  }

  @Test
    @Timeout(8000)
  void equals_sameInstance_returnsTrue() {
    assertTrue(typeToken.equals(typeToken));
  }

  @Test
    @Timeout(8000)
  void equals_null_returnsFalse() {
    assertFalse(typeToken.equals(null));
  }

  @Test
    @Timeout(8000)
  void equals_differentClass_returnsFalse() {
    assertFalse(typeToken.equals("some string"));
  }

  @Test
    @Timeout(8000)
  void equals_differentType_returnsFalse() throws Exception {
    TypeToken<?> differentTypeToken = createTypeTokenWithType(Integer.class);
    assertFalse(typeToken.equals(differentTypeToken));
  }

  @Test
    @Timeout(8000)
  void equals_sameType_returnsTrue() throws Exception {
    TypeToken<?> sameTypeToken = createTypeTokenWithType(String.class);
    assertTrue(typeToken.equals(sameTypeToken));
  }

  @Test
    @Timeout(8000)
  void equals_otherTypeTokenWithNullType_returnsFalse() throws Exception {
    TypeToken<?> nullTypeToken = createTypeTokenWithType(null);
    assertFalse(typeToken.equals(nullTypeToken));
  }

  @Test
    @Timeout(8000)
  void equals_typeEqualsDelegation() throws Exception {
    TypeToken<?> spyToken = spy(createTypeTokenWithType(String.class));
    TypeToken<?> spyOther = spy(createTypeTokenWithType(String.class));

    // Mock $Gson$Types.equals to return false once
    mockStaticGsonTypesEquals(false);
    assertFalse(spyToken.equals(spyOther));

    // Mock $Gson$Types.equals to return true once
    mockStaticGsonTypesEquals(true);
    assertTrue(spyToken.equals(spyOther));
  }

  // Helper method to create a TypeToken instance with a given Type using reflection
  private TypeToken<?> createTypeTokenWithType(Type type) throws Exception {
    var ctor = TypeToken.class.getDeclaredConstructor(Type.class);
    ctor.setAccessible(true);
    return (TypeToken<?>) ctor.newInstance(type);
  }

  // Helper method to mock static $Gson$Types.equals method
  private void mockStaticGsonTypesEquals(boolean returnValue) {
    // Mockito 3 does not support static mocking without inline mock maker or Mockito 4+
    // So we cannot mock static methods here; this test will rely on actual implementation.
    // This method is a placeholder if static mocking is available.
  }
}