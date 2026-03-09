package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.*;
import org.junit.jupiter.api.Test;

class TypeTokenGetTypeTokenTypeArgumentTest {

  // Subclass directly extending TypeToken with a concrete type argument
  static class DirectTypeTokenString extends TypeToken<String> {}

  // Subclass directly extending TypeToken without type argument (raw)
  @SuppressWarnings("rawtypes")
  static class RawTypeToken extends TypeToken {}

  // Subclass extending a subclass of TypeToken (indirect subclass)
  static class IndirectTypeToken extends DirectTypeTokenString {}

  @Test
    @Timeout(8000)
  void getTypeTokenTypeArgument_directParameterizedType_returnsCanonicalizedType() throws Exception {
    TypeToken<String> token = new DirectTypeTokenString();

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    Type result = (Type) method.invoke(token);

    // The expected type is String.class canonicalized by $Gson$Types.canonicalize
    Type expected = $Gson$Types.canonicalize(String.class);
    assertEquals(expected, result);
  }

  @Test
    @Timeout(8000)
  void getTypeTokenTypeArgument_rawType_throwsIllegalStateException() throws Exception {
    RawTypeToken token = new RawTypeToken();

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    Throwable thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(token);
    });

    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertTrue(cause.getMessage().contains("TypeToken must be created with a type argument"));
  }

  @Test
    @Timeout(8000)
  void getTypeTokenTypeArgument_indirectSubclass_throwsIllegalStateException() throws Exception {
    IndirectTypeToken token = new IndirectTypeToken();

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    Throwable thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(token);
    });

    Throwable cause = thrown.getCause();
    assertNotNull(cause);
    assertTrue(cause instanceof IllegalStateException);
    assertTrue(cause.getMessage().contains("Must only create direct subclasses of TypeToken"));
  }
}