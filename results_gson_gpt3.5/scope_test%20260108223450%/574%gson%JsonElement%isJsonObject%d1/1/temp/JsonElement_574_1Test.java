package com.google.gson;
import org.junit.jupiter.api.Timeout;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class JsonElement_574_1Test {

  private static class JsonElementSubclass extends JsonElement {
    @Override
    public JsonElement deepCopy() {
      return this;
    }
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_withJsonObjectInstance_returnsTrue() {
    // Create a proxy instance that implements JsonObject interface
    // and extends JsonElement by subclassing a concrete subclass
    JsonObject proxy = (JsonObject) Proxy.newProxyInstance(
      JsonObject.class.getClassLoader(),
      new Class<?>[] { JsonObject.class },
      new InvocationHandler() {
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
          if ("deepCopy".equals(method.getName())) {
            return proxy;
          }
          if ("isJsonObject".equals(method.getName())) {
            return true;
          }
          if (method.getReturnType() == boolean.class) {
            return false;
          }
          return null;
        }
      }
    );

    // Wrap the proxy in a subclass of JsonElement that delegates calls to the proxy
    JsonElement element = new JsonElementSubclass() {
      @Override
      public boolean isJsonObject() {
        return proxy instanceof JsonObject;
      }
      @Override
      public JsonElement deepCopy() {
        return this;
      }
    };

    assertTrue(element.isJsonObject());
  }

  @Test
    @Timeout(8000)
  void testIsJsonObject_withJsonElementInstance_returnsFalse() {
    JsonElement element = new JsonElementSubclass();
    assertFalse(element.isJsonObject());
  }

}