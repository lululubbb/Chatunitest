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
import static org.mockito.Mockito.*;

import java.lang.reflect.*;
import org.junit.jupiter.api.Test;

class TypeToken_getTypeTokenTypeArgument_Test {

  // Helper subclass to create a direct subclass of TypeToken with a concrete type argument
  static class StringTypeToken extends TypeToken<String> {}

  // Helper subclass to create a raw TypeToken subclass (no type argument)
  static class RawTypeToken extends TypeToken {}

  // Helper subclass to create an indirect subclass of TypeToken
  static class IndirectTypeToken extends StringTypeToken {}

  @Test
    @Timeout(8000)
  void test_getTypeTokenTypeArgument_directParameterizedType() throws Exception {
    TypeToken<String> token = new StringTypeToken();
    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);
    Type typeArgument = (Type) method.invoke(token);
    assertEquals(String.class, typeArgument);
  }

  @Test
    @Timeout(8000)
  void test_getTypeTokenTypeArgument_rawTypeToken_throws() throws Exception {
    RawTypeToken rawToken = new RawTypeToken();
    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      try {
        method.invoke(rawToken);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertTrue(ex instanceof IllegalStateException);
    assertTrue(ex.getMessage().contains("TypeToken must be created with a type argument"));
  }

  @Test
    @Timeout(8000)
  void test_getTypeTokenTypeArgument_indirectSubclass_throws() throws Exception {
    IndirectTypeToken indirectToken = new IndirectTypeToken();
    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      try {
        method.invoke(indirectToken);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertTrue(ex instanceof IllegalStateException);
    assertEquals("Must only create direct subclasses of TypeToken", ex.getMessage());
  }

  @Test
    @Timeout(8000)
  void test_getTypeTokenTypeArgument_parameterizedTypeRawTypeNotTypeToken() throws Exception {
    // Create a ParameterizedType with raw type != TypeToken.class
    ParameterizedType fakeParameterizedType = mock(ParameterizedType.class);
    when(fakeParameterizedType.getRawType()).thenReturn(String.class);
    when(fakeParameterizedType.getActualTypeArguments()).thenReturn(new Type[] {String.class});

    // Create a proxy for Class<?> that returns fakeParameterizedType for getGenericSuperclass()
    Class<?> fakeClassProxy = (Class<?>) Proxy.newProxyInstance(
        ClassLoader.getSystemClassLoader(),
        new Class<?>[] { Class.class },
        (proxy, method, args) -> {
          if ("getGenericSuperclass".equals(method.getName())) {
            return fakeParameterizedType;
          }
          if (method.getDeclaringClass() == Object.class) {
            return method.invoke(this, args);
          }
          throw new UnsupportedOperationException("Method " + method.getName() + " is not supported");
        });

    // Create an anonymous subclass of TypeToken
    TypeToken<String> tokenWithFakeSuperclass = new TypeToken<String>() {};

    // Use a spy to mock getClass() method to return our fakeClassProxy
    TypeToken<String> spyToken = spy(tokenWithFakeSuperclass);

    // Use doReturn instead of when().thenReturn() to avoid type issues
    doReturn(fakeClassProxy).when(spyToken).getClass();

    Method method = TypeToken.class.getDeclaredMethod("getTypeTokenTypeArgument");
    method.setAccessible(true);

    IllegalStateException ex = assertThrows(IllegalStateException.class, () -> {
      try {
        method.invoke(spyToken);
      } catch (InvocationTargetException e) {
        throw e.getCause();
      }
    });
    assertTrue(ex instanceof IllegalStateException);
    assertEquals("Must only create direct subclasses of TypeToken", ex.getMessage());
  }
}