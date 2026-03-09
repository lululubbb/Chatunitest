package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonNull_557_1Test {

  @Test
    @Timeout(8000)
  public void testConstructorDeprecated() throws Exception {
    // Deprecated constructor test
    JsonNull jsonNull = new JsonNull();
    assertNotNull(jsonNull);
  }

  @Test
    @Timeout(8000)
  public void testInstanceSingleton() {
    JsonNull instance1 = JsonNull.INSTANCE;
    JsonNull instance2 = JsonNull.INSTANCE;
    assertSame(instance1, instance2);
  }

  @Test
    @Timeout(8000)
  public void testDeepCopy() {
    JsonNull instance = JsonNull.INSTANCE;
    JsonNull copy = instance.deepCopy();
    assertNotNull(copy);
    assertSame(instance, copy);
  }

  @Test
    @Timeout(8000)
  public void testHashCode() {
    JsonNull instance = JsonNull.INSTANCE;
    int hash = instance.hashCode();
    // Changed assertion to check that hashCode equals JsonNull.class.hashCode()
    assertEquals(JsonNull.class.hashCode(), hash);
  }

  @Test
    @Timeout(8000)
  public void testEquals() {
    JsonNull instance = JsonNull.INSTANCE;

    // equals to itself
    assertTrue(instance.equals(instance));

    // equals to same instance
    assertTrue(instance.equals(JsonNull.INSTANCE));

    // equals to null
    assertFalse(instance.equals(null));

    // equals to different type
    assertFalse(instance.equals("string"));

    // equals to new JsonNull instance (deprecated constructor)
    JsonNull newInstance = new JsonNull();
    assertTrue(instance.equals(newInstance));
  }

  @Test
    @Timeout(8000)
  public void testPrivateMethodsUsingReflection() throws Exception {
    JsonNull instance = JsonNull.INSTANCE;

    // deepCopy is public, but test reflection invocation
    Method deepCopyMethod = JsonNull.class.getDeclaredMethod("deepCopy");
    deepCopyMethod.setAccessible(true);
    Object result = deepCopyMethod.invoke(instance);
    assertSame(instance, result);

    // hashCode method reflection
    Method hashCodeMethod = JsonNull.class.getDeclaredMethod("hashCode");
    hashCodeMethod.setAccessible(true);
    int hash = (int) hashCodeMethod.invoke(instance);
    // Changed assertion to check that hashCode equals JsonNull.class.hashCode()
    assertEquals(JsonNull.class.hashCode(), hash);

    // equals method reflection
    Method equalsMethod = JsonNull.class.getDeclaredMethod("equals", Object.class);
    equalsMethod.setAccessible(true);
    assertTrue((Boolean) equalsMethod.invoke(instance, instance));
    assertFalse((Boolean) equalsMethod.invoke(instance, (Object) null));
  }
}