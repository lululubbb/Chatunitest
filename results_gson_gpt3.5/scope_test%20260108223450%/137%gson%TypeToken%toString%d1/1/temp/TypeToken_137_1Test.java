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

import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import org.junit.jupiter.api.Test;

class TypeTokenToStringTest {

  @Test
    @Timeout(8000)
  void toString_returnsTypeToStringOfType() throws Exception {
    // Create a dynamic proxy for Type interface since Mockito.mock(Type.class) is not available
    Type type = (Type) Proxy.newProxyInstance(
        Type.class.getClassLoader(),
        new Class<?>[] {Type.class},
        (proxy, method, args) -> {
          if ("toString".equals(method.getName())) {
            return "proxyType";
          }
          throw new UnsupportedOperationException("Only toString() is supported");
        });

    // Create a TypeToken instance using the private constructor that accepts Type
    var constructor = TypeToken.class.getDeclaredConstructor(Type.class);
    constructor.setAccessible(true);
    TypeToken<?> token = (TypeToken<?>) constructor.newInstance(type);

    // Use reflection to set the private final fields rawType and hashCode
    var rawTypeField = TypeToken.class.getDeclaredField("rawType");
    rawTypeField.setAccessible(true);
    rawTypeField.set(token, Object.class);

    var hashCodeField = TypeToken.class.getDeclaredField("hashCode");
    hashCodeField.setAccessible(true);
    hashCodeField.set(token, 0);

    String result = token.toString();

    assertEquals("proxyType", result);
  }
}