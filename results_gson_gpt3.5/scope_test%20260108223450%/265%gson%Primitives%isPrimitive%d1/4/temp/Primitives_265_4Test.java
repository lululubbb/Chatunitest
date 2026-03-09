package com.google.gson.internal;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Type;
import java.lang.reflect.Method;

class Primitives_265_4Test {

  @Test
    @Timeout(8000)
  void testIsPrimitive_withPrimitiveClass() {
    assertTrue(Primitives.isPrimitive(int.class));
    assertTrue(Primitives.isPrimitive(boolean.class));
    assertTrue(Primitives.isPrimitive(double.class));
    assertTrue(Primitives.isPrimitive(void.class));
  }

  @Test
    @Timeout(8000)
  void testIsPrimitive_withNonPrimitiveClass() {
    assertFalse(Primitives.isPrimitive(String.class));
    assertFalse(Primitives.isPrimitive(Object.class));
    assertFalse(Primitives.isPrimitive(Integer.class));
  }

  @Test
    @Timeout(8000)
  void testIsPrimitive_withNonClassType() throws Exception {
    // Create a dummy Type that is not Class
    Type type = new Type() {};
    assertFalse(Primitives.isPrimitive(type));
  }

  @Test
    @Timeout(8000)
  void testPrivateConstructor() throws Exception {
    var constructor = Primitives.class.getDeclaredConstructor();
    constructor.setAccessible(true);
    Object instance = constructor.newInstance();
    assertNotNull(instance);
  }

  @Test
    @Timeout(8000)
  void testIsPrimitive_usingReflection() throws Exception {
    Method method = Primitives.class.getDeclaredMethod("isPrimitive", Type.class);
    method.setAccessible(true);

    assertTrue((Boolean) method.invoke(null, int.class));
    assertFalse((Boolean) method.invoke(null, String.class));
    assertFalse((Boolean) method.invoke(null, new Type() {}));
  }
}