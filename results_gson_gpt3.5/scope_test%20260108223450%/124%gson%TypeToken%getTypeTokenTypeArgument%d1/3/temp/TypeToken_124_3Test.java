package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

class TypeToken_124_3Test {

  // Subclass of TypeToken with concrete type argument to test getTypeTokenTypeArgument()
  static class StringTypeToken extends TypeToken<String> {}

  // Subclass of TypeToken with raw superclass to trigger exception
  static class RawTypeToken extends TypeToken {}

  // Subclass of a subclass of TypeToken to trigger exception
  static class IndirectTypeToken extends StringTypeToken {}

  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument_withParameterizedType() throws Exception {
    TypeToken<String> stringTypeToken = new StringTypeToken();

    // Use reflection to access private method
    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    Type typeArg = (Type) method.invoke(stringTypeToken);

    assertNotNull(typeArg);
    assertEquals(String.class, typeArg);
  }

  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument_withRawType_throws() throws Exception {
    // We cannot instantiate RawTypeToken without exception because exception is thrown in constructor
    // So we create an anonymous subclass of raw TypeToken to trigger the exception on constructor
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      new TypeToken() {};
    });

    assertTrue(ex.getMessage().contains("TypeToken must be created with a type argument"));
  }

  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument_withIndirectSubclass_throws() throws Exception {
    // Similarly, exception is thrown during construction of IndirectTypeToken
    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      new IndirectTypeToken();
    });

    assertEquals("Must only create direct subclasses of TypeToken", ex.getMessage());
  }
}