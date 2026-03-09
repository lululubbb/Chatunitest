package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.lang.reflect.Field;
import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

class TypeTokenHashCodeTest {

  @Test
    @Timeout(8000)
  void testHashCodeReturnsStoredHashCode() throws Exception {
    // Use reflection to get the private constructor TypeToken(Type)
    Constructor<TypeToken> constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    // Create a TypeToken instance with a specific Type, e.g. String.class
    TypeToken<?> token = constructor.newInstance(String.class);

    // Access the private final field 'hashCode'
    Field hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);
    int expectedHashCode = hashCodeField.getInt(token);

    // Call hashCode() method and verify it returns the stored hashCode value
    assertEquals(expectedHashCode, token.hashCode());
  }
}