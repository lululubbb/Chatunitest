package com.google.gson.reflect;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.TypeVariable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.jupiter.api.Test;

class TypeToken_124_1Test {

  // Helper subclass with direct ParameterizedType superclass
  static class DirectTypeToken<T> extends TypeToken<T> {
    DirectTypeToken() {
      super();
    }
  }

  // Helper subclass with raw TypeToken superclass (simulate raw usage)
  static class RawTypeToken extends TypeToken {
    RawTypeToken() {
      super();
    }
  }

  // Helper subclass that extends DirectTypeToken (indirect subclass)
  static class IndirectTypeToken<T> extends DirectTypeToken<T> {
    IndirectTypeToken() {
      super();
    }
  }

  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument_withDirectParameterizedType() throws Exception {
    // Create anonymous subclass with concrete type argument String
    TypeToken<String> stringToken = new TypeToken<String>() {};
    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    Type typeArg = (Type) method.invoke(stringToken);
    assertNotNull(typeArg);
    assertEquals(String.class, typeArg);
  }

  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument_withRawTypeToken_throws() throws Exception {
    RawTypeToken rawTokenInstance = null;
    try {
      rawTokenInstance = new RawTypeToken();
      fail("Expected IllegalStateException at RawTypeToken constructor");
    } catch (IllegalStateException expected) {
      // expected exception on construction, so create a proxy instance that bypasses constructor check
      rawTokenInstance = createRawTypeTokenProxy();
    }

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(rawTokenInstance);
    });
    // The cause should be IllegalStateException with expected message
    Throwable cause = ex.getCause();
    assertTrue(cause instanceof IllegalStateException);
    assertTrue(cause.getMessage().contains("TypeToken must be created with a type argument"));
  }

  @Test
    @Timeout(8000)
  void testGetTypeTokenTypeArgument_withIndirectSubclass_throws() throws Exception {
    IndirectTypeToken<String> indirectToken = null;
    try {
      indirectToken = new IndirectTypeToken<>();
      fail("Expected IllegalStateException at IndirectTypeToken constructor");
    } catch (IllegalStateException expected) {
      // expected exception on construction, so create a proxy instance that bypasses constructor check
      indirectToken = createIndirectTypeTokenProxy();
    }

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    InvocationTargetException ex = assertThrows(InvocationTargetException.class, () -> {
      method.invoke(indirectToken);
    });

    Throwable cause = ex.getCause();
    assertTrue(cause instanceof IllegalStateException);
    assertEquals("Must only create direct subclasses of TypeToken", cause.getMessage());
  }

  // Create proxies without overriding toString(), to avoid final method override error
  private static RawTypeToken createRawTypeTokenProxy() {
    return new RawTypeToken() {
      // No override of toString()
    };
  }

  private static IndirectTypeToken<String> createIndirectTypeTokenProxy() {
    return new IndirectTypeToken<String>() {
      // No override of toString()
    };
  }
}