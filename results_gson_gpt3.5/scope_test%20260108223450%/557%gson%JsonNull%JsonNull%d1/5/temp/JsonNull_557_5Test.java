package com.google.gson;
import org.junit.jupiter.api.Timeout;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

public class JsonNull_557_5Test {

  @Test
    @Timeout(8000)
  public void testConstructor_Deprecated() throws Exception {
    // Deprecated public constructor
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
    JsonNull jsonNull = JsonNull.INSTANCE;
    JsonNull copy = jsonNull.deepCopy();
    assertSame(jsonNull, copy);
  }

  @Test
    @Timeout(8000)
  public void testHashCode() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    int hashCode = jsonNull.hashCode();
    assertEquals(JsonNull.class.hashCode(), hashCode);
  }

  @Test
    @Timeout(8000)
  public void testEquals() {
    JsonNull jsonNull = JsonNull.INSTANCE;
    assertTrue(jsonNull.equals(jsonNull));
    assertTrue(jsonNull.equals(JsonNull.INSTANCE));
    assertFalse(jsonNull.equals(null));
    assertFalse(jsonNull.equals("string"));
  }

  @Test
    @Timeout(8000)
  public void testEqualsWithReflection() throws Exception {
    JsonNull jsonNull = JsonNull.INSTANCE;

    Method equalsMethod = JsonNull.class.getDeclaredMethod("equals", Object.class);
    equalsMethod.setAccessible(true);

    // equals to itself
    assertTrue((Boolean) equalsMethod.invoke(jsonNull, jsonNull));
    // equals to INSTANCE
    assertTrue((Boolean) equalsMethod.invoke(jsonNull, JsonNull.INSTANCE));
    // equals to null
    assertFalse((Boolean) equalsMethod.invoke(jsonNull, new Object[]{null}));
    // equals to different type
    assertFalse((Boolean) equalsMethod.invoke(jsonNull, "string"));
  }

  @Test
    @Timeout(8000)
  public void testDeepCopyWithReflection() throws Exception {
    JsonNull jsonNull = JsonNull.INSTANCE;

    Method deepCopyMethod = JsonNull.class.getDeclaredMethod("deepCopy");
    deepCopyMethod.setAccessible(true);

    JsonNull copy = (JsonNull) deepCopyMethod.invoke(jsonNull);
    assertSame(jsonNull, copy);
  }

  @Test
    @Timeout(8000)
  public void testHashCodeWithReflection() throws Exception {
    JsonNull jsonNull = JsonNull.INSTANCE;

    Method hashCodeMethod = JsonNull.class.getDeclaredMethod("hashCode");
    hashCodeMethod.setAccessible(true);

    int hashCode = (int) hashCodeMethod.invoke(jsonNull);
    assertEquals(JsonNull.class.hashCode(), hashCode);
  }
}