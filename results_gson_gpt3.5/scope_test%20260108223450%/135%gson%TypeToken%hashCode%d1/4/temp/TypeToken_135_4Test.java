package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

class TypeTokenHashCodeTest {

  @Test
    @Timeout(8000)
  void testHashCode_returnsPrecomputedHashCode() throws Exception {
    Type type = String.class;
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> typeToken = constructor.newInstance(type);

    Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);
    int expectedHashCode = hashCodeField.getInt(typeToken);

    int actualHashCode = typeToken.hashCode();
    assertEquals(expectedHashCode, actualHashCode);
  }

  @Test
    @Timeout(8000)
  void testHashCode_consistency() throws Exception {
    Type type = Integer.class;
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> typeToken = constructor.newInstance(type);

    int firstHashCode = typeToken.hashCode();
    int secondHashCode = typeToken.hashCode();

    assertEquals(firstHashCode, secondHashCode);
  }
}