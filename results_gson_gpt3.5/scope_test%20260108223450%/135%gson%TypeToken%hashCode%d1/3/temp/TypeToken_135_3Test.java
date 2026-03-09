package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

class TypeTokenHashCodeTest {

  @Test
    @Timeout(8000)
  void testHashCode_consistencyAndValue() {
    // Create a TypeToken for String class using the public static get method
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    int hashCode1 = stringTypeToken.hashCode();
    int hashCode2 = stringTypeToken.hashCode();

    // hashCode should be consistent on multiple calls
    assertEquals(hashCode1, hashCode2);

    // hashCode should equal the stored hashCode field (via reflection)
    int fieldHashCode = 0;
    try {
      Field field = TypeToken.class.getDeclaredField("hashCode");
      field.setAccessible(true);
      fieldHashCode = (int) field.get(stringTypeToken);
    } catch (NoSuchFieldException | IllegalAccessException e) {
      fail("Reflection failed to access hashCode field: " + e.getMessage());
    }
    assertEquals(fieldHashCode, hashCode1);
  }

  @Test
    @Timeout(8000)
  void testHashCode_differentTypesProduceDifferentHashCodes() {
    TypeToken<String> stringTypeToken = TypeToken.get(String.class);
    TypeToken<Integer> integerTypeToken = TypeToken.get(Integer.class);

    int hashCodeString = stringTypeToken.hashCode();
    int hashCodeInteger = integerTypeToken.hashCode();

    // It's possible (though unlikely) that hash codes collide, so also check equals returns false
    if (hashCodeString == hashCodeInteger) {
      assertNotEquals(stringTypeToken, integerTypeToken);
    } else {
      assertNotEquals(hashCodeString, hashCodeInteger);
    }
  }

  @Test
    @Timeout(8000)
  void testHashCode_sameTypeTokensProduceSameHashCode() {
    TypeToken<String> tt1 = TypeToken.get(String.class);
    TypeToken<String> tt2 = TypeToken.get(String.class);

    assertEquals(tt1.hashCode(), tt2.hashCode());
  }
}