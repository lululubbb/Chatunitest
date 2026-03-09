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

import java.lang.reflect.*;
import org.junit.jupiter.api.Test;

class TypeToken_getTypeTokenTypeArgument_Test {

  @Test
    @Timeout(8000)
  void test_getTypeTokenTypeArgument_directSubclassParameterized() throws Exception {
    class StringTypeToken extends TypeToken<String> {}
    TypeToken<String> token = new StringTypeToken();

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    Type result = (Type) method.invoke(token);

    // The result should be canonicalized String.class type
    assertEquals(String.class, result);
  }

  @Test
    @Timeout(8000)
  void test_getTypeTokenTypeArgument_rawTypeToken_throws() throws Exception {
    // Create a raw TypeToken subclass with no type argument
    @SuppressWarnings({"rawtypes", "unchecked"})
    class RawToken extends TypeToken {}
    RawToken raw = new RawToken();

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(raw);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertTrue(thrown.getCause().getMessage().contains("TypeToken must be created with a type argument"));
  }

  @Test
    @Timeout(8000)
  void test_getTypeTokenTypeArgument_subclassOfSubclass_throws() throws Exception {
    // Create direct subclass with type argument
    class DirectToken<T> extends TypeToken<T> {}
    // Create subclass of subclass with type argument
    class SubToken<T> extends DirectToken<T> {}

    SubToken<String> subToken = new SubToken<>();

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    InvocationTargetException thrown = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(subToken);
    });
    assertTrue(thrown.getCause() instanceof IllegalStateException);
    assertTrue(thrown.getCause().getMessage().contains("Must only create direct subclasses of TypeToken"));
  }
}