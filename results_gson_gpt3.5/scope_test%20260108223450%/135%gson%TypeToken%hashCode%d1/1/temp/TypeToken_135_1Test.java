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

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;

class TypeTokenHashCodeTest {

  @Test
    @Timeout(8000)
  void testHashCodeReturnsStoredValue() throws Exception {
    // Create an anonymous subclass instance of TypeToken to initialize fields
    TypeToken<String> typeToken = new TypeToken<String>() {};

    // Use reflection to get the private field 'hashCode' from the actual class of typeToken
    Field hashCodeField = typeToken.getClass().getSuperclass().getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);
    int storedHashCode = (int) hashCodeField.get(typeToken);

    // Call hashCode() method and assert it returns the stored hashCode field value
    int result = typeToken.hashCode();
    assertEquals(storedHashCode, result);
  }
}