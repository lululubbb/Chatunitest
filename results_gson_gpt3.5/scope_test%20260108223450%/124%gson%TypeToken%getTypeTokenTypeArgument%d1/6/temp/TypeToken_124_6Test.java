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

class TypeToken_getTypeTokenTypeArgument_Test {

  /**
   * Helper subclass to create a direct subclass of TypeToken with a type argument.
   */
  private static class StringTypeToken extends TypeToken<String> {}

  /**
   * Helper subclass that extends another subclass of TypeToken to test indirect subclass case.
   */
  private static class IntermediateTypeToken extends TypeToken<String> {}

  private static class IndirectTypeToken extends IntermediateTypeToken {}

  /**
   * Helper subclass with raw TypeToken superclass (no type argument).
   */
  private static class RawTypeToken extends TypeToken {}

  @Test
    @Timeout(8000)
  void test_getTypeTokenTypeArgument_directParameterized() throws Exception {
    TypeToken<String> token = new StringTypeToken();

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    Type typeArg = (Type) method.invoke(token);

    // The type argument should be java.lang.String.class canonicalized
    assertNotNull(typeArg);
    assertEquals(String.class, typeArg);
  }

  @Test
    @Timeout(8000)
  void test_getTypeTokenTypeArgument_rawSuperclass_throws() throws Exception {
    RawTypeToken rawToken = new RawTypeToken();

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> method.invoke(rawToken));
    // InvocationTargetException wraps the actual exception
    Throwable cause = ex.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertTrue(cause.getMessage().contains("TypeToken must be created with a type argument"));
  }

  @Test
    @Timeout(8000)
  void test_getTypeTokenTypeArgument_indirectSubclass_throws() throws Exception {
    IndirectTypeToken indirectToken = new IndirectTypeToken();

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> method.invoke(indirectToken));
    Throwable cause = ex.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertEquals("Must only create direct subclasses of TypeToken", cause.getMessage());
  }
}